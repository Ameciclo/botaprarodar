package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.Bike

class BikeRequestConvert : Converter<Bike, BicycleRequest> {
    override fun convert(toConvert: Bike): BicycleRequest {
        return buildBicycleRequest(toConvert)
    }
}