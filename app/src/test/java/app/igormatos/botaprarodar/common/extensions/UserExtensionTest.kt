package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.utils.buildListAvailableUsers
import app.igormatos.botaprarodar.utils.buildListUnavailableUsers
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class UserExtensionTest {

    @Test
    fun `given an user list with unavailable users, when call onlyAvailableUsers() should return an empty user list`() {
        val expectedSize = 5
        val listUnavailableUsers = buildListUnavailableUsers(expectedSize)
        val result = listUnavailableUsers.onlyAvailableUsers()

        assertEquals(0 , result.size)
    }

    @Test
    fun `given an user list with only available users, when call onlyAvailableUsers() should return the same user list`()  {
        val expectedSize = 5
        val listAvailableUsers = buildListAvailableUsers(expectedSize)
        val result = listAvailableUsers.onlyAvailableUsers()

        assertEquals(listAvailableUsers, result)
    }


}