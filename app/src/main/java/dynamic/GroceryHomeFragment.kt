package dynamic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomBottomNavigation
import `in`.opening.area.zustapp.compose.CustomTopBar
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.HomeMainContainer
import `in`.opening.area.zustapp.viewmodels.HomeViewModel

@AndroidEntryPoint
class GroceryHomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
//                Scaffold(
//                    topBar = {
//                        CustomTopBar(Modifier) {
//                            handleActionIntent(it)
//                        }
//                    },
//                    backgroundColor = colorResource(id = R.color.screen_surface_color),
//                    content = { paddingValue ->
//                        HomeMainContainer(paddingValues = paddingValue, callback = {
//                            handleActionIntent(it)
//                        }) {
//                            handleActionIntent(ACTION.OPEN_LOCATION)
//                        }
//                    })
//                LaunchedEffect(key1 = Unit, block = {
//                    getLatestOrderWhichNotDeliver()
//                })
            }
        }
    }

    private fun getLatestOrderWhichNotDeliver() {
        homeViewModel.getLatestOrderNotDeliver()
    }

    companion object {
        fun newInstance() =
            GroceryHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}