package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.data.network.BicycleApi
import app.igormatos.botaprarodar.di.bprModule
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class BicycleApiTest : KoinTest {

    private val api: BicycleApi by inject()

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            modules(bprModule)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun shouldReturnBicycles() = runBlocking {
        val bicyclesResult = api.getBicycles("-MLDOXs3p35DEHg0gdUU").await()

        assertNotNull(bicyclesResult)
    }

}