package com.example.notas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    suspend fun getAll(): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: NoteEntity)

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteAll(id: kotlin.String)

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getById(id: String): List<NoteEntity>
}