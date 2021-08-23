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
import org.hamcrest.CoreMatchers.equalTo
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFormFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<UserFormFragment>

    @Before
    fun setup() {
        val fragmentArgs = bundleOf()
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme,
            fragmentArgs = fragmentArgs)

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
    fun givenDocNumber_shouldHaveMaxLength() {
        userFormFragment {
            val longDocNumber = "123456789123456789"
            fillUserDocNumber(longDocNumber)
        } verify {
            checkDocNumberMaxLength()
        }
    }

    @Test
    fun shouldOpenRacialDialog_whenClickToSelectRacial() {
        userFormFragment {
            clickRacialEditText()
        } verify {
            verifyRacialDialogIsShowing()
        }
    }

    @Test
    fun shouldListRacialOptions_whenClickToSelectRacial() {
        userFormFragment {
            clickRacialEditText()
        } verify {
            verifyRacialOptionIsShowing()
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}