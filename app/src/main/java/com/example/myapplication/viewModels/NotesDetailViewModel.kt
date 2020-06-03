package com.example.myapplication.viewModels

import androidx.lifecycle.*
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.data.repository.NotesRepository
import com.example.myapplication.data.util.Result
import kotlinx.coroutines.launch

class NotesDetailViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    var noteId: String? = null
    var note: Note? = null

    private val _noteStateLiveData = MediatorLiveData<GetNoteState>()
    val noteStateLiveData: LiveData<GetNoteState> = _noteStateLiveData

    fun start(noteId: String) {
        this.noteId = noteId
        _noteStateLiveData.addSource(
            notesRepository.observeNote(noteId).distinctUntilChanged(), ::onNoteChanged
        )
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }

    private fun onNoteChanged(result: Result<Note>) {
        when (result) {
            is Result.Success -> {
                this.note = result.data
                _noteStateLiveData.value = GetNoteState.Loaded(result.data)
            }
            is Result.Error -> _noteStateLiveData.value = GetNoteState.Error
            is Result.Loading -> _noteStateLiveData.value = GetNoteState.Loading
        }
    }

    sealed class GetNoteState {
        data class Loaded(val note: Note) : GetNoteState()
        object Loading : GetNoteState()
        object Error : GetNoteState()
    }

}