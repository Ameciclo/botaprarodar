package app.igormatos.botaprarodar.presentation.user.userquiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.databinding.FragmentUserQuizBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserQuizFragment : Fragment() {

    private val viewModel: UserQuizViewModel by viewModel()

    private lateinit var binding: FragmentUserQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserQuizBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }
}