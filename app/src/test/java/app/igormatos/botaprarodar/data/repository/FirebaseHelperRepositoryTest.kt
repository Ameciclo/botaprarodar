package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.network.api.AdminApiService
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.admin.AdminRequest
import app.igormatos.botaprarodar.utils.mapOfBikesRequest
import com.brunotmgomes.ui.SimpleResult
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

class FirebaseHelperRepositoryTest {
    private lateinit var firebaseHelperRepository: FirebaseHelperRepository

    @MockK
    private lateinit var firebaseStorage: FirebaseStorage

    @MockK
    private lateinit var storageReference: StorageReference


    @Before
    fun setUp() {
        init(this)
        firebaseHelperRepository = FirebaseHelperRepository(
            firebaseStorage
        )
    }

    @Test
    fun `When deleteImageResource then should return a SimpleResult Success`(): Unit = runBlocking {
        val path = "imagePath"
        coEvery { firebaseStorage.reference.child(path) } returns storageReference
       // coEvery { storageReference.delete() }  returns
        val response = firebaseHelperRepository.deleteImageResource(path)
        assertEquals(response,SimpleResult.Success(Unit))
    }

}
