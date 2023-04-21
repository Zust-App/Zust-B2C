package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import android.location.Address
import com.google.errorprone.annotations.Keep

@Keep
sealed interface SearchPlacesUi {
    val isLoading: Boolean
    val errorMessage: String

    data class ErrorUi(
        override val isLoading: Boolean,
        override val errorMessage: String,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : SearchPlacesUi

    data class SearchPlaceResult(
        override val isLoading: Boolean,
        override val errorMessage: String,
        val data: ArrayList<SearchPlacesDataModel> = arrayListOf(),
        val timeStamp: Long = System.currentTimeMillis(),
    ) : SearchPlacesUi

    data class InitialUi(
        override val errorMessage: String,
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : SearchPlacesUi
}

sealed interface LocationAddressUi {
    val isLoading: Boolean

    data class ErrorUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : LocationAddressUi

    data class InitialUi(
        override val isLoading: Boolean,
    ) : LocationAddressUi

    data class Success(
        override val isLoading: Boolean,
        val data: Address? = null,
        val timeStamp: Long = System.currentTimeMillis(),
    ) : LocationAddressUi

}