package app.igormatos.botaprarodar.main.bikes

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
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
    @FlakyTest(detail = "bicycleAdapter list mock sometimes is cleaned in physical devices")
    fun whenClickInABike_shouldOpenBikeFormActivity() {
        addItemAtRecycler()

        bikesFragment {
            clickOnFirstBike()
        } verify {
            checkBikeFormActivityIsVisible()
        }
    }

    @Test
    @FlakyTest(detail = "bicycleAdapter list mock sometimes is cleaned in physical devices")
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
}