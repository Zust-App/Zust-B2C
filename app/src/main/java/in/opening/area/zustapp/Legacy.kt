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
//
//   }

//
//@Composable
//fun TypewriterText(text: String, modifier: Modifier = Modifier, textSize: TextUnit = 18.sp) {
//    var index by remember { mutableStateOf(0) }
//    val textToShow = text.take(index + 1)
//
//    LaunchedEffect(Unit) {
//        while (index < text.length) {
//            delay(80)
//            index++
//        }
//    }
//    Text(text = textToShow, modifier = modifier, style = Typography_Montserrat.body1,
//        fontSize = textSize,
//        color = colorResource(id = R.color.new_material_primary))
//}
//
//@OptIn(ExperimentalAnimationApi::class)
//@SuppressLint("UnrememberedMutableState")
//@Composable
//fun AnimatedTextContent(textList: List<String>) {
//    var currentIndex by remember { mutableStateOf(0) }
//
//    val currentText by derivedStateOf { textList[currentIndex] }
//
//    AnimatedContent(targetState = currentText, transitionSpec = {
//        addAnimation().using(SizeTransform(clip = false))
//    }) { targetCount ->
//        Row() {
//            Icon(painter = painterResource(id = R.drawable.ic_outline_check_circle_outline_24),
//                contentDescription = "check", tint = colorResource(id = R.color.light_green))
//            Spacer(modifier = Modifier.width(6.dp))
//            Text(text = targetCount, style = Typography_Montserrat.body2,
//                textAlign = TextAlign.Center,
//                fontSize = 18.sp,
//                color = colorResource(id = R.color.app_black))
//        }
//    }
//    LaunchedEffect(currentIndex) {
//        delay(2000)
//        currentIndex = (currentIndex + 1) % textList.size
//    }
//}
//
//
//@Composable
//fun AnimatedTextContent1(textList: List<String>) {
//    var currentIndex by remember { mutableStateOf(0) }
//
//    LaunchedEffect(Unit) {
//        while (currentIndex < textList.size - 1) {
//            delay(2000)
//            currentIndex += 1
//        }
//    }
//
//    val displayedTexts = textList.subList(0, currentIndex + 1)
//
//    LazyColumn(modifier = Modifier.height(200.dp)) {
//        itemsIndexed(displayedTexts) { index, text ->
//            AnimatedVisibility(
//                visible = true,
//                enter = fadeIn() + slideInVertically(),
//                exit = fadeOut() + slideOutVertically()
//            ) {
//                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
//                    val (icon, name, divider) = createRefs()
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_outline_check_circle_outline_24),
//                        contentDescription = "Check icon",
//                        tint = colorResource(id = R.color.light_green),
//                        modifier = Modifier.constrainAs(icon) {
//                            top.linkTo(parent.top)
//                            start.linkTo(parent.start)
//                        }
//                    )
//                    Text(
//                        text = text,
//                        style = Typography_Montserrat.body2,
//                        modifier = Modifier.constrainAs(name) {
//                            start.linkTo(icon.end, dp_6)
//                            end.linkTo(parent.end)
//                            top.linkTo(icon.top)
//                            bottom.linkTo(icon.bottom)
//                            width = Dimension.fillToConstraints
//                        }
//                    )
//                    val infiniteTransition = rememberInfiniteTransition()
//
//                    val height by infiniteTransition.animateFloat(
//                        initialValue = 0f,
//                        targetValue = 20f,
//                        animationSpec = infiniteRepeatable(
//                            animation = tween(durationMillis = 500),
//                            repeatMode = RepeatMode.Reverse
//                        )
//                    )
//
//                    if (index < displayedTexts.size - 1) {
//                        Divider(
//                            modifier = Modifier
//                                .constrainAs(divider) {
//                                    top.linkTo(icon.bottom)
//                                    start.linkTo(icon.start)
//                                    end.linkTo(icon.end)
//                                }
//                                .height(height.dp)
//                                .width(0.5.dp),
//                            color = Color.Gray
//                        )
//                    }
//
//                }
//            }
//        }
//    }
//}

////    engine {
////        https {
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                val customTrustManager = CustomX509TrustManager()
////                val sslContext = SSLContext.getInstance("TLS")
////                sslContext.init(null, arrayOf<TrustManager>(customTrustManager), SecureRandom())
////                this.trustManager = customTrustManager
////            }
////        }
////    }

// override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = BottomSheetDialog(requireContext())
//        dialog.setOnShowListener {
//            val bottomSheetDialog = it as BottomSheetDialog
//            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            parentLayout?.background = context?.let { it1 -> ContextCompat.getDrawable(it1, android.R.color.transparent) }
//            parentLayout?.let { pLayout ->
//                val behaviour = BottomSheetBehavior.from(pLayout)
//                setupFullHeight(pLayout)
//                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
//                behaviour.isDraggable = false
//            }
//        }
//        return dialog
//    }