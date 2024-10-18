package com.example.noteapp.data.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteapp.data.models.NoteModel

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(noteModel: NoteModel)

    @Query("SELECT * FROM notemodel")
    fun getAll(): LiveData<List<NoteModel>>

    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("DELETE FROM noteModel WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Update
    fun updateNote(noteModel: NoteModel)

    @Query("SELECT * FROM notemodel WHERE id = :id")
    fun getNoteById(id: Int?):NoteModel?

    @Query("UPDATE noteModel SET color = :color WHERE id = :noteId")
    suspend fun updateColor(noteId: Int, color: Int)

    @Query("SELECT * FROM noteModel WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteModel?

}