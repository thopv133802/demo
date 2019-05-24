package com.thopham.projects.desktop.demo.domain.service.rx

import com.thopham.projects.desktop.demo.common.Errors.ACCOUNT_IS_BLANK
import com.thopham.projects.desktop.demo.common.Errors.PASSWORD_IS_BLANK
import com.thopham.projects.desktop.demo.common.IO
import com.thopham.projects.desktop.demo.domain.api.DeviceAPI
import com.thopham.projects.desktop.demo.domain.api.UserAPI
import com.thopham.projects.desktop.demo.domain.repository.UserRepository
import com.thopham.projects.desktop.demo.common.LoginInput
import com.thopham.projects.desktop.demo.common.LoginOutput
import com.thopham.projects.desktop.demo.models.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
open class RxUserService (val userAPI: UserAPI, val userRepository: UserRepository, val deviceAPI: DeviceAPI){
    @Value("\${debug.enabled}")
    var isDebug: Boolean = false
    companion object{
        //Ensure only one user logged in on time
        private const val CURRENT_USER_ID = 1
    }
    fun login(account: String, password: String): Completable {
        if(account.isBlank()) return Completable.error(Exception(ACCOUNT_IS_BLANK))
        else if(password.isBlank()) return Completable.error(Exception(PASSWORD_IS_BLANK))
        else return Single.fromCallable {
            userAPI.login(
                    LoginInput(
                            username = account,
                            password = password,
                            device_id = deviceAPI.fetchMacAddress()
                    )
            )
        }
                .flatMapCompletable { output ->
                    saveLoginInformation(output)
                }
                .subscribeOn(IO)
    }
    private fun saveLoginInformation(output: LoginOutput): Completable{
        return Completable.fromAction {
            val user = User(
                    id = CURRENT_USER_ID,
                    userId = output.userId,
                    restaurantId = output.restaurantId,
                    restaurantName = output.restaurantName,
                    username = output.username,
                    token = output.token
            ).apply{
                println(this)
            }
            userRepository.save(user)
        }
                .subscribeOn(IO)
    }
    private fun clearLoginInformation(): Completable{
        return Completable.fromAction {
            userRepository.deleteById(CURRENT_USER_ID)
        }
                .subscribeOn(IO)
    }
    private fun fetchCurrentUserInformation(): User?{
        return userRepository.findByIdOrNull(CURRENT_USER_ID)
    }
    fun fetchCurrentUserID(): Single<Int>{
        return fetchForceCurrentUser()
                .map{user ->
                    user.userId
                }
                .subscribeOn(IO)
    }

    fun fetchCurrentUserToken(): Single<String> {
        return fetchForceCurrentUser()
                .map{user ->
                    user.token
                }
                .subscribeOn(IO)
    }
    fun fetchForceCurrentUser(): Single<User>{
         return Maybe.fromCallable{
            fetchCurrentUserInformation()
        }
                .switchIfEmpty(
                        Maybe.error(Exception("Người dùng chưa đăng nhập. Hãy đăng nhập để sử dụng dịch vụ."))
                )
                .flatMapSingle {user ->
                    Single.just(user)
                }
                 .subscribeOn(IO)
    }
    fun fetchCurrentRestaurantId(): Single<Int> {
        return fetchForceCurrentUser()
                .map{user ->
                    user.restaurantId
                }
                .subscribeOn(IO)
    }

    fun logout(): Completable {
        return clearLoginInformation()
    }
}