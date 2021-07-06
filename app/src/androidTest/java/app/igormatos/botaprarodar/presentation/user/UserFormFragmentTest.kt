package app.igormatos.botaprarodar.presentation.user

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.user.userform.UserFormFragment
import org.junit.*
import org.junit.Assert.fail
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFormFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<UserFormFragment>

    @Before
    fun setup() {
        val fragmentArgs = bundleOf()
        fragmentScenario = launchFragmentInContainer(themeResId = R.style.AppTheme, fragmentArgs = fragmentArgs)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    @SdkSuppress(minSdkVersion = 30)
    fun shouldOpenCameraView_whenClickToTakeProfilePicture() {
        userFormFragment {
            clickProfileImage()
            clickConfirm()
        } verify {
            intended(toPackage("com.android.camera2"))
        }
    }
}