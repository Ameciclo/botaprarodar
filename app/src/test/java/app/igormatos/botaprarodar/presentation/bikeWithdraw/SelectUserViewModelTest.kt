package app.igormatos.botaprarodar.presentation.bikeWithdraw

import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.usecase.users.GetUsersByCommunity
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.utils.userFlowSuccess
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class SelectUserViewModelTest {
    private val userHolder = mockk<UserHolder>()
    private val stepperAdapter = mockk<WithdrawStepper>()
    private val getUsersByCommunity = mockk<GetUsersByCommunity>()
    private val validateUserWithdraw = mockk<ValidateUserWithdraw>()

    private lateinit var selectUserViewModel : SelectUserViewModel

    @BeforeEach
    fun setup() {
        selectUserViewModel = SelectUserViewModel(userHolder, stepperAdapter,
            getUsersByCommunity, validateUserWithdraw )
    }


    @Test
    fun `when getUserList should call validateUserWithdraw`() {
        coEvery { getUsersByCommunity.execute(any()) } returns userFlowSuccess
        val anyTestResult = true
        coEvery { validateUserWithdraw.execute(any()) } returns anyTestResult

        selectUserViewModel.getUserList("communityIdTest")

        verify { runBlocking { validateUserWithdraw.execute(any()) } }
    }

}