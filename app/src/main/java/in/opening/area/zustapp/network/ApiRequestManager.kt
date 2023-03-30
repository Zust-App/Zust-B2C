package `in`.opening.area.zustapp.network

import `in`.opening.area.zustapp.address.model.*
import `in`.opening.area.zustapp.coupon.model.AppliedCouponResponse
import `in`.opening.area.zustapp.coupon.model.ApplyCouponReqBody
import `in`.opening.area.zustapp.payment.models.Payment
import `in`.opening.area.zustapp.coupon.model.CouponModel
import `in`.opening.area.zustapp.fcm.FcmReqBodyModel
import `in`.opening.area.zustapp.home.models.HomePageApiResponse
import `in`.opening.area.zustapp.login.model.UpdateUserProfileResponse
import `in`.opening.area.zustapp.login.model.GetOtpResponseModel
import `in`.opening.area.zustapp.login.model.VerifyOtpResponseModel
import `in`.opening.area.zustapp.network.NetworkUtility.Companion.UPSELLING_PRODUCTS
import `in`.opening.area.zustapp.network.requestBody.AuthVerificationBody
import `in`.opening.area.zustapp.network.requestBody.UserProfileUpdateBody
import `in`.opening.area.zustapp.orderDetail.models.OrderDetailModel
import `in`.opening.area.zustapp.orderHistory.models.OrderRatingBody
import `in`.opening.area.zustapp.orderHistory.models.RatingResponseModel
import `in`.opening.area.zustapp.orderHistory.models.UserOrderHistoryModel
import `in`.opening.area.zustapp.orderSummary.model.LockOrderResponseModel
import `in`.opening.area.zustapp.orderSummary.model.LockOrderSummaryModel
import `in`.opening.area.zustapp.payment.models.AvailTimeSlotsResponse
import `in`.opening.area.zustapp.payment.models.CreatePaymentReqBodyModel
import `in`.opening.area.zustapp.payment.models.CreatePaymentResponseModel
import `in`.opening.area.zustapp.payment.models.PaymentMethodResponseModel
import `in`.opening.area.zustapp.product.model.*
import `in`.opening.area.zustapp.profile.models.SuggestProductReqModel
import `in`.opening.area.zustapp.profile.models.UserProfileResponse
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRequestManager @Inject constructor() {
    companion object {
        const val Authorization = "Authorization"
    }

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    suspend inline fun makeLoginRequestForGetOtp(mobileNumber: String) = universalApiRequestManager {
        ktorHttpClient.get<GetOtpResponseModel>(NetworkUtility.END_POINT_REGISTER) {
            parameter("phoneNo", mobileNumber)
        }
    }

    suspend inline fun postAuthVerification(mobileNumber: String, otp: String) = universalApiRequestManager {
        val authVerificationBody = AuthVerificationBody(deviceId = "123", otp = otp, phoneNo = mobileNumber)
        ktorHttpClient.post<VerifyOtpResponseModel>(NetworkUtility.END_POINT_AUTH_VERIFICATION) {
            contentType(ContentType.Application.Json)
            body = authVerificationBody
        }
    }

    suspend inline fun postUpdateUserProfile(userName: String, userEmail: String?, referralCode: String?) = universalApiRequestManager {
        val userProfileUpdateBody = UserProfileUpdateBody(name = userName, userEmail = userEmail, referCode = referralCode)
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

    fun getHomePageDataWithFlow(lat: Double, lng: Double) = flow {
        emit(getHomePageDataFromServer(lat, lng))
    }

    private suspend fun getHomePageDataFromServer(lat: Double, lng: Double) = universalApiRequestManager {
        val token = sharedPrefManager.getUserAuthToken()
        val value = ktorHttpClient.get<HomePageApiResponse>(NetworkUtility.HOME_PAGE_V1) {
            headers {
                this.append(Authorization, "Bearer $token")
            }
        }
        value
    }

    suspend fun getTrendingProductsWithFlow() = flow {
        emit(getTrendingProducts())
    }

    private suspend fun getTrendingProducts() = universalApiRequestManager {
        val value = ktorHttpClient.get<ProductApiResponse>(NetworkUtility.TRENDING_PRODUCTS) {

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
            val value = ktorHttpClient.get<ProductApiResponse>(NetworkUtility.PRODUCT_LIST_BY_CATEGORY) {
                headers {
                    this.append(Authorization, "Bearer $authToken")
                }
                parameter("categoryId", categoryId)
                parameter("merchantId", 1)
            }
            emit(ResultWrapper.Success(value))
        } catch (e: Throwable) {
            emit(ResultWrapper.NetworkError)
        }
    }


    suspend fun createCartWithServer(orderRequestBody: CreateCartReqModel) = universalApiRequestManager {
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
                parameter("merchantId", 1)
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

    suspend fun invokePaymentToGetId(createPayment: CreatePaymentReqBodyModel) = universalApiRequestManager {
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
                this.append(Authorization, "Basic e7rc0cb7ffdbYlHQlvgUAw==")
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

    suspend fun saveUserAddress(saveAddressPostModel: SaveAddressPostModel) = universalApiRequestManager {
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
        val url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyB0oGlRPsaQr0xC-GGk5jHXgCmr0cMsjvI&components=country:in&input=$input"
        ktorHttpClient.get<String>(url)
    }

    suspend fun checkIsServiceAvail(lat: Double, lng: Double) = universalApiRequestManager {
        ktorHttpClient.get<String>(NetworkUtility.VERIFY_DELIVERABLE_ADDRESS) {
            parameter("latitude", lat)
            parameter("longitude", lng)
        }
    }

    suspend fun getUserProfileDetails() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<UserProfileResponse>(NetworkUtility.USER_PROFILE_PAGE) {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun sendSuggestProductResponse(suggestProductReqModel: SuggestProductReqModel) = universalApiRequestManager {
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

    suspend fun syncUserCartWithServerAndLock(lockOrderSummary: LockOrderSummaryModel) = universalApiRequestManager {
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
            parameter("destination", destination.latitude.toString() + "," + destination.longitude.toString())
            parameter("waypoints", "")//waypoints=lat2,lng2
            parameter("key", "AIzaSyB0oGlRPsaQr0xC-GGk5jHXgCmr0cMsjvI")
        }
    }

    suspend fun getDirectionsWithWayPoints(origin: LatLng, destination: LatLng, between: LatLng): String {
        return ktorHttpClient.get("https://maps.googleapis.com/maps/api/directions/json") {
            parameter("origin", origin.latitude.toString() + "," + origin.longitude.toString())
            parameter("destination", destination.latitude.toString() + "," + destination.longitude.toString())
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
        ktorHttpClient.get<String>(NetworkUtility.META_DATA) {

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


}