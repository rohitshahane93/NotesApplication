package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.data.util.Result

interface NotesRepository {
    fun observeNotes(): LiveData<Result<List<Note>>>

    suspend fun getNotes(): Result<List<Note>>

    fun observeNote(noteId: String): LiveData<Result<Note>>

    suspend fun getNote(noteId: String): Result<Note>

    suspend fun saveNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteAllNotes()

    suspend fun deleteNote(noteId: String)

    suspend fun deleteNotes(noteId: List<String>)
}