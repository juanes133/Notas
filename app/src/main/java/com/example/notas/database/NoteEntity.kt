package com.example.notas.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val note: String,
    val date: String
)
