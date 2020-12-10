package app.igormatos.botaprarodar.domain.converter

interface Converter<in T, out E> {

    fun convert(toConvert : T) : E

}