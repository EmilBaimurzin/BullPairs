package com.bull.game.domain.bull_pairs

import java.util.Random

data class BullPairsItem(
    val id: Int = Random().nextInt(),
    val value: Int,
    var isOpened: Boolean = false,
    var lastOpened: Boolean = false,
    var openAnimation: Boolean = false,
    var closeAnimation: Boolean = false
)