package com.jeluchu.wastickersonline.features.sticker.usecase

import com.jeluchu.jchucomponents.core.exception.Failure
import com.jeluchu.jchucomponents.core.functional.Either
import com.jeluchu.wastickersonline.core.interactor.UseCase
import com.jeluchu.wastickersonline.features.sticker.models.StickerPack
import com.jeluchu.wastickersonline.features.sticker.repository.StickersRepository
import kotlinx.coroutines.flow.Flow

class GetStickers(private val stickersRepository: StickersRepository) :
    UseCase<Either<Failure, List<StickerPack>>, UseCase.None>() {
    override fun run(params: None?): Flow<Either<Failure, List<StickerPack>>> =
        stickersRepository.stickers()
}