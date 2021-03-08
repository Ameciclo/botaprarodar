package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentStepFinalReturnBikeBinding

class StepFinalReturnBikeFragment : Fragment() {

    private lateinit var binding: FragmentStepFinalReturnBikeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_final_return_bike, container, false)
        return binding.root
    }
}