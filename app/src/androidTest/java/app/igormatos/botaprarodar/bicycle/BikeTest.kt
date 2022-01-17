package app.igormatos.botaprarodar.bicycle

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class BikeTest {
    private lateinit var scenario: ActivityScenario<HomeActivity>
    private lateinit var testModule: Module
    private lateinit var bikeActionUseCase: BikeActionUseCase
    private lateinit var bikesUseCase: BikesUseCase
    private lateinit var bikeFormUseCase: BikeFormUseCase

    @get:Rule
    val intentsTestRule = IntentsTestRule(BikeFormActivity::class.java)

    @Before
    fun setup() {

        bikeActionUseCase = spyk(BikeActionUseCase(mockk(relaxed = true)))
        bikesUseCase = mockk(relaxed = true)
        bikeFormUseCase = mockk(relaxed = true)

        testModule = module(override = true) {

            single {
                bikeActionUseCase
            }

            single {
                bikesUseCase
            }

            single {
                bikeFormUseCase
            }
        }

        loadKoinModules(testModule)

    }

    @Ignore
    @Test
    fun shouldAddBicycle() {
        defineUseCasesBehavior()
        scenario = launchActivity()


        bicycle {
            clickBicycleNavigation()
//            addBicycle()
            hideKeyboard()
            simulateCallbackFromCameraIntent(intentsTestRule, ::clickTakeBicyclePhoto)
            sleep(500)
            hideKeyboard()
            fillBicycleNumberSerie("123")
            fillBicycleName("Monaco")
            fillBicycleNumberOrder("098765")
            swipeOnAddBicycle()
            clickRegisterBicycle()
        } verify {
            val successText = context.getString(R.string.bicycle_add_success)
            waitViewByText(successText)
        }
    }

    private fun defineUseCasesBehavior() {
        coEvery {
            bikeActionUseCase.getBikes(any())
        } returns SimpleResult.Success(mutableListOf(BikeRequest()))


        coEvery {
            bikesUseCase.getBikes(any()) as SimpleResult.Success<List<Bike>>
        } returns SimpleResult.Success(mutableListOf(Bike()))

        coEvery {
            bikeFormUseCase.addNewBike(any())
        } returns SimpleResult.Success(AddDataResponse(""))
    }
}
