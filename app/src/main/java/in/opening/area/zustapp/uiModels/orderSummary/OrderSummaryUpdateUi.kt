package `in`.opening.area.zustapp.uiModels.orderSummary

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import com.google.errorprone.annotations.Keep

@Keep
internal sealed interface OrderSummaryUi {
    val isLoading: Boolean

    data class SummarySuccess(
        override val isLoading: Boolean,
        val data: OrderSummary = OrderSummary(),
    ) : OrderSummaryUi

    data class InitialUi(override val isLoading: Boolean) : OrderSummaryUi
}

@Keep
data class OrderSummary(
    val productsAlreadyInCart: List<ProductSingleItem> = arrayListOf(),
    val totalItemCount: Int = 0,
    val totalOriginalPrice: Double = 0.0,
    val totalCurrentPrice: Double = 0.0,
    val deliveryPartnerTip: Int = 0,
    val suggestedProducts: List<ProductSingleItem>? = arrayListOf(),
    ) {
    fun copy(deliveryPartnerTip: Int): OrderSummary {
        return OrderSummary(productsAlreadyInCart,totalItemCount, totalOriginalPrice, totalCurrentPrice,
            deliveryPartnerTip,suggestedProducts)
    }
    fun copy(suggestedProducts: List<ProductSingleItem>?): OrderSummary {
        return OrderSummary(productsAlreadyInCart,totalItemCount, totalOriginalPrice, totalCurrentPrice,
            deliveryPartnerTip,suggestedProducts)
    }
}