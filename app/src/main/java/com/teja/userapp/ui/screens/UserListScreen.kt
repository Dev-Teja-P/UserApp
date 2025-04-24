package com.teja.userapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.teja.userapp.data.model.Location
import com.teja.userapp.data.model.Name
import com.teja.userapp.data.model.Picture
import com.teja.userapp.data.model.Street
import com.teja.userapp.data.model.User
import com.teja.userapp.ui.navigation.Screen
import com.teja.userapp.ui.state.UserUiState
import com.teja.userapp.ui.viewmodel.UserViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(navController: NavController, viewModel: UserViewModel) {
    val userUiState by viewModel.userUiState.observeAsState()
    var input by remember { mutableStateOf("10") }
    var isTextFiledError by remember { mutableStateOf(false) }
    val isLoading = userUiState is UserUiState.Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "User List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.6f),
                    value = input,
                    onValueChange = { value ->
                        if (value.all { it.isDigit() }) {
                            input = value
                            isTextFiledError = (value.toIntOrNull() ?: 0) < 1
                        }
                    },
                    label = {
                        Text(
                            text = "User count",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    isError = isTextFiledError,
                    supportingText = {
                        if (isTextFiledError) {
                            Text(
                                "Value must be at least 1",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(0.4f),
                    onClick = {
                        input.toIntOrNull()?.let {
                            viewModel.getUsers(it)
                        }
                    },
                    enabled = !isLoading
                ) {
                    Text(text = "Get Users")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (userUiState) {
                    is UserUiState.Loading -> CircularProgressIndicator()
                    is UserUiState.Success -> LoadUserList(
                        navController = navController,
                        userList = (userUiState as UserUiState.Success).data
                    )

                    is UserUiState.Error -> ShowErrorScreen(
                        errorMessage = (userUiState as UserUiState.Error).message
                    )

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun LoadUserList(userList: List<User>, navController: NavController) {
    LazyColumn {
        items(userList) { user ->
            UserCard(user = user) {
                navController.navigate(
                    Screen.DetailScreen.createRoute(
                        name = "${user.name.first} ${user.name.last}",
                        address = "${user.location.street.number}, ${user.location.street.name}",
                        image = URLEncoder.encode(user.picture.large, "UTF-8")
                    )
                )
            }
        }
    }
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(user.picture.medium),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "${user.name.first} ${user.name.last}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "${user.location.street.number} ${user.location.street.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ShowErrorScreen(errorMessage: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
    UserCard(
        user = User(
            name = Name("Reema", "Uchil"),
            location = Location(Street(6818, "Colaba Causeway")),
            picture = Picture(
                large = "https://randomuser.me/api/portraits/women/23.jpg",
                medium = "https://randomuser.me/api/portraits/medium/women/23.jpg"
            )
        ),
        onClick = {}
    )
}
