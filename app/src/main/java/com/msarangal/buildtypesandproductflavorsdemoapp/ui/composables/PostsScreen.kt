package com.msarangal.buildtypesandproductflavorsdemoapp.ui.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.msarangal.buildtypesandproductflavorsdemoapp.MainViewModel
import com.msarangal.buildtypesandproductflavorsdemoapp.PostUiState
import com.msarangal.buildtypesandproductflavorsdemoapp.data.remote.model.Post

@Composable
fun PostsScreen(
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current

    val state by mainViewModel.uiStateFlowPosts.collectAsState()
    when (state) {
        PostUiState.Loading -> {
            showMessage(msg = "Loading Posts..", context = context)
        }

        is PostUiState.Failure -> showMessage(
            msg = (state as PostUiState.Failure).errorMessage,
            context = context
        )

        is PostUiState.Success -> {
//            val msg = (state as PostUiState.Success).posts.toString()
//            Log.d("Drake", msg)
            PostsView(posts = (state as PostUiState.Success).posts)
        }
    }
}

@Composable
fun PostsView(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        posts.forEach {
            item {
                PostItemView(post = it)
            }
        }
    }
}

@Composable
fun PostItemView(post: Post) {
    Column {
        Text(text = post.title, modifier = Modifier.background(color = Color.Gray))
        Text(text = post.body, modifier = Modifier.background(color = Color.LightGray))
    }
}

private fun showMessage(msg: String, context: Context) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}