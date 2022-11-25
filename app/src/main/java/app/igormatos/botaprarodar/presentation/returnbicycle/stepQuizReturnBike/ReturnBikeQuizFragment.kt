package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentReturnBikeQuizBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReturnBikeQuizFragment : Fragment(), AdapterView.OnItemClickListener {

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

        bindAdapter()
        return binding.root
    }

    private fun bindAdapter() {

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            layoutInflater.context,
            R.array.return_bike_purpose_list,
            R.layout.used_bike_items
        )
        (binding.usedBikeToMoveDropdownLayout.editText as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
        (binding.usedBikeToMoveDropdownLayout.editText as? MaterialAutoCompleteTextView)?.onItemClickListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = returnBikeQuizViewModel
        setupObservers()
    }

    private fun setupObservers() {
        binding.viewModel?.clickToNextFragment?.observe(viewLifecycleOwner) {
            if (it) {
                returnBikeQuizViewModel.setClickToNextFragmentToFalse()
                returnBikeQuizViewModel.navigateToNextStep()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        val chosenReason = parent?.getItemAtPosition(position).toString()
//        returnBikeQuizViewModel.setUsedBikeToMove(chosenReason)
    }
}
