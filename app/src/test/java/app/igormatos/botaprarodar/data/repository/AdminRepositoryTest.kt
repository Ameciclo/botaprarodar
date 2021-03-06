package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.network.api.AdminApiService
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.admin.AdminRequest
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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

class AdminRepositoryTest {
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var adminRemoteDataSource: AdminRemoteDataSource

    private val adminUser = mockk<Admin>()
    private val adminApiService: AdminApiService = mockk()
    private val adminMapper: AdminMapper = mockk(relaxed = true)

    private val email = "admin@admin.com"
    private val password = "admin"

    @Before
    fun setUp() {
        init(this)
        adminRepository = AdminRepository(
            adminRemoteDataSource,
            adminApiService,
            adminMapper
        )

        coEvery { adminUser.id } returns "123456"
    }

    @Test
    fun `When createAdmin, then created admin email should have given email`(): Unit = runBlocking {
        coEvery { adminRemoteDataSource.createAdmin(email, password) } returns adminUser
        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.email == email)
    }

    @Test
    fun `When createAdmin, then created admin should have same Uid`(): Unit = runBlocking {
        coEvery { adminRemoteDataSource.createAdmin(email, password) } returns adminUser
        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.id == adminUser.id)
    }

    @Test
    fun `When createAdmin, then created admin should have given password`(): Unit = runBlocking {
        coEvery { adminRemoteDataSource.createAdmin(email, password) } returns adminUser

        val result = adminRepository.createAdmin(email, password)

        assertTrue(result.password == password)
    }

    @Test
    fun `When authenticateAdmin with given params, then result admin should have given id`(): Unit =
        runBlocking {
            coEvery {
                adminRemoteDataSource.authenticateAdmin(
                    email,
                    password
                )
            } returns adminUser

            val result = adminRepository.authenticateAdmin(email, password)
            assertEquals(adminUser.id, result.id)
        }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `When authenticateAdmin, then should throw AdminNetwork exception`(): Unit =
        runBlocking {
            coEvery {
                adminRemoteDataSource.authenticateAdmin(
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
                adminRemoteDataSource.createAdmin(
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
                adminRemoteDataSource.isAdminRegistered(
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
                adminRemoteDataSource.isAdminRegistered(
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
                adminRemoteDataSource.isAdminRegistered(
                    email
                )
            } throws FirebaseNetworkException("")
            adminRepository.isAdminRegistered(email)
        }


    @Test
    fun `When reset password email successfully sent, then should return true`(): Unit =
        runBlocking {
            coEvery {
                adminRemoteDataSource.sendPasswordRecoverEmail(
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
                adminRemoteDataSource.sendPasswordRecoverEmail(
                    email
                )
            } throws Exception("")

            val result = adminRepository.sendPasswordResetEmail(email)
            assertFalse(result)
        }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `When reset password without connection, then should return Admin Network Exception`(): Unit =
        runBlocking {
            coEvery {
                adminRemoteDataSource.sendPasswordRecoverEmail(
                    email
                )
            } throws FirebaseNetworkException("")

            adminRepository.sendPasswordResetEmail(email)
        }

    @Test(expected = UserAdminErrorException.AdminPasswordInvalid::class)
    fun `When admin credentials are incorrect, then authenticateAdmin should throw AdminNotFound`(): Unit =
        runBlocking {
            val expectedError = FirebaseAuthInvalidCredentialsException("", "")
            coEvery {
                adminRemoteDataSource.authenticateAdmin(
                    email,
                    password
                )
            } throws expectedError
            adminRepository.authenticateAdmin(email, password)
        }

    @Test
    fun `should return Admin object when adminApiService execute with success and user is admin`() {
        runBlocking {
            // arrange
            val response: Response<AdminRequest> = mockk()
            val id = "1";
            val email = "email@email.com"
            val adminRequest = AdminRequest(id, email)
            val expectedAdmin = Admin("", id, email)

            every {
                response.isSuccessful
            } returns true

            every {
                response.body()
            } returns adminRequest

            coEvery {
                adminApiService.getAdminById(id)
            } returns response

            coEvery {
                adminMapper.adminRequestToAdmin(adminRequest)
            } returns expectedAdmin

            // action
            val result: Admin? = adminRepository.getAdminById(id)

            // assert
            assertEquals(expectedAdmin, result)
        }
    }

    @Test
    fun `should return null when adminApiService execute with success and user is not admin`() {
        runBlocking {
            // arrange
            val response: Response<AdminRequest> = mockk()
            val id = "1";
            val email = "email@email.com"
            val adminRequest = AdminRequest(id, email)
            val expectedAdmin = Admin("", id, email)

            every {
                response.isSuccessful
            } returns true

            every {
                response.body()
            } returns adminRequest

            coEvery {
                adminApiService.getAdminById(id)
            } returns response

            coEvery {
                adminMapper.adminRequestToAdmin(adminRequest)
            } returns expectedAdmin

            // action
            val result: Admin? = adminRepository.getAdminById(id)

            // assert
            assertEquals(expectedAdmin, result)
        }
    }

    @Test(expected = UserAdminErrorException.AdminNetwork::class)
    fun `should throws UserAdminErrorException AdminNetwork exception when adminApiService throws UnknownHostException`() {
        runBlocking {
            // arrange
            val id = "1";

            coEvery {
                adminApiService.getAdminById(id)
            } throws UnknownHostException()

            // action
            adminRepository.getAdminById(id)
        }
    }
}
