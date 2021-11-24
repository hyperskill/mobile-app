package org.hyperskill.app

import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hyperskill.app.login.remote.UserLoginRemoteDataSourceImpl
import org.hyperskill.app.network.injection.NetworkModule
import kotlin.test.Test

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