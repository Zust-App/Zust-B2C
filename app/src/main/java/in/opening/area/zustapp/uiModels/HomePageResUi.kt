package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.home.models.HomeData
import `in`.opening.area.zustapp.product.model.ProductItemResponse
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface HomePageResUi {
    val isLoading: Boolean

    data class ErrorUi(override val isLoading: Boolean,
                       val errorMsg: String? = "",
                       val errors: List<UserCustomError> = arrayListOf(),val errorCode:Int?=null) : HomePageResUi

    data class HomeSuccess(val homePageGrids: HomeData?=null,
                           val trendingProducts: ProductItemResponse?=null,
                           override val isLoading: Boolean,
                           val currentTime: Long = System.currentTimeMillis()) : HomePageResUi

    data class InitialUi(override val isLoading: Boolean) : HomePageResUi


}