package com.scrafts.sudoku.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.scrafts.sudoku.R
import com.scrafts.sudoku.databinding.ActivityPlaySudokuBinding
import com.scrafts.sudoku.game.Cell
import com.scrafts.sudoku.view.custom.SudokuBoardView
import com.scrafts.sudoku.viewmodel.PlaySudokuViewModel

class PlaySudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel: PlaySudokuViewModel
    private lateinit var binding: ActivityPlaySudokuBinding

    private lateinit var numberButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaySudokuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sudokuBoardView.registerListener(this)

        viewModel = ViewModelProvider(this).get(PlaySudokuViewModel::class.java)
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer {
            updateSelectedCellUi(it)
        })
        viewModel.sudokuGame.cellsLiveData.observe(this) {
            updateCells(it)
        }
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this) {
            updateNoteTakingUi(it)
        }
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this) {
            updateHighlightedKeys(it)
        }

        numberButtons = listOf(
            binding.oneButton,
            binding.twoButton,
            binding.threeButton,
            binding.fourButton,
            binding.fiveButton,
            binding.sixButton,
            binding.sevenButton,
            binding.eightButton,
            binding.nineButton,
        )
        numberButtons.forEachIndexed{ index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index + 1)}
        }

        binding.notesButton.setOnClickListener { viewModel.sudokuGame.changeNoteTakingState() }
        binding.deleteButton.setOnClickListener { viewModel.sudokuGame.delete() }
    }

    private fun updateSelectedCellUi(cell: Pair<Int, Int>?)  = cell?.let {
        binding.sudokuBoardView.updateSelectedCellUi(cell.first, cell.second)
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        binding.sudokuBoardView.updateCells(cells)
    }

    private fun updateNoteTakingUi(isNoteTaking: Boolean?) = isNoteTaking?.let {
        val defaultColor = ContextCompat.getColor(this, R.color.primary)
        if (it) {
            binding.notesButton.setBackgroundColor(defaultColor)
        } else {
            binding.notesButton.setBackgroundColor(Color.LTGRAY)
            numberButtons.forEach {button ->
                button.setBackgroundColor(defaultColor)
            }
        }
    }

    private fun updateHighlightedKeys(notesPair: Pair<Boolean, Set<Int>>?) = notesPair?.let {
        // If we are not in notes taking state use the default color for all
        // the buttons.
        numberButtons.forEachIndexed { index, button ->
            val color = if (!it.first || it.second.contains(index + 1)) {
                ContextCompat.getColor(
                    this,
                    R.color.primary
                )
            } else {
                Color.LTGRAY
            }
            button.setBackgroundColor(color)
        }
}

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}