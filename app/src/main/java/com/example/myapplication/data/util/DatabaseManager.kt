package com.example.myapplication.data.util

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.NotesDatabase
import com.example.myapplication.data.daos.NotesDao
import com.example.myapplication.data.repository.NotesRepository
import com.example.myapplication.data.repository.NotesRepositoryImpl

object DatabaseManager {
    private var database: NotesDatabase? = null
    @Volatile
    var notesRepository: NotesRepository? = null

    fun provideNotesRepository(context: Context): NotesRepository {
        synchronized(this) {
            return notesRepository ?: createNotesRepository(context)
        }
    }

    private fun createNotesRepository(context: Context): NotesRepository {
        val database = database ?: createDataBase(context)
        val newRepo = NotesRepositoryImpl(database.notesDao())
        notesRepository = newRepo
        return newRepo
    }

    private fun createDataBase(context: Context): NotesDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java, "Notes.db"
        ).build()
        database = result
        return result
    }
}