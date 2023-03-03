package `in`.opening.area.zustapp.address.model

import com.google.errorprone.annotations.Keep
import org.json.JSONObject

@Keep
data class SearchPlacesDataModel(val jsonObject: JSONObject? = null,
                                 val description: String? = null,
                                 val mainText: String? = null)