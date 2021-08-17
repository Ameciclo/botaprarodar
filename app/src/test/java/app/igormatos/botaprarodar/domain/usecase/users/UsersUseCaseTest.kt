package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.utils.buildMapStringUser
import app.igormatos.botaprarodar.utils.communityFixture
import app.igormatos.botaprarodar.utils.validUser
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class UsersUseCaseTest {

    private val repository = mockk<UserRepository>()
    private lateinit var usersUseCase: UsersUseCase

    @BeforeEach
    fun setUp() {
        usersUseCase = UsersUseCase(repository)
    }

    @Test
    fun `when getUsers() should return a list of users`() = runBlocking {
        val expectedSizeList = 5

        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Success(buildMapStringUser(expectedSizeList))

        val response = usersUseCase.getAvailableUsersByCommunityId(communityFixture.id) as SimpleResult.Success

        assertNotNull(response)
        assertEquals(response.data.size, expectedSizeList)
    }

    @Test
    fun `when getUsers() verify if has a correct item`() = runBlocking {
        val expectedSizeList = 5

        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Success(buildMapStringUser(expectedSizeList))

        val response = usersUseCase.getAvailableUsersByCommunityId("123")
        val actual = response as SimpleResult.Success<List<User>>

        assertEquals(validUser, actual.data[0])
    }

    @Test
    fun `when getUsers() should return an error`() = runBlocking {
        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Error(Exception())

        val response = usersUseCase.getAvailableUsersByCommunityId("123")

        assertNotNull(response)
        assertThat(response, instanceOf(SimpleResult.Error::class.java))
    }
}