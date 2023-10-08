package com.bull.game.ui.bull_pairs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bull.game.core.library.GameFragment
import com.bull.game.databinding.FragmentBullPairsBinding
import com.bull.game.domain.SharedP
import com.bull.game.domain.bull_pairs.BullPairsAdapter
import com.bull.game.ui.dialogs.DialogComplete
import com.bull.game.ui.other.MainActivity

class FragmentBullPairs :
    GameFragment<FragmentBullPairsBinding>(FragmentBullPairsBinding::inflate) {
    private lateinit var pairsAdapter: BullPairsAdapter
    private val sp by lazy {
        SharedP(requireContext())
    }
    private var isHard = false
    private lateinit var viewModel: BullPairsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isHard = arguments?.getBoolean("IS_HARD", false) ?: false
        viewModel = ViewModelProvider(
            this,
            BullPairsViewModelFactory(isHard)
        )[BullPairsViewModel::class.java]

        initAdapter()

        binding.menu.setOnClickListener {
            (requireActivity() as MainActivity).navigateBack()
        }

        binding.play.setOnClickListener {
            viewModel.pauseState = false
            viewModel.changePauseState()
            viewModel.startTimer()
        }

        binding.pause.setOnClickListener {
            viewModel.changePauseState()
            viewModel.stopTimer()
            viewModel.pauseState = true
        }

        viewModel.winCallback = {
            end()
        }

        viewModel.list.observe(viewLifecycleOwner) {
            pairsAdapter.items = it.toMutableList()
            pairsAdapter.notifyDataSetChanged()
        }
        viewModel.timer.observe(viewLifecycleOwner) { totalSecs ->
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            binding.time.text = String.format("%02d:%02d", minutes, seconds);
            if (totalSecs == 0 && viewModel.gameState && !viewModel.pauseState) {
                end()
            }
        }

        viewModel.score.observe(viewLifecycleOwner) {
            binding.score.text = it.toString()
        }

        viewModel.pause.observe(viewLifecycleOwner) {
            binding.pauseLayout.isVisible = it
        }

        if (viewModel.gameState && !viewModel.pauseState) {
            viewModel.startTimer()
        }
    }

    private fun end() {
        viewModel.stopTimer()
        viewModel.gameState = false
        if (sp.getBestScore(isHard) < viewModel.score.value!!) {
            sp.setBestScore(isHard, viewModel.score.value!!)
        }
        (requireActivity() as MainActivity).navigateToDialog(DialogComplete().apply {
            arguments = Bundle().apply {
                putInt("SCORE", viewModel.score.value!!)
                putBoolean("IS_HARD", isHard)
            }
        })
    }

    private fun initAdapter() {
        pairsAdapter = BullPairsAdapter ({
            if (viewModel.list.value!!.find { it.closeAnimation } == null && viewModel.list.value!!.find { it.openAnimation } == null) {
                viewModel.openItem(it)
            }
        }, isHard)
        with(binding.gameRV) {
            adapter = pairsAdapter
            layoutManager = GridLayoutManager(requireContext(), if (isHard) 6 else 4).also {
                it.orientation = GridLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
    }
}