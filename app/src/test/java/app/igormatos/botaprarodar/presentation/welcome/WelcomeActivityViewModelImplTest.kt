package app.igormatos.botaprarodar.presentation.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.model.community.Community
import org.junit.Rule
import org.junit.Test

class WelcomeActivityViewModelImplTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun userLoggedInCommunityChosen_navigatesToMainActivity() {
        WelcomeActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = true
        ) {
            triggerCheckPreviousState()

            verifyNavigatedToMainActivity()
        }
    }

    @Test
    fun userLoggedInCommunityNotChosenEmailNotVerified_showsEmailNotVerified(){
        WelcomeActivityViewModelRobot.test(
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
        WelcomeActivityViewModelRobot.test(
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
        WelcomeActivityViewModelRobot.test(
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
        WelcomeActivityViewModelRobot.test(
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
        WelcomeActivityViewModelRobot.test(
            userLoggedIn = true,
            userChoseCommunity = false
        ) {
            triggerReloadCommunities()

            verifyTriggeredLoadCommunities()
        }
    }

    @Test
    fun userLoggedInCommunityNotChosen_chooseCommunity_savesAndNavigates() {
        WelcomeActivityViewModelRobot.test(
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
        WelcomeActivityViewModelRobot.test(
            userLoggedIn = false
        ) {
            login()

            verifyTriggeredLoadCommunities()
        }
    }

}