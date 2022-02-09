package app.com.feedcat.ui.game

import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.Button
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import app.com.feedcat.App
import app.com.feedcat.R
import app.com.feedcat.core.extension.getViewModelExt
import app.com.feedcat.core.extension.navigateExt
import app.com.feedcat.data.preferences.UserPreferences
import app.com.feedcat.databinding.FragmentGameBinding
import app.com.feedcat.ui.activity.MainActivity

private const val ANIMATE_BTN_DURATION = 1000L
private const val END_ANIMATE_BTN_DURATION = 10L
private const val TRANSLATION_BTN = 20f
private const val ANIMATE_CAT_DURATION = 1000L
private const val ANIMATE_FEED_CAT_DURATION = 200L
private const val SCALE_FEED_CAT = 1.5f
private const val SCALE_TAKE_CAT = 0.7f

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private var btnId = -1
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val user = UserPreferences.getInstance(requireContext()).getUser()
        gameViewModel = getViewModelExt { GameViewModel(App.instance.gameResultRepository, user) }
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        initObservers()
        initListeners()

        return binding.root
    }

    private fun initObservers() {
        gameViewModel.satiety.observe(viewLifecycleOwner) {
            binding.textSatiety.text = getString(R.string.text_satiety, it)
        }
        gameViewModel.animateNumber.observe(viewLifecycleOwner) {
            when (it) {
                0 -> animateBtn(binding.btnFeed1)
                1 -> animateBtn(binding.btnFeed2)
                2 -> animateBtn(binding.btnFeed3)
            }
        }
    }

    private fun initListeners() {
        binding.btnFeed1.setOnClickListener { feed(btnId == R.id.btn_feed1) }
        binding.btnFeed2.setOnClickListener { feed(btnId == R.id.btn_feed2) }
        binding.btnFeed3.setOnClickListener { feed(btnId == R.id.btn_feed3) }
        binding.btnGameOver.setOnClickListener {
            gameViewModel.save()
            navigateExt(R.id.nav_home)
        }
    }

    private fun feed(flag: Boolean) {
        binding.imageCat.animation = null
        if (flag) {
            animateFeedCat(SCALE_FEED_CAT)
            if (gameViewModel.feed()) {
                animateCat()
            }
        } else {
            animateFeedCat(SCALE_TAKE_CAT)
            gameViewModel.take()
        }
    }

    private fun animateBtn(button: Button) {
        button.animate().apply {
            btnId = button.id
            duration = ANIMATE_BTN_DURATION
            translationYBy(-TRANSLATION_BTN)
            if (isAdded) {
                button.setBackgroundColor(getColor(requireContext(), R.color.md_light_green_A700))
            }
        }.withEndAction {
            button.animate().apply {
                btnId = 0
                duration = END_ANIMATE_BTN_DURATION
                translationYBy(TRANSLATION_BTN)
                if (isAdded) {
                    button.setBackgroundColor(getColor(requireContext(), R.color.colorAccent))
                }
            }
        }
    }

    private fun animateCat() {
        binding.imageCat.animate().apply {
            RotateAnimation(
                0.0f,
                360.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            val rotate = AnimationUtils.loadAnimation(context, R.anim.anim)
            binding.imageCat.animation = rotate
            binding.imageCat.animation.duration = ANIMATE_CAT_DURATION
            binding.imageCat.startAnimation(rotate)
        }
    }

    private fun animateFeedCat(scale: Float) {
        binding.imageCat.animate().apply {
            duration = ANIMATE_FEED_CAT_DURATION
            scaleX(scale)
            scaleY(scale)
        }.withEndAction {
            binding.imageCat.animate().apply {
                duration = ANIMATE_FEED_CAT_DURATION
                scaleX(1.0f)
                scaleY(1.0f)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.home_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        super.onStop()
        gameViewModel.stopTimer()
    }

    override fun onResume() {
        super.onResume()
        gameViewModel.resumeTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}