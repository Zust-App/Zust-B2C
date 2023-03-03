package `in`.opening.area.zustapp.utility

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PagingMetadata(
    @SerializedName("page")
    val currentPage: Int,
    val totalPage: Int,
)