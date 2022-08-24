package app.igormatos.botaprarodar.presentation.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.databinding.FragmentUserSuccessfullyRegisteredBinding
import app.igormatos.botaprarodar.presentation.components.WithdrawStepperActivity

class UserSuccessfullyRegisteredFragment : Fragment() {

    private lateinit var binding: FragmentUserSuccessfullyRegisteredBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSuccessfullyRegisteredBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupGoHomeButtonListener()
        setupLendButtonListener()
    }

    private fun setupGoHomeButtonListener() {
        binding.goHomeButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupLendButtonListener() {
        binding.lendButton.setOnClickListener {
            context?.startActivity(Intent(context, WithdrawStepperActivity::class.java))
            activity?.finish()
        }
    }
}
