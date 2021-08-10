package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class GetUsersByCommunityTest {

    private val repository = mockk<UserRepository>()
    private lateinit var byCommunityGet: GetUsersByCommunity

    @BeforeEach
    fun setUp() {
        byCommunityGet = GetUsersByCommunity(repository)
    }

    @Test
    fun `when getUsers() should return a list of users`() = runBlocking {
        coEvery { repository.getUsers(any()) } returns userFlowSuccess

        val response = byCommunityGet.execute("123").toList()
        val expectedSizeList = 1

        assertNotNull(response)
        assertEquals(response.size, expectedSizeList)
    }

    @Test
    fun `when getUsers() verify if has a correct item`() = runBlocking {
        coEvery { repository.getUsers(any()) } returns userFlowSuccess

        val response = byCommunityGet.execute("123").first()
        val actual = response as SimpleResult.Success<List<User>>

        assertEquals(validUser, actual.data[0])
    }

    @Test
    fun `when getUsers() should return an error`() = runBlocking {
        coEvery { repository.getUsers(any()) } returns userFlowError

        val response = byCommunityGet.execute("123").toList()

        assertNotNull(response)
        assertThat(response[0], CoreMatchers.instanceOf(SimpleResult.Error::class.java))
    }
}