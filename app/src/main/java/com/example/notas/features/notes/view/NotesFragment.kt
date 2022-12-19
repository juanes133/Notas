package com.example.notas.features.notes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notas.NotesApplication
import com.example.notas.databinding.FragmentNotesBinding
import com.example.notas.features.notes.viewmodel.NotesViewModel
import com.example.notas.features.notes.viewmodel.NotesViewModelFactory
import com.example.notas.model.NotesModel
import java.util.*
import kotlin.collections.ArrayList

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private var adapter: NotesAdapter? = null
    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory(
            (activity?.application as NotesApplication).notesRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToNotesAddFragment(
                (UUID.randomUUID().toString())
            ))
        }
        notesViewModel.notesList.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { list ->
                initRecyclerView(list)
                binding.notesContainer.isVisible = true
            }
        }
        notesViewModel.notesDeleted.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { note->
                binding.notesContainer.isVisible = true
                adapter?.remove(note)
            }

        }
        notesViewModel.notesError.observe(viewLifecycleOwner) {
            binding.notesContainer.isVisible = false
        }
        notesViewModel.getNotes()
        return binding.root
    }

    private fun initRecyclerView(list: ArrayList<NotesModel>) {
        binding.recyclerNotes.layoutManager = LinearLayoutManager(context)
        adapter = NotesAdapter(list, {
            findNavController().navigate(NotesFragmentDirections.actionNotesFragmentToNotesAddFragment(
                (it.id)))
        },
            {
                notesViewModel.deleteNotes(it)
            })
        binding.recyclerNotes.adapter = adapter
    }

    private fun getNotes(){
        activity?.applicationContext.let {
            notesViewModel.getNotes()
        }
    }

    override fun onResume() {
        super.onResume()
        getNotes()
    }

}