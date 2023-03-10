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
import `in`.opening.area.zustapp.network.requestBody.AuthVerificationBody
import `in`.opening.area.zustapp.network.requestBody.UserProfileUpdateBody
import `in`.opening.area.zustapp.orderDetail.models.OrderDetailModel
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
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

const val BASE_URL = "https://gcapi.grinzy.in/"
const val PROD_BASE_URL = "https://zcapi.zustapp.com/"
const val PROD_PAYMENT_URL = "https://paymentapi.zustapp.com/api/v1"
const val END_POINT_REGISTER = "/auth/register-send-otp"
const val END_POINT_AUTH_VERIFICATION = "/auth/register-verify-otp"
const val END_POINT_UPDATE_PROFILE = "/users/update-profile"
const val END_POINT_SED_FCM = "/users/save-fcm-token"
const val COMPLETE_BASE_URL = BASE_URL + "greenboys-api/api/v1"
const val TRENDING_PRODUCTS = "$COMPLETE_BASE_URL/products/trend-product"
const val SUB_CATEGORY = "/products/sub-category"
const val PRODUCT_LIST_BY_CATEGORY = "/products/category-by-product"
const val ORDERS_CART = "/orders/cart"
const val COUPONS = "/coupons/user-coupons"
const val PRODUCT_SEARCH = "/products/search"
const val PAYMENT_METHOD = "/orders/payment-methods"
const val CREATE_PAYMENT = "/orders/create-payment"
const val VERIFY_PAYMENT = "/payment/verify"
const val TIME_SLOT = "/orders/time-slot"
const val APPLY_COUPON = "/coupons/apply-coupon"
const val UPSELLING_PRODUCTS = "/products/suggestions/upselling-products"

@Singleton
class ApiRequestManager @Inject constructor() {
    companion object {
        const val Authorization = "Authorization"
    }

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    suspend inline fun makeLoginRequestForGetOtp(mobileNumber: String) = universalApiRequestManager {
        Log.e("url", "$COMPLETE_BASE_URL$END_POINT_REGISTER")
        ktorHttpClient.get<GetOtpResponseModel>("$COMPLETE_BASE_URL$END_POINT_REGISTER") {
            parameter("phoneNo", mobileNumber)
        }
    }

    suspend inline fun postAuthVerification(mobileNumber: String, otp: String) = universalApiRequestManager {
        val authVerificationBody = AuthVerificationBody(deviceId = "123", otp = otp, phoneNo = mobileNumber)
        ktorHttpClient.post<VerifyOtpResponseModel>("$COMPLETE_BASE_URL$END_POINT_AUTH_VERIFICATION") {
            contentType(ContentType.Application.Json)
            body = authVerificationBody
        }
    }

