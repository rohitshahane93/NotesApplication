package com.example.myapplication.util.extensions

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.NotesApplication
import com.example.myapplication.R
import com.example.myapplication.viewModels.ViewModelFactory
import com.example.myapplication.views.custom.ScrollChildSwipeRefreshLayout

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as NotesApplication).notesRepository
    return ViewModelFactory(repository)
}

fun Fragment.setupRefreshLayout(
    refreshLayout: ScrollChildSwipeRefreshLayout,
    scrollUpChild: View? = null
) {
    refreshLayout.setColorSchemeColors(
        ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
        ContextCompat.getColor(requireActivity(), R.color.colorAccent),
        ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
    )
    // Set the scrolling view in the custom SwipeRefreshLayout.
    scrollUpChild?.let {
        refreshLayout.scrollUpChild = it
    }
}