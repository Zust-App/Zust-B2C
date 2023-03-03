package `in`.opening.area.zustapp.product.model

import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductApiResponse(
    val `data`: ProductItemResponse? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String? = "",
    val statusCode: Int,
)

@Keep
data class ProductItemResponse(
    @SerializedName("bannerUrl")
    var bannerUrl: String? = null,
    @SerializedName("products")
    var productItems: List<ProductSingleItem>? = arrayListOf()
) {
    fun copy(productItems: List<ProductSingleItem>?): ProductItemResponse {
        return ProductItemResponse(bannerUrl, productItems?.map {
            it.copy(it.itemCountByUser)
        })
    }
}
