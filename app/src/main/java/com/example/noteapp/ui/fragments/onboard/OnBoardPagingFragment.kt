package com.example.noteapp.ui.fragments.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentOnBoardPagingBinding

class OnBoardPagingFragment : Fragment() {
    private lateinit var binding: FragmentOnBoardPagingBinding


    companion object {
        const val ARG_POSITION = "position"

        fun newInstance(position: Int): OnBoardPagingFragment {
            val fragment = OnBoardPagingFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardPagingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()


    }

    private fun initialize() = with(binding) {
        val position = arguments?.getInt(ARG_POSITION) ?: 0
        when (position) {
            0 -> {
                animationAll.setAnimation(R.raw.animation_first)
                txtTitle.text = "Удобство"
                txtDescription.text = "Создавайте заметки в два клика! Записывайте мысли, идеи и важные задачи мгновенно."
            }
            1 -> {
                animationAll.setAnimation(R.raw.animation_second)
                txtTitle.text = "Организация"
                txtDescription.text = "Организуйте заметки по папкам и тегам. Легко находите нужную информацию в любое время."
            }
            2 -> {
                animationAll.setAnimation(R.raw.animation_third)
                txtTitle.text = "Синхронизация"
                txtDescription.text = "Синхронизация на всех устройствах. Доступ к записям в любое время и в любом месте."
            }
        }

    }

}