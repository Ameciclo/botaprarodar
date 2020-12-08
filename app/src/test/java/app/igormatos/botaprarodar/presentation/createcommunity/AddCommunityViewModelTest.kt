package app.igormatos.botaprarodar.presentation.createcommunity

import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.utils.completeCommunityRequestStub
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.brunotmgomes.ui.SimpleResult

@ExtendWith(InstantExecutorExtension::class)
@DisplayName("Given AddCommunityViewModel")
internal class AddCommunityViewModelTest {

    private val addCommunityUseCaseMock = mockk<AddCommunityUseCase>()

    private val observerLoadingLiveDataMock = mockk<Observer<Boolean>>(relaxed = true)

    private val observerSuccessLiveDataMock = mockk<Observer<Boolean>>(relaxed = true)

    private val observerErrorLiveDataMock = mockk<Observer<Exception>>(relaxed = true)

    private val resultError = SimpleResult.Error(Exception())

    lateinit var viewModel: AddCommunityViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = AddCommunityViewModel(addCommunityUseCaseMock)
    }

    @Nested
    @DisplayName("When addCommunityUseCase return Result.Success should observer onChanged")
    inner class FlowSuccess {

        @Test
        fun `WHEN click to send a new community, THEN update loading live data to true`() {
            viewModel.getLoadingLiveDataValue().observeForever(observerLoadingLiveDataMock)

            val community = completeCommunityRequestStub()

            coEvery { addCommunityUseCaseMock.addNewCommunity(community) } returns SimpleResult.Success(
                "response"
            )

            runBlocking { viewModel.sendCommunity(community) }

            verify {
                observerLoadingLiveDataMock.onChanged(true)
            }
        }


        @Test
        fun `When firebase return is a success, should update success live data to true`() {
            viewModel.getSuccessLiveDataValue().observeForever(observerSuccessLiveDataMock)

            val community = completeCommunityRequestStub()

            coEvery { addCommunityUseCaseMock.addNewCommunity(community) } returns SimpleResult.Success(
                "response"
            )

            runBlocking { viewModel.sendCommunity(community) }

            verify {
                observerSuccessLiveDataMock.onChanged(true)
            }
        }
    }

    @Nested
    @DisplayName("When addCommunityUseCase return Result.Error should result with exception")
    inner class FlowError {

        @Test
        fun `When firebase return is an exception, should update error live data exception`() {
            viewModel.getErrorLiveDataValue().observeForever(observerErrorLiveDataMock)

            val community = completeCommunityRequestStub()

            coEvery { addCommunityUseCaseMock.addNewCommunity(community) } returns resultError

            runBlocking { viewModel.sendCommunity(community) }

            verify {
                observerErrorLiveDataMock.onChanged(resultError.exception)
            }
        }

    }
}