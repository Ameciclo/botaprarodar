package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.databinding.FragmentEmailValidationBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class EmailValidationFragment: Fragment() {
    private lateinit var binding : FragmentEmailValidationBinding
    private val emailValidationViewModel: EmailValidationViewModel by koinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailValidationBinding.inflate(inflater)
        binding.viewmodel = emailValidationViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configNextButton()
    }

    private fun configNextButton() {
        binding.next.setOnClickListener {
            //TODO implement Email Validation action
        }
    }
}