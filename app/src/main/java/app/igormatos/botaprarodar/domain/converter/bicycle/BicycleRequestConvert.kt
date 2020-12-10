package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.Bicycle

class BicycleRequestConvert : Converter<Bicycle, BicycleRequest> {

    override fun convert(toConvert: Bicycle): BicycleRequest {
        return buildBicycleRequest(toConvert)
    }

}