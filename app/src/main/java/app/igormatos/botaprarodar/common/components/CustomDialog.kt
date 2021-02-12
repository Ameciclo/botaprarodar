package app.igormatos.botaprarodar.common.components

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.DialogModel
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialog : DialogFragment() {

    private lateinit var tvTitleDialog: TextView
    private lateinit var tvMessageDialog: TextView
    private lateinit var ivIconDialog: ImageView
    private lateinit var btnPrimaryDialog: Button
    private lateinit var btnSecondaryDialog: Button

//    private lateinit var rootView: View

//    private lateinit var dialogModel: DialogModel

    companion object {

        const val TAG = "SimpleDialog"
        private const val DIALOG_KEY = "DIALOG_KEY"

        fun newInstance(dialogModel: DialogModel): CustomDialog {
            val args = Bundle()
            args.putParcelable(DIALOG_KEY, dialogModel)
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
        val rootView = inflater.inflate(R.layout.custom_dialog, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_corner)

        initUI()
        val dialogModel = arguments?.getParcelable<DialogModel>(DIALOG_KEY)

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
            dialogModel?.listener?.primaryOnClickListener()
        }
        btnSecondaryDialog.setOnClickListener {
            dialogModel?.listener?.secondaryOnClickListener()
        }
    }

    private fun setTitle(dialogModel: DialogModel?) {
        tvTitleDialog.text = dialogModel?.title
    }

    private fun setMessage(dialogModel: DialogModel?) {
        tvMessageDialog.text = dialogModel?.message
    }

    private fun setPrimaryButtonText(dialogModel: DialogModel?) {
        btnPrimaryDialog.text = dialogModel?.titlePrimaryButton
    }

    private fun setSecondaryButtonText(dialogModel: DialogModel?) {
        btn_secondary_dialog.text = dialogModel?.titleSecondaryButton
    }

    private fun setIcon(dialogModel: DialogModel?) {
        val imageDrawable = dialogModel?.icon?.let { icon ->
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        }

        ivIconDialog.setImageDrawable(imageDrawable)
    }
}