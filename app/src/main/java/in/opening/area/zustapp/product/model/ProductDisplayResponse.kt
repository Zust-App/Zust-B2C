package `in`.opening.area.zustapp.product.model

import com.google.errorprone.annotations.Keep

@Keep
data class ProductDisplayResponse(val data: ProductItemResponse? = null,
                                  val message: String? = null,
                                  val isLoading: Boolean = false)