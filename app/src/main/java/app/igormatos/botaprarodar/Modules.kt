package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.local.SharedPreferencesModule
import app.igormatos.botaprarodar.network.FirebaseAuthModule
import app.igormatos.botaprarodar.network.FirebaseAuthModuleImpl
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityViewModelImpl
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.screens.login.LoginActivityNavigator
import app.igormatos.botaprarodar.screens.login.LoginActivityViewModel
import app.igormatos.botaprarodar.screens.login.LoginActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bprModule = module {

    single { SharedPreferencesModule(appContext = get()) }
    single<FirebaseAuthModule> { FirebaseAuthModuleImpl() }
    single { FirebaseHelperModule() }
    single<SnackbarModule> { SnackbarModuleImpl() }

    single { LoginActivityNavigator() }
    viewModel<LoginActivityViewModel> {
        LoginActivityViewModelImpl(
            preferencesModule = get(),
            firebaseAuthModule = get(),
            firebaseHelperModule = get()
        )
    }

    viewModel<AddCommunityViewModel> {
        AddCommunityViewModelImpl(
            firebaseHelperModule = get()
        )
    }
}