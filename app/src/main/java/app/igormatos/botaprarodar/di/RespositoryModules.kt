package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.repository.*
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { AdminRepository(get(), get(), get()) }
    single { CommunityRepository(get(), get()) }
    single { DevolutionBikeRepository(get()) }
    single { WithdrawBikeRepository(get()) }
    factory { TripDetailRepository(get()) }
    single { BikeRepository(get()) }
    single { UserRepository(get()) }
}