package app.igormatos.botaprarodar.presentation.user

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.Fixtures.validUser
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.user.userform.UserFormFragment
import app.igormatos.botaprarodar.presentation.user.userform.UserFormViewModel
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFormFragmentUpdateTest {

    private lateinit var fragmentScenario: FragmentScenario<UserFormFragment>
    private lateinit var userFormViewModel: UserFormViewModel

    @Before
    fun setup() {
        val fragmentArgs = bundleOf("user" to validUser)

        userFormViewModel = mockk(relaxed = true)
        fragmentScenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme,
            fragmentArgs = fragmentArgs
        )

        Intents.init()
    }

    @Test
    fun shouldShowDialogWhenClickOnImageResidence_whenThereIsResidenceProofImage() {

        userFormFragment {

            clickResidenceProofImage()

        } verify {
            verifyDialogEditResidenceImageIsShowing()
        }
    }

    @Test
    fun shouldShowDialogWhenClickDeleteButton_whenTryToDeleteResidenceProofImage() {
        userFormFragment {
            clickResidenceProofImage()
            clickDeleteButtonOnDialogImage()
        } verify {
            verifyDialogDeleteResidenceImageIsShowing()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}