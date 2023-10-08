package com.bull.game.domain

import android.content.Context

class SharedP(private val context: Context) {
    private val sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE)

    fun getBestScore(isHard: Boolean) = sp.getInt("BEST$isHard", 0)
    fun setBestScore(isHard: Boolean, score: Int) = sp.edit().putInt("BEST$isHard", score).apply()
}