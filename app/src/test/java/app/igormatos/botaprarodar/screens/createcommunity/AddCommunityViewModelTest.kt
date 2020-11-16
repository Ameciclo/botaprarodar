package app.igormatos.botaprarodar.screens.createcommunity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule


class AddCommunityViewModelTest {

    val firebaseHelperModuleMock = mockk<FirebaseHelperModule>(relaxed = true)
    lateinit var viewModel: AddCommunityViewModel

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = AddCommunityViewModel(firebaseHelperModuleMock)
    }

}