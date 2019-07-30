package app.igormatos.botaprarodar.network

interface SingleRequestListener<T> {

    fun onStart()

    fun onCompleted(result: T)

    fun onError(error: RequestError)
}

enum class RequestError {
    DEFAULT, PARSE
}