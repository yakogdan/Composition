package com.yakogdan.composition.presentation.viewmodels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yakogdan.composition.R
import com.yakogdan.composition.data.repositories.GameRepoImpl
import com.yakogdan.composition.domain.entities.GameResult
import com.yakogdan.composition.domain.entities.GameSettings
import com.yakogdan.composition.domain.entities.Level
import com.yakogdan.composition.domain.entities.Question
import com.yakogdan.composition.domain.usecases.GenerateQuestionUseCase
import com.yakogdan.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val context = application

    private val repository = GameRepoImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTimeLD = MutableLiveData<String>()
    val formattedTimeLD: LiveData<String> get() = _formattedTimeLD

    private val _questionLD = MutableLiveData<Question>()
    val questionLD: LiveData<Question> get() = _questionLD

    private val _percentOfRightAnswerLD = MutableLiveData<Int>()
    val percentOfRightAnswerLD: LiveData<Int> get() = _percentOfRightAnswerLD

    private val _progressAnswersLD = MutableLiveData<String>()
    val progressAnswersLD: LiveData<String> get() = _progressAnswersLD

    private val _enoughCountOfRightAnswersLD = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswersLD: LiveData<Boolean> get() = _enoughCountOfRightAnswersLD

    private val _enoughPercentOfRightAnswersLD = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswersLD: LiveData<Boolean> get() = _enoughPercentOfRightAnswersLD

    private val _minPercentOfRightAnswersLD = MutableLiveData<Int>()
    val minPercentOfRightAnswersLD: LiveData<Int> get() = _minPercentOfRightAnswersLD

    private val _gameResultLD = MutableLiveData<GameResult>()
    val gameResultLD: LiveData<GameResult> get() = _gameResultLD

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswerLD.value = percent
        _progressAnswersLD.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )

        _enoughCountOfRightAnswersLD.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers

        _enoughPercentOfRightAnswersLD.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = questionLD.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++

        }
        countOfQuestions++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercentOfRightAnswersLD.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTimeLD.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun generateQuestion() {
        _questionLD.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResultLD.value = GameResult(
            winner = checkWinner(),
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }

    private fun checkWinner(): Boolean =
        enoughCountOfRightAnswersLD.value == true && enoughPercentOfRightAnswersLD.value == true


    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}