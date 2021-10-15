package app.igormatos.botaprarodar.data.repository


import com.brunotmgomes.ui.SimpleResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
        val path = "imagePath.jpg"

        coEvery { firebaseStorage.getReferenceFromUrl(path) } returns storageReference

        coEvery { storageReference.delete() } returns mockk<Task<Void>>()

        val response = firebaseHelperRepository.deleteImageResource(path)

        assertEquals(response, SimpleResult.Success(Unit))
    }

}
