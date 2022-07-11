package app.igormatos.botaprarodar.accessapphome

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.presentation.login.FirebaseAuthModuleTestImpl
import app.igormatos.botaprarodar.presentation.login.selectCommunity.*
import com.brunotmgomes.ui.SimpleResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class AccessHomeTest {
    private lateinit var scenario: ActivityScenario<SelectCommunityActivity>
    private lateinit var testModule: Module
    private lateinit var selectCommunityUseCase: SelectCommunityUseCase
    private lateinit var bikeActionUseCase: BikeActionUseCase

    @Before
    fun setup() {
        selectCommunityUseCase = mockk(relaxed = true)
        bikeActionUseCase = spyk(BikeActionUseCase(mockk(relaxed = true)))

        testModule = module(override = true) {

            factory {
                selectCommunityUseCase
            }

            single {
                bikeActionUseCase
            }

            single<FirebaseAuthModule> {
                FirebaseAuthModuleTestImpl(mockk(relaxed = true))
            }
        }

        loadKoinModules(testModule)
    }

    @Ignore
    @Test
    fun access_home_as_admin() {
        defineUseCasesBehavior()
        scenario = launchActivity()

        accessHome {
            selectCommunityActivity {
                selectAnyCommunity()
            }
        } verify {
            assertThat(checkIfIsHome()).isTrue()
        }
    }

    private fun defineUseCasesBehavior() {
        val communities: MutableList<Community> = mutableListOf(Community(name = "Test Community"))
        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(any(), any())
        } returns SelectCommunityState.Success(UserInfoState.Admin(communities))

        coEvery {
            bikeActionUseCase.getBikes(any())
        } returns SimpleResult.Success(mutableListOf(BikeRequest()))
    }
}
