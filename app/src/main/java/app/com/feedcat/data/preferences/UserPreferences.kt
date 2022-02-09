package app.com.feedcat.data.preferences

import android.content.Context
import android.content.SharedPreferences
import app.com.feedcat.data.entity.User
import app.com.feedcat.data.preferences.PreferenceHelper.editMe

private const val USER_PREFERENCES = "USER_PREFERENCES"
private const val USER_ID = "USER_ID"
private const val USER_NAME = "NAME"
private const val USER_PASSWORD = "PASSWORD"

class UserPreferences private constructor(context: Context) {

    private val preferences = PreferenceHelper.customPreference(context, USER_PREFERENCES)

    fun saveUser(user: User) {
        preferences.userId = user.id
        preferences.userName = user.name
        preferences.userPassword = user.password
    }

    fun getUser(): User? {
        if (preferences.userId > 0L) {
            val user = User(preferences.userName!!, preferences.userPassword!!)
            user.id = preferences.userId
            return user
        }
        return null
    }

    fun removeUser() {
        preferences.editMe {
            it.remove(USER_ID)
            it.remove(USER_NAME)
            it.remove(USER_PASSWORD)
        }
    }

    private var SharedPreferences.userId
        get() = getLong(USER_ID, 0L)
        set(value) {
            editMe { it.putLong(USER_ID, value) }
        }

    private var SharedPreferences.userName
        get() = getString(USER_NAME, "")
        set(value) {
            editMe { it.putString(USER_NAME, value) }
        }

    private var SharedPreferences.userPassword
        get() = getString(USER_PASSWORD, "")
        set(value) {
            editMe { it.putString(USER_PASSWORD, value) }
        }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = UserPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }
}