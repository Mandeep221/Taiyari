package com.msarangal.buildtypesandproductflavorsdemoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.msarangal.buildtypesandproductflavorsdemoapp.MainViewModel
import com.msarangal.buildtypesandproductflavorsdemoapp.ui.composables.PostsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPostsFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setContent {
                PostsScreen(viewModel)
            }
        }
    }
}