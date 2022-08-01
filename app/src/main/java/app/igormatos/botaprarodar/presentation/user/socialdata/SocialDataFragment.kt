package app.igormatos.botaprarodar.presentation.user.socialdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentSocialDataBinding
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.user.UserActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SocialDataFragment : Fragment() {

    private val args: SocialDataFragmentArgs by navArgs()

    private val socialDataViewModel: SocialDataViewModel by viewModel {
        parametersOf(createMapOptions(), args.user, args.editMode)
    }
    private lateinit var binding: FragmentSocialDataBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSocialDataBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = socialDataViewModel
        return binding.root
    }

    private fun createMapOptions(): Map<String, List<String>> {
        val racialOptions = resources.getStringArray(R.array.racial_options).toList()
        val incomeOptions = resources.getStringArray(R.array.income_options).toList()
        val schoolingOptions = resources.getStringArray(R.array.schooling_options).toList()
        val schoolingStatusOptions =
            resources.getStringArray(R.array.schooling_status_options).toList()
        val genderOptions = resources.getStringArray(R.array.gender_options).toList()
        return mapOf(
            "racialOptions" to racialOptions,
            "incomeOptions" to incomeOptions,
            "schoolingOptions" to schoolingOptions,
            "schoolingStatusOptions" to schoolingStatusOptions,
            "genderOptions" to genderOptions
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCancelButtonListener()
        setupListeners()
        setupViewModelStatus()
        checkEditMode()
    }

    private fun setupCancelButtonListener() {
        binding.backButton.setOnClickListener {
            navController.popBackStack()
            (activity as UserActivity).navigateToPrevious()
        }
    }

    private fun setupListeners() {
        binding.userGenderCst.setupClick {
            createDialogGender()
        }

        binding.userSchoolingCst.setupClick {
            createDialogSchooling()
        }

        binding.userRacialCst.setupClick {
            openDialogToSelectRace()
        }

        binding.userIncomeCst.setupClick {
            openDialogToSelectIncome()
        }

        binding.schoolingStatusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.viewModel?.setSelectSchoolingStatusIndex(checkedId)
            binding.viewModel?.confirmUserSchoolingStatus()
        }
    }

    private fun setupViewModelStatus() {
        socialDataViewModel.openQuiz.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                val (user, editMode, deleteImagePaths) = data
                val direction =
                    SocialDataFragmentDirections.actionSocialDataFragmentToUserQuizFragment(
                        user,
                        editMode,
                        deleteImagePaths.toTypedArray()
                    )
                navController.navigate(direction)
            }
        }
    }

    private fun checkEditMode() {
        if (args.editMode) setValuesToEditUser(args.user)
    }

    private fun setValuesToEditUser(user: User) {
        socialDataViewModel.updateUserValues(user, getCommunityUsers())
        if (user.schoolingStatus.isNullOrBlank()) {
            binding.schoolingStatusRadioGroup.clearCheck()
        }
    }

    private fun getCommunityUsers(): ArrayList<User> {
        val communityUsers = args.communityUsers.toCollection(ArrayList())
        if (communityUsers.isNullOrEmpty()) {
            return arrayListOf()
        }
        return communityUsers
    }

    private fun createDialogGender() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_gender))
            setSingleChoiceItems(
                binding.viewModel?.getGenderList()?.toTypedArray(),
                binding.viewModel?.getSelectedGenderListIndex() ?: 0
            ) { _, which ->
                binding.viewModel?.setSelectGenderIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserGender()
            }
            create().show()
        }
    }

    private fun createDialogSchooling() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_schooling))
            setSingleChoiceItems(
                binding.viewModel?.getSchoolingList()?.toTypedArray(),
                binding.viewModel?.getSelectedSchoolingListIndex() ?: 0
            ) { _, which ->
                binding.viewModel?.setSelectSchoolingIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserSchooling()
            }
            create().show()
        }
    }

    private fun openDialogToSelectIncome() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_income))
            setSingleChoiceItems(
                binding.viewModel?.getIncomeList()?.toTypedArray(),
                binding.viewModel?.getSelectedIncomeListIndex() ?: 0
            ) { _, which ->
                binding.viewModel?.setSelectIncomeIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserIncome()
            }
            create().show()
        }
    }

    private fun openDialogToSelectRace() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_racial))
            setSingleChoiceItems(
                binding.viewModel?.getRacialList()?.toTypedArray(),
                binding.viewModel?.getSelectedRacialListIndex() ?: 0
            ) { _, which ->
                binding.viewModel?.setSelectRacialIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserRace()
            }
            create().show()
        }
    }
}

