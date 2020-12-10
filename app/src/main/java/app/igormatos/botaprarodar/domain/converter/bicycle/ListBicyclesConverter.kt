package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.Bicycle

class ListBicyclesConverter : Converter<Map<String, Bicycle>, List<Bicycle>> {

    override fun convert(toConvert: Map<String, Bicycle>): List<Bicycle> {
        return toConvert.values.toMutableList()
    }

}