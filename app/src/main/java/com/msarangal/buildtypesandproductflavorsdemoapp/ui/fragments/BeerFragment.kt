package com.msarangal.buildtypesandproductflavorsdemoapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.compose.collectAsLazyPagingItems
import com.msarangal.buildtypesandproductflavorsdemoapp.MainViewModel
import com.msarangal.buildtypesandproductflavorsdemoapp.ui.composables.BeerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BeerScreen(beers = viewModel.beerPagingFlow.collectAsLazyPagingItems())
            }
        }
    }
}