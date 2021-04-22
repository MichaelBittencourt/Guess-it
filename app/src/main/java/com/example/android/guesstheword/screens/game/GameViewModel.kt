package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        //These represent different important times in the game, such as game length.

        //This is when the game is over.
        private const val DONE = 0L

        //This is the number of milliseconds in a minute.
        private const val ONE_SECOND = 1000L

        //This is the total time of the game.
        private const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _currentTimer = MutableLiveData<Long>()
    val currentTimer: LiveData<Long>
        get() = _currentTimer


    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished


    init {
        Log.i("GameViewModel", "GameViewModel Created")
        _score.value = 0
        _word.value = ""
        _eventGameFinished.value = false
        _currentTimer.value = COUNTDOWN_TIME/1000
        resetList()
        nextWord()
        timer = object: CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTimer.value = currentTimer.value?.minus(ONE_SECOND/1000)
            }

            override fun onFinish() {
                _eventGameFinished.value = true
                //_currentTimer.value = COUNTDOWN_TIME
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel Destroyed")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }
    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }
}