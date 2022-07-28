package app.igormatos.botaprarodar.presentation.user

import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.user.userform.UserFormFragment
import app.igormatos.botaprarodar.presentation.user.userform.UserFormViewModel
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFormFragmentCreateTest {

    private lateinit var fragmentScenario: FragmentScenario<UserFormFragment>
    private lateinit var userFormViewModel: UserFormViewModel

    @Before
    fun setup() {
        val fragmentArgs = bundleOf()

        userFormViewModel = mockk(relaxed = true)
        fragmentScenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme,
            fragmentArgs = fragmentArgs
        )

        Intents.init()
    }

    @Test
    @SdkSuppress(minSdkVersion = 30)
    fun givenAndroidSdk30_shouldOpenCameraView_whenClickToTakeProfilePicture() {
        userFormFragment {
            clickProfileImage()
            clickConfirmDialog()
        } verify {
            intended(hasAction(equalTo(MediaStore.ACTION_IMAGE_CAPTURE)))
        }
    }

    @Test
    fun givenPhoneNumber_shouldHaveMaxLength() {
        userFormFragment {
            val longPhoneNumber = "1234567890123"
            fillUserPhone(longPhoneNumber)
        } verify {
            checkPhoneNumberMaxLength()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}
