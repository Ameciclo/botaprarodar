package app.igormatos.botaprarodar.presentation.returnbicycle.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentReturnBikeQuizBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReturnBikeQuizFragment : Fragment() {

    private val returnBikeQuizViewModel: ReturnBikeQuizViewModel by viewModel()

    private lateinit var binding: FragmentReturnBikeQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_return_bike_quiz, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = returnBikeQuizViewModel
        setupObservers()
    }

    private fun setupObservers() {
        binding.viewModel?.finishQuiz?.observe(viewLifecycleOwner, {
            //TODO navegação para o próximo passo quando vier true
        })
    }
}