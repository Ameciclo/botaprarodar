package app.igormatos.botaprarodar.screens.createcommunity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.network.Community
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import utils.Result
import java.lang.Exception

class AddCommunityViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val addCommunityInteractorMock = mockk<AddCommunityInteractor>()

    private val observerMock = mockk<Observer<Boolean>>(relaxed = true)

    private val exceptionMock = mockk<Exception>(relaxed = true)

    lateinit var viewModelTest: AddCommunityViewModel

    @Before
    fun setUp() {
        viewModelTest = AddCommunityViewModel(addCommunityInteractorMock)
    }

    @Test
    fun `GIVEN loading state, WHEN sendCommunity called, THEN update live data for loading`() {
        viewModelTest.getLoadingLiveDataValue().observeForever(observerMock)

        every { addCommunityInteractorMock.addCommunityToServer(any()) } returns Result.Success(true)

        viewModelTest.sendCommunity(Community())

        verify {
            observerMock.onChanged(viewModelTest.getLoadingLiveDataValue().value)
        }
    }

    @Test
    fun `GIVEN success return, WHEN sendCommunity called, THEN update live data for success return`() {
        viewModelTest.getSuccessLiveDataValue().observeForever(observerMock)

        every { addCommunityInteractorMock.addCommunityToServer(any()) } returns Result.Success(true)

        viewModelTest.sendCommunity(Community())

        verify {
            observerMock.onChanged(viewModelTest.getSuccessLiveDataValue().value)
        }
    }

    @Test
    fun `GIVEN error return, WHEN sendCommunity called, THEN update live data for error return`() {
        viewModelTest.getErrorLiveDataValue().observeForever(observerMock)

        every { addCommunityInteractorMock.addCommunityToServer(any()) } returns Result.Error(exceptionMock)

        viewModelTest.sendCommunity(Community())

        verify {
            observerMock.onChanged(viewModelTest.getErrorLiveDataValue().value)
        }
    }
}