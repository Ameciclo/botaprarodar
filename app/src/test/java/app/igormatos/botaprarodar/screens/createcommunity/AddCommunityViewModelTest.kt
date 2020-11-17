package app.igormatos.botaprarodar.screens.createcommunity

import androidx.arch.core.executor.JunitTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.AndroidJUnitRunner
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener
import com.brunotmgomes.ui.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class AddCommunityViewModelTest {

    val firebaseHelperModuleMock = mockk<FirebaseHelperModule>()
    lateinit var viewModel: AddCommunityViewModel

    val name = "Teste"
    val description = "Teste"
    val address = "Teste"
    val orgName = "Teste"
    val orgEmail = "orgtest@orgtest.com"

    @Rule
    @JvmField
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = AddCommunityViewModel(firebaseHelperModuleMock)
    }

    @Test
    fun `GIVEN community data, WHEN createCommunity called, THEN update live data for community data`() {
        viewModel.createCommunity(name, description, address, orgName, orgEmail)

        assertNotNull(viewModel.getCommunityDataValue().getOrAwaitValue())
    }

    @Test
    fun `GIVEN loading state, WHEN sendCommunityToServer called, THEN update live data for loading`() {
        // GIVEN
        val expectedValue = true

        // WHEN
        viewModel.createCommunity(name, description, address, orgName, orgEmail)
        viewModel.sendCommunityToServer()

        // THEN
        assertEquals(expectedValue, viewModel.getLoadingValue().getOrAwaitValue())
    }
}