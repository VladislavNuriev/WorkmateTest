package com.example.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.example.profile.ui.Gradient
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

            val context = LocalContext.current
            val imageUri = File(context.filesDir, currentState.user.pictureFileName).toUri()

            var selectedTab by remember { mutableStateOf(0) } // 0 — телефон, 1 — почта, 2 — геолокация, 3 — доп. информация

            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onFinished }) {
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
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primaryFixed)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "User avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "Hi, how are you today? I'm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "${currentState.user.firstName} ${currentState.user.firstName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = { selectedTab = 0 },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (selectedTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Phone")
                    }
                    IconButton(
                        onClick = { selectedTab = 1 },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (selectedTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Mail, contentDescription = "Email")
                    }
                    IconButton(
                        onClick = { selectedTab = 2 },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (selectedTab == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location")
                    }
                    IconButton(
                        onClick = { selectedTab = 3 },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = if (selectedTab == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                }

                when (selectedTab) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            InfoItemRow(field = "Phone", value = currentState.user.cell)
                            Text(text = "Carrier: ${currentState.user.cell}", fontSize = 16.sp)
                        }
                    }

                    1 -> { // Почта
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(text = "Email: ${currentState.user.email}", fontSize = 16.sp)
                        }
                    }

                    2 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(text = "City: ${currentState.user.city}", fontSize = 16.sp)
                            Text(text = "Country: ${currentState.user.country}", fontSize = 16.sp)
                        }
                    }

                    3 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text(text = "First name: ${currentState.user.phone}", fontSize = 16.sp)

                        }
                    }
                }
            }
        }

        UserProfileScreenState.Initial -> {}
    }
}


@Composable
private fun ProfileInfoCard(modifier: Modifier = Modifier) {

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

