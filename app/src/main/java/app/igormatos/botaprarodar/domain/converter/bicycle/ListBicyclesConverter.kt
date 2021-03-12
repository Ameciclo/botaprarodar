package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.domain.converter.Converter
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Withdraws

class ListBicyclesConverter : Converter<Map<String, BikeRequest>, List<Bike>> {
    private val convertMapperInUsers = ConvertMapperInUsers()

    override fun convert(toConvert: Map<String, BikeRequest>): List<Bike> {
        val list1 = toConvert.values.toMutableList()
        val listToReturn = mutableListOf<Bike>()

        list1.forEachIndexed { index, bikeRequest ->
            bikeRequest.withdraws?.let { it ->
                val bike = Bike().apply {
                    name = bikeRequest.name
                    communityId = bikeRequest.communityId
                    serialNumber = bikeRequest.serialNumber
                    orderNumber = bikeRequest.orderNumber
                    createdDate = bikeRequest.createdDate
                    inUse = bikeRequest.inUse
                    photoPath = bikeRequest.photoPath
                    photoThumbnailPath = bikeRequest.photoThumbnailPath
                    id = bikeRequest.id
                    isAvailable = bikeRequest.isAvailable
                }
                val listUsers = convertMapperInUsers.convert(it)
                bike.withdraws = listUsers
                listToReturn.add(bike)
            }
        }
        return listToReturn
    }

}

class TesteConverter : Converter<List<BikeRequest>, List<Bike>> {
    private val convertMapperInUsers = ConvertMapperInUsers()

    override fun convert(toConvert: List<BikeRequest>): List<Bike> {
        val listToReturn = mutableListOf<Bike>()

        toConvert.forEachIndexed { index, bikeRequest ->
            val bike = Bike().apply {
                name = bikeRequest.name
                communityId = bikeRequest.communityId
                serialNumber = bikeRequest.serialNumber
                orderNumber = bikeRequest.orderNumber
                createdDate = bikeRequest.createdDate
                inUse = bikeRequest.inUse
                photoPath = bikeRequest.photoPath
                photoThumbnailPath = bikeRequest.photoThumbnailPath
                id = bikeRequest.id
                isAvailable = bikeRequest.isAvailable
            }

            bikeRequest.withdraws?.let { it ->
                val listUsers = convertMapperInUsers.convert(it)
                bike.withdraws = listUsers
            }

            listToReturn.add(bike)

        }
        return listToReturn
    }

}

class ConvertMapperInUsers : Converter<Map<String, Withdraws>, List<Withdraws>> {

    override fun convert(toConvert: Map<String, Withdraws>): List<Withdraws> {
        return toConvert.values.toMutableList()
    }
}