package `in`.opening.area.zustapp.compose

import androidx.compose.foundation.Image
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zustbase.services.models.ZustService


enum class HomeBottomNavTypes {
    Home, Orders, MoveToCartPage, Food
}

val navIconModifier = Modifier.size(22.dp)

@Composable
fun CustomBottomNavigation(availServiceList: List<ZustService>, navActionCallback: (HomeBottomNavTypes, Any?) -> Unit) {

    var homeBottomNav by rememberSaveable {
        mutableStateOf(HomeBottomNavTypes.Home.ordinal)
    }

    BottomNavigation(backgroundColor = colorResource(R.color.white), modifier = Modifier.fillMaxWidth()) {
        BottomNavigationItem(
            selected = homeBottomNav == HomeBottomNavTypes.Home.ordinal,
            onClick = {
                if (homeBottomNav != HomeBottomNavTypes.Home.ordinal) {
                    homeBottomNav = HomeBottomNavTypes.Home.ordinal
                    navActionCallback.invoke(HomeBottomNavTypes.Home, null)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home_nav_selected_icon),
                    modifier = navIconModifier,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(text = "Home",
                    style = ZustTypography.subtitle1,
                    color = if (homeBottomNav == HomeBottomNavTypes.Home.ordinal) {
                        colorResource(id = R.color.app_black)
                    } else {
                        colorResource(id = R.color.new_hint_color)
                    }, fontWeight = FontWeight.W500)
            }
        )

        BottomNavigationItem(
            selected = homeBottomNav == HomeBottomNavTypes.Orders.ordinal,
            onClick = {
                navActionCallback.invoke(HomeBottomNavTypes.Orders, null)
            },
            icon = {
                Icon(
                    modifier = navIconModifier,
                    painter = painterResource(id = R.drawable.shopping_bag_nav_icon),
                    contentDescription = "Orders"
                )
            },
            label = {
                Text(text = "Orders", style = ZustTypography.subtitle1,
                    color = if (homeBottomNav == HomeBottomNavTypes.Home.ordinal) {
                        colorResource(id = R.color.app_black)
                    } else {
                        colorResource(id = R.color.new_hint_color)
                    })
            }
        )

        BottomNavigationItem(
            selected = homeBottomNav == HomeBottomNavTypes.Food.ordinal,
            onClick = {
                navActionCallback.invoke(HomeBottomNavTypes.Food, null)
            },
            icon = {
                Image(
                    modifier = navIconModifier,
                    painter = painterResource(id = R.drawable.food_btm_logo),
                    contentDescription = "food",
                )
            },
            label = {
                Text(text = "Food", style = ZustTypography.subtitle1,
                    color = if (homeBottomNav == HomeBottomNavTypes.Food.ordinal) {
                        colorResource(id = R.color.app_black)
                    } else {
                        colorResource(id = R.color.app_black)
                    })
            }
        )
    }

}
