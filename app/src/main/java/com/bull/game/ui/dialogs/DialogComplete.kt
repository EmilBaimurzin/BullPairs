package com.bull.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.bull.game.R
import com.bull.game.core.library.ViewBindingDialog
import com.bull.game.databinding.DialogCompleteBinding
import com.bull.game.domain.SharedP
import com.bull.game.ui.bull_pairs.FragmentBullPairs
import com.bull.game.ui.other.MainActivity

class DialogComplete : ViewBindingDialog<DialogCompleteBinding>(DialogCompleteBinding::inflate) {
    private val sp by lazy {
        SharedP(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                (requireActivity() as MainActivity).navigateBack("main")
                dialog?.cancel()
                true
            } else {
                false
            }
        }
        binding.restart.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack("main")
            (requireActivity() as MainActivity).navigate(FragmentBullPairs().apply {
                arguments = Bundle().apply {
                    putBoolean("IS_HARD", arguments?.getBoolean("IS_HARD") ?: true)
                }
            })
            dialog?.cancel()
        }
        val score = arguments?.getInt("SCORE").toString()
        binding.score.text = score

        val bestScore = sp.getBestScore(arguments?.getBoolean("IS_HARD")!!).toString()
        binding.bestScore.text = bestScore
    }
}