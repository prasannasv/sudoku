package com.scrafts.sudoku.viewmodel

import androidx.lifecycle.ViewModel
import com.scrafts.sudoku.game.SudokuGame

class PlaySudokuViewModel : ViewModel() {
    val sudokuGame = SudokuGame()
}