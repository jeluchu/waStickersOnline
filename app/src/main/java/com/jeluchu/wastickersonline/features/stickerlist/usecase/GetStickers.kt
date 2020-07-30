package com.jeluchu.wastickersonline.features.stickerlist.usecase

import com.jeluchu.wastickersonline.core.exception.Failure
import com.jeluchu.wastickersonline.core.functional.Either
import com.jeluchu.wastickersonline.core.interactor.UseCase
import com.jeluchu.wastickersonline.features.stickerlist.models.StickerPack
import com.jeluchu.wastickersonline.features.stickerlist.repository.StickersRepository
import kotlinx.coroutines.flow.Flow

class GetStickers(private val stickersRepository: StickersRepository): UseCase<Either<Failure, List<StickerPack>>, UseCase.None>() {
    override fun run(params: None?): Flow<Either<Failure, List<StickerPack>>> = stickersRepository.stickers()
}