    suspend inline fun postUpdateUserProfile(userName: String, userEmail: String?, referralCode: String?) = universalApiRequestManager {
        val userProfileUpdateBody = UserProfileUpdateBody(name = userName, userEmail = userEmail, referCode = referralCode)
        val token = sharedPrefManager.getUserAuthToken()

        ktorHttpClient.put<UpdateUserProfileResponse>("$COMPLETE_BASE_URL$END_POINT_UPDATE_PROFILE") {
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
            ktorHttpClient.put<String>("$COMPLETE_BASE_URL$END_POINT_SED_FCM") {
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
        val url = "$COMPLETE_BASE_URL/products/home-page1"
        val value = ktorHttpClient.get<HomePageApiResponse>(url) {
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
        val value = ktorHttpClient.get<ProductApiResponse>(TRENDING_PRODUCTS) {

        }
        value
    }


    suspend fun getSubCategoryFromServer(categoryId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<SubCategoryApiResponse>("$COMPLETE_BASE_URL$SUB_CATEGORY") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("categoryId", categoryId)
        }
    }

    suspend fun productListFromServer(categoryId: Int) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value = ktorHttpClient.get<ProductApiResponse>("$COMPLETE_BASE_URL$PRODUCT_LIST_BY_CATEGORY") {
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

    //TODO()
    suspend fun createCartWithServer(orderRequestBody: CreateCartReqModel) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<CreateCartResponseModel>("$COMPLETE_BASE_URL$ORDERS_CART") {
            headers(fun HeadersBuilder.() {
                this.append(Authorization, "Bearer $authToken")
            })
            orderRequestBody.addressId = 8
            body = orderRequestBody
        }
    }

    suspend fun getCouponsFromServer() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<CouponModel>("$COMPLETE_BASE_URL$COUPONS") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun productSearchApi(searchText: String) = flow {
        try {
            val authToken = sharedPrefManager.getUserAuthToken()
            val value = ktorHttpClient.get<ProductApiResponse>("$COMPLETE_BASE_URL$PRODUCT_SEARCH") {
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
        ktorHttpClient.get<PaymentMethodResponseModel>("$COMPLETE_BASE_URL$PAYMENT_METHOD") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun invokePaymentToGetId(createPayment: CreatePaymentReqBodyModel) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<CreatePaymentResponseModel>("$COMPLETE_BASE_URL$CREATE_PAYMENT") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = createPayment
        }
    }

    suspend fun verifyPaymentWithServer(paymentBody: Payment) = universalApiRequestManager {
        ktorHttpClient.post<String>("$PROD_PAYMENT_URL$VERIFY_PAYMENT") {
            headers {
                this.append(Authorization, "Basic e7rc0cb7ffdbYlHQlvgUAw==")
            }
            body = paymentBody
        }
    }

    suspend fun getAvailableTimeSlots() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<AvailTimeSlotsResponse>("$COMPLETE_BASE_URL$TIME_SLOT") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun validateCoupon(couponBody: ApplyCouponReqBody) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<AppliedCouponResponse>("$COMPLETE_BASE_URL$APPLY_COUPON") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = couponBody
        }
    }

    suspend fun getUserBookings(pageNumber: Int): UserOrderHistoryModel {
        val authToken = sharedPrefManager.getUserAuthToken()
        return ktorHttpClient.get("$COMPLETE_BASE_URL/orders") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            parameter("pageNumber", pageNumber)
            parameter("pageSize", 10)
        }
    }

    suspend fun getOrderDetails(orderId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<OrderDetailModel>("$COMPLETE_BASE_URL/orders/$orderId") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun getAllAddress() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<GetUserAddressModel>("$COMPLETE_BASE_URL/addresses") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun saveUserAddress(saveAddressPostModel: SaveAddressPostModel) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<SaveAddressPostResponse>("$COMPLETE_BASE_URL/addresses") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = saveAddressPostModel
        }
    }

    suspend fun deleteAddress(id: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.delete<String>("$COMPLETE_BASE_URL/addresses/$id") {
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
        ktorHttpClient.get<String>("$COMPLETE_BASE_URL/dashboard/warehouses/verify-address") {
            parameter("latitude", lat)
            parameter("longitude", lng)
        }
    }

    suspend fun getUserProfileDetails() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<UserProfileResponse>("$COMPLETE_BASE_URL/users/profile-page") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun sendSuggestProductResponse(suggestProductReqModel: SuggestProductReqModel) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<String>("$COMPLETE_BASE_URL/products/suggest-product") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
            body = suggestProductReqModel
        }
    }

    suspend fun getLatestOrderWhichNotDelivered() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<OrderDetailModel>("$COMPLETE_BASE_URL/orders/delivery") {
            headers {
                this.append(Authorization, "Bearer $authToken")
            }
        }
    }

    suspend fun syncUserCartWithServerAndLock(lockOrderSummary: LockOrderSummaryModel) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.post<LockOrderResponseModel>("$COMPLETE_BASE_URL/orders/cart") {
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
        ktorHttpClient.get<AllCategoryResponse>("$COMPLETE_BASE_URL/dashboard/categories/allcategory") {

        }
    }

    suspend fun checkPinCodeIsDeliverableOrNot(
        inputPinCode: String,
        lat: Double? = null,
        lng: Double? = null,
    ) = universalApiRequestManager {
        val deliverablePinCodeModel = DeliverablePinCodeModel(inputPinCode, lat, lng)
        ktorHttpClient.post<DeliverableAddressResponse>("$COMPLETE_BASE_URL/addresses/checkdeliverable") {
            body = deliverablePinCodeModel
        }
    }

    suspend fun getUpsellingProducts(params: String) = universalApiRequestManager {
        ktorHttpClient.get<ProductApiResponse>("$COMPLETE_BASE_URL$UPSELLING_PRODUCTS") {
            parameter("items", params)
        }
    }

    suspend fun getMetaData() = universalApiRequestManager {
        ktorHttpClient.get<String>("$COMPLETE_BASE_URL/greenboys-api/api/v1/metadata") {

        }
    }


}