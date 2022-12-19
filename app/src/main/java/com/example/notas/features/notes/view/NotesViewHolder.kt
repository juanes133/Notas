package com.example.notas.features.notes.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.databinding.ItemNotesBinding
import com.example.notas.model.NotesModel

class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemNotesBinding.bind(view)

    fun render(
        notesModel: NotesModel,
        onClickListener: (NotesModel) -> Unit,
        onDeleteListener: (NotesModel) -> Unit
    ) {
        binding.title.text = notesModel.title
        binding.note.text = notesModel.note
        binding.date.text = notesModel.date
        itemView.setOnClickListener {
            onClickListener(notesModel)
        }
        binding.iconDelete.setOnClickListener {
            onDeleteListener(notesModel)
        }
    }

}
