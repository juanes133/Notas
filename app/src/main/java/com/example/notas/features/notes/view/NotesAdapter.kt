package com.example.notas.features.notes.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.R
import com.example.notas.model.NotesModel

class NotesAdapter(
    private val notesList: ArrayList<NotesModel>,
    private val onClickListener: (NotesModel) -> Unit,
    private val onDeleteListener: (NotesModel) -> Unit,
) :
    RecyclerView.Adapter<NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotesViewHolder(
            layoutInflater.inflate(R.layout.item_notes, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.render(notesList[position], onClickListener, onDeleteListener)
    }

    fun remove(notesModel: NotesModel) {
        val index = notesList.indexOf(notesModel)
        notesList.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, notesList.size)
    }

    override fun getItemCount(): Int = notesList.size
}