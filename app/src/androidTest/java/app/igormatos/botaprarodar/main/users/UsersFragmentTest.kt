package app.igormatos.botaprarodar.main.users

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.validUser
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.presentation.main.users.UsersFragment
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class UsersFragmentTest {
    private lateinit var usersUseCase: UsersUseCase
    private lateinit var testModule: Module
    private lateinit var fragmentScenario: FragmentScenario<UsersFragment>

    @Before
    fun setup() {
        usersUseCase = mockk(relaxed = true)
        testModule = module(override = true) {
            single {
                usersUseCase
            }
        }
        loadKoinModules(testModule)
        defineUseCaseBehavior()
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    @Test
    fun whenLoadFragment_shouldAllViewsVisible() {
        usersFragment {
        } verify {
            checkRecyclerIsVisible()
            checkTitleIsVisible()
            clickRegisterUserButton()
        }
    }

    @Test
    fun whenClickToRegisterNewUser_shouldOpenUserFormActivity() {
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
            checkSaveButtonIsEnable()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyFirstItemAtRecycler() {
        addItemAtRecycler()

        usersFragment {
        } verify {
            checkUserName()
            checkUserPhoneNumber()
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyUserIconBlockedAtRecyclerIsVisible() {
        addItemAtRecycler()
        sleep(5000)
        usersFragment {
        } verify {
            checkUserIconBlockedIsVisible(0)
        }
    }

    @Test
    fun whenLoadFragment_shouldVerifyUserIconBlockedAtRecyclerNotIsVisible() {
        validUser.isBlocked = false
        addItemAtRecycler()
        usersFragment {
        } verify {
            checkUserIconBlockedNotIsVisible(0)
        }
    }

    private fun addItemAtRecycler() {
        fragmentScenario.withFragment {
            this.usersAdapter.submitList(mutableListOf(validUser))
            this.usersAdapter.filteredList = mutableListOf(validUser)
            this.usersAdapter.notifyDataSetChanged()
        }
    }

    private fun defineUseCaseBehavior() {
        coEvery {
            usersUseCase.getAvailableUsersByCommunityId(any()) as SimpleResult.Success<List<User>>
        } returns SimpleResult.Success(mutableListOf(User()))
    }
}