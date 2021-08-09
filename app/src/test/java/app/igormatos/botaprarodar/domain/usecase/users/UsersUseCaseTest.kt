package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.utils.buildMapStringUser
import app.igormatos.botaprarodar.utils.communityFixture
import app.igormatos.botaprarodar.utils.userFake
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class UsersUseCaseTest {

    private val repository = mockk<UserRepository>()
    private lateinit var byCommunity: UsersUseCase

    @BeforeEach
    fun setUp() {
        byCommunity = UsersUseCase(repository)
    }

    @Test
    fun `when getUsers() should return a list of users`() = runBlocking {
        val expectedSizeList = 5

        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Success(buildMapStringUser(expectedSizeList))

        val response = byCommunity.getAvailableUsersByCommunityId(communityFixture.id) as SimpleResult.Success

        assertNotNull(response)
        assertEquals(response.data.size, expectedSizeList)
    }

    @Test
    fun `when getUsers() verify if has a correct item`() = runBlocking {
        val expectedSizeList = 5

        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Success(buildMapStringUser(expectedSizeList))

        val response = byCommunity.getAvailableUsersByCommunityId("123")
        val actual = response as SimpleResult.Success<List<User>>

        assertEquals(userFake, actual.data[0])
    }

    @Test
    fun `when getUsers() should return an error`() = runBlocking {
        coEvery { repository.getUsersByCommunityId(any()) } returns SimpleResult.Error(Exception())

        val response = byCommunity.getAvailableUsersByCommunityId("123")

        assertNotNull(response)
        assertThat(response, CoreMatchers.instanceOf(SimpleResult.Error::class.java))
    }
}