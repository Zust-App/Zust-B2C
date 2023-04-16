package `in`.opening.area.zustapp.productDetails.models

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.utility.UserCustomError
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ProductDetailsModel(
    val `data`: ProductDetailData? = null,
    val errors: List<UserCustomError>? = arrayListOf(),
    val message: String? = null,
    val statusCode: Int? = -1,
)

data class ProductDetailData(
    val brand: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val name: String? = null,
    val productId: String? = null,
    val variant: List<ProductPriceDetails>? = null,
    val subcategoryId: Int? = null,
    val thumbnail: String? = null,
)

data class ProductPriceDetails(
    val discountPercentage: Double? = null,
    val itemInStock: Double? = null,
    val price: Double? = null,
    val productPriceId: String? = null,
    val quantity: Double? = null,
    val unit: String? = null,
    val mrp: Double? = null,
)

fun ProductDetailData.convertToProductSingleItems(): ArrayList<ProductSingleItem>? {
    if (variant == null) {
        return null
    }

    val variants: ArrayList<ProductSingleItem> = arrayListOf()
    variant.forEach {
        val singleItems = ProductSingleItem(
            brand = brand!!,
            description = description!!,
            categoryId = categoryId!!,
            mrp = it.mrp!!,
            discountPercentage = it.discountPercentage!!,
            thumbnail = thumbnail!!,
            subcategoryId = subcategoryId!!,
            itemInStock = it.itemInStock!!,
            isOutOfStock = false,
            productName = name!!,
            price = it.price!!,
            quantityUnit = it.unit!!,
            quantity = it.quantity,
            productGroupId = productId!!,
            productPriceId = it.productPriceId!!,
            wareHouseId = "1",
            itemCountByUser = 0
        )
        variants.add(singleItems)
    }
    return variants
}
