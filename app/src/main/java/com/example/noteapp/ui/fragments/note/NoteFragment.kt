package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.models.Note
import com.example.noteapp.ui.adapters.NoteAdapter

class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: List<Note>
    private var filteredNoteList: List<Note> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteList = getNotes()
        noteAdapter = NoteAdapter(noteList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = noteAdapter
        setupSearchView()
    }
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })
    }

    private fun filterNotes(query: String?) {
        filteredNoteList = if (query.isNullOrEmpty()) {
            noteList
        } else {
            noteList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
            }
        }
        noteAdapter.updateList(filteredNoteList)
    }
    private fun getNotes(): List<Note> {
        return listOf(
            Note("План на жизнь", "Посадить сына, вырастить дом, построить дерево. Нужно...", "31 мая 12:45", "Личная"),
            Note("Нужно сделать", "Работы с проектом, сделать домашку, построить бизнес и...", "31 мая 12:45", "Работа"),
            Note("Нужно сделать", "Работы с проектом, сделать домашку, построить бизнес и...", "31 мая 12:45", "Другое")
        )
    }
}