package app.igormatos.botaprarodar.createcommunity

import android.content.Intent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.appendTimestamp
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.presentation.login.FirebaseAuthModuleTestImpl
import app.igormatos.botaprarodar.presentation.login.selectCommunity.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class AddCommunityActivityTest {

    private lateinit var scenario: ActivityScenario<SelectCommunityActivity>
    private lateinit var testModule: Module
    private lateinit var selectCommunityUseCase: SelectCommunityUseCase
    private lateinit var addCommunityUseCase: AddCommunityUseCase

    @Before
    fun setUp() {

        selectCommunityUseCase = mockk(relaxed = true)
        addCommunityUseCase = mockk(relaxed = true)

        testModule = module(override = true) {

            factory {
                selectCommunityUseCase
            }

            single<FirebaseAuthModule> {
                FirebaseAuthModuleTestImpl(mockk(relaxed = true))
            }

            single {
                addCommunityUseCase
            }
        }

        loadKoinModules(testModule)
    }

    @Test
    @LargeTest
    fun shouldAddNewCommunityUserJourney() {
        val communities: MutableList<Community> = mutableListOf()
        defineUseCasesBehavior(communities)

        val intent = Intent(ApplicationProvider.getApplicationContext(), SelectCommunityActivity::class.java)
            .putExtra("adminId", "any_id")
            .putExtra("adminEmail", "any_email")

        scenario = launchActivity(intent)

        val communityName = appendTimestamp("Comunidade")
        selectCommunityActivity {
            clickBtnAddCommunity()
            addCommunity {
                communities.add(Community(name = communityName))
                saveNewCommunity(communityName)
            }
        } verify {
            findItemOnRecyclerView(communityName)
        }
    }

    private fun defineUseCasesBehavior(communities: MutableList<Community>) {
        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(any(), any())
        } returns SelectCommunityState.Success(UserInfoState.Admin(communities))

        coEvery {
            addCommunityUseCase.addNewCommunity(any())
        } returns SimpleResult.Success("")
    }
}
