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
import app.igormatos.botaprarodar.domain.model.DialogModel
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialog : DialogFragment() {

    private lateinit var tvTitleDialog: TextView
    private lateinit var tvMessageDialog: TextView
    private lateinit var ivIconDialog: ImageView
    private lateinit var btnPrimaryDialog: Button
    private lateinit var btnSecondaryDialog: Button

    companion object {
        const val TAG = "CUSTOM_DIALOG"
        private const val DIALOG_KEY = "DIALOG_KEY"
        private const val EIGHTY_FIVE_PERCENTAGE = 0.85

        fun newInstance(dialogModel: DialogModel): CustomDialog {
            val args = Bundle()
            args.putSerializable(DIALOG_KEY, dialogModel)
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
        return inflater.inflate(R.layout.custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)
        val width = (resources.displayMetrics.widthPixels * EIGHTY_FIVE_PERCENTAGE).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialogModel = arguments?.getSerializable(DIALOG_KEY) as DialogModel

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
            tvTitleDialog = it.findViewById(R.id.tv_title_dialog)
            tvMessageDialog = it.findViewById(R.id.tv_message_dialog)
            ivIconDialog = it.findViewById(R.id.iv_icon_dialog)
            btnPrimaryDialog = it.findViewById(R.id.btn_primary_dialog)
            btnSecondaryDialog = it.findViewById(R.id.btn_secondary_dialog)
        }
    }

    private fun setClickListeners(dialogModel: DialogModel?) {
        btnPrimaryDialog.setOnClickListener {
            dialogModel?.primaryListenerButton?.onClick(it)
            dialog?.dismiss()
        }
        btnSecondaryDialog.setOnClickListener {
            dialogModel?.secondListenerButton?.onClick(it)
            dialog?.dismiss()
        }
    }

    private fun setTitle(dialogModel: DialogModel?) {
        tvTitleDialog.isVisible = !dialogModel?.title.isNullOrEmpty()
        tvTitleDialog.text = dialogModel?.title
    }

    private fun setMessage(dialogModel: DialogModel?) {
        tvMessageDialog.isVisible = !dialogModel?.message.isNullOrEmpty()
        tvMessageDialog.text = dialogModel?.message
    }

    private fun setPrimaryButtonText(dialogModel: DialogModel?) {
        btnPrimaryDialog.text = dialogModel?.titlePrimaryButton
    }

    private fun setSecondaryButtonText(dialogModel: DialogModel?) {
        btn_secondary_dialog.isVisible = !dialogModel?.titleSecondaryButton.isNullOrEmpty()
        btn_secondary_dialog.text = dialogModel?.titleSecondaryButton
    }

    private fun setIcon(dialogModel: DialogModel?) {
        val imageDrawable = dialogModel?.icon?.let { icon ->
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        }

        ivIconDialog.isVisible = dialogModel?.icon != null
        ivIconDialog.setImageDrawable(imageDrawable)
    }
}