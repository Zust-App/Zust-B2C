package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.viewmodels.HomeViewModel
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun FloatingCartButton(homeViewModel: HomeViewModel) {

    FloatingActionButton(onClick = {

    }) {
        Icon(painter = painterResource(id = R.drawable.tabler_paper_bag), contentDescription = "cart")
    }

}