package `in`.opening.area.zustapp.home.navigations

import `in`.opening.area.zustapp.R

sealed class HomeNavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : HomeNavigationItem("home", R.drawable.ic_round_home_24, "Home")
    object Order : HomeNavigationItem("order", R.drawable.ic_outline_shopping_bag_24, "Orders")
    object Cart : HomeNavigationItem("cart", R.drawable.ic_outline_shopping_cart_24, "Cart")
}