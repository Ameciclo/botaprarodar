package app.igormatos.botaprarodar.screens.createcommunity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.network.Community
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import utils.SimpleResult
import java.lang.Exception

class AddCommunityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val addCommunityUseCaseMock = mockk<AddCommunityUseCase>()

    private val observerLoadingLiveDataMock = mockk<Observer<Boolean>>(relaxed = true)

    private val observerSuccessLiveDataMock = mockk<Observer<Boolean>>(relaxed = true)

    private val observerErrorLiveDataMock = mockk<Observer<Exception>>(relaxed = true)

    private val resultError = SimpleResult.Error(Exception())

    lateinit var viewModel: AddCommunityViewModel

    @Before
    fun setUp() {
        viewModel = AddCommunityViewModel(addCommunityUseCaseMock)
    }


    @Test
    fun `WHEN click to send a new community, THEN update loading live data to true`() {
        viewModel.getLoadingLiveDataValue().observeForever(observerLoadingLiveDataMock)

        coEvery { addCommunityUseCaseMock.addCommunityToServer(any()) } returns SimpleResult.Success(
            true
        )

        viewModel.sendCommunity(Community())

        verify {
            observerLoadingLiveDataMock.onChanged(true)
        }
    }

    @Test
    fun `WHEN firebase return is a success, THEN update success live data to true`() {
        viewModel.getSuccessLiveDataValue().observeForever(observerSuccessLiveDataMock)

        coEvery { addCommunityUseCaseMock.addCommunityToServer(any()) } returns SimpleResult.Success(
            true
        )

        viewModel.sendCommunity(Community())

        verify {
            observerSuccessLiveDataMock.onChanged(true)
        }
    }

    @Test
    fun `WHEN firebase return is an exception, THEN update error live data exception`() {
        viewModel.getErrorLiveDataValue().observeForever(observerErrorLiveDataMock)

        val community = Community()

        coEvery { addCommunityUseCaseMock.addCommunityToServer(community) } returns resultError

        viewModel.sendCommunity(community)

        verify {
            observerErrorLiveDataMock.onChanged(resultError.exception)
        }
    }
}