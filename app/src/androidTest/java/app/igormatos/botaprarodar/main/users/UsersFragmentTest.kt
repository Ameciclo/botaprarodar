package app.igormatos.botaprarodar.main.users

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.user
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.users.UsersFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsersFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<UsersFragment>

    @Before
    fun setup() {
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun whenLoadFragment_shouldAllViewsVisible() {
        usersFragment {
        } verify {
            checkRecyclerIsVisible()
            checkButtonIsVisible()
            checkTitleIsVisible()
        }
    }

    @Test
    fun whenClickToRegisterNewBike_shouldOpenBikeFormActivity() {
        usersFragment {
            clickRegisterUserButton()
        } verify {
            checkUserFormFragmentIsVisible()
        }
    }

    @Test
    fun whenClickOnUser_shouldOpenUserFormFragment() {
        addItemAtRecycler()

        usersFragment {
            clickOnFirstUser()
        } verify {
            checkUserFormFragmentIsVisible()
            checkButtonIsVisible()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyFirstItemAtRecycler() {
        addItemAtRecycler()

        usersFragment {
        } verify {
            checkUserName()
            checkUserDate()
        }
    }


    private fun addItemAtRecycler() {
        fragmentScenario.withFragment {
            this.usersAdapter.submitList(mutableListOf(user))
            this.usersAdapter.filteredList = mutableListOf(user)
            this.usersAdapter.notifyDataSetChanged()
        }
    }
}