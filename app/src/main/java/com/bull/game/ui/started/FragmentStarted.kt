package com.bull.game.ui.started

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bull.game.core.library.GameFragment
import com.bull.game.databinding.FragmentStartedBinding
import com.bull.game.ui.bull_pairs.FragmentBullPairs
import com.bull.game.ui.other.MainActivity

class FragmentStarted : GameFragment<FragmentStartedBinding>(FragmentStartedBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mode1.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentBullPairs().apply {
                arguments = Bundle().apply {
                    putBoolean("IS_HARD", false)
                }
            })
        }

        binding.mode2.setOnClickListener {
            (requireActivity() as MainActivity).navigate(FragmentBullPairs().apply {
                arguments = Bundle().apply {
                    putBoolean("IS_HARD", true)
                }
            })
        }

        binding.exit.setOnClickListener {
            requireActivity().finish()
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}