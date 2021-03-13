package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModuleImpl
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModuleImpl
import app.igormatos.botaprarodar.data.repository.*
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.domain.usecase.users.GetUsersByCommunity
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import app.igormatos.botaprarodar.presentation.main.users.UsersViewModel
import app.igormatos.botaprarodar.presentation.main.trips.TripsViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeViewModel
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeConfirmationViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.userForm.UserFormViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityNavigator
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
val bprModule = module {

    single { SharedPreferencesModule(appContext = get()) }
    single<FirebaseAuthModule> { FirebaseAuthModuleImpl() }
    single<FirebaseHelperModule> { FirebaseHelperModuleImpl() }
    single<SnackbarModule> { SnackbarModuleImpl() }

    single { WelcomeActivityNavigator() }
    viewModel<WelcomeActivityViewModel> {
        WelcomeActivityViewModelImpl(
            preferencesModule = get(),
            firebaseAuthModule = get(),
            firebaseHelperModule = get()
        )
    }

    single { buildRetrofit() }

    single<CommunityApiService> {
        get<Retrofit>().create(CommunityApiService::class.java)
    }
    single { CommunityMapper() }
    single {
        CommunityRepository(
            communityApiService = get(),
            communityMapper = get()
        )
    }
    single { AddCommunityUseCase(communityRepository = get()) }
    viewModel {
        AddCommunityViewModel(
            communityUseCase = get()
        )
    }

    viewModel {
        TripsViewModel(
            bikeActionUseCase = get()
        )
    }

    single<BicycleApi> {
        get<Retrofit>().create(BicycleApi::class.java)
    }

    single<UserApi> {
        get<Retrofit>().create(UserApi::class.java)
    }

    single {
        FirebaseStorage.getInstance()
    }

    single {
        FirebaseDatabase.getInstance()
    }

    single {
        FirebaseHelperRepository(get())
    }


    single {
        FirebaseAuth.getInstance()
    }

    single {
        providesAdminDataSource(get())
    }

    single {
        AdminRepository(get())
    }

    single {
        provideEmailValidator()
    }

    single {
        UserRequestConvert()
    }

    single {
        BikeActionUseCase()
    }

    viewModel {
        EmailValidationViewModel(get(), get())
    }

    viewModel {
        SignInViewModel(get(), PasswordValidator())
    }

    viewModel {
        RegistrationViewModel(get())
    }

    viewModel {
        PasswordRecoveryViewModel(get(), get())
    }

    //BikeForm

    single {
        BikeFormUseCase(
            bikeRepository = get(),
            firebaseHelperRepository = get()
        )
    }

    viewModel {
        BikeFormViewModel(
            bikeFormUseCase = get(),
            community = get<SharedPreferencesModule>().getJoinedCommunity()
        )
    }

    //Bikes Fragment

    single {
        BikeRepository(get(), get())
    }

    single {
        BikesUseCase(get())
    }

    viewModel {
        BikesViewModel(get())
    }

    //UserForm

    single {
        UserFormUseCase(
            userRepository = get(),
            firebaseHelperRepository = get(),
            userConverter = get()
        )
    }

    viewModel {
        UserFormViewModel(
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            userUseCase = get()
        )
    }

    //Users Fragment

    single {
        UserRepository(userApi = get(), firebaseDatabase = get())
    }

    factory {
        GetUsersByCommunity(get())
    }

    viewModel {
        UsersViewModel(get())
    }

    //Return Bikes

    single {
        StepOneReturnBikeUseCase(bikeRepository = get())
    }

    single {
        providesReturnStepperAdapter()
    }
    single {
        providesWithdrawStepperAdapter()
    }

    single { BikeHolder() }
    single { UserHolder() }

    single {
        StepOneReturnBikeViewModel(
            stepOneReturnBikeUseCase = get(),
            stepperAdapter = get(),
            bikeHolder = get()
        )
    }

    single {
        ReturnBikeViewModel(stepper = get())
    }

    viewModel {
        BikeWithdrawViewModel(get())
    }
    viewModel {
        BikeConfirmationViewModel(get(), get(), get(), get())
    }
    viewModel {
        SelectBikeViewModel(get(), get(), get())
    }
    viewModel {
        SelectUserViewModel(get(), get(), get())
    }

    factory {
        GetAvailableBikes(get())
    }

    factory {
        SendBikeWithdraw()
    }

}

fun provideEmailValidator(): Validator<String> {
    return EmailValidator()
}

private fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

fun providesAdminDataSource(firebaseAuth: FirebaseAuth): AdminDataSource {
    return AdminRemoteDataSource(firebaseAuth)
}

fun providesReturnStepperAdapter(): ReturnStepper {
    val steps =
        listOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.QUIZ,
            StepConfigType.CONFIRM_DEVOLUTION
        )
    return ReturnStepper(StepConfigType.SELECT_BIKE)
}

fun providesWithdrawStepperAdapter(): WithdrawStepper {
    val steps =
        listOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.SELECT_USER,
            StepConfigType.CONFIRM_DEVOLUTION
        )
    return WithdrawStepper(StepConfigType.SELECT_BIKE)
}

