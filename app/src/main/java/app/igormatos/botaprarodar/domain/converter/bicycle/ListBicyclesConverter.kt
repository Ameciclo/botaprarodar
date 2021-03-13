package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.Bike

class ListBicyclesConverter : Converter<Map<String, Bike>, List<Bike>> {

    override fun convert(toConvert: Map<String, Bike>): List<Bike> {
        toConvert.get("id")
        return toConvert.values.toMutableList()
    }

}