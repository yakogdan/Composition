package com.yakogdan.composition.presentation.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yakogdan.composition.domain.entities.Level
import com.yakogdan.composition.presentation.viewmodels.GameViewModel

class GameViewModelFactory(
    private val level: Level,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, level) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}