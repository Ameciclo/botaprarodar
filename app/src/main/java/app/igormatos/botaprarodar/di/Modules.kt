package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.network.AuthTokenInterceptor
import app.igormatos.botaprarodar.data.network.NoConnectionInterceptor
import app.igormatos.botaprarodar.data.network.api.AdminApiService
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.data.network.buildRetrofit
import app.igormatos.botaprarodar.data.network.firebase.*
import app.igormatos.botaprarodar.data.repository.*
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.*
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginUseCase
import app.igormatos.botaprarodar.presentation.login.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.PasswordRecoveryUseCase
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.RecoveryPasswordViewModel
import app.igormatos.botaprarodar.presentation.login.registration.RegisterUseCase
import app.igormatos.botaprarodar.presentation.login.registration.RegisterViewModel
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityUseCase
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import app.igormatos.botaprarodar.presentation.main.trips.TripsViewModel
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailUseCase
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailViewModel
import app.igormatos.botaprarodar.presentation.main.users.UsersViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.StepFinalReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.ReturnBikeQuizViewModel
import app.igormatos.botaprarodar.presentation.splash.SplashViewModel
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import app.igormatos.botaprarodar.presentation.user.UserViewModel
import app.igormatos.botaprarodar.presentation.user.userform.UserFormViewModel
import app.igormatos.botaprarodar.presentation.user.userquiz.UserQuizViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityNavigator
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

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

    viewModel {
        SplashViewModel(
            preferencesModule = get(),
            firebaseAuthModule = get(),
            firebaseHelperModule = get()
        )
    }

    viewModel {
        LoginViewModel(
            loginUseCase = get(),
            resendEmailUseCase = get()
        )
    }

    factory {
        LoginUseCase(
            emailValidator = get(named(EMAIL_VALIDATOR_NAME)),
            passwordValidator = get(named(PASSWORD_VALIDATOR_NAME)),
            adminRepository = get()
        )
    }

    factory {
        ResendEmailUseCase(
            adminRepository = get()
        )
    }

    viewModel {
        RecoveryPasswordViewModel(
            passwordRecoveryUseCase = get()
        )
    }

    factory {
        PasswordRecoveryUseCase(
            adminRepository = get(),
            emailValidator = get(named(EMAIL_VALIDATOR_NAME))
        )
    }

    viewModel {
        RegisterViewModel(
            registerUseCase = get()
        )
    }

    factory {
        RegisterUseCase(
            adminRepository = get(),
            emailValidator = get(named(EMAIL_VALIDATOR_NAME)),
            passwordValidator = get(named(PASSWORD_VALIDATOR_NAME))
        )
    }

    viewModel {
        SelectCommunityViewModel(
            firebaseAuthModule = get(),
            preferencesModule = get(),
            selectCommunityUseCase = get(),
        )
    }

    factory {
        SelectCommunityUseCase(
            adminUseCase = get(),
            communityUseCase = get()
        )
    }

    factory {
        CommunityUseCase(
            communityRepository = get()
        )
    }

    factory {
        AdminUseCase(
            adminRepository = get()
        )
    }

    factory<AdminApiService> {
        get<Retrofit>().create(AdminApiService::class.java)
    }

    factory { AdminMapper() }

    single { buildRetrofit(get(), get()) }

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
        FirebaseHelperRepository(get<FirebaseStorage>())
    }


    single {
        FirebaseAuth.getInstance()
    }

    single {
        providesAdminDataSource(get())
    }

    single {
        AdminRepository(
            adminRemoteDataSource = get(),
            adminApiService = get(),
            adminMapper = get()
        )
    }

    single {
        BikeActionUseCase(get())
    }

    single { NoConnectionInterceptor(get()) }

    viewModel {
        EmailValidationViewModel(
            adminRepository = get(),
            emailValidator = get(named(EMAIL_VALIDATOR_NAME))
        )
    }

    viewModel {
        SignInViewModel(
            adminRepository = get(),
            passwordValidator = get(named(PASSWORD_VALIDATOR_NAME))
        )
    }

    viewModel {
        PasswordRecoveryViewModel(
            emailValidator = get(named(EMAIL_VALIDATOR_NAME)),
            adminRepository = get()
        )
    }

    single {
        BikeFormUseCase(
            bikeRepository = get<BikeRepository>(),
            firebaseHelperRepository = get<FirebaseHelperRepository>()
        )
    }

    viewModel { (communityBikesSerialNumbers: ArrayList<String>) ->
        BikeFormViewModel(
            bikeFormUseCase = get(),
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            communityBikesSerialNumbers = communityBikesSerialNumbers
        )
    }

    single {
        BikeRepository(get<BicycleApi>())
    }

    single {
        BikesUseCase(get<BikeRepository>())
    }

    viewModel {
        BikesViewModel(get<BikesUseCase>())
    }

    factory {
        UserFormUseCase(
            userRepository = get(),
            firebaseHelperRepository = get(),
        )
    }

    viewModel { (communityUsers: ArrayList<User>, mapOptions: Map<String, List<String>>) ->
        UserFormViewModel(
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            stepper = get(),
            communityUsers = communityUsers,
            mapOptions = mapOptions
        )
    }

    single {
        UserRepository(userApi = get())
    }

    single {
        UsersUseCase(get<UserRepository>())
    }

    single {
        ValidateUserWithdraw(get(), get())
    }

    viewModel {
        UsersViewModel(get())
    }

    single { BikeHolder() }

    single { UserHolder() }

    single { BikeDevolutionQuizBuilder() }

    single { DevolutionBikeRepository(bikeApi = get()) }

    single { WithdrawBikeRepository(bikeApi = get()) }

    single {
        StepOneReturnBikeUseCase(bikeRepository = get())
    }

    single {
        providesReturnStepperAdapter()
    }
    single {
        providesWithdrawStepperAdapter()
    }

    single {
        StepFinalReturnBikeUseCase(
            devolutionRepository = get(),
            userRepository = get()
        )
    }

    viewModel {
        StepOneReturnBikeViewModel(
            stepOneReturnBikeUseCase = get(),
            stepperAdapter = get(),
            bikeHolder = get()
        )
    }

    viewModel {
        StepFinalReturnBikeViewModel(
            bikeHolder = get(),
            quizBuilder = get(),
            stepFinalUseCase = get(),
            devolutionStepper = get()
        )
    }

    viewModel {
        ReturnBikeQuizViewModel(stepperAdapter = get(), quizBuilder = get())
    }

    single {
        ReturnBikeViewModel(stepper = get())
    }

    viewModel {
        BikeWithdrawViewModel(get())
    }
    viewModel {
        SelectBikeViewModel(get(), get(), get())
    }
    viewModel {
        SelectUserViewModel(
            userHolder = get(),
            stepperAdapter = get(),
            usersUseCase = get(),
            validateUserWithdraw = get()
        )
    }
    viewModel {
        BikeConfirmationViewModel(get(), get(), get(), get())
    }

    viewModel {
        WithdrawViewModel(get(), get(), get(), get(), get(), get(), get())
    }

    factory {
        GetAvailableBikes(get())
    }

    factory {
        SendBikeWithdraw(withdrawRepository = get(), userRepository = get())
    }

    single { RegisterUserStepper(StepConfigType.USER_FORM) }

    viewModel {
        UserViewModel(get())
    }

    viewModel {
        UserQuizViewModel(
            userUseCase = get()
        )
    }

    factory {
        TripDetailRepository(get())
    }

    factory {
        TripDetailUseCase(get())
    }

    viewModel {
        TripDetailViewModel(get())
    }

    factory<Validator<String?>>(named(EMAIL_VALIDATOR_NAME)) {
        EmailValidator()
    }

    factory<Validator<String?>>(named(PASSWORD_VALIDATOR_NAME)) {
        PasswordValidator()
    }

    single { FirebaseSessionManager(firebaseAuth = get(), sharedPreferencesModule = get()) }

    single { AuthTokenInterceptor(firebaseSessionManager = get()) }
}

const val EMAIL_VALIDATOR_NAME = "email_validator"
const val PASSWORD_VALIDATOR_NAME = "password_validator"

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