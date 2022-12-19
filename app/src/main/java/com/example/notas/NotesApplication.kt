package com.example.notas

import android.app.Application
import com.example.notas.database.NotesDb
import com.example.notas.features.notes.repository.NotesRepository

class NotesApplication : Application() {

    private val roomDatabase by lazy { NotesDb.getDatabase(this) }
    val notesRepository by lazy { NotesRepository(roomDatabase.noteDao()) }
}