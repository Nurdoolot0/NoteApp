package com.example.noteapp.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.ui.intetface.OnClickItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NoteAdapter(
    private val onLongClick: OnClickItem,
    private val onClick: OnClickItem
) : ListAdapter<NoteModel, NoteAdapter.ViewHolder>(DiffCallBack()) {
    private var isLinearLayout: Boolean = true
    fun setLayoutType(isLinear: Boolean) {
        isLinearLayout = isLinear
    }

    class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: NoteModel, isLinearLayout: Boolean) {
            binding.txtTitle.text = item.title
            binding.txtDescription.text = item.description
            item.color?.let { binding.cardView.setCardBackgroundColor(it) }
            val backgroundColor = item.color ?: Color.WHITE
            binding.cardView.setCardBackgroundColor(backgroundColor)
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            val currentTime = timeFormat.format(calendar.time)
            binding.txtDate.text = currentDate
            binding.txtTime.text = currentTime
            val layoutParams = binding.root.layoutParams
            if (isLinearLayout) {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            } else {

            }
            binding.root.layoutParams = layoutParams
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
        if (position < currentList.size) {
            val note = getItem(position)
            holder.onBind(note, isLinearLayout)
            holder.itemView.setOnLongClickListener {
                onLongClick.onLongClick(note)
                true
            }
            holder.itemView.setOnClickListener {
                onClick.onClick(note)
            }
        } else {
            Log.e("NoteAdapter", "Invalid position: $position for list of size ${currentList.size}")
        }
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