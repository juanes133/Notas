package com.example.notas.features.addnotes.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notas.NotesApplication
import com.example.notas.databinding.FragmentAddNotesBinding
import com.example.notas.features.addnotes.viewmodel.AddNotesViewModel
import com.example.notas.features.addnotes.viewmodel.AddNotesViewModelFactory
import com.example.notas.model.NotesModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class NotesAddFragment : Fragment() {

    private lateinit var binding: FragmentAddNotesBinding
    private var notes: NotesModel? = null
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
    private val formatted = current.format(formatter)
    private val args: NotesAddFragmentArgs by navArgs()
    private val addNotesViewModel: AddNotesViewModel by viewModels {
        AddNotesViewModelFactory(
            (activity?.application as NotesApplication).notesRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        addNotesViewModel.findById(args.idnotes)
        binding.btnAddNote.isEnabled = false
        addNotesViewModel.noteList.observe(viewLifecycleOwner) { it ->
            it.getContentIfNotHandled()?.let { notesDB ->
                notes = notesDB.firstOrNull()
                notes?.let {
                    binding.title.setText(it.title)
                    binding.note.setText(it.note)
                    binding.date.setText(formatted)

                }
                binding.btnAddNote.isEnabled = true
            }
        }
        binding.btnAddNote.setOnClickListener {
            if (notes == null) {
                notes = NotesModel(UUID.randomUUID().toString(),
                    binding.title.text.toString(),
                    binding.note.text.toString(),
                    formatted.toString())
            } else {
                notes?.let { note ->
                    notes = NotesModel(note.id,
                        binding.title.text.toString(),
                        binding.note.text.toString(),
                        formatted.toString())
                }
            }
            insertNotes()
        }
        addNotesViewModel.noteInsert.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        addNotesViewModel.noteError.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun insertNotes() {
        context?.let {
            notes?.let { note ->
                addNotesViewModel.insertNotes(note)
            }
        }
    }
}










