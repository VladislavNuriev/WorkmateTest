package com.example.generateuser.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.generateuser.GenerateUserViewModel
import com.example.generateuser.helper.Nationality

@Composable
fun GenerateUserScreen(
    modifier: Modifier = Modifier,
    viewModel: GenerateUserViewModel = hiltViewModel(),
    navigateToUsers: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }


    when (val currentState = state) {

        is GenerateUserScreenState.Generate -> {
            Log.d("SnackBarTest", "GenerateUserScreenState.Generate: ${currentState.error}")
            LaunchedEffect(currentState.error) {
                Log.d("SnackBarTest", "LaunchedEffect: ${currentState.error}")
                if (currentState.error != null) {
                    val action = snackbarHostState.showSnackbar(
                        message = currentState.error,
                        actionLabel = "Повторить попытку",
                        duration = SnackbarDuration.Long
                    )
                    if (action == SnackbarResult.ActionPerformed) {
                        viewModel.handleIntent(GenerateUserIntent.GenerateUser)
                    }
                }
            }

            Scaffold(
                modifier = modifier,
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { paddingValues ->
                GenerateUser(
                    modifier = Modifier.padding(paddingValues),
                    state = currentState,
                    onGenderSelected = { viewModel.handleIntent(GenerateUserIntent.GenderSelected(it)) },
                    onNationalitySelected = {
                        viewModel.handleIntent(
                            GenerateUserIntent.NationalitySelected(
                                it
                            )
                        )
                    },
                    onGenerateClicked = {
                        viewModel.handleIntent(GenerateUserIntent.GenerateUser)
                    },
                    onGoToUsersClicked = navigateToUsers
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenerateUser(
    modifier: Modifier,
    state: GenerateUserScreenState.Generate,
    onGenderSelected: (String) -> Unit,
    onNationalitySelected: (String) -> Unit,
    onGenerateClicked: () -> Unit,
    onGoToUsersClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Generate user",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(text = "Select gender")
            Spacer(modifier = Modifier.height(8.dp))
            GenderDropdownMenu(
                options = listOf("male", "female"),
                selected = state.gender,
                onOptionSelected = onGenderSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Select nationality")
            Spacer(modifier = Modifier.height(8.dp))
            NationalityDropdownMenu(
                options = Nationality.entries.map { it.countryCode to it.countryFullName },
                selected = state.nationality,
                onOptionSelected = onNationalitySelected
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGenerateClicked,
            enabled = state.isSaveEnabled,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Generate")
            }
        }
        Button(
            onClick = onGoToUsersClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Открыть список пользователей")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdownMenu(
    options: List<String>,
    selected: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = selected.ifEmpty { "Select gender" },
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NationalityDropdownMenu(
    options: List<Pair<String, String>>,
    selected: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = if (selected.isNotEmpty()) {
                val nationality = Nationality.fromCode(selected)
                nationality?.countryFullName ?: "Select nationality"
            } else {
                "Select nationality"
            },
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            options.forEach { (code, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onOptionSelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
