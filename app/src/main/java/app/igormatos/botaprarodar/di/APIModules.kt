package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.network.AuthTokenInterceptor
import app.igormatos.botaprarodar.data.network.NoConnectionInterceptor
import app.igormatos.botaprarodar.data.network.api.*
import app.igormatos.botaprarodar.data.network.buildRetrofit
import org.koin.dsl.module
import retrofit2.Retrofit

val APIModule = module {
    single { buildRetrofit(get(), get()) }
    single<CommunityApiService> { get<Retrofit>().create(CommunityApiService::class.java) }
    single<BicycleApi> { get<Retrofit>().create(BicycleApi::class.java) }
    single<UserApi> { get<Retrofit>().create(UserApi::class.java) }
    single<NeighborhoodApi> { get<Retrofit>().create(NeighborhoodApi::class.java) }
    factory<AdminApiService> { get<Retrofit>().create(AdminApiService::class.java) }
    single { AuthTokenInterceptor(get()) }
    single { NoConnectionInterceptor(get()) }
}