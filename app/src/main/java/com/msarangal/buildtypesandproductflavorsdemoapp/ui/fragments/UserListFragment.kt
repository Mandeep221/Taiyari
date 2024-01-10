package com.msarangal.buildtypesandproductflavorsdemoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.msarangal.buildtypesandproductflavorsdemoapp.MainViewModel
import com.msarangal.buildtypesandproductflavorsdemoapp.R
import com.msarangal.buildtypesandproductflavorsdemoapp.ui.composables.UsersScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(context = requireContext()).apply {
            setContent {
                UsersScreen(viewModel = viewModel, onUserClick = ::handleUserClick)
            }
        }
    }

    private fun handleUserClick(userId: Int) {
        findNavController().navigate(R.id.action_userListFragment_to_userPostsFragment)
        //viewModel.fetchPostsByUser(userId = userId.toString())
        //viewModel.fetchPostsWithSeriesApiCall()
        Toast
            .makeText(requireContext(), "$userId", Toast.LENGTH_SHORT)
            .show()
    }
}