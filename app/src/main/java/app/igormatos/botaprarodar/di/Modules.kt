package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModuleImpl
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModuleImpl
import app.igormatos.botaprarodar.data.repository.*
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationViewModel
import app.igormatos.botaprarodar.domain.usecase.bicycle.BikeFormUseCase
import app.igormatos.botaprarodar.domain.usecase.bicycle.BicyclesListUseCase
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.domain.usecase.user.UserUseCase
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormViewModel
import app.igormatos.botaprarodar.presentation.userForm.UserFormViewModel
import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityNavigator
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModel
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        BikeFormViewModel(
            bikeFormUseCase = get(),
            community = get<SharedPreferencesModule>().getJoinedCommunity()
        )
    }

    single<BicycleApi> {
        get<Retrofit>().create(BicycleApi::class.java)
    }

    single<UserApi> {
        get<Retrofit>().create(UserApi::class.java)
    }

    single {
        BikeRepository(get<BicycleApi>())
    }

    single {
        FirebaseStorage.getInstance()
    }

    single {
        FirebaseHelperRepository(get<FirebaseStorage>())
    }

    single {
        BikeFormUseCase(
            bikeRepository = get<BikeRepository>(),
            firebaseHelperRepository = get<FirebaseHelperRepository>()
        )
    }

    single {
        BicyclesListUseCase(get<BikeRepository>())
    }

    single {
        FirebaseAuth.getInstance()
    }

    single {
        AdminRemoteDataSource(get())
    }

    single {
        AdminRepository(get())
    }

    single {
        provideEmailValidator()
    }

    single {
        UserRepository(userApi = get())
    }

    single {
        UserRequestConvert()
    }

    single { UserUseCase(userRepository = get(), firebaseHelperRepository = get(), userConverter = get()) }

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

    viewModel {
        UserFormViewModel(
            community = get<SharedPreferencesModule>().getJoinedCommunity(),
            userUseCase = get()
        )
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