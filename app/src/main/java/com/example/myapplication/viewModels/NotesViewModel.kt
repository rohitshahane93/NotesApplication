package com.example.myapplication.viewModels

import androidx.lifecycle.*
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.data.repository.NotesRepository
import com.example.myapplication.data.util.Result
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val _notesStateLiveData = MediatorLiveData<GetAllNotesState>()
    val notesStateLiveData: LiveData<GetAllNotesState> = _notesStateLiveData

    fun start() {
        _notesStateLiveData.addSource(
            notesRepository.observeNotes().distinctUntilChanged(),
            ::onNotesChanged
        )
    }

    private fun onNotesChanged(result: Result<List<Note>>) {
        when (result) {
            is Result.Success -> _notesStateLiveData.value = GetAllNotesState.Loaded(result.data)
            is Result.Error -> _notesStateLiveData.value = GetAllNotesState.Error
            is Result.Loading -> _notesStateLiveData.value = GetAllNotesState.Loading
        }
    }

    fun deleteNotes(noteIds: List<String>) {
        viewModelScope.launch {
            notesRepository.deleteNotes(noteIds)
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
        }
    }

    sealed class GetAllNotesState {
        data class Loaded(val notes: List<Note>) : GetAllNotesState()
        object Loading : GetAllNotesState()
        object Error : GetAllNotesState()
    }

}