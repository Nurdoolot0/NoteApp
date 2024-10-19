package com.example.noteapp.ui.fragments.note

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.ui.adapters.NoteAdapter
import com.example.noteapp.ui.intetface.OnClickItem
import com.example.noteapp.utils.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteFragment : Fragment(), OnClickItem {

    private lateinit var binding: FragmentNoteBinding
    private val noteAdapter = NoteAdapter(this, this)
    private var isLinearLayout = true
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        preferenceHelper = PreferenceHelper()
        preferenceHelper.init(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLayoutState()
        initialize()
        setupListeners()
        getData()
        binding.addBtn.bringToFront()
        binding.addBtn.show()
    }

    private fun initialize() {
        setRecyclerViewLayoutManager()
        binding.recyclerView.adapter = noteAdapter
        updateSwitchIcon()
    }

    private fun setupListeners() = with(binding) {
        addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_noteDetailFragment)
        }

        menuSwitchIcon.setOnClickListener {
            isLinearLayout = !isLinearLayout
            setRecyclerViewLayoutManager()
            noteAdapter.setLayoutType(isLinearLayout)
            updateSwitchIcon()
            saveLayoutState(isLinearLayout)
        }

        setIc.setOnClickListener {
            isLinearLayout = !isLinearLayout
            setRecyclerViewLayoutManager()
            noteAdapter.setLayoutType(isLinearLayout)
            updateSwitchIcon()
            saveLayoutState(isLinearLayout)
        }
    }

    private fun setRecyclerViewLayoutManager() {
        if (isLinearLayout) {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun updateSwitchIcon() {
        if (isLinearLayout) {
            binding.menuSwitchIcon.setImageResource(R.drawable.grid_lay)
            binding.setIc.visibility = View.INVISIBLE
            binding.menuSwitchIcon.visibility = View.VISIBLE
        } else {
            binding.setIc.setImageResource(R.drawable.ic_set)
            binding.menuSwitchIcon.visibility = View.INVISIBLE
            binding.setIc.visibility = View.VISIBLE

        }
    }

    private fun saveLayoutState(isLinearLayout: Boolean) {
        preferenceHelper.isLinearLayout = isLinearLayout
    }

    private fun loadLayoutState() {
        isLinearLayout = preferenceHelper.isLinearLayout
    }

    private fun getData() {
        App.appDataBase?.noteDao()?.getAll()?.observe(viewLifecycleOwner) { listNote ->
            noteAdapter.submitList(listNote)
        }
    }

    override fun onLongClick(noteModel: NoteModel) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle("Удалить заметку?")
            setPositiveButton("Удалить") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    App.appDataBase?.noteDao()?.deleteNote(noteModel)
                    withContext(Dispatchers.Main) {
                        getData()
                    }
                }
            }
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
        builder.create()
    }

    override fun onClick(noteModel: NoteModel) {
        val action =
            NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(noteModel.id)
        findNavController().navigate(action)
    }
}