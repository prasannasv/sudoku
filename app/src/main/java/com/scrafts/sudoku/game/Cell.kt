package com.scrafts.sudoku.game

class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var isFixed: Boolean = false,
    var notes: MutableSet<Int> = mutableSetOf()
)