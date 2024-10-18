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
import androidx.appcompat.app.AlertDialog

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding
    private var selectedColor: Int? = null
    private var noteId: Int? = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNote()
        setupListeners()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        val currentTime = timeFormat.format(calendar.time)
        binding.tvDate.text = currentDate
        binding.tvTime.text = currentTime

        binding.btnMore.setOnClickListener {
            showColorPickerDialog()
        }
    }

    private fun updateNote() {
        arguments?.let { args ->
            noteId = args.getInt("noteId", -1)
        }
        if (noteId != -1){
            val argsNote = App.appDataBase?.noteDao()?.getNoteById(noteId)
            argsNote?.let { item ->
                binding.titleEt.setText(item.title)
                binding.etDescription.setText(item.title)
            }
        }
    }

    private fun setupListeners() = with(binding) {
        btnAddText.setOnClickListener {
            val etTitle = titleEt.text.toString()
            val etDescription = etDescription.text.toString()
            if(noteId != -1){
                val updateNote = NoteModel(etTitle, etDescription,  color = selectedColor)
                updateNote.id = noteId!!
                App.appDataBase?.noteDao()?.updateNote(updateNote)
            } else{
                App.appDataBase?.noteDao()?.insertNote(NoteModel(etTitle, etDescription, selectedColor))
            }
            findNavController().navigateUp()
        }
    }

    private fun showColorPickerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_color_picker, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<View>(R.id.colorButton1).setOnClickListener {
            selectedColor = resources.getColor(R.color.color1, null)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.colorButton2).setOnClickListener {
            selectedColor = resources.getColor(R.color.color2, null)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.colorButton3).setOnClickListener {
            selectedColor = resources.getColor(R.color.color3, null)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.colorButton4).setOnClickListener {
            selectedColor = resources.getColor(R.color.color4, null)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.colorButton5).setOnClickListener {
            selectedColor = resources.getColor(R.color.color5, null)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.colorButton6).setOnClickListener {
            selectedColor = resources.getColor(R.color.color6, null)
            dialog.dismiss()
        }

        dialog.show()
    }
}
