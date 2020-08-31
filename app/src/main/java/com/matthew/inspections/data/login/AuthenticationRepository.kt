package com.matthew.inspections.data.login

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.NonNull
import com.matthew.inspections.dagger.qualifier.ForDatabase
import com.matthew.inspections.network.InspectionsService
import com.matthew.inspections.room.InspectionsDao
import com.matthew.inspections.room.converters.LocalDateTimeConverter
import com.matthew.inspections.room.data.LocalAuthorisation
import com.matthew.inspections.room.data.update
import com.matthew.inspections.user.Authorisation
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class AuthenticationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: InspectionsService,
    private val dao: InspectionsDao,
    @ForDatabase @NonNull private val databaseExecutor: Executor
) {

    companion object {
        const val STRING_LENGTH = 10
        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }

    fun save(localAuthorisation: LocalAuthorisation?) {
        localAuthorisation?.apply {
            databaseExecutor.execute {
                dao.has(userId)?.let {
                    dao.update(it.update(this))
                } ?: dao.insert(this)
            }
        }
    }

    /**
     * Authenticate user with server using InspectionsService
     * valid offline using database 
     */
    suspend fun login(username: String, password: String, offline: Boolean = false): Authorisation {
        return try {
            //temp value to bypass "random" network failure
            var authorised = false
            if(offline){
                dao.hasUsername(username)?.let {
                    when {
                        it.expiryDate == null || it.expiryDate!!.isBefore(LocalDateTime.now()) ->
                            throw AuthenticationExpiredException("Your authentication has expired")
                        it.password != password -> throw InvalidUserNameOrPasswordException("Your password is incorrect")
                        else -> authorised = true
                    }
                }?:throw InvalidUserNameOrPasswordException("Username does not exist locally")
            }else if(!isInternetAvailable()){
                throw NetworkErrorException("Internet is not available")
            }

            /**
             * for this example server does not exist
             * so create random authorisation for user
             *
             *   Example of how the authorise call would be made
             *   val call = service.authorise(username, password)
             *   if(call.authorised){
             *       //go ahead
             *   }else{
             *       //set error
             *   }
             */

            if (Random.nextBoolean() || authorised) {
                val dateNow = LocalDateTime.now()
                val dateThen = dateNow.plusDays(7)
                val converter = LocalDateTimeConverter()
                Authorisation(
                    true,
                    12345,//Random.nextInt()
                    converter.fromLocalDateTime(dateNow),
                    converter.fromLocalDateTime(dateThen),
                    RANDOM_MD5
                )
            } else {
                throw InvalidUserNameOrPasswordException("Your password is incorrect")
            }

        } catch (e: Exception) {
            when(e){
                is NetworkErrorException -> {
                    login(username, password, true)
                }
                else -> Authorisation(errorResponse = e.message)
            }
        }
    }

    inner class InvalidUserNameOrPasswordException(message: String) : Exception(message)
    inner class AuthenticationExpiredException(message: String) : Exception(message)

    private val RANDOM_MD5 = (1..STRING_LENGTH)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")


    fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }

        return result
    }

}