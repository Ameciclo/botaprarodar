package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class AdminSignIn {
    private lateinit var scenario: ActivityScenario<WelcomeActivity>

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
            initAuthentication()
            fillUserField(registeredEmail)
            clickNext()
            showLoginScreen()
            fillPasswordField(password)
            clickSignIn()
        } verify {
            assertThat(checkCommunityScreen()).isTrue()
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
            initAuthentication()
            fillUserField(registeredEmail)
            clickNext()
            showLoginScreen()
            fillPasswordField(password)
            clickSignIn()
        } verify {
            assertThat(incorrectPasswordMessage()).isTrue()
        }
    }
}
