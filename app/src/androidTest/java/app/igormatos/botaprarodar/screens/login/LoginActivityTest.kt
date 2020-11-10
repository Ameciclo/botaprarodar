package app.igormatos.botaprarodar.screens.login

import androidx.test.filters.MediumTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.data.model.UserCommunityInfo
import app.igormatos.botaprarodar.network.Community
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Test
    fun navigateTriggered_navigates() {
        val robot = LoginActivityRobot()

        robot.triggerNavigateMain()

        robot.verifyNavigatedToMainActivity()
        robot.finish()
    }

    @Test
    fun nothingTriggered_snackbarHidden() {
        val robot = LoginActivityRobot(emailNotVerified = false)

        robot.verifyEmailSnackbarShown(false)
        robot.finish()
    }

    @Test
    fun emailNotVerifiedTriggered_showsResendEmail_onResend_dismiss() {
        val robot = LoginActivityRobot(emailNotVerified = true)

        robot.verifyEmailSnackbarShown(true)

        robot.startResendEmail()
        robot.finishResendEmail()

        robot.verifyEmailSnackbarDismissed()
        robot.finish()
    }

    @Test
    fun pressLogin_loginSuccess_triggersLoginSuccess() {
        val robot = LoginActivityRobot()

        robot.clickLogin(isSuccess = true)

        robot.verifyLoggedIn(true)
        robot.finish()
    }

    @Test
    fun pressLogin_loginFails_showErrorSnackbar() {
        val robot = LoginActivityRobot()

        robot.clickLogin(isSuccess = false)

        robot.verifyLoggedIn(false)
        robot.verifyErrorSnackbarShown()
        robot.finish()
    }

    @Test
    fun loading_showsAndHidesDialog() {
        val robot = LoginActivityRobot()
        robot.verifyLoadingDialogShown(false)

        robot.setLoading(true)
        robot.verifyLoadingDialogShown(true)
        robot.setLoading(false)

        robot.verifyLoadingDialogShown(false)
        robot.finish()
    }

    @Test
    fun communitiesLoaded_clickCommunity_setsCommunityViewModel() {
        val robot = LoginActivityRobot()
        val communityOne = Community("carangueijo tabaiares")
        val communityTwo = Community("second community")
        val communities = UserCommunityInfo(isAdmin = true, listOf(communityOne, communityTwo))
        robot.triggerCommunitiesLoaded(communities)

        robot.verifyCommunityShown(communityOne)
        robot.clickCommunity(communityOne)

        robot.verifyCommunityChosen(communityOne)
        robot.finish()
    }

    @Test
    fun communitiesLoadedNoAccess_showsNoCommunitiesDialog(){
        val robot = LoginActivityRobot()
        val communities = UserCommunityInfo(isAdmin = false, emptyList())
        robot.triggerCommunitiesLoaded(communities)

        robot.verifyNoCommunityDialogShown()
        robot.finish()
    }

    @Test
    fun communitiesLoadedNotAdmin_hidesAddCommunity(){
        val robot = LoginActivityRobot()
        val communities = UserCommunityInfo(isAdmin = false, emptyList())
        robot.triggerCommunitiesLoaded(communities)

        robot.verifyAddCommunityButtonShown(false)
        robot.finish()
    }

    @Test
    fun communitiesLoadedAdmin_clickAddCommunity_navigatesToAddCommunity(){
        val robot = LoginActivityRobot()
        val communities = UserCommunityInfo(isAdmin = true, emptyList())
        robot.triggerCommunitiesLoaded(communities)

        robot.clickAddCommunity()

        robot.verifyNavigatedToAddCommunity()
        robot.finish()
    }

}