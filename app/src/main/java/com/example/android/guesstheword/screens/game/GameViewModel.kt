package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val timer: CountDownTimer

    //Creamos un objeto companion donde se guardarán los objetos de los timers
    companion object {

        // Tiempo cuando el juego está terminando
        private const val DONE = 0L

        // Intervalo de la cuenta atrás
        private const val ONE_SECOND = 1000L

        // Tiempo total del juego
        private const val COUNTDOWN_TIME = 60000L

    }

    // Palabra actual
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // Puntuación actual
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Tiempo que queda
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime


    // Tiempo actual en formato String
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // Donde termina el juego
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish


    // Lista de palabras
    private lateinit var wordList: MutableList<String>


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

    init {
        _word.value = ""
        _score.value = 0
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()

        // Creamos un timer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }
    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        nextWord()
    }

    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()

        } else {
            //Select and remove a _word from the list
            _word.value = wordList.removeAt(0)
        }
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

}
