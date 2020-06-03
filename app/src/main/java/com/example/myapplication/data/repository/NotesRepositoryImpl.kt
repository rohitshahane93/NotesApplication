package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.myapplication.data.daos.NotesDao
import com.example.myapplication.data.dataClasses.Note
import com.example.myapplication.data.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val notesDao: NotesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : NotesRepository {

    override fun observeNotes(): LiveData<Result<List<Note>>> {
        return notesDao.observeNotes().map {
            Result.Success(it)
        }
    }

    override suspend fun getNotes(): Result<List<Note>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(notesDao.getNotes())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun observeNote(noteId: String): LiveData<Result<Note>> {
        return notesDao.observeNoteById(noteId).map {
            Result.Success(it)
        }
    }

    override suspend fun getNote(noteId: String): Result<Note> = withContext(ioDispatcher) {
        try {
            val note = notesDao.getNoteById(noteId)
            if (note != null) {
                return@withContext Result.Success(note)
            } else {
                return@withContext Result.Error(Exception("Note not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun saveNote(note: Note) = withContext(ioDispatcher) {
        notesDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) =
        withContext<Unit>(ioDispatcher) { notesDao.updateNote(note) }

    override suspend fun deleteAllNotes() = withContext(ioDispatcher) {
        notesDao.deleteNotes()
    }

    override suspend fun deleteNote(noteId: String) = withContext<Unit>(ioDispatcher) {
        notesDao.deleteNoteById(noteId)
    }

    override suspend fun deleteNotes(noteIds: List<String>) {
        notesDao.deleteNotesById(noteIds)
    }

}