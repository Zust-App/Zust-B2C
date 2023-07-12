package zustElectronics.network

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ktorHttpClient
import `in`.opening.area.zustapp.network.universalApiRequestManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import zustElectronics.zeLanding.models.ZeLandingPageProductModel
import zustElectronics.zeProductDetails.model.ZeProductDetailsModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZeApiReqManager @Inject constructor() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    companion object {
        const val Authorization = "Authorization"
    }

    suspend fun getZeProducts() = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<ZeLandingPageProductModel>(ZeApiMetaData.PRODUCT_LIST_URL) {
            if (!authToken.isNullOrEmpty()) {
                headers {
                    this.append(ApiRequestManager.Authorization, "Bearer $authToken")
                }
            }
        }
    }

    suspend fun getZeProductDetails(productId: Int) = universalApiRequestManager {
        val authToken = sharedPrefManager.getUserAuthToken()
        ktorHttpClient.get<ZeProductDetailsModel>(ZeApiMetaData.PRODUCT_BY_ID_URL) {
            if (!authToken.isNullOrEmpty()) {
                headers {
                    this.append(ApiRequestManager.Authorization, "Bearer $authToken")
                }
            }
           // parameter("productId",productId)
        }
    }


}