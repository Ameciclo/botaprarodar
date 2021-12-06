package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentSelectUserBinding
import app.igormatos.botaprarodar.presentation.adapter.SelectUserListAdapter
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.decoration.UserDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectUserFragment : Fragment() {
    private val binding: FragmentSelectUserBinding by lazy {
        FragmentSelectUserBinding.inflate(layoutInflater)
    }

    private val viewModel: SelectUserViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()

    private val selectUserAdapter by lazy {
        SelectUserListAdapter {
            viewModel.setUser(it)
            viewModel.navigateToNextStep()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        binding.viewModel?.userList?.observe(viewLifecycleOwner, Observer { userList ->
            selectUserAdapter.submitList(userList)
        })
    }

    private fun initUI() {
        val marginTop = resources.getDimensionPixelSize(R.dimen.padding_medium)

        binding.userList.layoutManager = LinearLayoutManager(context)
        binding.userList.adapter = selectUserAdapter
        binding.userList.addItemDecoration(UserDecoration(marginTop))

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getUserList(joinedCommunityId)

        configureUserFilter(joinedCommunityId)
    }

    private fun configureUserFilter(
        joinedCommunityId: String
    ) {
        val searchedText = binding.tieUserSearch.editText
        (searchedText as EditText).setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && textView.text.toString().isNotEmpty()) {
                viewModel.filterBy(textView.text.toString())
                return@setOnEditorActionListener true
            } else {
                viewModel.getUserList(joinedCommunityId)
                return@setOnEditorActionListener false
            }
        }
    }
}