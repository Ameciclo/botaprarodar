package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.utils.userRequest
import com.google.firebase.database.FirebaseDatabase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserFormRepositoryTest {

    private var mockApi = mockk<UserApi>()
    private var firebaseDatabase = mockk<FirebaseDatabase>()
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = UserRepository(userApi = mockApi, firebaseDatabase = firebaseDatabase)
    }

    @Test
    fun `when 'addNewUser' should add a user`() =
        runBlocking {
            coEvery {
                mockApi.addUser(any(), any())
            } returns AddDataResponse("User registered")

            val request = userRepository.addNewUser("100", userRequest)

            assertEquals("User registered", request)
        }

    @Test
    fun `when 'updateUser' should edit a user`() =
        runBlocking {
            coEvery {
                mockApi.updateUser(any(), any(), any())
            } returns AddDataResponse("User edited")

            val request = userRepository.updateUser("100", userRequest)

            assertEquals("User edited", request)
        }
}