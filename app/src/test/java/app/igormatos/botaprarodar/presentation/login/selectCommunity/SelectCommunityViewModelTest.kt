package app.igormatos.botaprarodar.presentation.login.selectCommunity

import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class SelectCommunityViewModelTest {

    private lateinit var viewModel: SelectCommunityViewModel
    private lateinit var firebaseAuthModule: FirebaseAuthModule
    private lateinit var preferencesModule: SharedPreferencesModule
    private lateinit var selectCommunityUseCase: SelectCommunityUseCase

    private val observerSelectCommunityStateMock =
        mockk<Observer<SelectCommunityState>>(relaxed = true)

    @BeforeEach
    fun setup() {
        firebaseAuthModule = mockk(relaxed = true)
        preferencesModule = mockk()
        selectCommunityUseCase = mockk()
        viewModel = SelectCommunityViewModel(
            firebaseAuthModule,
            preferencesModule,
            selectCommunityUseCase
        )
    }

    @Test
    fun `should change SelectCommunityState to Success when execute loadCommunitiesByAdmin with success`() {
        // arrange
        val communities = listOf<Community>()
        val expectedSelectCommunityState =
            SelectCommunityState.Success(UserInfoState.Admin(communities))

        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(
                any(),
                any()
            )
        } returns expectedSelectCommunityState

        viewModel.selectCommunityState.observeForever(observerSelectCommunityStateMock)

        // action
        viewModel.loadCommunities("any_uid", "any_email")

        // assert
        verifyOrder {
            observerSelectCommunityStateMock.onChanged(SelectCommunityState.Loading)
            observerSelectCommunityStateMock.onChanged(expectedSelectCommunityState)
        }
    }

    @Test
    fun `should change SelectCommunityState to NETWORK error when execute loadCommunitiesByAdmin without network connection`() {
        // arrange
        val expectedSelectCommunityState =
            SelectCommunityState.Error(BprErrorType.NETWORK)

        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(
                any(),
                any()
            )
        } returns expectedSelectCommunityState

        viewModel.selectCommunityState.observeForever(observerSelectCommunityStateMock)

        // action
        viewModel.loadCommunities("any_uid", "any_email")

        // assert
        verifyOrder {
            observerSelectCommunityStateMock.onChanged(SelectCommunityState.Loading)
            observerSelectCommunityStateMock.onChanged(expectedSelectCommunityState)
        }
    }

    @Test
    fun `should change SelectCommunityState to UNKNOWN error when execute loadCommunitiesByAdmin with unmapped Exception`() {
        // arrange
        val expectedSelectCommunityState =
            SelectCommunityState.Error(BprErrorType.UNKNOWN)

        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(
                any(),
                any()
            )
        } returns expectedSelectCommunityState

        viewModel.selectCommunityState.observeForever(observerSelectCommunityStateMock)

        // action
        viewModel.loadCommunities("any_uid", "any_email")

        // assert
        verifyOrder {
            observerSelectCommunityStateMock.onChanged(SelectCommunityState.Loading)
            observerSelectCommunityStateMock.onChanged(expectedSelectCommunityState)
        }
    }
}