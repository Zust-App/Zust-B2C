package `in`.opening.area.zustapp.home.navigations

import `in`.opening.area.zustapp.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        HomeNavigationItem.Home,
        HomeNavigationItem.Order,
        HomeNavigationItem.Cart,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = false,
                onClick = {

                }
            )
        }
    }
}