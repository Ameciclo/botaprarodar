package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdminRepositoryTest {
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var firebaseAdminDataSource: FirebaseAdminDataSource

    private val firebaseUser = mockk<FirebaseUser>()

    @Before
    fun setUp(){
        init(this)
        adminRepository = AdminRepository(firebaseAdminDataSource)

        coEvery { firebaseUser.uid } returns "123456"
    }

    @Test
    fun `When saveAdmin with given params, then should be an Admin`(): Unit = runBlocking {
        val email = "admin@admin.com"
        val password = "admin"

        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns firebaseUser
        coEvery { firebaseAdminDataSource.getFirebaseUserUid(firebaseUser) } returns firebaseUser.uid
        val result = adminRepository.saveAdmin(email, password)

        assertTrue(result is Admin)
    }

    @Test
    fun `When saveAdmin, then saved admin email should have given email`(): Unit = runBlocking {
        val email = "admin@admin.com"
        val password = "admin"

        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns firebaseUser
        coEvery { firebaseAdminDataSource.getFirebaseUserUid(firebaseUser) } returns firebaseUser.uid
        val result = adminRepository.saveAdmin(email, password)

        assertTrue(result?.email == email)
    }

    @Test
    fun `When saveAdmin, then saved admin should have same Uid`(): Unit = runBlocking {
        val email = "admin@admin.com"
        val password = "admin"

        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns firebaseUser
        coEvery { firebaseAdminDataSource.getFirebaseUserUid(firebaseUser) } returns firebaseUser.uid
        val result = adminRepository.saveAdmin(email, password)

        assertTrue(result?.id == firebaseUser.uid)
    }

    @Test
    fun `When saveAdmin, then saved admin should have given password`(): Unit = runBlocking {
        val email = "admin@admin.com"
        val password = "admin"

        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns firebaseUser
        coEvery { firebaseAdminDataSource.getFirebaseUserUid(firebaseUser) } returns firebaseUser.uid

        val result = adminRepository.saveAdmin(email, password)

        assertTrue(result?.password == password)
    }
}
