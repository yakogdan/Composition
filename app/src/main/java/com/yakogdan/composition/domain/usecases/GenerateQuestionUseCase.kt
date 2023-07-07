package com.yakogdan.composition.domain.usecases

import com.yakogdan.composition.domain.entities.Question
import com.yakogdan.composition.domain.repositories.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(
            maxSumValue = maxSumValue,
            countOfOptions = COUNT_OF_OPTIONS
        )
    }

    private companion object {

        private const val COUNT_OF_OPTIONS = 6
    }
}