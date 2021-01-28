package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class PasswordRecovery {

    private lateinit var scenario: ActivityScenario<AuthenticationActivity>
    private val  adminRemoteDataSource =  mockk<AdminRemoteDataSource>()
    private val testModule = module {
        single(override = true) {
            adminRemoteDataSource
        }
    }

    private val emailMock  = "testuser@gmail.com"
    private val passwordMock  = "abcd1234"

    @Before
    fun setup() {
        loadKoinModules(testModule)
        scenario = launchActivity()
    }

    //    Fluxo 1 - Criação de usuário administrador com sucesso
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de sign in
    //    E: usuário clickar no botão de recuperação de senha
    //    E: usuário digitar email
    //    Então: deve exibir dialogo de sucesso

    @Test
    fun successfulSendPasswordEmail() {
        coEvery {
            adminRemoteDataSource.createFirebaseUser(emailMock, passwordMock)
        } returns mockk(relaxed = true)

        login {
            fillUserField(emailMock)
            clickNext()
            showLoginScreen()
            fillUserField(emailMock)
            fillPasswordField(passwordMock)
            clickSaveButton()
        } verify {
            Truth.assertThat(successRegistrationDialog()).isTrue()
        }
    }

    //    Fluxo 1 - Criação de usuário administrador com sucesso
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de sign in
    //    E: usuário clickar no botão de recuperação de senha
    //    E: usuário digitar email
    //    Então: deve exibir dialogo de erro

    @Test
    fun failureSendPasswordEmail() {
        coEvery {
            adminRemoteDataSource.createFirebaseUser(emailMock, passwordMock)
        }  throws Exception("")

        login {
            fillUserField(emailMock)
            clickNext()
            showRegistrationScreen()
            fillUserField(emailMock)
            fillPasswordField(passwordMock)
            clickSaveButton()
        } verify {
            Truth.assertThat(incorrectRegistrationMessage()).isTrue()
        }
    }
}