package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.common.enumType.UserMotivationType
import app.igormatos.botaprarodar.data.network.api.UserApi
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val userApi = mockk<UserApi>(relaxed = true)

    @BeforeEach
    fun setUp() {
        userRepository = spyk(UserRepository(userApi))
    }

    @ParameterizedTest
    @EnumSource(UserMotivationType::class)
    fun `WHEN call user motivations THEN should return  user motivations with motivation equals enum type elements`(
        userMotivationType: UserMotivationType
    ) {
        assertTrue(userRepository.getUserMotivations().containsValue(userMotivationType.value))
        assertTrue(userRepository.getUserMotivations().containsKey(userMotivationType.index))
    }
}
