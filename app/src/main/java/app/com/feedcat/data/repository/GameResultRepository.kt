package app.com.feedcat.data.repository

import androidx.annotation.WorkerThread
import app.com.feedcat.data.dao.GameResultDao
import app.com.feedcat.data.entity.GameResult

class GameResultRepository(private val gameResultDao: GameResultDao) {

    @WorkerThread
    suspend fun insert(gameResult: GameResult) {
        gameResultDao.insert(gameResult)
    }

    @WorkerThread
    suspend fun delete(gameResult: GameResult) {
        gameResultDao.delete(gameResult)
    }

    @WorkerThread
    suspend fun getGameResults(userId: Long) = gameResultDao.getGameResults(userId)

    @WorkerThread
    suspend fun getLastGameResult(userId: Long) = gameResultDao.getLastGameResult(userId)
}
