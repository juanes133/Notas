package com.example.notas.features.addnotes.viewmodel

import androidx.lifecycle.*
import com.example.notas.Event
import com.example.notas.features.notes.repository.NotesRepository
import com.example.notas.model.NotesModel
import kotlinx.coroutines.launch

class AddNotesViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    private val mutableNoteList = MutableLiveData<Event<ArrayList<NotesModel>>>()
    val noteList: LiveData<Event<ArrayList<NotesModel>>> get() = mutableNoteList

    private val mutableNoteInsert = MutableLiveData<Event<Boolean>>()
    val noteInsert: LiveData<Event<Boolean>> get() = mutableNoteInsert

    private val mutableNoteError = MutableLiveData<Event<Exception>>()
    val noteError: LiveData<Event<Exception>> get() = mutableNoteError

    fun insertNotes(notesModel: NotesModel) {
        viewModelScope.launch {
            notesRepository.insertNotes(notesModel, {
                mutableNoteInsert.value = Event(true)
            }, {
                mutableNoteError.value = Event(it)
            })
        }
    }

    fun findById(id: String) {
        viewModelScope.launch {
            notesRepository.findById(id, { list ->
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
            }) {
                mutableNoteError.value = Event(it)
            }
        }
    }
}

class AddNotesViewModelFactory(
    private val notesRepository: NotesRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNotesViewModel(notesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
