package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import org.koin.dsl.module

val presentationModule = module {
    single { BikeHolder() }
    single { UserHolder() }
}