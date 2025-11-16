package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetUserByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = UserProfileViewModel.Factory::class)
class UserProfileViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: Int,
    getUserById: GetUserByIdUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<UserProfileScreenState> = MutableStateFlow(
        UserProfileScreenState.Initial
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                UserProfileScreenState.UserProfile(getUserById(userId))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") noteId: Int
        ): UserProfileViewModel
    }
}