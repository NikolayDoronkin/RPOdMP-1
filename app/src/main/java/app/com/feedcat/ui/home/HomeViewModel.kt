package app.com.feedcat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.com.feedcat.core.viewmodel.SingleLiveEvent
import app.com.feedcat.data.entity.GameResult
import app.com.feedcat.data.entity.User
import app.com.feedcat.data.repository.GameResultRepository
import app.com.feedcat.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val gameResultRepository: GameResultRepository,
) : ViewModel() {

    private val _existUser = SingleLiveEvent<Boolean>()
    val existUser: LiveData<Boolean> = _existUser

    private val _loginError = SingleLiveEvent<Boolean>()
    val loginError: LiveData<Boolean> = _loginError

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    fun setUser(user: User) {
        _user.value = user
    }

    private val _lastGameResult = MutableLiveData<GameResult?>()
    val lastGameResult: LiveData<GameResult?> = _lastGameResult

    fun loadLastGameResult() {
        user.value?.let {
            viewModelScope.launch {
                val results = gameResultRepository.getLastGameResult(it.id)
                if (results.isNotEmpty()) {
                    _lastGameResult.value = results[0]
                } else {
                    _lastGameResult.value = null
                }
            }
        }
    }

    fun save(user: User) {
        viewModelScope.launch {
            val users = userRepository.existUser(user.name)
            if (users.isEmpty()) {
                userRepository.insert(user)
                login(user)
            } else {
                _existUser.value = true
            }
        }
    }

    fun login(user: User) {
        viewModelScope.launch {
            val users = userRepository.getUser(user)
            if (users.isEmpty()) {
                _loginError.value = true
            } else {
                _user.value = users[0]
            }
        }
    }

    fun logout() {
        _user.value = null
        _lastGameResult.value = null
    }

}