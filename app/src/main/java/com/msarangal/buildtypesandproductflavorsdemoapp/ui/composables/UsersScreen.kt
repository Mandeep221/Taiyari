package com.msarangal.buildtypesandproductflavorsdemoapp.ui.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msarangal.buildtypesandproductflavorsdemoapp.MainViewModel
import com.msarangal.buildtypesandproductflavorsdemoapp.UserUiState

@Composable
fun UsersScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onUserClick: (Int) -> Unit
) {
    val state by viewModel.uiStateFlow.collectAsState()
    val snackBarState by viewModel.snackBarSharedFlow.collectAsState(false)
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.snackBarSharedFlow.collect { shouldLaunchToast ->
            if (shouldLaunchToast) {
                Toast.makeText(context, "Data loaded successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    when (state) {
        is UserUiState.Loading -> {
            UserDataView(textToDisplay = "Loading", modifier = modifier)
        }

        is UserUiState.Success -> {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (state as UserUiState.Success).users.forEach {
                    item {
                        UserDataView(
                            textToDisplay = "${it.name} || ${it.username}",
                            modifier = Modifier
                                .background(color = Color.LightGray)
                                .clickable {
                                    onUserClick.invoke(it.id)
                                }
                        )
                    }
                }
            }
        }

        is UserUiState.Failure -> {
            UserDataView(textToDisplay = "Failure", modifier = modifier)
        }
    }
}

@Composable
fun UserDataView(textToDisplay: String, modifier: Modifier) {
    Text(
        text = "$textToDisplay",
        modifier = modifier
            .fillMaxSize()
    )
}

@Composable
fun UserItemView() {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UserDataView(textToDisplay = "Mandeep Sarangal", modifier = Modifier)

}