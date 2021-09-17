package app.igormatos.botaprarodar.main.bikes

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.presentation.main.bikes.BikesFragment
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class BikesFragmentTest {
    private lateinit var bikesUseCase: BikesUseCase
    private lateinit var testModule: Module
    private lateinit var fragmentScenario: FragmentScenario<BikesFragment>

    @Before
    fun setup() {
        bikesUseCase = mockk(relaxed = true)
        testModule = module(override = true) {
            single {
                bikesUseCase
            }
        }
        loadKoinModules(testModule)
        defineUseCaseBehavior()
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun whenLoadFragment_shouldAllViewsVisible() {
        bikesFragment {
        } verify {
            checkRecyclerIsVisible()
            checkButtonIsVisible()
            checkTitleIsVisible()
        }
    }

    @Test
    fun whenClickToRegisterNewBike_shouldOpenBikeFormActivity() {
        bikesFragment {
            clickRegisterBikesButton()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    fun whenClickInABike_shouldOpenBikeFormActivity() {
        addItemAtRecycler()

        bikesFragment {
            clickOnFirstBike()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyFirstItemAtRecycler() {
        addItemAtRecycler()

        bikesFragment {
        } verify {
            checkBikeName()
            checkBikeOrderNumber()
            checkBikeSerialNumber()
        }
    }

    private fun addItemAtRecycler() {
        fragmentScenario.withFragment {
            this.bicycleAdapter.submitList(mutableListOf(bike))
            this.bicycleAdapter.filteredList = mutableListOf(bike)
            this.bicycleAdapter.notifyDataSetChanged()
        }
    }

    private fun defineUseCaseBehavior() {
        coEvery {
            bikesUseCase.getBikes(any()) as SimpleResult.Success<List<Bike>>
        } returns SimpleResult.Success(mutableListOf(Bike()))
    }
}