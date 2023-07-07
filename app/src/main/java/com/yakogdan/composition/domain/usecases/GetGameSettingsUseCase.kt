package com.yakogdan.composition.domain.usecases

import com.yakogdan.composition.domain.entities.GameSettings
import com.yakogdan.composition.domain.entities.Level
import com.yakogdan.composition.domain.repositories.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level = level)
    }
}