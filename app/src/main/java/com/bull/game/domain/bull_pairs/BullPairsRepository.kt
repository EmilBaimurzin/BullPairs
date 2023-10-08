package com.bull.game.domain.bull_pairs

class BullPairsRepository {
    fun generateList(isHard: Boolean): List<BullPairsItem> {
        val listToReturn = mutableListOf<BullPairsItem>()


        repeat(2) {
            repeat(if (isHard) 15 else 8) { ind ->
                listToReturn.add(BullPairsItem(value = ind + 1))
            }
        }

        listToReturn.shuffle()
        return listToReturn
    }
}