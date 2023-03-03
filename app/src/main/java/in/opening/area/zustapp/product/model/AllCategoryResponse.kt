package `in`.opening.area.zustapp.product.model

import androidx.annotation.Keep

@Keep
data class AllCategoryResponse(
    val data: List<SingleCategoryData>? = arrayListOf(),
) {
    fun copy(): List<SingleCategoryData>? {
        return data?.map {
            it.copy()
        }
    }
}

@Keep
data class SingleCategoryData(val id: Int, val name: String, val thumbnail: String) {
    fun copy(): SingleCategoryData {
        return SingleCategoryData(id, name, thumbnail)
    }
}
