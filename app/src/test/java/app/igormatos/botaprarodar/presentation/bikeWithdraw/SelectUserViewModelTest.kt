package app.igormatos.botaprarodar.presentation.bikeWithdraw

import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.utils.communityFixture
import app.igormatos.botaprarodar.utils.listUsers
import com.brunotmgomes.ui.SimpleResult
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
    private val getUsersByCommunity = mockk<UsersUseCase>()
    private val validateUserWithdraw = mockk<ValidateUserWithdraw>()

    private lateinit var selectUserViewModel : SelectUserViewModel

    @BeforeEach
    fun setup() {
        selectUserViewModel = SelectUserViewModel(userHolder, stepperAdapter,
            getUsersByCommunity, validateUserWithdraw )
    }


    @Test
    fun `when getUserList should call validateUserWithdraw`() {
        coEvery { getUsersByCommunity.getAvailableUsersByCommunityId(any()) } returns SimpleResult.Success(
            listUsers)
        val anyTestResult = true
        coEvery { validateUserWithdraw.execute(any()) } returns anyTestResult

        selectUserViewModel.getUserList(communityFixture.id)

        verify { runBlocking { validateUserWithdraw.execute(any()) } }
    }

}