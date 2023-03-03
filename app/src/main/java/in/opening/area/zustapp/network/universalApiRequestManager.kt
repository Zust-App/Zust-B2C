package `in`.opening.area.zustapp.network

import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

//generic function
suspend fun <T> universalApiRequestManager(apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(Dispatchers.IO) {
        try {
            System.out.println("name-->" + Thread.currentThread().name)
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    ResultWrapper.NetworkError
                }
                is ClientRequestException -> {
                    ResultWrapper.GenericError(throwable.response.status.value, null)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}

suspend fun <T> parseApiResponse(
    inputResponse: ResultWrapper<T>,
    successCallback: suspend (T) -> Unit,
    genericError: suspend (String) -> Unit
) {
    when (inputResponse) {
        is ResultWrapper.Success -> {
            successCallback.invoke(inputResponse.value)
        }
        else -> {
            genericError.invoke("Please try again")
        }
    }

}