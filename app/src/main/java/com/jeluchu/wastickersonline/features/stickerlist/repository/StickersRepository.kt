package com.jeluchu.wastickersonline.features.stickerlist.repository

import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.functional.Either
import com.jeluchu.wastickersonline.core.extensions.request
import com.jeluchu.wastickersonline.core.platform.NetworkHandler
import com.jeluchu.wastickersonline.features.stickerlist.models.PacksEntity
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPack
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface StickersRepository {
    
    fun stickers(): Flow<Either<Failure, List<StickerPack>>>
    
    class Network(private val networkHandler: NetworkHandler,
                  private val service: StickersService
    ) : StickersRepository {

        override fun stickers(): Flow<Either<Failure, List<StickerPack>>> =
            flow {
                emit(getRemoteStickers())
            }.catch {
                emit(Either.Left(Failure.CustomError(404, "Not Found")))
            }.flowOn(Dispatchers.IO)


        private fun getRemoteStickers(): Either<Failure, List<StickerPack>> =
            when (networkHandler.isConnected) {
                true -> request(
                    service.getStickers(),
                    { packsEntity ->
                        val stickerList: List<StickerPackEntity> = packsEntity.stickerPacks
                        stickerList.map { it.toStickersPack() }
                    },
                    PacksEntity(emptyList())
                )
                false -> Either.Left(Failure.NetworkConnection())
            }


    }
}