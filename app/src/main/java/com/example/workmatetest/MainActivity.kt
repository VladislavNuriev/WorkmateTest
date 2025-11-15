package com.example.workmatetest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.domain.usecases.DeleteUserUseCase
import com.example.domain.usecases.GetSavedUsersUseCase
import com.example.domain.usecases.GetUserByIdUseCase
import com.example.domain.usecases.SaveRandomUserUseCase
import com.example.workmatetest.ui.theme.WorkmateTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var saveRandomUser: SaveRandomUserUseCase
    @Inject
    lateinit var getUserById: GetUserByIdUseCase
    @Inject
    lateinit var deleteUser: DeleteUserUseCase
    @Inject
    lateinit var getSavedUsers: GetSavedUsersUseCase

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            saveRandomUser("female", nationality = "gb").onSuccess {
                Log.d(TAG, "onCreate: ")
                val user = getUserById(2)
                Log.d(TAG, "onCreate: ${user.toString()}")
                //deleteUser(userId = 1, imageFilePath = user.pictureFilePath)
                val users = getSavedUsers("").collect {
                    Log.d(TAG, "onCreate: ${it}")
                }
            }.onFailure {
                Log.d(TAG, "onCreate: $it")
            }
        }

        enableEdgeToEdge()
        setContent {
            WorkmateTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkmateTestTheme {
        Greeting("Android")
    }
}