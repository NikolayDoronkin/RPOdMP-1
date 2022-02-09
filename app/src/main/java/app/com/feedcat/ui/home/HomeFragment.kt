package app.com.feedcat.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import app.com.feedcat.App
import app.com.feedcat.R
import app.com.feedcat.core.extension.getViewModelExt
import app.com.feedcat.core.extension.navigateExt
import app.com.feedcat.data.entity.User
import app.com.feedcat.data.preferences.UserPreferences
import app.com.feedcat.databinding.FragmentHomeBinding
import app.com.feedcat.ui.activity.ShareTextInterface

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = getViewModelExt {
            HomeViewModel(App.instance.userRepository, App.instance.gameResultRepository)
        }
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        userPreferences = UserPreferences.getInstance(requireContext())

        initObservers()
        initListeners()

        val user = userPreferences.getUser()
        user?.let { homeViewModel.setUser(it) }

        return binding.root
    }

    private fun initObservers() {
        homeViewModel.existUser.observe(viewLifecycleOwner) {
            if (it) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = getString(R.string.registr_error_exists)
            } else {
                binding.textError.visibility = View.GONE
            }
        }
        homeViewModel.loginError.observe(viewLifecycleOwner) {
            if (it) {
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = getString(R.string.login_error)
            } else {
                binding.textError.visibility = View.GONE
            }
        }
        homeViewModel.user.observe(viewLifecycleOwner) {
            it?.let {
                binding.layoutLogin.visibility = View.GONE
                binding.layoutMain.visibility = View.VISIBLE
                binding.textWelcome.text = getString(R.string.text_welcome, it.name)

                userPreferences.saveUser(it)
                homeViewModel.loadLastGameResult()
            }
        }
        homeViewModel.lastGameResult.observe(viewLifecycleOwner) {
            it?.let { r ->
                binding.layoutResult.visibility = View.VISIBLE
                binding.textSatiety.text = getString(R.string.text_satiety, r.satiety)
                binding.textDate.text = getString(R.string.text_date, r.datetime)
            }
            if (it == null) {
                binding.layoutResult.visibility = View.GONE
            }
        }
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegistr.setOnClickListener { registr() }
        binding.btnPlay.setOnClickListener { navigateExt(R.id.nav_game) }
        binding.btnResults.setOnClickListener { navigateExt(R.id.nav_results) }
        binding.btnLogout.setOnClickListener { logout() }
    }

    private fun validate(): User? {
        val name = binding.editTextName.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        when {
            name.isEmpty() -> {
                binding.editTextName.requestFocus()
                binding.editTextName.error = getString(R.string.name_field_error)
            }
            password.isEmpty() -> {
                binding.editTextPassword.requestFocus()
                binding.editTextPassword.error = getString(R.string.password_field_error)
            }
            else -> {
                return User(name, password)
            }
        }
        return null
    }

    private fun registr() {
        validate()?.let { homeViewModel.save(it) }
    }

    private fun login() {
        validate()?.let { homeViewModel.login(it) }
    }

    private fun logout() {
        binding.layoutLogin.visibility = View.VISIBLE
        binding.layoutMain.visibility = View.GONE
        homeViewModel.logout()
        userPreferences.removeUser()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.home_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.share -> {
                homeViewModel.lastGameResult.value?.let { r ->
                    homeViewModel.user.value?.let { u ->
                        val str = "Application: Feed the Cat\n" +
                                "User: ${u.name}\n" +
                                "Result: satiety = ${r.satiety}\n" +
                                "Date: ${r.datetime}\n"
                        val shareTextInterface = requireActivity() as ShareTextInterface
                        shareTextInterface.shareText(str)
                    }
                }
                true
            }
            else -> false
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}