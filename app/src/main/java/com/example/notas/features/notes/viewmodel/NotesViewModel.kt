package com.example.notas.features.notes.viewmodel

import androidx.lifecycle.*
import com.example.notas.Event
import com.example.notas.features.notes.repository.NotesRepository
import com.example.notas.model.NotesModel
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val mutableNoteList = MutableLiveData<Event<ArrayList<NotesModel>>>()
    val notesList: LiveData<Event<ArrayList<NotesModel>>> get() = mutableNoteList

    private val mutableNoteDeleted = MutableLiveData<Event<NotesModel>>()
    val notesDeleted: LiveData<Event<NotesModel>> get() = mutableNoteDeleted

    private val mutableNoteError = MutableLiveData<Event<Exception>>()
    val notesError: LiveData<Event<Exception>> get() = mutableNoteError

    fun getNotes() {
        viewModelScope.launch {
            notesRepository.getNotes({ list ->
                val result = ArrayList<NotesModel>()
                list.forEach {
                    result.add(
                        NotesModel(
                            it.id,
                            it.title,
                            it.note,
                            it.date
                        )
                    )
                }
                mutableNoteList.value = Event(result)
            }, {
                mutableNoteError.value = Event(it)
            })
        }
    }

    fun deleteNotes(notesModel: NotesModel){
        viewModelScope.launch {
            notesRepository.deleteNotes(notesModel, {
                mutableNoteDeleted.value = Event(it)
            }) {
                mutableNoteError.value = Event(it)
            }
        }
    }
}

class NotesViewModelFactory(
    private val noteRepository: NotesRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
