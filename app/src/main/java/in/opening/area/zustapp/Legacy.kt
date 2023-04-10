package `in`.opening.area.zustapp

//import `in`.opening.area.zustapp.ui.theme.montserrat
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.material.Text
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp

//Spacer(modifier = Modifier.height(8.dp))
//if (value.data.deliveryPartnerTip != 0) {
//    Row() {
//        Text(
//            fontWeight = FontWeight.W500,
//            fontFamily = montserrat, fontSize = 12.sp,
//            color = colorResource(id = R.color.black_4),
//            text = "Delivery Tip")
//        Spacer(modifier = Modifier.weight(1f))
//        Text(
//            fontWeight = FontWeight.W500,
//            color = colorResource(id = R.color.new_hint_color),
//            fontFamily = montserrat, fontSize = 12.sp,
//            text = rupees + value.data.deliveryPartnerTip)
//    }
//}

//private fun startOrderSummaryActivity(createCartData: CreateCartData) {
//        homeViewModel.createCartUiState.update { CreateCartResponseUi.InitialUi(false) }
//        val intent = Intent(this, OrderSummaryActivity::class.java)
//        val paymentActivityReqData = PaymentActivityReqData()
//        paymentActivityReqData.apply {
//            orderId = createCartData.orderId
//            itemPrice = createCartData.itemTotalPrice
//            deliveryFee = createCartData.deliveryFee
//            packagingFee = createCartData.packagingFee
//            couponDiscount = createCartData.couponMaxDiscountPrice
//            couponString = createCartData.couponCode
//            isFreeDelivery = createCartData.isFreeDelivery
//            expectedDelivery = createCartData.expectedDelivery
//        }
//        intent.putExtra(PaymentActivity.PAYMENT_MODEL_KEY, paymentActivityReqData)
//        startActivity(intent)
//    }