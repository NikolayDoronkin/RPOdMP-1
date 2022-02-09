package app.com.feedcat.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.com.feedcat.R
import app.com.feedcat.core.extension.getViewModelExt
import app.com.feedcat.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var helpViewModel: HelpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        helpViewModel = getViewModelExt { HelpViewModel() }
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        initPages()
        return binding.root
    }

    private fun initPages() {
        val pages: Array<String> = resources.getStringArray(R.array.help_pages_array)
        val adapter = HelpPagerAdapter(pages)
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}