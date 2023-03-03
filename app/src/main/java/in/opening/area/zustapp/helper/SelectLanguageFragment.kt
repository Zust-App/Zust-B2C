package `in`.opening.area.zustapp.helper


import `in`.opening.area.zustapp.helper.model.LanguageDTO
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.databinding.FragmentSelectLangaugeBinding
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SelectLanguageFragment : BottomSheetDialogFragment() {
    private var binding: FragmentSelectLangaugeBinding? = null
    private var mLanguageChangeListener: LanguageChangeListener? = null
    private var mAdapter: SelectLanguageAdapter? = null
    private var mSelectedLanguagePosition = -1

    private var alreadySelectedLangCode: String? = LanguageManager.ENG

    internal interface LanguageChangeListener {
        fun languageChange(languageCode: String)
    }

    @Inject
    lateinit var sharedPrefManager: dagger.Lazy<SharedPrefManager>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LanguageChangeListener) {
            mLanguageChangeListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alreadySelectedLangCode = arguments?.getString(alreadySelectedLangKey)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectLangaugeBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.setCanceledOnTouchOutside(true)
    }

    private fun initViews() {
        val languageList: List<LanguageDTO> = getLanguageList()
        onNextClick(languageList)
        configureAdapter(languageList)
    }

    private fun onNextClick(languageList: List<LanguageDTO>) {
        binding?.btnNext?.setOnClickListener {
            if (mSelectedLanguagePosition >= 0 && binding?.btnNext?.isEnabled == true) {
                val languageCode = languageList[mSelectedLanguagePosition].languageCode
                LanguageManager.setLanguage(languageCode)
                sharedPrefManager.get().saveUserSelectedLanguage(languageCode)
                mLanguageChangeListener?.languageChange(languageCode)
                dismissAllowingStateLoss()
            } else {
                Toast.makeText(context, "Please select language to proceed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun configureAdapter(languageList: List<LanguageDTO>) {
        if (context != null) {
            configureSelectedPosition(languageList)
            val recyclerView = binding?.rvSelectLanguage
            recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mAdapter = SelectLanguageAdapter(mSelectedLanguagePosition, languageList, object : ItemClickListener {
                override fun onItemClick(position: Int, language: String?) {
                    mSelectedLanguagePosition = position
                    binding?.btnNext?.isEnabled = true
                    mAdapter?.notifyDataSetChanged()
                }
            }, context = requireContext())
            recyclerView?.adapter = mAdapter
        }
    }

    //configure already selected language
    private fun configureSelectedPosition(languageList: List<LanguageDTO>) {
        languageList.forEachIndexed { index, value ->
            if (value.languageCode == alreadySelectedLangCode) {
                mSelectedLanguagePosition = index
            }
        }
    }

    companion object {
        private const val DIALOG_HEIGHT_RATIO = 0.5
        private const val TAG = "LanguageSelection"
        private const val alreadySelectedLangKey: String = "already_selected_lang"
        fun showDialog(fragmentManager: FragmentManager,
                       isCancellable: Boolean, alreadySelectedLang: String? = "en") {
            val fragment = SelectLanguageFragment()
            val bundle = Bundle()
            bundle.putString(alreadySelectedLangKey, alreadySelectedLang)
            fragment.isCancelable = isCancellable
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(fragment, TAG)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    private fun getLanguageList(): List<LanguageDTO> {
        val lngList = ArrayList<LanguageDTO>()
        lngList.add(LanguageDTO(LanguageManager.ENG, "English (अंग्रेज़ी)"))
        lngList.add(LanguageDTO(LanguageManager.HINDI, "Hindi (हिंदी)"))
        return lngList
    }
}