package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
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
class AdminPasswordRecovery {
    private lateinit var scenario: ActivityScenario<AuthenticationActivity>
    private lateinit var adminRemoteDataSource: AdminDataSource
    private lateinit var testModule: Module

    private val emailMock = "testuser@gmail.com"

    private var mockException: Exception? = null
    private fun sendPasswordRecoverEmailStub(): () -> Unit? {
         throw mockException ?: return {}
    }

    @Before
    fun setup() {
        adminRemoteDataSource = TestAdminRemoteDataSource(
            sendPasswordRecoverEmailStub = ::sendPasswordRecoverEmailStub
        )
        testModule = module {
            single(override = true) {
                adminRemoteDataSource
            }
        }
        loadKoinModules(testModule)
        scenario = launchActivity()
    }

    //    Fluxo 1 - Envio de email de recuperação de senha com sucesso
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de sign in
    //    E: usuário clickar no botão de recuperação de senha
    //    E: usuário digitar email
    //    E: usuário clickar em botão salvar
    //    Então: deve exibir dialogo de sucesso

    @Test
    fun successfulSendPasswordEmail() {
        login {
            fillUserField(emailMock)
            clickNext()
            showLoginScreen()
            clickRecoveryPassword()
            showPasswordRecoveryScreen()
            fillUserField(emailMock)
            clickSaveButton()
        } verify {
            assertThat(correctEmailPasswordResetMessage()).isTrue()
        }
    }

    //    Fluxo 1 - Envio de email de recuperação de senha com falha
    //
    //    Dado: email digitado
    //    Quando: usuário clickar em botão 'Próxima'
    //    E: exibir tela de sign in
    //    E: usuário clickar no botão de recuperação de senha
    //    E: usuário digitar email
    //    E: usuário clickar em botão salvar
    //    Então: deve exibir mensagem de erro

    @Test
    fun failureSendPasswordEmail() {
        adminRemoteDataSource

        mockException = Exception("")

        login {
            fillUserField(emailMock)
            clickNext()
            showLoginScreen()
            clickRecoveryPassword()
            showPasswordRecoveryScreen()
            fillUserField(emailMock)
            clickSaveButton()
        } verify {
            assertThat(incorrectEmailPasswordResetMessage()).isTrue()
        }
    }
}
