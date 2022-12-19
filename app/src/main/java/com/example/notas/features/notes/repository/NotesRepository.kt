package com.example.notas.features.notes.repository

import com.example.notas.database.NoteDao
import com.example.notas.database.NoteEntity
import com.example.notas.model.NotesModel

class NotesRepository(private val notesDao: NoteDao) {

    suspend fun getNotes(
        onSuccess: (ArrayList<NoteEntity>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
       try {
           onSuccess(
               notesDao.getAll() as ArrayList<NoteEntity>
           )
       } catch (e: Exception){
           onFailure(e)
       }
    }

    suspend fun insertNotes(
        notesModel: NotesModel,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val notes = NoteEntity(
                notesModel.id,
                notesModel.title,
                notesModel.note,
                notesModel.date
            )
            notesDao.insertAll(notes)
            onSuccess()
        }catch (e: Exception){
            onFailure(e)
        }
    }

    suspend fun deleteNotes(
        notesModel: NotesModel,
        onSuccess: (NotesModel) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        try {
            notesDao.deleteAll(notesModel.id)
            onSuccess(notesModel)
        }catch (e: Exception){
            onFailure(e)
        }
    }

    suspend fun findById(
        idNotes: String,
        onSuccess: (List<NoteEntity>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            onSuccess(notesDao.getById(idNotes) as ArrayList<NoteEntity>)
        }catch (e: Exception){
            if (e is NoSuchElementException){
                onSuccess(ArrayList())
            }else{
                onFailure(e)
            }
        }
    }
}