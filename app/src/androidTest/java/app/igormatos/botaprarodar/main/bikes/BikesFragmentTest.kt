package app.igormatos.botaprarodar.main.bikes

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.bikes.BikesFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BikesFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<BikesFragment>

    @Before
    fun setup() {
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        fragmentScenario.withFragment {
            this.bicycleAdapter.submitList(mutableListOf(bike))
        }
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
        bikesFragment {
            clickOnFirstBike()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyFirstItemAtRecycler() {
        bikesFragment {
        } verify {
            checkBikeName()
            checkBikeOrderNumber()
            checkBikeSerialNumber()
        }
    }
}