package org.hyperskill.app.android

import org.hyperskill.app.login.remote.UserLoginRemoteDataSourceImpl
import org.hyperskill.app.network.injection.NetworkModule
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GreetingTest {
    @Test
    fun greetingTest() {
        print("Kek")
        val json = NetworkModule.provideJson()
        val httpClient = NetworkModule.provideClient(json)

        runBlocking {
            val data = UserLoginRemoteDataSourceImpl(httpClient).getCsrfToken()
            print(data)
        }
    }
}