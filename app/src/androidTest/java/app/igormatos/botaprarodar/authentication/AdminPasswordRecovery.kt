package app.igormatos.botaprarodar.authentication

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
class AdminPasswordRecovery {

    private lateinit var scenario: ActivityScenario<AuthenticationActivity>
    private val adminRemoteDataSource = mockk<AdminRemoteDataSource>()
    private val testModule = module {
        single(override = true) {
            adminRemoteDataSource
        }
    }

    private val emailMock = "testuser@gmail.com"

    @Before
    fun setup() {
        loadKoinModules(testModule)
        scenario = launchActivity()

        coEvery { adminRemoteDataSource.isUserRegistered(emailMock) } returns true
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
        coEvery {
            adminRemoteDataSource.sendPasswordRecoverEmail(emailMock)
        } returns Unit

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
        coEvery {
            adminRemoteDataSource.sendPasswordRecoverEmail(emailMock)
        } throws Exception("")

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