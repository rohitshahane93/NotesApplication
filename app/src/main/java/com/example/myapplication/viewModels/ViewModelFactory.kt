package com.example.myapplication.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.NotesRepository


class ViewModelFactory(private val notesRepository: NotesRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            return NotesViewModel(notesRepository) as T
        }
        if (modelClass.isAssignableFrom(NotesDetailViewModel::class.java)) {
            return NotesDetailViewModel(notesRepository) as T
        }
        throw IllegalArgumentException("ViewModelFactory cannot provide viewModel for type ${modelClass.name}")
    }
}