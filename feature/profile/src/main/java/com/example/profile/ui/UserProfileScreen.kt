package com.example.profile.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.domain.model.User
import com.example.profile.UserProfileViewModel
import com.example.ui.theme.Gradient
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    userId: Int,
    viewModel: UserProfileViewModel = hiltViewModel(
        creationCallback = { factory: UserProfileViewModel.Factory ->
            factory.create(userId)
        }
    ),
    onFinished: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is UserProfileScreenState.UserProfile -> {
            val user = currentState.user
            val context = LocalContext.current
            val imageUri = File(context.filesDir, currentState.user.pictureFileName).toUri()



            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onFinished) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(brush = Gradient.PrimaryGradient),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 75.dp, horizontal = 16.dp)
            ) {
                AvatarImage(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    imageUri = imageUri
                )
                GreetingAndName(
                    user = user,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                ProfileInfoCard(user = user)
            }
        }

        UserProfileScreenState.Initial -> {}
    }
}

@Composable
private fun AvatarImage(
    modifier: Modifier = Modifier,
    imageUri: Uri
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.primaryFixed)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "User avatar",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun GreetingAndName(
    modifier: Modifier = Modifier,
    user: User
) {
    Text(
        text = "Hi, how are you today? I'm",
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
            .padding(top = 8.dp)
    )
    Text(
        text = "${user.firstName} ${user.lastName}",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 4.dp)
    )
}


@Composable
private fun ProfileInfoCard(
    modifier: Modifier = Modifier,
    user: User
) {
    var selectedInfoTab by remember { mutableStateOf(0) }

    ElevatedCard(
        modifier = modifier
            .height(300.dp)
            .padding(top = 24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            selectedInfoTab = 0
                        }
                        .background(
                            color = if (selectedInfoTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                    contentAlignment = Alignment.Center,
                )
                {
                    Icon(
                        Icons.Default.Person, contentDescription = "Person",
                        tint = if (selectedInfoTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            selectedInfoTab = 1
                        }
                        .background(
                            color = if (selectedInfoTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                    contentAlignment = Alignment.Center,
                )
                {
                    Icon(
                        Icons.Default.Call, contentDescription = "Phone",
                        tint = if (selectedInfoTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            selectedInfoTab = 2
                        }
                        .background(
                            color = if (selectedInfoTab == 2) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                    contentAlignment = Alignment.Center,
                )
                {
                    Icon(
                        Icons.Default.Email, contentDescription = "Email",
                        tint = if (selectedInfoTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            selectedInfoTab = 3
                        }
                        .background(
                            color = if (selectedInfoTab == 3) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                        ),
                    contentAlignment = Alignment.Center,
                )
                {
                    Icon(
                        Icons.Default.LocationOn, contentDescription = "Location",
                        tint = if (selectedInfoTab == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            when (selectedInfoTab) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    ) {
                        InfoItemRow(field = "First name", value = user.firstName)
                        InfoItemRow(field = "Last name", value = user.lastName)
                        InfoItemRow(field = "Gender", value = user.gender)
                        InfoItemRow(field = "Age", value = "${user.age}")
                        InfoItemRow(field = "Date of birth", value = user.birthDate)
                    }
                }

                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    ) {
                        InfoItemRow(field = "Phone", value = user.phone)
                        InfoItemRow(field = "Cell", value = user.cell)
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    ) {
                        InfoItemRow(field = "Email", value = user.email)
                    }
                }

                3 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                    ) {
                        InfoItemRow(field = "City", value = user.city)
                        InfoItemRow(field = "State", value = user.state)
                        InfoItemRow(field = "Country", value = user.country)
                        InfoItemRow(field = "Post code", value = user.postcode)
                    }
                }
            }
        }
    }
}


@Composable
private fun InfoItemRow(field: String, value: String) {
    Row {
        Text(
            text = "$field: ",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

