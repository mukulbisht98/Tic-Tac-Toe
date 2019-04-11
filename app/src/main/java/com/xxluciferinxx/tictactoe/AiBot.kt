package com.xxluciferinxx.tictactoe

import android.util.Log

class AiBot {
    private var arrState = Array(3) { CharArray(3) }

    fun getData(arr: Array<CharArray>) {
        this.arrState = arr
        Log.i("arrayState",arrState.toString())
        calc()
    }

    private fun calc() {
        Log.i("calc","calculations.")
    }

    private fun respond(): Array<CharArray>? {
        return arrState
    }
}