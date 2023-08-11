package zustbase.basepage.routing

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import zustbase.orderDetail.ui.FragmentTypes

@Composable
fun ZustComposeAppScreen(callback: (FragmentTypes) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Home",
    ) {
        composable(route = "Home") {
            callback.invoke(FragmentTypes.GROCERY)
        }
        composable(route = "Detail") {
            callback.invoke(FragmentTypes.NON_VEG)
        }
    }
}