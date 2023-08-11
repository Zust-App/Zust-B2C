package dynamic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import `in`.opening.area.zustapp.home.ACTION
import non_veg.home.ui.CustomNonVegHomeTopBar
import non_veg.home.ui.ZustNvEntryMainUi
import zustbase.utility.handleActionIntent

class ZustLandingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                Scaffold(modifier = Modifier, content = { profilePaddingValues ->
                    ZustNvEntryMainUi(paddingValues = profilePaddingValues) {
                        (activity as? AppCompatActivity?)?.handleActionIntent(ACTION.SEARCH_PRODUCT)
                    }
                }, topBar = {
                    CustomNonVegHomeTopBar(modifier = Modifier) {
                        (activity as? AppCompatActivity?)?.handleActionIntent(it)
                    }
                })
            }
        }
    }
}