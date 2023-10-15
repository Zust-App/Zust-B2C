package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import `in`.opening.area.zustapp.ui.theme.dp_8
import okhttp3.internal.wait


enum class HomeBottomNavTypes {
    Landing, Orders, MoveToCartPage, Food, Analysis, ZustPay
}

val navIconModifier = Modifier.size(22.dp)

@Composable
fun CustomBottomNavigation(navController: NavHostController, navActionCallback: (HomeBottomNavTypes, Any?) -> Unit) {
    var currentSelectedNav by remember {
        mutableStateOf(HomeBottomNavTypes.Landing.name)
    }

    NavigationBar(
        containerColor = colorResource(R.color.white),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = dp_8,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        navItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconResId),
                        modifier = navIconModifier,
                        contentDescription = screen.contentDescription
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        style = ZustTypography.bodySmall,
                        color = if (screen.type.name == currentSelectedNav) {
                            colorResource(id = R.color.app_black)
                        } else {
                            colorResource(id = R.color.new_hint_color)
                        },
                        fontWeight = FontWeight.W500
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.type.name } == true,
                onClick = {
                    if (screen.type.name == HomeBottomNavTypes.Orders.name) {
                        navActionCallback.invoke(HomeBottomNavTypes.Orders, null)
                    } else {
                        currentSelectedNav = screen.type.name
                        navController.navigate(screen.type.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}


val navItems = listOf(
    HomeBottomNavItem(
        HomeBottomNavTypes.Landing,
        R.drawable.outline_home_24,
        "Home"
    ),
    HomeBottomNavItem(
        HomeBottomNavTypes.ZustPay,
        R.drawable.baseline_account_balance_wallet_24,
        "ZustPay"
    ),
    HomeBottomNavItem(
        HomeBottomNavTypes.Analysis,
        R.drawable.outline_analytics_24,
        "Analysis"
    ),
    HomeBottomNavItem(
        HomeBottomNavTypes.Orders,
        R.drawable.shopping_bag_nav_icon,
        "Orders"
    )
)

data class HomeBottomNavItem(
    val type: HomeBottomNavTypes,
    val iconResId: Int,
    val label: String,
    val contentDescription: String = label,
)

