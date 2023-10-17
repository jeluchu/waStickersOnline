package com.jeluchu.wastickersonline.features.sticker.repository

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.ktx.time.isFetchTwentyDays
import com.jeluchu.jchucomponents.utils.network.models.Resource
import com.jeluchu.jchucomponents.utils.network.networkBoundResource
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.wastickersonline.core.network.NetworkHandler
import com.jeluchu.wastickersonline.core.utils.LocalShared
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.repository.local.StickersDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StickersRepository {

    fun stickers(): Flow<Resource<Failure, List<StickerPack>>>

    class StickersRepositoryImpl @Inject constructor(
        private val networkHandler: NetworkHandler,
        private val service: StickersService,
        private val local: StickersDao
    ) : StickersRepository {

        private val preferences by lazy { SharedPrefsHelpers() }

        override fun stickers() =
            networkBoundResource(
                query = { local.getStickers() },
                fetch = { service.getStickers().stickerPacks.map { it.toStickersPack() } },
                saveFetchResult = { data ->
                    local.deleteAll()
                    preferences.saveLong(LocalShared.Stickers.stickers, System.currentTimeMillis())
                    data.forEach { local.insertStickers(it.toStickerPackEntity()) }
                },
                shouldFetch = {
                    local.getStickersForServerQuery().isNullOrEmpty() || isFetchTwentyDays(
                        preferences.getLong(LocalShared.Stickers.stickers, 0L)
                    ) && networkHandler.isNetworkAvailable()
                },
                dbTransform = { data -> data.map { it.toStickersPack() } }
            )
    }
}