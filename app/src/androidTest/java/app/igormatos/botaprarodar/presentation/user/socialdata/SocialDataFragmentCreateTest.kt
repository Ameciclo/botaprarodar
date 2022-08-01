package app.igormatos.botaprarodar.presentation.user.socialdata

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
class SocialDataFragmentCreateTest {

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

//    @Test
//    fun shouldOpenGenderDialog_whenClickToSelectGender() {
//        userFormFragment {
//            clickGenderEditText()
//        } verify {
//            verifyGenderDialogIsShowing()
//        }
//    }
//
//    @Test
//    fun shouldListGenderOptions_whenClickToSelectGender() {
//        userFormFragment {
//            clickGenderEditText()
//        } verify {
//            verifyGenderOptionIsShowing()
//        }
//    }
//
//    @Test
//    fun shouldShowSelectedOption_whenGenderOptionIsSelected() {
//        val genderSelectedIndex = 1
//        userFormFragment {
//            clickGenderEditText()
//            sleep(1000)
//            clickOptionOnUserFormFragmentDialog(genderSelectedIndex)
//            clickPositiveButton()
//        } verify {
//            verifyGenderEditTextIsEqualSelected(genderSelectedIndex)
//        }
//    }
//
//    @Test
//    fun givenPhoneNumber_shouldHaveMaxLength() {
//        userFormFragment {
//            val longPhoneNumber = "1234567890123"
//            fillUserPhone(longPhoneNumber)
//        } verify {
//            checkPhoneNumberMaxLength()
//        }
//    }
//
//    @Ignore("Unused")
//    @Test
//    fun shouldListRacialOptions_whenClickToSelectRacial() {
//        userFormFragment {
//            clickRacialEditText()
//        } verify {
//            verifyRacialOptionIsShowing()
//        }
//    }
//
//    @Ignore("Unused")
//    @Test
//    fun shouldShowSelectedOption_whenRacialOptionIsSelected() {
//        val racialSelectedIndex = 2
//        userFormFragment {
//            clickRacialEditText()
//            sleep(1000)
//            clickOptionOnUserFormFragmentDialog(racialSelectedIndex)
//            clickPositiveButton()
//        } verify {
//            verifyRacialEditTextIsEqualSelected(racialSelectedIndex)
//        }
//    }
//
//    @Test
//    fun shouldOpenIncomeDialog_whenClickToSelectIncome() {
//        userFormFragment {
//            clickIncomeEditText()
//        } verify {
//            verifyIncomeDialogIsShowing()
//        }
//    }
//
//    @Test
//    fun shouldOpenSchoolingDialog_whenClickToSelectSchooling() {
//        userFormFragment {
//            clickSchoolingEditText()
//        } verify {
//            verifySchoolingDialogIsShowing()
//        }
//    }
//
//    @Test
//    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusComplete() {
//        userFormFragment {
//            clickSchoolingStatusComplete()
//        } verify {
//            verifySchoolingStatusComplete()
//        }
//    }
//
//    @Test
//    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusIncomplete() {
//        userFormFragment {
//            clickSchoolingStatusIncomplete()
//        } verify {
//            verifySchoolingStatusIncompleteg()
//        }
//    }
//
//    @Test
//    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusStudying() {
//        userFormFragment {
//            clickSchoolingStatusStudying()
//        } verify {
//            verifySchoolingStatusStudying()
//        }
//    }
//
//    @Test
//    fun shouldShowSelectedOption_whenIncomeOptionIsSelected() {
//        val incomeSelectedIndex = 2
//        userFormFragment {
//            clickIncomeEditText()
//            sleep(1000)
//            clickOptionOnUserFormFragmentDialog(incomeSelectedIndex)
//            clickPositiveButton()
//        } verify {
//            verifyIncomeEditTextIsEqualSelected(incomeSelectedIndex)
//        }
//    }
//
//    @Test
//    fun shouldListSchoolingOptions_whenClickToSelectSchooling() {
//        userFormFragment {
//            clickSchoolingEditText()
//        } verify {
//            verifySchoolingOptionIsShowing()
//        }
//    }
//
//    @Test
//    fun shouldShowSelectedOption_whenSchoolingOptionIsSelected() {
//        val schoolingSelectedPosition = 2
//        userFormFragment {
//            clickSchoolingEditText()
//            sleep(1000)
//            clickOptionOnSchoolingDialog(schoolingSelectedPosition)
//            clickSchoolingPositiveButton()
//        } verify {
//            verifySchoolingEditTextIsEqualSelected(schoolingSelectedPosition)
//        }
//    }
//
//    @Test
//    fun shouldShowSelectedOption_whenSchoolingOptionIsNotSelected() {
//        val schoolingSelectedPosition = 2
//        userFormFragment {
//            clickSchoolingEditText()
//            sleep(1000)
//            clickOptionOnUserFormFragmentDialog(schoolingSelectedPosition)
//            clickBackButton()
//        } verify {
//            verifySchoolingEditTextNotEqualSelected(schoolingSelectedPosition)
//        }
//    }
//
//    fun shouldShowSelectedOptionAndCloseDialog_whenRacialOptionIsNotSelected() {
//        val racialSelectedIndex = 2
//        userFormFragment {
//            clickRacialEditText()
//            sleep(1000)
//            clickOptionOnUserFormFragmentDialog(racialSelectedIndex)
//            clickBackButton()
//        } verify {
//            verifyRacialEditTextIsNotEqualSelected(racialSelectedIndex)
//        }
//    }

    @After
    fun tearDown() {
        Intents.release()
    }
}