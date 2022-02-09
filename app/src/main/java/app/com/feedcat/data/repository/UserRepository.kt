package app.com.feedcat.data.repository

import androidx.annotation.WorkerThread
import app.com.feedcat.data.dao.UserDao
import app.com.feedcat.data.entity.User

class UserRepository(private val userDao: UserDao) {

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    @WorkerThread
    suspend fun existUser(name: String) = userDao.existUser(name)

    @WorkerThread
    suspend fun getUser(user: User) = userDao.getUser(user.name, user.password)
}
