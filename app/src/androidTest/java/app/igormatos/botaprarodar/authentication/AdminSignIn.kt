package app.igormatos.botaprarodar.authentication

import android.app.Activity.RESULT_OK
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
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
    //    E: terminar carregamento
    //    Então: deve fechar a tela de authenticação com resultado OK

    @Test
    fun successfulAdminLogin() {
        val registeredEmail = "brunotmg@gmail.com"
        val password = "abcd1234"
        login {
            fillUserField(registeredEmail)
            clickNext()
            showLoginScreen()
            fillPasswordField(password)
            clickSignIn()
            waitLoadingView(false)
        } verify {
            assert(scenario.result.resultCode == RESULT_OK)
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
    fun failureAdminLogin() {
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
}
