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
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@OptIn(ExperimentalCoroutinesApi::class)
val bprModule = module {

    single { SharedPreferencesModule(appContext = get()) }
    single<FirebaseAuthModule> { FirebaseAuthModuleImpl() }
    single<FirebaseHelperModule> { FirebaseHelperModuleImpl() }
    single<SnackbarModule> { SnackbarModuleImpl() }

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

    single { NoConnectionInterceptor(get()) }

    single {
        BikeRepository(get<BicycleApi>())
    }

    single {
        UserRepository(userApi = get())
    }

    single {
        ValidateUserWithdraw(get(), get())
    }

    single { BikeHolder() }

    single { UserHolder() }

    single { BikeDevolutionQuizBuilder() }

    single { DevolutionBikeRepository(bikeApi = get()) }

    single { WithdrawBikeRepository(bikeApi = get()) }

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

    single {
        ReturnBikeViewModel(stepper = get())
    }

    factory {
        GetAvailableBikes(get())
    }

    factory {
        SendBikeWithdraw(withdrawRepository = get(), userRepository = get())
    }

    single { RegisterUserStepper(StepConfigType.USER_FORM) }

    factory {
        TripDetailRepository(get())
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

fun providesReturnStepperAdapter() = ReturnStepper(StepConfigType.SELECT_BIKE)

fun providesWithdrawStepperAdapter() = WithdrawStepper(StepConfigType.SELECT_BIKE)