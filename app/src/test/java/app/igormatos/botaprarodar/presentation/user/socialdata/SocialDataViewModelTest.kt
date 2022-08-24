package app.igormatos.botaprarodar.presentation.user.socialdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.getIndexFromList
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.utils.*
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SocialDataViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val mapOptions: Map<String, List<String>> = mapOf(
        "racialOptions" to racialOptions, "incomeOptions" to incomeOptions,
        "schoolingOptions" to schoolingOptions, "schoolingStatusOptions" to schoolingStatusOptions,
        "genderOptions" to genderOptions
    )

    private lateinit var formViewModel: SocialDataViewModel
    private val user = mockk<User>()

    @Before
    fun setup() {
        formViewModel = SocialDataViewModel(
            mapOptions,
            user,
            false
        )
    }

    @Test
    fun `when user is valid then button should be enabled`() {
        val testValidUser = createTestValidUser()
        createUserValues(testValidUser)
        observeValidationResultFields()

        doRegisterButtonAssertions(true)
    }

    private fun createTestValidUser(): User {
        val testValidUser = validUser.copy()
        testValidUser.docNumber = 11111111111
        return testValidUser
    }

    private fun createUserValues(testValidUser: User) {
        with(formViewModel) {
            userGender.value = testValidUser.gender.orEmpty()
            userRacial.value = testValidUser.racial.orEmpty()
            userSchooling.value = testValidUser.schooling.orEmpty()
            userSchoolingStatus.value = testValidUser.schoolingStatus.orEmpty()
            userIncome.value = testValidUser.income.orEmpty()
        }
    }

    private fun observeValidationResultFields() {
        val observerUserResultMock = mockk<Observer<Boolean>>(relaxed = true)
        formViewModel.isButtonEnabled.observeForever(observerUserResultMock)
    }

    private fun doRegisterButtonAssertions(enableStateExpected: Boolean) {
        val isButtonEnabled = formViewModel.isButtonEnabled.value
        Assert.assertNotNull(isButtonEnabled)
        if (isButtonEnabled != null) {
            MatcherAssert.assertThat(isButtonEnabled, CoreMatchers.equalTo(enableStateExpected))
        }
    }

    @Test
    fun `when  call updateUser should update the liveData values`() {
        formViewModel.updateUserValues(validUser, arrayListOf(validUser))

        Assert.assertEquals(validUser.gender, formViewModel.userGender.value)
        Assert.assertEquals(validUser.racial, formViewModel.userRacial.value)
        Assert.assertEquals(validUser.schooling, formViewModel.userSchooling.value)
        Assert.assertEquals(validUser.schoolingStatus, formViewModel.userSchoolingStatus.value)
        Assert.assertEquals(validUser.income, formViewModel.userIncome.value)

    }

    @Test
    fun `when call setSelectGenderIndex() then user gender value should be updated`() {
        val index = 1

        formViewModel.setSelectGenderIndex(index)
        Assert.assertEquals(index, formViewModel.selectedGenderIndex)
    }

    @Test
    fun `when call setSelectSchoolingIndex() then user schooling value should be updated`() {
        val index = 2

        formViewModel.setSelectSchoolingIndex(index)
        Assert.assertEquals(index, formViewModel.selectedSchoolingIndex)
    }

    @Test
    fun `when call setSelectSchoolingStatusIndex() then user schoolingStatus value should be updated`() {
        val index = R.id.schoolingStatusIncomplete

        formViewModel.setSelectSchoolingStatusIndex(index)
        formViewModel.confirmUserSchoolingStatus()
        val indexExpected = formViewModel.getSelectedSchoolingStatusListIndex()
        assertEquals(indexExpected, formViewModel.selectedSchoolingStatusIndex.value)
    }

    @Test
    fun `when call confirmUserSchoolingStatus() then user schoolingStatus value should be updated`() {
        val index = R.id.schoolingStatusIncomplete

        formViewModel.setSelectSchoolingStatusIndex(index)
        formViewModel.confirmUserSchoolingStatus()

        val indexExpected = mapOptions.getIndexFromList(
            "schoolingStatusOptions",
            formViewModel.userSchoolingStatus.value.toString()
        )
        Assert.assertEquals(
            schoolingStatusOptions[indexExpected],
            formViewModel.userSchoolingStatus.value
        )
    }

    @Test
    fun `when call setSelectRacialIndex() then user racial value should be updated`() {
        val index = 2

        formViewModel.setSelectRacialIndex(index)
        Assert.assertEquals(index, formViewModel.selectedRacialIndex)
    }

    @Test
    fun `when call setSelectIncomeIndex() then user income value should be updated`() {
        val index = 2

        formViewModel.setSelectIncomeIndex(index)
        Assert.assertEquals(index, formViewModel.selectedIncomeIndex)
    }

    @Test
    fun `when call confirmUserGender() then user gender value should be updated`() {
        val index = 1

        formViewModel.setSelectGenderIndex(index)
        formViewModel.confirmUserGender()
        Assert.assertEquals(genderOptions[index], formViewModel.userGender.value)
    }

    @Test
    fun `when call confirmUserSchooling() then user schooling value should be updated`() {
        val index = 2

        formViewModel.setSelectSchoolingIndex(index)
        formViewModel.confirmUserSchooling()
        Assert.assertEquals(schoolingOptions[index], formViewModel.userSchooling.value)
    }

    @Test
    fun `when call confirmUserRace() then user racial value should be updated`() {
        val index = 2

        formViewModel.setSelectRacialIndex(index)
        formViewModel.confirmUserRace()
        Assert.assertEquals(racialOptions[index], formViewModel.userRacial.value)
    }

    @Test
    fun `when call confirmUserIncome() then user income value should be updated`() {
        val index = 2

        formViewModel.setSelectIncomeIndex(index)
        formViewModel.confirmUserIncome()
        Assert.assertEquals(incomeOptions[index], formViewModel.userIncome.value)
    }

}
