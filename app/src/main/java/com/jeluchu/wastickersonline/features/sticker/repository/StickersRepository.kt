package com.jeluchu.wastickersonline.features.sticker.repository

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.core.functional.Either
import com.jeluchu.jchucomponents.ktx.context.handler.NetworkHandler
import com.jeluchu.jchucomponents.ktx.retrofit.request
import com.jeluchu.jchucomponents.ktx.time.isFetchSixHours
import com.jeluchu.wastickersonline.core.extensions.sharedprefs.SharedPrefsHelpers
import com.jeluchu.wastickersonline.core.utils.LocalShared
import com.jeluchu.wastickersonline.features.sticker.models.PacksEntity
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.models.StickerPackEntity
import com.jeluchu.wastickersonline.features.sticker.repository.local.StickerLocal
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface StickersRepository {

    fun stickers(): Flow<Either<Failure, List<StickerPack>>>

    class Network(
        private val networkHandler: NetworkHandler,
        private val service: StickersService,
        private val local: StickerLocal
    ) : StickersRepository {

        private val preferences by lazy { SharedPrefsHelpers() }

        override fun stickers(): Flow<Either<Failure, List<StickerPack>>> =
            flow {

                val stickers = local.getStickers()
                val time = preferences.getLong(LocalShared.Stickers.stickers, 0L)

                if (stickers.isNullOrEmpty() || isFetchSixHours(time)) {
                    local.deleteAllStickers()
                    emit(getRemoteStickers())
                } else {
                    emit(Either.Right(local.getStickers().map { it.toStickersPack() }))
                }

            }.catch {
                emit(Either.Left(Failure.CustomError(404, "Not Found")))
            }.flowOn(Dispatchers.IO)


        private fun getRemoteStickers(): Either<Failure, List<StickerPack>> =
            when (networkHandler.isConnected) {
                true -> request(
                    service.getStickers(),
                    { packsEntity ->

                        val stickerList: List<StickerPackEntity> = packsEntity.stickerPacks

                        preferences.saveLong(LocalShared.Stickers.stickers, Date().time)

                        addAllStickers(stickerList)
                        stickerList.map { it.toStickersPack() }

                    },
                    PacksEntity(emptyList())
                )
                false -> Either.Left(Failure.NetworkConnection(404, "Not Found"))
            }

        private fun addAllStickers(stickers: List<StickerPackEntity>) {
            for (sticker in stickers) {
                local.addStickers(sticker)
            }
        }

    }
}