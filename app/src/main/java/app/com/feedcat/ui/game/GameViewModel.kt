package app.com.feedcat.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.feedcat.core.viewmodel.SingleLiveEvent
import app.com.feedcat.data.entity.GameResult
import app.com.feedcat.data.entity.User
import app.com.feedcat.data.repository.GameResultRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val MAX_COUNT = 15
private const val ANIMATION_DELAY = 1500L

class GameViewModel(
    private val gameResultRepository: GameResultRepository,
    val user: User?
) : ViewModel() {

    private lateinit var job: Job
    private val _satiety = MutableLiveData(0)
    val satiety: LiveData<Int> = _satiety
    private val _animateNumber = SingleLiveEvent<Int>()
    val animateNumber: LiveData<Int> = _animateNumber

    init {
        startTimer()
    }

    fun feed(): Boolean {
        _satiety.value = _satiety.value!! + 1
        return _satiety.value != 0 && _satiety.value!!.mod(MAX_COUNT) == 0
    }

    fun take() {
        _satiety.value = _satiety.value!! - 1
    }

    fun save() {
        user?.let { u ->
            _satiety.value?.let {
                val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm:ss", Locale.US)
                val currentDate = sdf.format(Calendar.getInstance().time)
                val gameResult = GameResult(it, u.id, currentDate)
                viewModelScope.launch {
                    gameResultRepository.insert(gameResult)
                }
            }
        }
    }

    private fun timer() = viewModelScope.launch {
        while (true) {
            _animateNumber.postValue(Random().nextInt(90) / 30)
            delay(ANIMATION_DELAY)
        }
    }

    private fun startTimer() {
        job = timer()
    }

    fun stopTimer() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }

    fun resumeTimer() {
        if (::job.isInitialized && !job.isActive) {
            job.cancel()
            job = timer()
        }
    }
}