package `in`.opening.area.zustapp.home.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = HomeNavigationItem.Home.route) {
        composable(HomeNavigationItem.Home.route) {

        }
    }
}