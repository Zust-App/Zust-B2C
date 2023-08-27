package dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.databinding.ZustEntryPointLayoutBinding
import zustbase.orderDetail.ui.FragmentTypes

@AndroidEntryPoint
class ZustGroceryEntryPointActivity : AppCompatActivity() {
    var binding: ZustEntryPointLayoutBinding? = null
    private var intentSource: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ZustEntryPointLayoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        loadDynamicFragments(FragmentTypes.GROCERY.name)
    }

    private fun loadDynamicFragments(name: String?) {
        if (name == null) {
            return
        }
        val existingFragment = supportFragmentManager.findFragmentByTag(name)

        if (existingFragment != null) {
            return
        }

        val fragment = when (name) {
            FragmentTypes.GROCERY.name -> GroceryHomeFragment.newInstance()
            FragmentTypes.NON_VEG.name -> NonVegHomeFragment.newInstance()
            else -> null
        }

        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.zust_entry_page_frame_layout, fragment, name).commit()
        }
    }


}