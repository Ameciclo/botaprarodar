package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.GetNeighborhoodsUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.domain.usecase.users.GetUserByIdUseCase
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.presentation.login.LoginUseCase
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.PasswordRecoveryUseCase
import app.igormatos.botaprarodar.presentation.login.registration.RegisterUseCase
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityUseCase
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val useCasesModule = module {
    factory {
        LoginUseCase(
            get(),
            get(named(EMAIL_VALIDATOR_NAME)),
            get(named(PASSWORD_VALIDATOR_NAME))
        )
    }

    factory {
        RegisterUseCase(
            get(),
            get(named(EMAIL_VALIDATOR_NAME)),
            get(named(PASSWORD_VALIDATOR_NAME))
        )
    }

    factory {
        PasswordRecoveryUseCase(
            get(),
            get(named(EMAIL_VALIDATOR_NAME))
        )
    }
    single { StepFinalReturnBikeUseCase(get(), get()) }
    factory { SelectCommunityUseCase(get(), get()) }
    single { StepOneReturnBikeUseCase(get()) }
    factory { UserFormUseCase(get(), get()) }
    single { BikeFormUseCase(get(), get()) }
    factory { ResendEmailUseCase(get()) }
    single { AddCommunityUseCase(get()) }
    factory { TripDetailUseCase(get()) }
    single { BikeActionUseCase(get()) }
    factory { CommunityUseCase(get()) }
    factory { AdminUseCase(get()) }
    single { BikesUseCase(get()) }
    single { UsersUseCase(get()) }
    single { GetUserByIdUseCase(get()) }

    factory { GetNeighborhoodsUseCase(get()) }
}