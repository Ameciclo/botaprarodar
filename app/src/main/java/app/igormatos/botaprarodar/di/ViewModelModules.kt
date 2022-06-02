package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.RecoveryPasswordViewModel
import app.igormatos.botaprarodar.presentation.login.registration.RegisterViewModel
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityViewModel
import app.igormatos.botaprarodar.presentation.main.viewModel.HomeViewModel
import app.igormatos.botaprarodar.presentation.main.viewModel.TripDetailViewModel
import app.igormatos.botaprarodar.presentation.main.viewModel.TripsViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.StepFinalReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.ReturnBikeQuizViewModel
import app.igormatos.botaprarodar.presentation.splash.SplashViewModel
import app.igormatos.botaprarodar.presentation.user.UserViewModel
import app.igormatos.botaprarodar.presentation.user.userform.UserFormViewModel
import app.igormatos.botaprarodar.presentation.user.userquiz.UserQuizViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val viewModelModule = module {
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { RecoveryPasswordViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SelectCommunityViewModel(get(), get(), get()) }
    viewModel { AddCommunityViewModel(get()) }
    viewModel { TripsViewModel(get()) }
    viewModel { StepOneReturnBikeViewModel(get(), get(), get()) }
    viewModel { StepFinalReturnBikeViewModel(get(), get(), get(), get()) }
    viewModel { ReturnBikeQuizViewModel(get(), get()) }
    viewModel { WithdrawViewModel(get(), get(), get(), get(), get(), get(), get()) }
//    single { ReturnBikeViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { UserQuizViewModel(get()) }
    viewModel { TripDetailViewModel(get()) }

    single { ReturnBicycleViewModel(get(), get(), get(), get()) }
    
    viewModel { EmailValidationViewModel(get(), get(named(EMAIL_VALIDATOR_NAME))) }
    viewModel { SignInViewModel(get(), get(named(PASSWORD_VALIDATOR_NAME))) }
    viewModel { PasswordRecoveryViewModel(get(named(EMAIL_VALIDATOR_NAME)), get()) }

    viewModel { (communityBikesSerialNumbers: ArrayList<String>) ->
        BikeFormViewModel(
            get(),
            get<SharedPreferencesModule>().getJoinedCommunity(),
            communityBikesSerialNumbers
        )
    }
    viewModel { (communityUsers: ArrayList<User>, mapOptions: Map<String, List<String>>) ->
        UserFormViewModel(
            get<SharedPreferencesModule>().getJoinedCommunity(),
            get(),
            communityUsers,
            mapOptions
        )
    }
}