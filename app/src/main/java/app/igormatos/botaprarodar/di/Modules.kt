package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.network.firebase.FirebaseSessionManager
import app.igormatos.botaprarodar.data.repository.AdminDataSource
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val bprModule = module {
    factory { AdminMapper() }
    single { CommunityMapper() }

    single {
        FirebaseStorage.getInstance()
    }

    single {
        FirebaseDatabase.getInstance()
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        providesAdminDataSource(get())
    }

    single {
        ValidateUserWithdraw(get(), get())
    }

    single { BikeDevolutionQuizBuilder() }

    single {
        providesReturnStepperAdapter()
    }
    single {
        providesWithdrawStepperAdapter()
    }

    factory {
        GetAvailableBikes(get())
    }

    factory {
        SendBikeWithdraw(withdrawRepository = get(), userRepository = get())
    }

    single { RegisterUserStepper(StepConfigType.USER_FORM) }

    factory<Validator<String?>>(named(EMAIL_VALIDATOR_NAME)) {
        EmailValidator()
    }

    factory<Validator<String?>>(named(PASSWORD_VALIDATOR_NAME)) {
        PasswordValidator()
    }

    single { FirebaseSessionManager(firebaseAuth = get(), sharedPreferencesModule = get()) }
}

const val EMAIL_VALIDATOR_NAME = "email_validator"
const val PASSWORD_VALIDATOR_NAME = "password_validator"

fun providesAdminDataSource(firebaseAuth: FirebaseAuth): AdminDataSource {
    return AdminRemoteDataSource(firebaseAuth)
}

fun providesReturnStepperAdapter() = ReturnStepper(StepConfigType.SELECT_BIKE)

fun providesWithdrawStepperAdapter() = WithdrawStepper(StepConfigType.SELECT_BIKE)
