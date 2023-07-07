package com.yakogdan.composition.domain.repositories

import com.yakogdan.composition.domain.entities.GameSettings
import com.yakogdan.composition.domain.entities.Level
import com.yakogdan.composition.domain.entities.Question

interface GameRepository {

    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question

    fun getGameSettings(level: Level): GameSettings
}