package app.igormatos.botaprarodar.screens.createcommunity

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import com.google.firebase.FirebaseApp
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

@RunWith(MockitoJUnitRunner::class)
class AddCommunityViewModelTest {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<Community>

    @Mock
    lateinit var context: Context

    lateinit var viewModelTest: AddCommunityViewModel

    @Before
    fun setUo() {
        MockitoAnnotations.initMocks(this)

        viewModelTest.communityData.value = Community()
    }


    @Test
    fun getCommunityFromInputs_shouldChangeLiveDataWithCommunity_shouldBeObserved() {
        `when`(viewModelTest.getCommunityFromInputs()).then { true }
        assertNotNull(viewModelTest.communityData)
        assertTrue(viewModelTest.communityData.hasObservers())
    }
}