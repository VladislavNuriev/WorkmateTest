package com.example.generateuser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.SaveRandomUserUseCase
import com.example.generateuser.ui.GenerateUserIntent
import com.example.generateuser.ui.GenerateUserScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateUserViewModel @Inject constructor(
    private val saveRandomUser: SaveRandomUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<GenerateUserScreenState>(
        GenerateUserScreenState.Generate()
    )
    val state: StateFlow<GenerateUserScreenState> = _state.asStateFlow()

    fun handleIntent(intent: GenerateUserIntent) {
        when (intent) {
            is GenerateUserIntent.GenderSelected -> updateGender(intent.gender)

            is GenerateUserIntent.NationalitySelected -> {
                updateNationality(intent.nationality)
            }

            GenerateUserIntent.GenerateUser -> generateUser()
        }
    }

    private fun updateGender(gender: String) {
        val currentState = _state.value as? GenerateUserScreenState.Generate ?: return
        _state.value = currentState.copy(
            gender = gender,
            error = null
        )
    }

    private fun updateNationality(nationality: String) {
        val currentState = _state.value as? GenerateUserScreenState.Generate ?: return
        _state.value = currentState.copy(
            nationality = nationality,
            error = null
        )
    }

    private fun generateUser() {
        val currentState = _state.value as? GenerateUserScreenState.Generate ?: return
        if (!currentState.isSaveEnabled) return

        _state.value = currentState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            saveRandomUser(currentState.gender, currentState.nationality)
                .onSuccess {
                    _state.value = GenerateUserScreenState.Finished
                }.onFailure {
                    val errorMsg = it.message ?: "Unknown error"
                    _state.value = currentState.copy(isLoading = false, error = errorMsg)
                }
        }
    }
}
