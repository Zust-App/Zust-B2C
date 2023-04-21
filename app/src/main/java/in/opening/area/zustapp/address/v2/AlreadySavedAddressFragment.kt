package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.adapter.SavedAddressAdapter
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import `in`.opening.area.zustapp.databinding.FragmentSavedAddressBinding
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlreadySavedAddressFragment : Fragment() {
    private var binding: FragmentSavedAddressBinding? = null

    private val viewModel: AddressViewModel by viewModels()

    private val savedAddressAdapter: SavedAddressAdapter by lazy {
        SavedAddressAdapter {
            listener?.didTapOnSavedAddress(it)
        }
    }

    private var listener: AddressFragmentCommunicator? = null

    private val dividerDrawable: Drawable? by lazy {
        context?.let { ContextCompat.getDrawable(it, R.drawable.divider_1dp) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddressFragmentCommunicator) {
            listener = context
        }
        if (parentFragment != null) {
            listener = parentFragment as AddressFragmentCommunicator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedAddressBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpViews() {
        binding?.savedLocationRecyclerView?.apply {
            if (context != null && dividerDrawable != null) {
                val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(dividerDrawable!!)
                addItemDecoration(itemDecorator)
            }
            layoutManager = LinearLayoutManager(context)
            adapter = savedAddressAdapter
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAddressListUiState.collectLatest {
                    parseResponseForSavedAddress(it)
                }
            }
        }
        viewModel.getAllAddress()
    }

    private fun setUpClickListeners() {
        binding?.noSavedAddressFound?.setOnClickListener{
            listener?.showAddNewFragment()
        }
    }

    private fun parseResponseForSavedAddress(userSavedAddressUi: UserSavedAddressUi) {
        showHideProgressBar(userSavedAddressUi.isLoading)
        when (userSavedAddressUi) {
            is UserSavedAddressUi.UserAddressResponse -> {
                if (userSavedAddressUi.data?.addresses.isNullOrEmpty()) {
                    savedAddressAdapter.submitList(arrayListOf())
                    showNoSavedAddressTag(true)
                } else {
                    showNoSavedAddressTag(false)
                    savedAddressAdapter.submitList(userSavedAddressUi.data?.addresses ?: arrayListOf())
                }
            }
            is UserSavedAddressUi.ErrorState -> {
                AppUtility.showToast(context, userSavedAddressUi.message)
            }
            is UserSavedAddressUi.InitialUi -> {
                showHideProgressBar(userSavedAddressUi.isLoading)
            }
        }
    }

    private fun showHideProgressBar(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showNoSavedAddressTag(canShow: Boolean) {
        if (canShow) {
            binding?.apply {
                noSavedAddressFound.visibility = View.VISIBLE
            }
        } else {
            binding?.apply {
                noSavedAddressFound.visibility = View.GONE
            }
        }
    }

    companion object {
        fun newInstance(): AlreadySavedAddressFragment {
            val args = Bundle()
            val fragment = AlreadySavedAddressFragment()
            fragment.arguments = args
            return fragment
        }
    }
}