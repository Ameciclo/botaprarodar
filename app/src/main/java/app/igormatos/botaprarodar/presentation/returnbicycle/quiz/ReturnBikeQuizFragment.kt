package app.igormatos.botaprarodar.presentation.returnbicycle.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentReturnBikeQuizBinding
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReturnBikeQuizFragment : Fragment() {

    private val returnBikeQuizViewModel: ReturnBikeQuizViewModel by viewModel()

    private lateinit var binding: FragmentReturnBikeQuizBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_return_bike_quiz, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = returnBikeQuizViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = returnBikeQuizViewModel
        setupObservers()
    }

    private fun setupObservers() {
        binding.viewModel?.finishQuiz?.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    returnBikeQuizViewModel.navigateToNextStep()
                    val direction =
                        ReturnBikeQuizFragmentDirections.actionReturnBikeQuizFragmentToStepFinalReturnBikeFragment()
                    navController.navigate(direction)
                }
                false -> {
                }
            }
        })
    }
}