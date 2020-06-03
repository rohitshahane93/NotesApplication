package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.repository.NotesRepository
import com.example.myapplication.data.util.DatabaseManager

class NotesApplication: Application() {

    val notesRepository: NotesRepository
        get() = DatabaseManager.provideNotesRepository(this)

}