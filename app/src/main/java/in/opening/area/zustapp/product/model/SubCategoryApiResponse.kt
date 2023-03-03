package `in`.opening.area.zustapp.product.model

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SubCategoryApiResponse(
    @SerializedName("data")
    val data: ArrayList<SingleSubCategory>? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = 500,
    @SerializedName("timestamp")
    val timestamp: String? = null
)


@Keep
data class SingleSubCategory(val id: Int,
                             val name: String? = null,
                             val thumbnail: String,
                             val defaultColorCode: Long = 0xffffffff, var isSelected: Boolean = false) {
    fun copy(isSelected: Boolean): SingleSubCategory {
        return SingleSubCategory(id, name, thumbnail, defaultColorCode, isSelected)
    }
}

@Keep
data class SubCategoryDataMode(
    val data: List<SingleSubCategory>? = null,
    val message: String? = null,
    val isLoading: Boolean = false
) {
    fun copy(data: List<SingleSubCategory>?): SubCategoryDataMode {
        return SubCategoryDataMode(data, message, isLoading)
    }
}