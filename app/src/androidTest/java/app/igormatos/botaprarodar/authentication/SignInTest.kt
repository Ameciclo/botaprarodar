package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import app.igormatos.botaprarodar.presentation.main.MainActivity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.stopKoin


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class SignInTest {

    private lateinit var scenario: ActivityScenario<AuthenticationActivity>

    @Before
    fun setup() {
        scenario = launchActivity()
    }

    //    Fluxo 1 - Login de usuário com sucesso
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de login
    //    E: usuário digitar a senha corretamente
    //    E: usuário clicar no botão de login
    //    Então: deve mostrar a tela principal da aplicação

    @Test
    fun successfulUserLogin() {
        val registeredEmail = "brunotmg@gmail.com"
        val password = "abcd1234"
        login {
            fillUserField(registeredEmail)
            clickNext()
            showLoginScreen()
            fillPasswordField(password)
            clickSignIn()
        } verify {
            assertThat(checkMainScreen()).isTrue()
        }
    }

    //    Fluxo 2 - Login de usuário com falha
//
//    Dado: email digitado
//    Quando: usuário clickar em botão 'Próxima'
//    E: exibir tela de login
//    E: usuário digitar a senha incorretamente
//    E: usuário clicar no botão de login
//    Então: deve exibir mensagem de senha incorreta
    @Test
    fun failureUserLogin() {
        val registeredEmail = "brunotmg@gmail.com"
        val password = "123456"
        login {
            fillUserField(registeredEmail)
            clickNext()
            showLoginScreen()
            fillPasswordField(password)
            clickSignIn()
        } verify {
            assertThat(incorrectPasswordMessage()).isTrue()
        }
    }

    @Test
    fun should4SignUpNewUser() {
        login {
            fillUserField("new-user@gmail.com")
            clickNext()
        } verify {
            checkMessage("Sign up")
            checkMessage("new-user@gmail.com")
            checkMessageOnHint("Password")
            checkMessage("SAVE")
        }
    }

    @Test
    fun should5DoLoginSuccessful() {
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
            sleep(4000)
        } verify {
            checkMessage("Bota pra Rodar")
            checkMessage("ADICIONAR COMUNIDADE")
        }
    }

}