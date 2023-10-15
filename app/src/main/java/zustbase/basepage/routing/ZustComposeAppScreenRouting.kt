package zustbase.basepage.routing

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import `in`.opening.area.zustapp.compose.HomeBottomNavTypes
import wallet.ZustPayMainUi
import zustbase.ZustLandingViewModel
import zustbase.analysis.ui.ZustUserAnalysisMainUi
import zustbase.basepage.ui.ZustBasePageMainUi
import zustbase.utility.handleActionIntent
import zustbase.utility.handleBasicCallbacks

@Composable
fun AppCompatActivity.ZustComposeAppScreenRouting(navController: NavHostController, zustLandingViewModel: ZustLandingViewModel) {
    LaunchedEffect(key1 = Unit, block = {
        zustLandingViewModel.getUserAnalysisData()
    })
    NavHost(
        navController = navController,
        startDestination = HomeBottomNavTypes.Landing.name,
    ) {
        composable(route = HomeBottomNavTypes.Landing.name) {
            ZustBasePageMainUi(zustLandingViewModel, genericCallback = {
                handleActionIntent(it)
            }) {
                handleBasicCallbacks(it)
            }
        }
        composable(route = HomeBottomNavTypes.Analysis.name) {
            ZustUserAnalysisMainUi(zustLandingViewModel)
        }
        composable(route = HomeBottomNavTypes.ZustPay.name) {
            ZustPayMainUi(zustLandingViewModel)
        }
    }
}