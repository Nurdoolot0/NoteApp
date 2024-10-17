package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        val currentTime = timeFormat.format(calendar.time)
        binding.tvDate.text = currentDate
        binding.tvTime.text = currentTime

    }

    private fun setupListeners() = with(binding) {
        btnAddText.setOnClickListener{
            val etTitle = titleEt.text.toString()
            val etDescription = etDescription.text.toString()
            App.appDataBase?.noteDao()?.insertNote(NoteModel(etTitle, etDescription))
            findNavController().navigateUp()
        }

    }
}