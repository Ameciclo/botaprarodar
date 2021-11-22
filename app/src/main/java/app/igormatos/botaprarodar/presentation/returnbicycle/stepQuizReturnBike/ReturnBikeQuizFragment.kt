package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentReturnBikeQuizBinding
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

        loadDropdownItems()
        return binding.root
    }

    private fun loadDropdownItems() {
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            layoutInflater.context,
            R.array.used_bike_to_move_list,
            R.layout.used_bike_items
        )
        (binding.usedBikeToMoveDropdownLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = returnBikeQuizViewModel
        setupObservers()
    }

    private fun setupObservers() {
        binding.viewModel?.clickToNextFragment?.observe(viewLifecycleOwner, Observer {
            if (it) {
                returnBikeQuizViewModel.setClickToNextFragmentToFalse()
                returnBikeQuizViewModel.navigateToNextStep()
                val direction =
                    ReturnBikeQuizFragmentDirections.actionReturnBikeQuizFragmentToStepFinalReturnBikeFragment()
                navController.navigate(direction)
            }
        })
    }
}