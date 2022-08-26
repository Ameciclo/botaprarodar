package app.igormatos.botaprarodar.presentation.user.socialdata

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.User
import io.mockk.MockK
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SocialDataFragmentCreateTest {

    private lateinit var fragmentScenario: FragmentScenario<SocialDataFragment>
    private lateinit var socialDataViewModel: SocialDataViewModel

    @Before
    fun setup() {
        val user = mockk<User>(relaxed = true)
        val fragmentArgs = bundleOf(
            "user" to user,
            "editMode" to false,
            "deleteImagePaths" to arrayOf(""),
            "communityUsers" to arrayOf(user)
        )

        socialDataViewModel = mockk(relaxed = true)
        fragmentScenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme,
            fragmentArgs = fragmentArgs
        )

        Intents.init()
    }

    @Test
    fun shouldOpenGenderDialog_whenClickToSelectGender() {
        socialDataFragment {
            clickGenderEditText()
        } verify {
            verifyGenderDialogIsShowing()
        }
    }

    @Test
    fun shouldListGenderOptions_whenClickToSelectGender() {
        socialDataFragment {
            clickGenderEditText()
        } verify {
            verifyGenderOptionIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenGenderOptionIsSelected() {
        val genderSelectedIndex = 1
        socialDataFragment {
            clickGenderEditText()
            sleep(1000)
            clickOptionOnSocialDataFragmentDialog(genderSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyGenderEditTextIsEqualSelected(genderSelectedIndex)
        }
    }

    @Test
    fun shouldListRacialOptions_whenClickToSelectRacial() {
        socialDataFragment {
            clickRacialEditText()
        } verify {
            verifyRacialOptionIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenRacialOptionIsSelected() {
        val racialSelectedIndex = 2
        socialDataFragment {
            clickRacialEditText()
            sleep(1000)
            clickOptionOnSocialDataFragmentDialog(racialSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyRacialEditTextIsEqualSelected(racialSelectedIndex)
        }
    }

    @Test
    fun shouldOpenIncomeDialog_whenClickToSelectIncome() {
        socialDataFragment {
            clickIncomeEditText()
        } verify {
            verifyIncomeDialogIsShowing()
        }
    }

    @Test
    fun shouldOpenSchoolingDialog_whenClickToSelectSchooling() {
        socialDataFragment {
            clickSchoolingEditText()
        } verify {
            verifySchoolingDialogIsShowing()
        }
    }

    @Test
    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusComplete() {
        socialDataFragment {
            clickSchoolingStatusComplete()
        } verify {
            verifySchoolingStatusComplete()
        }
    }

    @Test
    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusIncomplete() {
        socialDataFragment {
            clickSchoolingStatusIncomplete()
        } verify {
            verifySchoolingStatusIncompleteg()
        }
    }

    @Test
    fun shouldOpenSchoolingStatusRadioButton_whenClickToSelectSchoolingStatusStudying() {
        socialDataFragment {
            clickSchoolingStatusStudying()
        } verify {
            verifySchoolingStatusStudying()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenIncomeOptionIsSelected() {
        val incomeSelectedIndex = 2
        socialDataFragment {
            clickIncomeEditText()
            sleep(1000)
            clickOptionOnSocialDataFragmentDialog(incomeSelectedIndex)
            clickPositiveButton()
        } verify {
            verifyIncomeEditTextIsEqualSelected(incomeSelectedIndex)
        }
    }

    @Test
    fun shouldListSchoolingOptions_whenClickToSelectSchooling() {
        socialDataFragment {
            clickSchoolingEditText()
        } verify {
            verifySchoolingOptionIsShowing()
        }
    }

    @Test
    fun shouldShowSelectedOption_whenSchoolingOptionIsSelected() {
        val schoolingSelectedPosition = 2
        socialDataFragment {
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
        socialDataFragment {
            clickSchoolingEditText()
            sleep(1000)
            clickOptionOnSocialDataFragmentDialog(schoolingSelectedPosition)
            clickBackButton()
        } verify {
            verifySchoolingEditTextNotEqualSelected(schoolingSelectedPosition)
        }
    }

    fun shouldShowSelectedOptionAndCloseDialog_whenRacialOptionIsNotSelected() {
        val racialSelectedIndex = 2
        socialDataFragment {
            clickRacialEditText()
            sleep(1000)
            clickOptionOnSocialDataFragmentDialog(racialSelectedIndex)
            clickBackButton()
        } verify {
            verifyRacialEditTextIsNotEqualSelected(racialSelectedIndex)
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}
