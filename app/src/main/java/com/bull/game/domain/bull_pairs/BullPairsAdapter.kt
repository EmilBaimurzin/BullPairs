package com.bull.game.domain.bull_pairs

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bull.game.R
import com.bull.game.databinding.ItemPairEasyBinding
import com.bull.game.databinding.ItemPairHardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BullPairsAdapter(
    private var onItemClick: ((Int) -> Unit)? = null,
    private val isHard: Boolean
) :
    RecyclerView.Adapter<BullPairsViewHolder>() {
    var items = mutableListOf<BullPairsItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BullPairsViewHolder {
        return BullPairsViewHolder(
            (if (isHard)
                ItemPairHardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ) else
                ItemPairEasyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )), isHard
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BullPairsViewHolder, position: Int) {
        holder.bind(items[position])
        holder.onItemClick = onItemClick
    }
}

class BullPairsViewHolder(private val binding: ViewBinding, private val isHard: Boolean) :
    RecyclerView.ViewHolder(binding.root) {
    var onItemClick: ((Int) -> Unit)? = null
    fun bind(item: BullPairsItem) {
        if (isHard) binding as ItemPairHardBinding else binding as ItemPairEasyBinding
        val itemImg: ImageView = if (binding is ItemPairEasyBinding) binding.itemImg else {
            binding as ItemPairHardBinding
            binding.itemImg
        }

        binding.apply {
            val img = when (item.value) {
                1 -> R.drawable.mode02_symbol01
                2 -> R.drawable.mode02_symbol02
                3 -> R.drawable.mode02_symbol03
                4 -> R.drawable.mode02_symbol04
                5 -> R.drawable.mode02_symbol05
                6 -> R.drawable.mode02_symbol06
                7 -> R.drawable.mode02_symbol07
                8 -> R.drawable.mode02_symbol08
                9 -> R.drawable.mode02_symbol09
                10 -> R.drawable.mode02_symbol10
                11 -> R.drawable.mode02_symbol11
                12 -> R.drawable.mode02_symbol12
                13 -> R.drawable.mode02_symbol13
                14 -> R.drawable.mode02_symbol14
                else -> R.drawable.mode02_symbol15
            }
            if (item.isOpened) {
                itemImg.setImageResource(img)
                itemImg.setBackgroundResource(R.drawable.mode02_symbol_box)
            } else {
                itemImg.setImageDrawable(null)
                itemImg.setBackgroundResource(R.drawable.mode02_symbol_box)
            }
            if (item.openAnimation) {
                flipImage(binding.root, img)
            }

            if (item.closeAnimation) {
                flipImage(binding.root, null)
            }

            binding.root.setOnClickListener {
                if (!item.openAnimation && !item.closeAnimation && !item.isOpened) {
                    onItemClick?.invoke(adapterPosition)
                }
            }
        }
    }

    private fun flipImage(
        view: View,
        @DrawableRes img: Int?,
    ) {
        val itemImg: ImageView = if (binding is ItemPairEasyBinding) binding.itemImg else {
            binding as ItemPairHardBinding
            binding.itemImg
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(200)
            if (img != null) {
                itemImg.setImageResource(img)
                itemImg.setBackgroundResource(R.drawable.mode02_symbol_box)
            } else {
                itemImg.setImageDrawable(null)
                itemImg.setBackgroundResource(R.drawable.mode02_symbol_box)
            }
        }
        val animatorSet = AnimatorSet()
        val rotateAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0f, 180f)
        rotateAnimator.duration = 400

        val scaleXAnimator = ValueAnimator.ofFloat(1f, -1f)
        scaleXAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            view.scaleX = scale
        }
        scaleXAnimator.duration = 400

        animatorSet.playTogether(rotateAnimator, scaleXAnimator)
        animatorSet.start()
    }

}