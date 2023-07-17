package `in`.opening.area.zustapp.helpline

import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import `in`.opening.area.zustapp.databinding.HelplineBtmSheetBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelplineBtmSheet : BottomSheetDialogFragment() {
    private var binding: HelplineBtmSheetBinding? = null

    private val profileViewModel: ProfileViewModel? by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = HelplineBtmSheetBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComposeView()
    }

    private fun setUpComposeView() {
        binding?.composeView?.setContent {
            HelplineContainer(profileViewModel) {
                handleAction(it)
            }
        }
    }

    private fun handleAction(data: Any) {
        if (data is String) {
            if (data == "close") {
                dialog?.dismiss()
            }
        }
    }

    companion object {
        fun newInstance(): HelplineBtmSheet {
            val args = Bundle()
            val fragment = HelplineBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }
}