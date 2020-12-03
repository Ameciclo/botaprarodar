package app.igormatos.botaprarodar.domain.model

import app.igormatos.botaprarodar.data.network.RequestError

class RequestException(
    val requestError: RequestError
) : Exception()