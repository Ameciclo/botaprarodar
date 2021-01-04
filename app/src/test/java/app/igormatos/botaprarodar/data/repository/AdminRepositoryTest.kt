package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdminRepositoryTest {
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var firebaseAdminDataSource: FirebaseAdminDataSource

    private val adminUser = mockk<FirebaseUser>()

    private val email = "admin@admin.com"
    private val password = "admin"

    @Before
    fun setUp() {
        init(this)
        adminRepository = AdminRepository(firebaseAdminDataSource)

        coEvery { adminUser.uid } returns "123456"
    }

    @Test
    fun `When createAdmin with given params, then created admin should have given id`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.createFirebaseUser(
                    email,
                    password
                )
            } returns adminUser
            coEvery { firebaseAdminDataSource.getFirebaseUserUid(adminUser) } returns adminUser.uid
            val result = adminRepository.createAdmin(email, password)

            assertEquals(adminUser.uid, result.id)
        }

    @Test
    fun `When createAdmin, then created admin email should have given email`(): Unit = runBlocking {
        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns adminUser
        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.email == email)
    }

    @Test
    fun `When createAdmin, then created admin should have same Uid`(): Unit = runBlocking {
        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns adminUser
        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.id == adminUser.uid)
    }

    @Test
    fun `When createAdmin, then created admin should have given password`(): Unit = runBlocking {
        coEvery { firebaseAdminDataSource.createFirebaseUser(email, password) } returns adminUser

        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.password == password)
    }

    @Test(expected = UserAdminErrorException.AdminNotCreated::class)
    fun `When admin is null, then createAdmin should throw AdminNotCreated exception`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.createFirebaseUser(
                    email,
                    password
                )
            } returns null

            adminRepository.createAdmin(email, password)
        }

    @Test(expected = UserAdminErrorException.AdminNotFound::class)
    fun `When admin is null, then authenticateAdmin should throw AdminNotFound exception`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.authenticateFirebaseUser(
                    email,
                    password
                )
            } returns null

            adminRepository.authenticateAdmin(email, password)
        }

    @Test
    fun `When authenticateAdmin with given params, then result admin should have given id`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.authenticateFirebaseUser(
                    email,
                    password
                )
            } returns adminUser

            val result = adminRepository.authenticateAdmin(email, password)
            assertEquals(adminUser.uid, result.id)
        }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `When authenticateAdmin, then should throw AdminNetwork exception`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.authenticateFirebaseUser(
                    email,
                    password
                )
            } throws FirebaseNetworkException("")
            adminRepository.authenticateAdmin(email, password)
        }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `When createAdmin, then should throw AdminNetwork exception`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.createFirebaseUser(
                    email,
                    password
                )
            } throws FirebaseNetworkException("")
            adminRepository.createAdmin(email, password)
        }

//    @Test(expected = UserAdminErrorException.Network::class)
//    fun `When email or password are incorrect, then createAdmin should throw InvalidParams exception`(): Unit =
//        runBlocking {
//            val email = "admin@admin.com"
//            val password = "admin"
//
//            coEvery {
//                firebaseAdminDataSource.createFirebaseUser(
//                    email,
//                    password
//                )
//            } returns firebaseUser
//            coEvery { firebaseAdminDataSource.getFirebaseUserUid(firebaseUser) } returns firebaseUser.uid
//
//            adminRepository.createAdmin(email, password)
//        }
}
