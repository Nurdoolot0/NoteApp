package com.example.noteapp.ui.fragments.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentOnBoardBinding
import com.example.noteapp.ui.adapters.OnBoardPagerAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class OnBoardFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
    }

    private fun initialize() {
        binding.viewpager2.adapter = OnBoardPagerAdapter(this)
        val dotsIndicator: WormDotsIndicator = binding.dotsIndicator
        dotsIndicator.setViewPager2(binding.viewpager2)


        binding.btnStart.visibility = View.GONE
    }

    private fun setupListeners() = with(binding.viewpager2) {
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.btnStart.visibility = View.VISIBLE
                } else {
                    binding.btnStart.visibility = View.GONE
                }

                binding.txtSkip.visibility = if (position == 2) View.INVISIBLE else View.VISIBLE
            }
        })

        binding.txtSkip.setOnClickListener {
            if (currentItem < 2) {
                setCurrentItem(currentItem + 2, true)
            }
        }

        binding.btnStart.setOnClickListener {
            findNavController().navigate(
                R.id.action_onBoardFragment_to_signUpFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_host_fragment, true)
                    .build()
            )
        }
            }
        }
