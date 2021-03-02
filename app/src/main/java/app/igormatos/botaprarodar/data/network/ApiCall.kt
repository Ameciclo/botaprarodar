package app.igormatos.botaprarodar.data.network

import com.brunotmgomes.ui.SimpleResult

suspend fun <T> safeApiCall(apiCall: suspend () -> T): SimpleResult<T> {
    return try {
        SimpleResult.Success(apiCall.invoke())
    } catch (exception: Exception) {
        SimpleResult.Error(exception)
    }
}