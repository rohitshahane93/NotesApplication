package com.example.myapplication.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.dataClasses.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Notes ORDER BY createdTime DESC")
    fun observeNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM Notes WHERE id = :id")
    fun observeNoteById(id: String): LiveData<Note>

    @Query("SELECT * FROM Notes")
    suspend fun getNotes(): List<Note>

    @Query("SELECT * FROM Notes WHERE id = :id")
    fun getNoteById(id: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note): Int

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNoteById(id: String): Int

    @Query("DELETE FROM Notes")
    suspend fun deleteNotes()

    @Query("delete from Notes where id in (:idList)")
    suspend fun deleteNotesById(idList: List<String>)

}