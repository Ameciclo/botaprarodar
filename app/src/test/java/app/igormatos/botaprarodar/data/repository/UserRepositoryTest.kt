package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.UserApi
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val userApi = mockk<UserApi>(relaxed = true)
    private val userMotivationsExpected = mapOf(
        0 to "Para economizar dinheiro, usar bicicleta é mais barato.",
        1 to "Porque é mais ecológico. A bicicleta não polui o ambiente.",
        2 to "Para economizar tempo. Usar a bicicleta como transporte é mais eficiente.",
        3 to "Para melhorar a saúde física e emocional.Porque começou a trabalhar com entregas .",
        4 to "Outro"
    )

    @BeforeEach
    fun setUp() {
        userRepository = spyk(UserRepository(userApi))
    }

    @Test
    fun `WHEN call getUserMotivations THEN should return motivations list`() {
        Assertions.assertEquals(userMotivationsExpected, userRepository.getUserMotivations())
    }
}
