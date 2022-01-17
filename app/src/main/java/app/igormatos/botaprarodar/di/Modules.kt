package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val EMAIL_VALIDATOR_NAME = "email_validator"
const val PASSWORD_VALIDATOR_NAME = "password_validator"

@ExperimentalCoroutinesApi
val BPRModule = module {
    factory<Validator<String?>>(named(PASSWORD_VALIDATOR_NAME)) { PasswordValidator() }
    factory<Validator<String?>>(named(EMAIL_VALIDATOR_NAME)) { EmailValidator() }
    single { RegisterUserStepper(StepConfigType.USER_FORM) }
    single<SnackbarModule> { SnackbarModuleImpl() }
    single { providesWithdrawStepperAdapter() }
    single { SharedPreferencesModule(get()) }
    single { providesAdminDataSource(get()) }
    single { providesReturnStepperAdapter() }
    single { BikeDevolutionQuizBuilder() }
    single { CommunityMapper() }
    factory { AdminMapper() }
}

fun providesAdminDataSource(firebaseAuth: FirebaseAuth) = AdminRemoteDataSource(firebaseAuth)
fun providesWithdrawStepperAdapter() = WithdrawStepper(StepConfigType.SELECT_BIKE)
fun providesReturnStepperAdapter() = ReturnStepper(StepConfigType.SELECT_BIKE)
