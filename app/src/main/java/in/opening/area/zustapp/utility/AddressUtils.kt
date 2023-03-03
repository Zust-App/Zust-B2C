package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import org.json.JSONArray
import org.json.JSONObject

class AddressUtils {
    companion object {
         fun parseSearchResult(response: String): ArrayList<SearchPlacesDataModel> {
            val placesList = ArrayList<SearchPlacesDataModel>()
            val jsonObject = JSONObject(response)
            val predict: JSONArray = jsonObject.getJSONArray("predictions")
            for (i in 0 until predict.length()) {
                val structuredFormatting = predict.getJSONObject(i)
                placesList.add(SearchPlacesDataModel(structuredFormatting, structuredFormatting.getString("description"), structuredFormatting.getJSONObject("structured_formatting").getString("main_text")))
            }
            return placesList
        }
    }
}