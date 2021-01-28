package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
class AdminRegistration {

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
    //    E: exibir tela de cadastro
    //    E: usuário preencher campo usuário e senha corretamente
    //    E: usuário clicar no botão salvar
    //    Então: deve exibir dialogo de sucesso

    @Test
    fun successfulAdminRegistration() {
        coEvery {
            adminRemoteDataSource.createFirebaseUser(emailMock, passwordMock)
        } returns mockk(relaxed = true)

        login {
            fillUserField(emailMock)
            clickNext()
            showRegistrationScreen()
            fillUserField(emailMock)
            fillPasswordField(passwordMock)
            clickSaveButton()
        } verify {
            Truth.assertThat(successRegistrationDialog()).isTrue()
        }
    }

    //    Fluxo 2 - Criação de usuário administrador com falha
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de cadastro
    //    E: usuário preencher campo usuário e senha corretamente
    //    E: usuário clicar no botão salvar
    //    Então: deve exibir mensagem de falha de login

    @Test
    fun failureAdminRegistration() {
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