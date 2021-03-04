package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.Fixtures
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.repository.AdminDataSource
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
class AdminRegistration {

    private lateinit var scenario: ActivityScenario<AuthenticationActivity>
    private lateinit var adminRemoteDataSource: AdminDataSource
    private lateinit var testModule: Module
    private val emailMock = "testuser@gmail.com"
    private val passwordMock = "abcd1234"

    private var mockException: Exception? = null
    private fun createAdminStub(): Admin {
        throw mockException ?: return Fixtures.adminUser
    }

    private fun isAdminRegisteredStub(): Boolean {
        return false
    }

    @Before
    fun setup() {
        adminRemoteDataSource = TestAdminRemoteDataSource(
            createAdminStub = ::createAdminStub,
            isAdminRegisteredStub = ::isAdminRegisteredStub
        )

        testModule = module {
            single(override = true) {
                adminRemoteDataSource
            }
        }
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
        login {
            fillUserField(emailMock)
            clickNext()
            showRegistrationScreen()
            fillUserField(emailMock)
            fillPasswordField(passwordMock)
            clickSaveButton()
        } verify {
            assertThat(successRegistrationDialog()).isTrue()
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
        mockException = Exception("")

        login {
            fillUserField(emailMock)
            clickNext()
            showRegistrationScreen()
            fillUserField(emailMock)
            fillPasswordField(passwordMock)
            clickSaveButton()
        } verify {
            assertThat(incorrectRegistrationMessage()).isTrue()
        }
    }
}