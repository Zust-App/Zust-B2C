package `in`.opening.area.zustapp.home.models

import `in`.opening.area.zustapp.R


//class for BottomNavItem
sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){
    object Home : BottomNavItem("Home", R.drawable.ic_round_home_24,"home")
    object Vegetables: BottomNavItem("Veg",R.drawable.ic_round_home_24,"add_post")
    object Fruits: BottomNavItem("Fruits",R.drawable.ic_round_home_24,"notification")
}