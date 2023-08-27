package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.viewmodels.GroceryHomeViewModel
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun FloatingCartButton(groceryHomeViewModel: GroceryHomeViewModel) {

    FloatingActionButton(onClick = {

    }) {
        Icon(painter = painterResource(id = R.drawable.tabler_paper_bag), contentDescription = "cart")
    }

}