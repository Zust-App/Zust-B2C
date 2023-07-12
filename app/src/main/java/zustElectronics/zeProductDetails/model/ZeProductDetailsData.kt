package zustElectronics.zeProductDetails.model

data class ZeProductDetailsData(
    val description: String,
    val discount: Double,
    val id: Int,
    val images: String,
    val modelNumber: String,
    val name: String,
    val price: Double,
    val status: String,
    val stock: Int
)