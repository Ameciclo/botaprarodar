package app.igormatos.botaprarodar.presentation.addbicycle

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.databinding.FragmentBikeFormBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adduser.AddUserFragmentArgs
import app.igormatos.botaprarodar.presentation.main.MainActivity
import com.brunotmgomes.ui.extensions.*
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class BikeFormFragment : Fragment() {

    var imagePath: String? = null
    private lateinit var loadingDialog: AlertDialog

    private val formViewModel: BikeFormViewModel by koinViewModel()

    private val args: BikeFormFragmentArgs by navArgs()

    private lateinit var binding: FragmentBikeFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bike_form, container, false)
        binding.viewModel = formViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickBicyclePhotoImage()
        waitBicycleRegisterResult()
        checkEditMode()
        loadingDialog = requireActivity().createLoading(R.layout.loading_dialog_animation)
    }

    private fun checkEditMode() {
        args.bike?.let {
            (activity as MainActivity).supportActionBar?.title = "Editar Bicicleta"
            setValuesToEditBike(it)
        }
    }

    private fun onClickBicyclePhotoImage() {
        binding.bikePhotoImageView.setOnClickListener {
            requireActivity().takePictureIntent(REQUEST_PHOTO) { path ->
                this.imagePath = path
            }
        }
    }

    private fun waitBicycleRegisterResult() {
        binding.viewModel?.state?.observe(viewLifecycleOwner, Observer { bikeFormStatus ->
            when (bikeFormStatus) {
                is BikeFormStatus.Success -> {
                    loadingDialog.dismiss()
                    successText()
                }
                is BikeFormStatus.Loading -> {
                    requireActivity().window.decorView.hideKeyboard()
                    loadingDialog.show()
                }
                is BikeFormStatus.Error -> {
                    loadingDialog.dismiss()
                    errorText(bikeFormStatus.message)
                }
            }
        })
    }

    private fun successText() {
        val intent = Intent().putExtra("isEditModeAvailable", formViewModel.isEditModeAvailable)
        requireActivity().setResult(RESULT_OK, intent)
        findNavController().popBackStack()
    }

    private fun errorText(errorMessage: String) {
        snackBarMaker(errorMessage, binding.containerAddBike).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
            show()
        }
    }

    private fun setValuesToEditBike(bike: Bike?) {
        bike?.let { formViewModel.updateBikeValues(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO && resultCode == Activity.RESULT_OK) {
            imagePath?.let {
                binding.viewModel?.updateImagePath(it)
            }
        }
    }
}
