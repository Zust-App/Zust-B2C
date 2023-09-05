package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.address.model.*
import `in`.opening.area.zustapp.coupon.model.AppliedCouponResponse
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.coupon.model.CouponModel
import `in`.opening.area.zustapp.data.AppMetaDataResponse
import `in`.opening.area.zustapp.fcm.FcmReqBodyModel
import `in`.opening.area.zustapp.home.models.HomePageApiResponse
import `in`.opening.area.zustapp.login.model.GetOtpResponseModel
import `in`.opening.area.zustapp.login.model.UpdateUserProfileResponse
import `in`.opening.area.zustapp.login.model.VerifyOtpResponseModel
import `in`.opening.area.zustapp.network.NetworkUtility.Companion.UPSELLING_PRODUCTS
import `in`.opening.area.zustapp.network.requestBody.AuthVerificationBody
import `in`.opening.area.zustapp.network.requestBody.UserProfileUpdateBody
import zustbase.orderDetail.models.OrderDetailModel
import zustbase.orderHistory.models.OrderRatingBody
import zustbase.orderHistory.models.RatingResponseModel
import zustbase.orderHistory.models.UserOrderHistoryModel
import `in`.opening.area.zustapp.orderSummary.model.LockOrderResponseModel
import `in`.opening.area.zustapp.orderSummary.model.LockOrderSummaryModel
import `in`.opening.area.zustapp.payment.models.*
import `in`.opening.area.zustapp.product.model.*
import `in`.opening.area.zustapp.productDetails.models.ProductDetailsModel
import `in`.opening.area.zustapp.profile.models.SuggestProductReqModel
import `in`.opening.area.zustapp.profile.models.UserProfileResponse
import `in`.opening.area.zustapp.rapidwallet.model.RWCreatePaymentModel
import `in`.opening.area.zustapp.rapidwallet.model.RWUserExistWalletResponse
import `in`.opening.area.zustapp.rapidwallet.model.RapidWalletPaymentResponse
import `in`.opening.area.zustapp.rapidwallet.model.RwSendOTPResponse
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.utility.DeviceInfo
import `in`.opening.area.zustapp.webpage.model.InvoiceResponseModel
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import non_veg.cart.models.CreateCartReqBody
import non_veg.cart.models.CreateFinalCartReqBody
import non_veg.cart.models.NonVegCartDetailsModel
import non_veg.cart.models.UpdateNonVegCartItemReqBody
import non_veg.common.model.GetOrderDetailReqBody
import non_veg.home.model.NonVegCategoryModel
import non_veg.home.model.NonVegHomePageBannerModel
import non_veg.home.model.NonVegHomePageCombinedResponse
import non_veg.home.model.NonVegMerchantResponseModel
import non_veg.listing.models.NonVegItemListModel
import non_veg.payment.models.NonVegCartPaymentReqBody
import non_veg.payment.models.NonVegCreateOrderResModel
import subscription.model.SubscriptionFormReqBody
import subscription.model.SubscriptionFormResponseModel
import zustbase.analysis.models.UserAnalysisResponseModel
import zustbase.basepage.models.ZustServicePageResponseReceiver
import zustbase.services.models.ZustAvailableServiceResult
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRequestManager @Inject constructor() {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    companion object {
        const val Authorization = "Authorization"
        const val NOT_COVERAGE_ERROR_CODE = 27
    }

    suspend inline fun makeLoginRequestForGetOtp(mobileNumber: String) =
        universalApiRequestManager {
            ktorHttpClient.get<GetOtpResponseModel>(NetworkUtility.END_POINT_REGISTER) {
                parameter("phoneNo", mobileNumber)
            }
        }

    suspend inline fun postAuthVerification(mobileNumber: String, otp: String) =
        universalApiRequestManager {
            val authVerificationBody = AuthVerificationBody(
                deviceId = DeviceInfo.getDeviceIdInfo(),
                otp = otp,
                phoneNo = mobileNumber
            )
            ktorHttpClient.post<VerifyOtpResponseModel>(NetworkUtility.END_POINT_AUTH_VERIFICATION) {
                contentType(ContentType.Application.Json)
                body = authVerificationBody
            }
        }

    suspend inline fun postUpdateUserProfile(
        userName: String,
        userEmail: String?,
        referralCode: String?,
    ) = universalApiRequestManager {
        val userProfileUpdateBody =
            UserProfileUpdateBody(name = userName, userEmail = userEmail, referCode = referralCode)
        val token = sharedPrefManager.getUserAuthToken()

        ktorHttpClient.put<UpdateUserProfileResponse>(NetworkUtility.END_POINT_UPDATE_PROFILE) {
            headers {
                this.append(Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            body = userProfileUpdateBody
        }
    }

    suspend fun sendFcmTokenToServer(fcmToken: String) {
        sharedPrefManager.saveFcmToken(fcmToken)
        val token = sharedPrefManager.getUserAuthToken()
        if (!token.isNullOrEmpty()) {
            val fcmReqBodyModel = FcmReqBodyModel(fcmToken = fcmToken)
            ktorHttpClient.put<String>(NetworkUtility.END_POINT_SED_FCM) {
                try {
                    headers {
                        this.append(Authorization, "Bearer $token")
                    }
                    body = fcmReqBodyModel
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getHomePageDataWithFlow(lat: Double, lng: Double, pinCode: String?) = flow {
        emit(getHomePageDataFromServer(lat, lng, pinCode))
    }

    private suspend fun getHomePageDataFromServer(lat: Double, lng: Double, pinCode: String?) =
        universalApiRequestManager {
            val token = sharedPrefManager.getUserAuthToken()
            val value = ktorHttpClient.get<HomePageApiResponse>(NetworkUtility.HOME_PAGE) {
                headers {
                    this.append(Authorization, "Bearer $token")
                }
                parameter("latitude", lat)
                parameter("longitude", lng)
                parameter("pincode", pinCode)
            }
            value
        }

    suspend fun getTrendingProductsWithFlow(lat: Double?, lng: Double?, pinCode: String?) = flow {
        emit(getTrendingProducts(lat, lng, pinCode))
    }

    private suspend fun getTrendingProducts(lat: Double?, lng: Double?, pinCode: String?) = universalApiRequestManager {
        val value = ktorHttpClient.get<ProductApiResponse>(NetworkUtility.TRENDING_PRODUCTS) {
            parameter("lat", lat ?: 0.0)
            parameter("lng", lng ?: 0.0)
            parameter("pincode", pinCode ?: "000000")
        }
        value
    }


    suspend fun getSubCategoryFromServer(categoryId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<SubCategoryApiResponse>(NetworkUtility.SUB_CATEGORY) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("categoryId", categoryId)
        }
    }

    suspend fun productListFromServer(categoryId: Int) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value =
                ktorHttpClient.get<ProductApiResponse>(NetworkUtility.PRODUCT_LIST_BY_CATEGORY) {
                    headers {
                        this.append(Authorization, "Bearer $authToken")
                    }
                    parameter("categoryId", categoryId)
                    parameter("merchantId", sharedPrefManager.getMerchantId())
                }
            emit(ResultWrapper.Success(value))
        } catch (e: Throwable) {
            print(e.message)
            emit(ResultWrapper.NetworkError)
        }
    }


    suspend fun createCartWithServer(orderRequestBody: CreateCartReqModel) =
        universalApiRequestManager {
            val authToken = sharedPrefManager.getUserAuthToken()
            ktorHttpClient.post<CreateCartResponseModel>(NetworkUtility.ORDERS_CART) {
                headers(fun HeadersBuilder.() {
                    this.append(Authorization, "Bearer $authToken")
                })
                orderRequestBody.addressId = sharedPrefManager.getUserAddress()?.id ?: -1
                body = orderRequestBody
            }
        }

    suspend fun getCouponsFromServer() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<CouponModel>(NetworkUtility.COUPONS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun productSearchApi(searchText: String) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value = ktorHttpClient.get<ProductApiResponse>(NetworkUtility.PRODUCT_SEARCH) {
                parameter("merchantId", sharedPrefManager.getMerchantId())
                parameter("searchText", searchText)
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
            }
            emit(ResultWrapper.Success(value))
        } catch (e: Throwable) {
            emit(ResultWrapper.NetworkError)
        }
    }

    suspend fun getPaymentMethods() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<PaymentMethodResponseModel>(NetworkUtility.PAYMENT_METHOD) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun invokePaymentToGetId(createPayment: CreatePaymentReqBodyModel) =
        universalApiRequestManager {
            val authToken = sharedPrefManager.getUserAuthToken()
            ktorHttpClient.post<CreatePaymentResponseModel>(NetworkUtility.CREATE_PAYMENT) {
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
                body = createPayment
            }
        }

    suspend fun verifyPaymentWithServer(paymentBody: Payment) = universalApiRequestManager {
        ktorHttpClient.post<String>(NetworkUtility.VERIFY_PAYMENT) {
            headers {
                this.append(Authorization, BuildConfig.PAYMENT_TOKEN)
            }
            body = paymentBody
        }
    }

    suspend fun getAvailableTimeSlots() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<AvailTimeSlotsResponse>(NetworkUtility.TIME_SLOT) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun validateCoupon(couponBody: ApplyCouponReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<AppliedCouponResponse>(NetworkUtility.APPLY_COUPON) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = couponBody
        }
    }

    suspend fun getUserBookings(pageNumber: Int): UserOrderHistoryModel {
        val authToken = sharedPrefManager.getUserAuthToken()
        return ktorHttpClient.get(NetworkUtility.USER_ORDERS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pageNumber", pageNumber)
            parameter("pageSize", 10)
        }
    }

    suspend fun getOrderDetails(orderId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<OrderDetailModel>(NetworkUtility.USER_ORDERS + "/$orderId") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun getAllAddress() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<GetUserAddressModel>(NetworkUtility.ADDRESS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun saveUserAddress(saveAddressPostModel: SaveAddressPostModel) =
        universalApiRequestManager {
            val authToken = sharedPrefManager.getUserAuthToken()
            ktorHttpClient.post<SaveAddressPostResponse>(NetworkUtility.ADDRESS) {
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
                body = saveAddressPostModel
            }
        }

    suspend fun deleteAddress(id: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.delete<String>(NetworkUtility.ADDRESS + "/$id") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun searchPlaces(input: String) = universalApiRequestManager {
        val url = NetworkUtility.PLACES_SEARCH_NAME
        ktorHttpClient.get<String>(url) {
            parameter("input", input)
        }
    }

    suspend fun checkIsServiceAvail(lat: Double?, lng: Double?, postalCode: String?) =
        universalApiRequestManager {
            ktorHttpClient.get<String>(NetworkUtility.VERIFY_DELIVERABLE_ADDRESS) {
                parameter("latitude", lat)
                parameter("longitude", lng)
                parameter("pincode", postalCode)
            }
        }

    suspend fun getUserProfileDetails(merchantId: Int, nonVegMerchantId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<UserProfileResponse>(NetworkUtility.USER_PROFILE_PAGE) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("merchantId", merchantId)
            parameter("nonVegMerchantId", nonVegMerchantId)
        }
    }

    suspend fun sendSuggestProductResponse(suggestProductReqModel: SuggestProductReqModel) =
        universalApiRequestManager {
            val authToken = sharedPrefManager.getUserAuthToken()
            ktorHttpClient.post<String>(NetworkUtility.SUGGEST_PRODUCT) {
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
                body = suggestProductReqModel
            }
        }

    suspend fun getLatestOrderWhichNotDelivered() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<OrderDetailModel>(NetworkUtility.ORDER_HISTORY) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun syncUserCartWithServerAndLock(lockOrderSummary: LockOrderSummaryModel) =
        universalApiRequestManager {
            val authToken = sharedPrefManager.getUserAuthToken()
            ktorHttpClient.post<LockOrderResponseModel>(NetworkUtility.USER_CART) {
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
                body = lockOrderSummary
            }
        }

    suspend fun getDirections(origin: LatLng, destination: LatLng): String {
        return ktorHttpClient.get("https://maps.googleapis.com/maps/api/directions/json") {
            parameter("origin", origin.latitude.toString() + "," + origin.longitude.toString())
            parameter(
                "destination",
                destination.latitude.toString() + "," + destination.longitude.toString()
            )
            parameter("waypoints", "")//waypoints=lat2,lng2
            parameter("key", "AIzaSyB0oGlRPsaQr0xC-GGk5jHXgCmr0cMsjvI")
        }
    }

    suspend fun getDirectionsWithWayPoints(
        origin: LatLng,
        destination: LatLng,
        between: LatLng,
    ): String {
        return ktorHttpClient.get("https://maps.googleapis.com/maps/api/directions/json") {
            parameter("origin", origin.latitude.toString() + "," + origin.longitude.toString())
            parameter(
                "destination",
                destination.latitude.toString() + "," + destination.longitude.toString()
            )
            parameter("waypoints", between.latitude.toString() + "," + between.longitude.toString())
            parameter("key", "AIzaSyB0oGlRPsaQr0xC-GGk5jHXgCmr0cMsjvI")
        }
    }

    suspend fun getAllCategory() = universalApiRequestManager {
        ktorHttpClient.get<AllCategoryResponse>(NetworkUtility.ALL_CATEGORY) {

        }
    }

    suspend fun checkPinCodeIsDeliverableOrNot(
        inputPinCode: String,
        lat: Double? = null,
        lng: Double? = null,
    ) = universalApiRequestManager {
        val deliverablePinCodeModel = DeliverablePinCodeModel(inputPinCode, lat, lng)
        ktorHttpClient.post<DeliverableAddressResponse>(NetworkUtility.CHECK_DELIVERABLE_ADDRESS) {
            body = deliverablePinCodeModel
        }
    }

    suspend fun getUpsellingProducts(params: String) = universalApiRequestManager {
        ktorHttpClient.get<ProductApiResponse>(UPSELLING_PRODUCTS) {
            parameter("items", params)
        }
    }

    suspend fun getMetaData() = universalApiRequestManager {
        ktorHttpClient.get<AppMetaDataResponse>(NetworkUtility.META_DATA) {

        }
    }

    suspend fun updateRating(orderRatingBody: OrderRatingBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.put<RatingResponseModel>(NetworkUtility.UPDATE_RATING) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = orderRatingBody
        }
    }

    suspend fun getProductDetails(productId: Long, merchantId: Long) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<ProductDetailsModel>(NetworkUtility.PRODUCT_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("merchantId", merchantId)
            parameter("productId", productId)
        }
    }

    suspend fun getInvoice(orderId: Int, intentSource: String) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<InvoiceResponseModel>(NetworkUtility.ORDER_INVOICE) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("orderId", orderId)
            parameter("source",intentSource)
        }
    }

    suspend fun checkRapidWalletAndBalance(rapidUserId: String) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<RWUserExistWalletResponse>(NetworkUtility.RAPID_WALLET_VERIFY_USER) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("rapidUserId", rapidUserId)
        }
    }

    suspend fun sendRapidWalletOTP(rapidUserId: String) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<RwSendOTPResponse>(NetworkUtility.RAPID_WALLET_SEND_OTP) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("phoneNo", rapidUserId)
        }
    }

    suspend fun createPaymentWithRapidWallet(
        rapidUserId: String,
        payableAmount: Double? = null,
        walletType: String,
        transactionId: String,
    ) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        val createRapidWalletPaymentModel =
            RWCreatePaymentModel(rapidUserId, payableAmount, walletType, transactionId)
        ktorHttpClient.post<RapidWalletPaymentResponse>(NetworkUtility.RAPID_DO_PAYMENT) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = createRapidWalletPaymentModel
        }
    }

    //non veg
    suspend fun getNonVegMerchantDetails(pinCode: String, lat: Double?, lng: Double?) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegMerchantResponseModel>(NetworkUtility.NON_VEG_MERCHANT_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pincode", pinCode)
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }

    suspend fun getNonVegHomePageBanner(bannerType: String) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegHomePageBannerModel>(NetworkUtility.NON_VEG_HOME_PAGE_BANNER) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("bannerType", bannerType)

        }
    }

    suspend fun getNonVegCategory() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegCategoryModel>(NetworkUtility.NON_VEG_CATEGORY) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun getNonVegProductByCategoryAndMerchant(categoryId: Int, merchantId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegItemListModel>(NetworkUtility.NON_VEG_PRODUCT_BY_CATEGORY_MERCHANT) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("categoryId", categoryId)
            parameter("merchantId", merchantId)
        }
    }

    suspend fun getNonVegProductByCategoryAndMerchantId(categoryId: Int, merchantId: Int) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value =
                ktorHttpClient.get<NonVegItemListModel>(NetworkUtility.NON_VEG_PRODUCT_BY_CATEGORY_MERCHANT) {
                    headers {
                        this.append(Authorization, "Bearer $authToken")
                    }
                    parameter("categoryId", categoryId)
                    parameter("merchantId", merchantId)
                }
            emit(ResultWrapper.Success(value))
        } catch (e: Throwable) {
            print(e.message)
            emit(ResultWrapper.NetworkError)
        }
    }

    suspend fun getNonVegCartDetails(cartId: Int, merchantId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegCartDetailsModel>(NetworkUtility.NON_VEG_CART_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("cartId", cartId)
            parameter("merchantId", merchantId)
        }
    }

    suspend fun createNonVegCart(createCartReqBody: CreateCartReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<NonVegCartDetailsModel>(NetworkUtility.NON_VEG_CREATE_CART) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = createCartReqBody
        }
    }

    suspend fun finalLockNonVegCart(createCartReqBody: CreateFinalCartReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<NonVegCartDetailsModel>(NetworkUtility.NON_VEG_CREATE_CART) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = createCartReqBody
        }
    }

    suspend fun updateUserNonVegCart(updateCartItemReqBody: UpdateNonVegCartItemReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<NonVegCartDetailsModel>(NetworkUtility.NON_VEG_UPDATE_CART) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = updateCartItemReqBody
        }
    }


    suspend fun createNonVegOrder(reqBody: NonVegCartPaymentReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<NonVegCreateOrderResModel>(NetworkUtility.NON_VEG_COD_CREATE_PAYMENT) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = reqBody
        }
    }

    suspend fun getOrderDetailsForNonVeg(orderId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<OrderDetailModel>(NetworkUtility.NON_VEG_ORDER_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = GetOrderDetailReqBody(orderId)
        }
    }


    suspend fun createNonVegRapidPayment(
        rapidUserId: String,
        payableAmount: Double? = null,
        walletType: String,
        transactionId: String,
    ) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        val createRapidWalletPaymentModel =
            RWCreatePaymentModel(rapidUserId, payableAmount, walletType, transactionId)
        ktorHttpClient.post<RapidWalletPaymentResponse>(NetworkUtility.NON_VEG_RAPID_PAYMENT) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = createRapidWalletPaymentModel
        }
    }

    suspend fun searchNonVegProduct(merchantId: Int, searchInput: String) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value =
                ktorHttpClient.get<NonVegItemListModel>(NetworkUtility.NON_VEG_SEARCH_ITEM) {
                    headers {
                        this.append(Authorization, "Bearer $authToken")
                    }
                    parameter("searchText", searchInput)
                    parameter("merchantId", merchantId)
                }
            emit(ResultWrapper.Success(value))
        } catch (e: Throwable) {
            print(e.message)
            emit(ResultWrapper.NetworkError)
        }
    }


    suspend fun getUserNonVegBookings(pageNumber: Int): UserOrderHistoryModel {
        val authToken = sharedPrefManager.getUserAuthToken()
        return ktorHttpClient.post(NetworkUtility.NON_VEG_USER_BOOKING_HISTORY) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pageNumber", pageNumber)
            parameter("pageSize", 10)
        }
    }

    suspend fun getNonVegHomePageData(pinCode: String, lat: Double?, lng: Double?) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegHomePageCombinedResponse>(NetworkUtility.NON_VEG_HOME_PAGE_DATA) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pincode", pinCode)
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }

    suspend fun getNonVegProductDetails(productId: Int, merchantId: Int, productPriceId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<NonVegItemListModel>(NetworkUtility.NON_VEG_PRODUCT_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("merchantId", merchantId)
            parameter("productId", productId)
            parameter("productPriceId", productPriceId)
        }
    }

    suspend fun getAllAvailableService(pinCode: String, lat: Double?, lng: Double?) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<ZustAvailableServiceResult>(NetworkUtility.GET_SERVICE_LIST) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pincode", pinCode)
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }

    suspend fun getServicePageData(pinCode: String, lat: Double?, lng: Double?) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<ZustServicePageResponseReceiver>(NetworkUtility.GET_SERVICE_PAGE_DATA) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pincode", pinCode)
            parameter("lat", lat)
            parameter("lng", lng)
        }
    }

    suspend fun getUserAnalysisData() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<UserAnalysisResponseModel>(NetworkUtility.GET_USER_ANALYSIS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun sendSubscriptionFormResponse(subscriptionFormReqBody: SubscriptionFormReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<SubscriptionFormResponseModel>(NetworkUtility.SEND_SUBSCRIPTION_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = subscriptionFormReqBody
        }
    }

    suspend fun getUserLatestLocalCartDetails(reqData: List<Int>, merchantId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<NonVegItemListModel>(NetworkUtility.GET_USER_LATEST_LOCAL_NV_CART_DETAILS) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("merchantId", merchantId)
            body = reqData
        }
    }

}