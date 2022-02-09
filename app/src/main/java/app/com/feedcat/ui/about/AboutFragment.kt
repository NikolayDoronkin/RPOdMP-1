package app.com.feedcat.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import app.com.feedcat.R
import app.com.feedcat.core.extension.getViewModelExt
import app.com.feedcat.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private lateinit var aboutViewModel: AboutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        aboutViewModel = getViewModelExt { AboutViewModel() }
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.infoView.text = HtmlCompat.fromHtml(getString(R.string.info_view), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.emailBtn.setOnClickListener { sendEmail() }

        return binding.root
    }

    private fun sendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.feedcat_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            startActivity(Intent.createChooser(intent, getString(R.string.choose_email_client)))
        } catch (ex: android.content.ActivityNotFoundException) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}