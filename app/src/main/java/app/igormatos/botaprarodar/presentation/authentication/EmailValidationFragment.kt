package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentEmailValidationBinding

class EmailValidationFragment: Fragment() {
    private lateinit var binding : FragmentEmailValidationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailValidationBinding.inflate(inflater)
        val rootView = binding.root

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.login.setOnClickListener {

        }
    }
}