package com.scrafts.sudoku.game

import android.util.Log
import androidx.lifecycle.MutableLiveData

class SudokuGame {
    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Pair<Boolean, Set<Int>>>()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false

    private val board: Board

    init {
        val cells = List(9 * 9) {i -> Cell(i / 9, i % 9, i % 9)}
        cells[11].isFixed = true
        cells[42].isFixed = true
        board = Board(9, cells)
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return

        val cell = board.getCell(selectedRow, selectedCol)
        if (cell.isFixed) return
        // If the cell is non-empty, ignore input in notes taking mode
        if (isTakingNotes && cell.value != 0) return

        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            Log.v("sudoku_game", "cell.notes: " + cell.notes)
            highlightedKeysLiveData.postValue(Pair(isTakingNotes, cell.notes))
        } else {
            cell.value = number
        }
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
        if (isTakingNotes) {
            val cell = board.getCell(row, col)
            highlightedKeysLiveData.postValue(Pair(isTakingNotes, cell.notes))
        }
    }

    fun changeNoteTakingState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        if (selectedRow != -1 && selectedCol != -1) {
            val curNotes = if (isTakingNotes) {
                board.getCell(selectedRow, selectedCol).notes
            } else {
                setOf()
            }
            highlightedKeysLiveData.postValue(Pair(isTakingNotes, curNotes))
        }
    }

    fun delete() {
        if (selectedRow == -1 || selectedCol == -1) return

        val cell = board.getCell(selectedRow, selectedCol)
        if (cell.isFixed) return

        if (isTakingNotes) {
            cell.notes.clear()
            highlightedKeysLiveData.postValue(Pair(isTakingNotes, setOf()))
        } else {
            cell.value = 0
        }
        cellsLiveData.postValue(board.cells)
    }
}