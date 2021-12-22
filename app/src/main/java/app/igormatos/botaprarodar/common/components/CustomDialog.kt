package app.igormatos.botaprarodar.common.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.CustomDialogBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialog : DialogFragment() {

    private lateinit var titleDialog: TextView
    private lateinit var messageDialog: TextView
    private lateinit var iconDialog: ImageView
    private lateinit var primaryDialogButton: Button
    private lateinit var secondaryDialogButton: Button
    private lateinit var binding: CustomDialogBinding

    companion object {
        const val TAG = "CUSTOM_DIALOG"
        private const val DIALOG_KEY = "DIALOG_KEY"
        private const val EIGHTY_FIVE_PERCENTAGE = 0.85

        fun newInstance(customDialogModel: CustomDialogModel): CustomDialog {
            val args = Bundle()
            args.putSerializable(DIALOG_KEY, customDialogModel)
            val fragment = CustomDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_dialog, container, false)
        binding = CustomDialogBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)
        val width = (resources.displayMetrics.widthPixels * EIGHTY_FIVE_PERCENTAGE).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialogModel = arguments?.getSerializable(DIALOG_KEY) as CustomDialogModel

        initUI()
        setTitle(dialogModel)
        setMessage(dialogModel)
        setIcon(dialogModel)
        setPrimaryButtonText(dialogModel)
        setSecondaryButtonText(dialogModel)
        setClickListeners(dialogModel)
    }

    private fun initUI() {
        view?.let {
            titleDialog = binding.tvTitleDialog
            messageDialog = binding.tvMessageDialog
            iconDialog = binding.ivIconDialog
            primaryDialogButton = binding.btnPrimaryDialog
            secondaryDialogButton = binding.btnSecondaryDialog
        }
    }

    private fun setTitle(customDialogModel: CustomDialogModel?) {
        titleDialog.isVisible = !customDialogModel?.title.isNullOrEmpty()
        titleDialog.text = customDialogModel?.title
    }

    private fun setIcon(customDialogModel: CustomDialogModel?) {
        val imageDrawable = customDialogModel?.icon?.let { icon ->
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        }

        iconDialog.isVisible = customDialogModel?.icon != null
        iconDialog.setImageDrawable(imageDrawable)
    }

    private fun setMessage(customDialogModel: CustomDialogModel?) {
        messageDialog.isVisible = !customDialogModel?.message.isNullOrEmpty()
        messageDialog.text = customDialogModel?.message
    }

    private fun setPrimaryButtonText(customDialogModel: CustomDialogModel?) {
        primaryDialogButton.text = customDialogModel?.primaryButtonText
    }

    private fun setSecondaryButtonText(customDialogModel: CustomDialogModel?) {
        btn_secondary_dialog.isVisible = !customDialogModel?.secondaryButtonText.isNullOrEmpty()
        btn_secondary_dialog.text = customDialogModel?.secondaryButtonText
    }

    private fun setClickListeners(customDialogModel: CustomDialogModel?) {
        primaryDialogButton.setOnClickListener {
            customDialogModel?.primaryButtonListener?.onClick(it)
            dialog?.dismiss()
        }
        secondaryDialogButton.setOnClickListener {
            customDialogModel?.secondaryButtonListener?.onClick(it)
            dialog?.dismiss()
        }
    }
}