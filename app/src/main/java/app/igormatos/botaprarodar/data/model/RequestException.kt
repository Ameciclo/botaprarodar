package app.igormatos.botaprarodar.data.model

import app.igormatos.botaprarodar.network.RequestError

class RequestException(
    val requestError: RequestError
) : Exception()