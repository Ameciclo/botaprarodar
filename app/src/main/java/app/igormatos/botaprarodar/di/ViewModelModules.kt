package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.RecoveryPasswordViewModel
import app.igormatos.botaprarodar.presentation.login.registration.RegisterViewModel
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.StepFinalReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.ReturnBikeQuizViewModel
import app.igormatos.botaprarodar.presentation.splash.SplashViewModel
import app.igormatos.botaprarodar.presentation.user.UserViewModel
import app.igormatos.botaprarodar.presentation.user.userform.UserFormViewModel
import app.igormatos.botaprarodar.presentation.user.userquiz.UserQuizViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (communityUsers: ArrayList<User>, mapOptions: Map<String, List<String>>) ->
        UserFormViewModel(
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            stepper = get(),
            communityUsers = communityUsers,
            mapOptions = mapOptions
        )
    }

    viewModel { (communityBikesSerialNumbers: ArrayList<String>) ->
        BikeFormViewModel(
            bikeFormUseCase = get(),
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            communityBikesSerialNumbers = communityBikesSerialNumbers
        )
    }

    viewModel { PasswordRecoveryViewModel(get(named(EMAIL_VALIDATOR_NAME)), get()) }
    viewModel { EmailValidationViewModel(get(), get(named(EMAIL_VALIDATOR_NAME))) }
    viewModel { SignInViewModel(get(), get(named(PASSWORD_VALIDATOR_NAME))) }
    viewModel { StepFinalReturnBikeViewModel(get(), get(), get(), get()) }
    viewModel { StepOneReturnBikeViewModel(get(), get(), get()) }
    viewModel { SelectCommunityViewModel(get(), get(), get()) }
    viewModel { ReturnBikeQuizViewModel(get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { RecoveryPasswordViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { AddCommunityViewModel(get()) }
    viewModel { UserQuizViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { UserViewModel(get()) }
}