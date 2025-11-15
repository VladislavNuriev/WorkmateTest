package com.example.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.DeleteUserUseCase
import com.example.domain.usecases.GetSavedUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val deleteUser: DeleteUserUseCase,
    private val getSavedUsers: GetSavedUsersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UsersScreenState())
    val state: StateFlow<UsersScreenState> = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        query
            .onEach { input ->
                _state.update {
                    it.copy(searchQuery = input)
                }
            }
            .flatMapLatest { input ->
                getSavedUsers(input)
            }
            .onEach { users ->
                _state.update { it.copy(users = users) }
            }.launchIn(viewModelScope)

    }


    fun handleIntent(intent: UsersIntent) {
        when (intent) {
            is UsersIntent.SearchQueryChanged -> {
                query.update { intent.query.trim() }
            }

            is UsersIntent.DeleteUser -> {
                viewModelScope.launch {
                    deleteUser(userId = intent.id, imageFilePath = intent.imageFilePath)
                }
            }
        }
    }
}