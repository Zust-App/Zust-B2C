package `in`.opening.area.zustapp.profile.models

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.ProfileActionCallback
import `in`.opening.area.zustapp.profile.ProfileActionCallback.Companion.MY_ADDRESS
import `in`.opening.area.zustapp.profile.ProfileActionCallback.Companion.MY_ORDER
import `in`.opening.area.zustapp.profile.ProfileActionCallback.Companion.SUGGEST_PRODUCT

data class ProfileStaticItem(
    val text: String,
    val deepLink: String? = null, val iconRes: Int? = null,
    val type: ProfileItemViewType? = ProfileItemViewType.ITEM, val eventCode: Int? = -1,
)

enum class ProfileItemViewType {
    TITLE, ITEM, DIVIDER
}

fun getProfileStaticItems(): ArrayList<ProfileStaticItem> {
    val staticItems = ArrayList<ProfileStaticItem>()
    staticItems.add(ProfileStaticItem("General Information", deepLink = "", type = ProfileItemViewType.TITLE))
    staticItems.add(ProfileStaticItem("My Orders", iconRes = R.drawable.ic_outline_shopping_bag_24, deepLink = "order_history", eventCode = MY_ORDER))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Manage Address", iconRes = R.drawable.ic_outline_edit_location_alt_24, deepLink = "", eventCode = MY_ADDRESS))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Refer And Earn", iconRes = R.drawable.share_icon, eventCode = ProfileActionCallback.SHARE_APP))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Rate us on PlayStore", iconRes = R.drawable.ic_round_star_border_24, eventCode = ProfileActionCallback.RATE_US_PLAY_STORE))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Contact us", iconRes = R.drawable.ic_outline_contact_support_24, eventCode = ProfileActionCallback.HELPLINE))


    staticItems.add(ProfileStaticItem("Other Information", deepLink = "", iconRes = null, type = ProfileItemViewType.TITLE))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Suggest items", iconRes = R.drawable.ic_outline_feedback_24, eventCode = SUGGEST_PRODUCT))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Terms & Conditions", iconRes = R.drawable.page_icon, eventCode = ProfileActionCallback.TC))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Privacy Policy", iconRes = R.drawable.privacy_policy, eventCode = ProfileActionCallback.PRIVACY_POLICY))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("About us", iconRes = R.drawable.ic_outline_info_24, eventCode = ProfileActionCallback.ABOUT_US))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Open source", iconRes = R.drawable.open_source, eventCode = ProfileActionCallback.OPEN_SOURCE))
    staticItems.add(ProfileStaticItem("", type = ProfileItemViewType.DIVIDER))
    staticItems.add(ProfileStaticItem("Logout", iconRes = R.drawable.logout_icon, eventCode = ProfileActionCallback.LOGOUT))

    return staticItems
}

