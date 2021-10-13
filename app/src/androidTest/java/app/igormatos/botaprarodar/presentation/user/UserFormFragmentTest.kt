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
    fun shouldOpenGenderDialog_whenClickToSelectGender() {
        userFormFragment {
            clickGenderEditText()
        } verify {
            verifyGenderDialogIsShowing()
        }
    }

    @Test
    fun shouldListGenderOptions_whenClickToSelectGender() {
        userFormFragment {
            clickGenderEditText()
        } verify {
            verifyGenderOptionIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenGenderOptionIsSelected() {
        val genderSelectedIndex = 1
        userFormFragment {
            clickGenderEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(genderSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyGenderEditTextIsEqualSelected(genderSelectedIndex)
        }
    }

    fun givenPhoneNumber_shouldHaveMaxLength() {
        userFormFragment {
            val longPhoneNumber = "123456789123456789"
            fillUserPhone(longPhoneNumber)
        } verify {
            checkPhoneNumberMaxLength()
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

    @Test
    fun shouldShowSelectedOption_whenRacialOptionIsSelected() {
        val racialSelectedIndex = 2
        userFormFragment {
            clickRacialEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(racialSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyRacialEditTextIsEqualSelected(racialSelectedIndex)
        }
    }

    @Test
    fun shouldOpenIncomeDialog_whenClickToSelectIncome() {
        userFormFragment {
            clickIncomeEditText()
        } verify {
            verifyIncomeDialogIsShowing()
        }
    }

    @Test
    fun shouldOpenSchoolingDialog_whenClickToSelectSchooling() {
        userFormFragment {
            clickSchoolingEditText()
        } verify {
            verifySchoolingDialogIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenIncomeOptionIsSelected() {
        val incomeSelectedIndex = 2
        userFormFragment {
            clickIncomeEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(incomeSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyIncomeEditTextIsEqualSelected(incomeSelectedIndex)
        }
    }

    @Test
    fun shouldListSchoolingOptions_whenClickToSelectSchooling() {
        userFormFragment {
            clickSchoolingEditText()
        } verify {
            verifySchoolingOptionIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenSchoolingOptionIsSelected() {
        val schoolingSelectedPosition = 2
        userFormFragment {
            clickSchoolingEditText()
            sleep(1000)
            clickOptionOnSchoolingDialog(schoolingSelectedPosition)
            clickSchoolingPositiveButton()
        } verify {
            verifySchoolingEditTextIsEqualSelected(schoolingSelectedPosition)
        }
    }

    @Test
    fun shouldShowSelectedOption_whenSchoolingOptionIsNotSelected() {
        val schoolingSelectedPosition = 2
        userFormFragment {
            clickSchoolingEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(schoolingSelectedPosition)
            clickBackButton()
        } verify {
            verifySchoolingEditTextNotEqualSelected(schoolingSelectedPosition)
        }
    }

    fun shouldShowSelectedOptionAndCloseDialog_whenRacialOptionIsNotSelected() {
        val racialSelectedIndex = 2
        userFormFragment {
            clickRacialEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(racialSelectedIndex)
            clickBackButton()
        } verify {
            verifyRacialEditTextIsNotEqualSelected(racialSelectedIndex)
        }
    }


    @Test
    fun shouldShowSelectedOptionAndCloseDialog_whenIncomeOptionIsNotSelected() {
        val incomeSelectedIndex = 2
        userFormFragment {
            clickIncomeEditText()
            sleep(1000)
            clickOptionOnUserFormFragmentDialog(incomeSelectedIndex)
            clickBackButton()
        } verify {
            verifyIncomeEditTextIsNotEqualSelected(incomeSelectedIndex)
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}