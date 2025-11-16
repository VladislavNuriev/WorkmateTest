package com.example.users

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.model.User
import com.example.ui.theme.Gradient
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel(),
    onUserClick: (User) -> Unit,
    onAddUserClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onAddUserClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = Gradient.PrimaryGradient),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddUserClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add user"
                )
            }
        }
    ) { innerPadding ->
        UsersList(
            modifier = modifier.padding(innerPadding),
            state = state,
            onQueryChanged = { query ->
                viewModel.handleIntent(UsersIntent.SearchQueryChanged(query))
            },
            onUserClick = onUserClick,
            onUserDelete = {
                viewModel.handleIntent(UsersIntent.DeleteUser(it.id, it.pictureFileName))
            }
        )
    }
}


@Composable
private fun UsersList(
    modifier: Modifier = Modifier,
    state: UsersScreenState,
    onQueryChanged: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onUserDelete: (User) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        SearchBar(
            onQueryChanged = onQueryChanged,
            query = state.searchQuery,
        )
        if (state.users.isEmpty()) {
            NoSearchResultPlaceHolder()
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = state.users,
                    key = { it.id }
                ) {
                    UserItem(
                        user = it,
                        onClick = onUserClick,
                        onDelete = onUserDelete
                    )
                    Log.d("User", "UsersList: ${it.pictureFileName} ")
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = modifier
                .weight(1f)
                .height(50.dp)
                .shadow(0.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            placeholder = {
                if (!isFocused) {
                    Text(
                        text = "Введи имя...",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            value = query,
            onValueChange = { onQueryChanged(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search icon",
                )
            },
            trailingIcon = {
                if (isFocused && query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Clear search",
                        Modifier.clickable { onQueryChanged("") },
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else if (isFocused) {
                    Box(Modifier.size(24.dp))
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            )
        )
        if (isFocused) {
            val focusManager = LocalFocusManager.current
            Text(
                text = "Отмена",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        focusManager.clearFocus()
                        onQueryChanged("")
                    },
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    onClick: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    val context = LocalContext.current
    val imageUri = File(context.filesDir, user.pictureFileName).toUri()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(top = 6.dp, bottom = 6.dp, end = 4.dp)
            .clickable(enabled = true, onClick = { onClick(user) })
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primaryFixed)
                .align(Alignment.CenterStart)

        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "User avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .padding(start = 88.dp), // 72dp + 16dp
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "${user.firstName} ${user.lastName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = user.phone,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            onClick = { onDelete(user) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete user",
                tint = MaterialTheme.colorScheme.error  // Красный цвет для иконки удаления
            )
        }
    }
}


@Composable
fun NoSearchResultPlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "No results",
                modifier = Modifier.size(56.dp),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Мы никого не нашли",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Попробуйте скорректировать запрос",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}