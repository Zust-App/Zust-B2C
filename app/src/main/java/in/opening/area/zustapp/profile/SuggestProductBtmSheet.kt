package `in`.opening.area.zustapp.profile

import android.app.Dialog
import android.content.Context
import `in`.opening.area.zustapp.databinding.SuggestProductBtmSheetBinding
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuggestProductBtmSheet : BottomSheetDialogFragment() {
    private var binding: SuggestProductBtmSheetBinding? = null

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = SuggestProductBtmSheetBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.composeView?.setContent {
            SuggestProductContainer(profileViewModel) {
                dialog?.dismiss()
            }
        }
    }
   

    companion object {
        const val TAG = "suggest_product"
        fun newInstance(): SuggestProductBtmSheet {
            val args = Bundle()
            val fragment = SuggestProductBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }


}
