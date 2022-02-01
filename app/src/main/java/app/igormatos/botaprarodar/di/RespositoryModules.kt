package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.repository.*
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single { CommunityRepository(get(), get()) }
    single { FirebaseHelperRepository(get<FirebaseStorage>()) }
    single { AdminRepository(get(), get(), get()) }
    single { UserRepository(get()) }
    single { DevolutionBikeRepository(get()) }
    single { WithdrawBikeRepository(get()) }
    factory { TripDetailRepository(get()) }
    single { BikeRepository(get<BicycleApi>()) }
}