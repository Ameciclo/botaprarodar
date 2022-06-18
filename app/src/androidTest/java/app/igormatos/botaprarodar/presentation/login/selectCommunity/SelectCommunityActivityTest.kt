package app.igormatos.botaprarodar.presentation.login.selectCommunity

import android.content.Intent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.presentation.login.FirebaseAuthModuleTestImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
internal class SelectCommunityActivityTest {

    private lateinit var scenario: ActivityScenario<SelectCommunityActivity>
    private lateinit var viewModel: SelectCommunityViewModel
    private lateinit var testModule: Module

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)

        testModule = module {

            viewModel(override = true) {
                viewModel
            }

            single<FirebaseAuthModule>(override = true) {
                FirebaseAuthModuleTestImpl(mockk(relaxed = true))
            }
        }

        loadKoinModules(testModule)
    }

    @Test
    fun shouldShowButtonAddCommunity_whenUserIsAdmin() {
        val successAdminState = SelectCommunityState.Success(UserInfoState.Admin(listOf()))
        defineLiveDataState(successAdminState)

        scenario = launchActivity(makeScenarioIntent())

        selectCommunityActivity {
            sleep(200)
        } verify {
            checkBtnAddCommunityIsVisible()
        }
    }

    @Test
    fun shouldHideButtonAddCommunity_whenUserIsNotAdminAndBelongsACommunity() {
        val successNotAdminState = SelectCommunityState.Success(UserInfoState.NotAdmin(listOf()))
        defineLiveDataState(successNotAdminState)

        scenario = launchActivity(makeScenarioIntent())

        selectCommunityActivity {
            sleep(200)
        } verify {
            checkBtnAddCommunityIsNotVisible()
        }
    }

    @Test
    fun shouldShowNetworkError_whenThereIsNoConnectionToDoApiRequests() {
        val errorNetworkState = SelectCommunityState.Error(BprErrorType.NETWORK)
        defineLiveDataState(errorNetworkState)

        scenario = launchActivity(makeScenarioIntent())

        selectCommunityActivity {
            sleep(200)
        } verify {
            checkNetworkErrorMessage()
        }
    }

    @Test
    fun shouldShowUnknownError_whenOccursUnmappedExceptionToDoApiRequests() {
        val errorUnknownState = SelectCommunityState.Error(BprErrorType.UNKNOWN)
        defineLiveDataState(errorUnknownState)

        scenario = launchActivity(makeScenarioIntent())

        selectCommunityActivity {
            sleep(200)
        } verify {
            checkUnknownErrorMessage()
        }
    }

    @Test
    fun shouldShowContactAmecicloMessage_whenUserIsNotAdminAndDoesNotBelongsACommunity() {
        val successNotAdminWithoutCommunitiesState =
            SelectCommunityState.Success(UserInfoState.NotAdminWithoutCommunities)
        defineLiveDataState(successNotAdminWithoutCommunitiesState)

        scenario = launchActivity(makeScenarioIntent())

        selectCommunityActivity {
            sleep(200)
        } verify {
            checkContactAmecicloMessage()
        }
    }

    private fun defineLiveDataState(state: SelectCommunityState) {
        val liveData = MutableLiveData<SelectCommunityState>()
        runBlocking {
            liveData.postValue(state)
        }

        every {
            viewModel.selectCommunityState
        } returns liveData
    }

    private fun makeScenarioIntent(): Intent {
        return Intent(ApplicationProvider.getApplicationContext(), SelectCommunityActivity::class.java)
            .putExtra("adminId", "any_id")
            .putExtra("adminEmail", "any_email")
    }
}