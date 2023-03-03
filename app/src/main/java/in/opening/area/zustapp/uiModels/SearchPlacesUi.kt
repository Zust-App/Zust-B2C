package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import com.google.errorprone.annotations.Keep

@Keep
sealed interface SearchPlacesUi {
    val isLoading: Boolean
    val errorMessage: String

    data class ErrorUi(
        override val isLoading: Boolean,
        override val errorMessage: String,
        val timeStamp: Long = System.currentTimeMillis()
    ) : SearchPlacesUi

    data class SearchPlaceResult(
        override val isLoading: Boolean,
        override val errorMessage: String,
        val data: ArrayList<SearchPlacesDataModel> = arrayListOf(),
        val timeStamp: Long = System.currentTimeMillis(),
    ) : SearchPlacesUi

    data class InitialUi(override val errorMessage: String,
                         override val isLoading: Boolean,
                         val timeStamp: Long = System.currentTimeMillis()) : SearchPlacesUi
}
