package com.scrafts.sudoku.game

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class PuzzleGeneratorTest {
    @Test
    fun generate() {
        val expected = intArrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            4, 5, 6, 7, 8, 9, 1, 2, 3,
            7, 8, 9, 1, 2, 3, 4, 5, 6,
            2, 1, 4, 3, 6, 5, 8, 9, 7,
            3, 6, 5, 8, 9, 7, 2, 1, 4,
            8, 9, 7, 2, 1, 4, 3, 6, 5,
            5, 3, 1, 6, 4, 2, 9, 7, 8,
            6, 4, 2, 9, 7, 8, 5, 3, 1,
            9, 7, 8, 5, 3, 1, 6, 4, 2,
        ).toTypedArray()
        val generator = PuzzleGenerator()
        val actual = generator.generate()
        println("generated puzzle: ")
        generator.printBoard(actual)
        assertArrayEquals(expected, actual)
    }

    @Test
    fun isAcceptableChecksRowsAndColumns() {
        val base = intArrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
        ).toTypedArray()
        for (c in 0..8) {
            // Create a conflict in each column
            assertFalse(PuzzleGenerator().isAcceptable(base, 9 + c, c + 1))
        }
        for (r in 1..8) {
            // Create a conflict in a row
            assertFalse(PuzzleGenerator().isAcceptable(base, r, 1))
        }
    }

    @Test
    fun isAcceptableChecksGrids() {
        val base = intArrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
        ).toTypedArray()
        val conflictingValue = intArrayOf(2, 3, 1, 5, 6, 4, 8, 9, 7)
        val acceptableValue = intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3)
        // Create a conflict in each grid
        // For each of the 9 grids
        for (grid in 0..8) {
            // for each of the 3 columns in the grid
            for (i in 0..2) {
                // for each of the 2 other rows in the grid
                for (j in 1..2) {
                    val row = (grid / 3) * 3 + j
                    val col = (grid % 3) * 3 + i
                    assertFalse("Expecting conflict at " +
                            "grid: $grid, row: $row, col: $col, value: " + conflictingValue[col],
                        PuzzleGenerator().isAcceptable(
                            base,
                            9 * row + col,
                            conflictingValue[col]
                        )
                    )
                    assertTrue("Unexpected conflict at " +
                            "grid: $grid, row: $row, col: $col, value: " + acceptableValue[col],
                        PuzzleGenerator().isAcceptable(
                            base,
                            9 * row + col,
                            acceptableValue[col]
                        )
                    )
                }
            }
        }
    }

    @Test
    fun generateFrom() {
        val from = intArrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            4, 5, 6, 7, 8, 9, 1, 2, 3,
            7, 8, 9, 1, 2, 3, 4, 5, 6,
            2, 1, 4, 3, 6, 5, 8, 9, 7,
            3, 6, 5, 8, 9, 7, 2, 1, 4,
            8, 9, 7, 2, 1, 4, 3, 6, 5,
            5, 3, 1, 6, 4, 2, 9, 7, 8,
            6, 4, 2, 9, 7, 8, 5, 3, 1,
            9, 7, 8, 5, 3, 1, 6, 4, 2,
        ).toTypedArray()
        val expected = intArrayOf(
            1, 2, 3, 4, 5, 6, 7, 8, 9,
            4, 5, 6, 7, 8, 9, 1, 2, 3,
            7, 8, 9, 1, 2, 3, 4, 5, 6,
            2, 1, 4, 3, 6, 5, 8, 9, 7,
            3, 6, 5, 8, 9, 7, 2, 1, 4,
            8, 9, 7, 2, 1, 4, 3, 6, 5,
            5, 3, 1, 6, 4, 2, 9, 7, 8,
            6, 4, 8, 9, 7, 1, 5, 3, 2,
            9, 7, 2, 5, 3, 8, 6, 4, 1,
        ).toTypedArray()
        val generator = PuzzleGenerator()
        assertArrayEquals(expected, generator.generateFrom(from))
    }
}
