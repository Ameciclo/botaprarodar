package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.databinding.FragmentEmailValidationBinding

class EmailValidationFragment: Fragment() {
    private lateinit var binding : FragmentEmailValidationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailValidationBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.next.setOnClickListener {
            //TODO implement Email Validation action
        }
    }
}