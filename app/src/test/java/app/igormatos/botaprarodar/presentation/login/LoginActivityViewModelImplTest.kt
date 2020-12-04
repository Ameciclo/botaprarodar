package app.igormatos.botaprarodar.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.model.community.Community
import org.junit.Rule
import org.junit.Test

class LoginActivityViewModelImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun userLoggedInCommunityChosen_navigatesToMainActivity() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = true
        ) {
            triggerCheckPreviousState()

            verifyNavigatedToMainActivity()
        }
    }

    @Test
    fun userLoggedInCommunityNotChosenEmailNotVerified_showsEmailNotVerified(){
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false,
            emailIsVerified = false,
        ) {
            triggerCheckPreviousState()

            verifyTriggeredShowResendEmail()
        }
    }

    @Test
    fun userLoggedInCommunityNotChosen_communitiesLoading_showsLoading() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            triggerCheckPreviousState()

            startCommunitiesRequest()
            verifyLoading(true)
            finishCommunitiesRequest(success = true)

            verifyLoading(false)
        }
    }

    @Test
    fun userLoggedInCommunityNotChosen_loadCommunities_showsResults() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            val isAdmin = false
            val communitiesLoaded = listOf(
                Community(name = "testCommunity")
            )

            triggerCheckPreviousState()
            startCommunitiesRequest()
            finishCommunitiesRequest(
                success = true,
                isAdmin = isAdmin,
                communities = communitiesLoaded
            )

            verifyLoadedCommunities(
                expectedAdmin = isAdmin,
                expectedCommunities = communitiesLoaded
            )
        }
    }

    @Test
    fun userLoggedInCommunityNotChosen_errorLoadCommunities_showsLoadFail() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            triggerCheckPreviousState()
            startCommunitiesRequest()
            finishCommunitiesRequest(
                success = false,
            )

            verifyLoadedCommunitiesFailed()
        }
    }

    @Test
    fun errorLoadCommunities_reloadCommunities_callsLoadCommunities(){
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            triggerReloadCommunities()

            verifyTriggeredLoadCommunities()
        }
    }

    @Test
    fun userLoggedInCommunityNotChosen_chooseCommunity_savesAndNavigates() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            val chosenCommunity = Community(id="chooseCommunity", name = "testCommunity")

            chooseCommunity(chosenCommunity)

            verifySavedCommunity(chosenCommunity)
            verifyNavigatedToMainActivity()
        }
    }

    @Test
    fun userLoggedOut_logIn_loadCommunities() {
        LoginActivityViewModelRobot.test(
            userLoggedIn = false
        ) {
            login()

            verifyTriggeredLoadCommunities()
        }
    }

}