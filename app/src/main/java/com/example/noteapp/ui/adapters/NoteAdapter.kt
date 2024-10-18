package com.example.noteapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.component1
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.ui.intetface.OnClickItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoteAdapter(private val onLongClick: OnClickItem,
                  private val onClick: OnClickItem) : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(DiffCallBack()) {

    class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: NoteModel) {
            binding.txtTitle.text = item.title
            binding.txtDescription.text = item.description

            item.color?.let { binding.cardView.setCardBackgroundColor(it) }
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            val currentTime = timeFormat.format(calendar.time)
            binding.txtDate.text = currentDate
            binding.txtTime.text = currentTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
        holder.itemView.setOnLongClickListener {
            onLongClick.onLongClick(getItem(position))
            true
        }
        holder.itemView.setOnClickListener{
            onClick.onClick(getItem(position))
        }
    }

    fun updateItemColor(position: Int, color: Int) {
        val item = getItem(position)
        item.color = color
        notifyItemChanged(position)
    }

    class DiffCallBack : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem == newItem
        }
    }
}
