package com.example.noteapp.ui.fragments.note

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.ui.adapters.NoteAdapter
import com.example.noteapp.ui.intetface.OnClickItem
import com.example.noteapp.ui.viewmodels.NoteViewModel
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
    private val viewModel: NoteViewModel by viewModels()

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra("title") ?: "Новое уведомление"
            val message = intent?.getStringExtra("message") ?: "Новое сообщение"
            showNotificationDialog(title, message)
            if (context != null) {
                showNotification(context, title, message)
            }
        }

        private fun showNotification(context: Context, title: String, message: String) {
            val channelId = "my_channel_id"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "MyChannel"
                val descriptionText = "Channel for app notifications"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder.build())
            }
        }
    }

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

        if (shouldShowNotificationPermission() && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission()
        }
        var notificationTitle = requestNotificationPermission()

        loadLayoutState()
        initialize()
        setupListeners()
        getData()

        viewModel.notificationTitle.observe(viewLifecycleOwner, Observer { title ->
            viewModel.notificationMessage.observe(viewLifecycleOwner, Observer { message ->
                showNotificationDialog(title, message)
            })
        })

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(notificationReceiver, IntentFilter("PushNotification"))

        binding.addBtn.bringToFront()
        binding.addBtn.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(notificationReceiver)
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
            toggleLayoutType()
        }

        setIc.setOnClickListener {
            toggleLayoutType()
        }
    }

    private fun toggleLayoutType() {
        isLinearLayout = !isLinearLayout
        setRecyclerViewLayoutManager()
        noteAdapter.setLayoutType(isLinearLayout)
        updateSwitchIcon()
        saveLayoutState(isLinearLayout)
    }

    private fun setRecyclerViewLayoutManager() {
        binding.recyclerView.layoutManager = if (isLinearLayout) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 2)
        }
        noteAdapter.notifyDataSetChanged()
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
            noteAdapter.submitList(listNote) {
                noteAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showNotificationDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION
            )
        }
    }

    private fun shouldShowNotificationPermission(): Boolean {
        val isFirstTime = preferenceHelper.getBoolean("isFirstTimeNoteFragment", true)
        if (isFirstTime) {
            preferenceHelper.putBoolean("isFirstTimeNoteFragment", false)
        }
        return isFirstTime
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
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

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
    }
}
