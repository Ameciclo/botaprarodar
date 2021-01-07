package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
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

    @Test
    fun `When admin is registered, then isAdminRegistered should return true`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.isUserRegistered(
                    email
                )
            } returns true

            val result = adminRepository.isAdminRegistered(email)
            assertTrue(result)
        }



    @Test
    fun `When admin is NOT registered, then isAdminRegistered should return false`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.isUserRegistered(
                    email
                )
            } returns false

            val result = adminRepository.isAdminRegistered(email)
            assertFalse(result)
        }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `When isAdminRegistered is called, then should throw AdminNetwork exception`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.isUserRegistered(
                    email
                )
            } throws FirebaseNetworkException("")
            adminRepository.isAdminRegistered(email)
        }


    @Test
    fun `When reset password email successfully sent, then should return true`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.sendPasswordRecoverEmail(
                    email
                )
            } returns Unit

            val result = adminRepository.sendPasswordResetEmail(email)
            assertTrue(result)
        }



    @Test
    fun `When reset password email NOT sent, then should return false`(): Unit =
        runBlocking {
            coEvery {
                firebaseAdminDataSource.sendPasswordRecoverEmail(
                    email
                )
            } throws FirebaseNetworkException("")

            val result = adminRepository.sendPasswordResetEmail(email)
            assertFalse(result)
        }
}
