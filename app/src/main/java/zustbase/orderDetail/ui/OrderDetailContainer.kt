package zustbase.orderDetail.ui

const val PREFIX_ORDER_ID_GROCERY = "ORDZUST"
const val PREFIX_ORDER_ID_NON_VEG = "ORDZUSTNV"

const val INTENT_SOURCE = "source"
const val INTENT_SOURCE_NON_VEG = "non_veg"
const val INTENT_SOURCE_GROCERY = "grocery"
const val JUST_ORDERED = "just_ordered"
const val ORDER_ID = "order_id"

enum class FragmentTypes {
    GROCERY, NON_VEG
}