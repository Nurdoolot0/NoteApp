package com.example.noteapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.models.Note
import androidx.core.content.ContextCompat
import com.example.noteapp.R

class NoteAdapter(private var noteList: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.noteDate.text = note.date

            when (note.type) {
                "Личная" -> binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.note_color1)
                )
                "Работа" -> binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.note_color2)
                )
                "Другое" -> binding.root.setCardBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.note_color3)
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = noteList.size
    fun updateList(newNoteList: List<Note>) {
        noteList = newNoteList
        notifyDataSetChanged()
    }
}