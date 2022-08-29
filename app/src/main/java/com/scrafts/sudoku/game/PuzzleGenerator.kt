package com.scrafts.sudoku.game

class PuzzleGenerator {
    fun printBoard(cells: Array<Int>) {
        cells.forEachIndexed { index, i ->
            print(i)
            if ((index + 1) % 9 == 0) {
                println(",")
            } else {
                print(", ")
            }
        }
    }

    fun generateFrom(cells: Array<Int>): Array<Int> {
        var i = 80
        while (i >= 0) {
            val startCandidate = cells[i] + 1
            var foundCandidate = false
            for (potentialCandidate in startCandidate..9) {
                if (isAcceptable(cells, i, potentialCandidate)) {
                    cells[i] = potentialCandidate
                    foundCandidate = true
                    break
                }
            }
            if (foundCandidate) {
                i++
                if (i == cells.size) {
                    return cells
                }
            } else {
                cells[i] = 0
                i--
            }
        }
        return cells
    }

    fun generate(): Array<Int> {
        val cells = IntArray(81).toTypedArray()
        var i = 0
        while (i < cells.size) {
            val startCandidate = cells[i] + 1
            var foundCandidate = false
            for (potentialCandidate in startCandidate..9) {
                if (isAcceptable(cells, i, potentialCandidate)) {
                    cells[i] = potentialCandidate
                    foundCandidate = true
                    break
                }
            }
            if (foundCandidate) {
                i++
            } else {
                // println("Backtracking at i: $i")
                // printBoard(cells)
                cells[i] = 0
                if (i == 0) {
                    break
                }
                i--
            }
        }
        return cells
    }

    fun isAcceptable(cells: Array<Int>, currentPosition: Int, potentialCandidate: Int): Boolean {
        val row = currentPosition / 9
        val col = currentPosition % 9
        // Check for conflict in all other columns of this row
        for (c in 0..8) {
            val index = row * 9 + c
            if (index >= currentPosition) {
                break
            }
            if (cells[index] == potentialCandidate) {
                return false
            }
        }
        // Check for conflict in all other rows of this col
        for (r in 0..8) {
            val index = r * 9 + col
            if (index >= currentPosition) {
                break
            }
            if (cells[index] == potentialCandidate) {
                return false
            }
        }
        // Check for conflict in the same 3x3 grid
        val gridStartRow = (row / 3) * 3
        val gridStartCol = (col / 3) * 3
        for (r in gridStartRow..gridStartRow+2) {
            for (c in gridStartCol..gridStartCol+2) {
                val index = r * 9 + c
                if (index >= currentPosition) {
                    break
                }
                if (cells[index] == potentialCandidate) {
                    return false
                }
            }
        }
        return true
    }
}
