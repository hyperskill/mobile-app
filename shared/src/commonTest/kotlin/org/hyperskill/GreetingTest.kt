package org.hyperskill

import kotlinx.coroutines.test.runTest
import org.hyperskill.app.login.remote.UserLoginRemoteDataSourceImpl
import org.hyperskill.app.network.injection.NetworkModule
import kotlin.test.Test

class GreetingTest {
    @Test
    fun greetingTest() {
        print("Kek")
        val json = NetworkModule.provideJson()
        val httpClient = NetworkModule.provideStubClient(json)

        runTest {
            val data = UserLoginRemoteDataSourceImpl(httpClient).getCsrfToken()
            print(data)
        }
    }
}