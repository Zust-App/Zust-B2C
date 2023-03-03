package `in`.opening.area.zustapp.extensions

import `in`.opening.area.zustapp.login.LoginNav
import `in`.opening.area.zustapp.utility.AppUtility.Companion.checkIfOnline
import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberGreenBoyzAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    GreenBoyzAppState(navController, context)
}

class GreenBoyzAppState(
    val navController: NavHostController,
    private val context: Context
) {
    var isOnline by mutableStateOf(checkIfOnline(context))
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline(context)
    }

    fun loginActivityNavigation(destinationKey: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(destinationKey) {
                if (destinationKey == LoginNav.MOVE_TO_PROFILE) {
                    popUpTo(0)
                }
            }
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
