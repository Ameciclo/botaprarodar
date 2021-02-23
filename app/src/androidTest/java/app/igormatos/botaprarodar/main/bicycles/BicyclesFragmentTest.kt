package app.igormatos.botaprarodar.main.bicycles

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.bikes.BicyclesFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BicyclesFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<BicyclesFragment>

    @Before
    fun setup() {
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        fragmentScenario.withFragment {
//            this.bicycleAdapter.addItem(bike)
        }
    }

    @Test
    fun whenLoadFragment_shouldAllViewsVisible() {
        bicyclesFragment {
            delay()
        } verify {
            checkRecyclerIsVisible()
            checkButtonIsVisible()
            checkTitleIsVisible()
        }
    }

    @Test
    fun whenClickToRegisterNewBike_shouldOpenBikeFormActivity() {
        bicyclesFragment {
            clickRegisterBikesButton()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    fun whenClickInABike_shouldOpenBikeFormActivity() {
        bicyclesFragment {
            clickOnFirstBike()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyFirstItemAtRecycler() {
        bicyclesFragment {
            delay()
        } verify {
            checkBikeName()
            checkBikeOrderNumber()
            checkBikeSerialNumber()
        }
    }
}