package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.extensions.createLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class ReturnBicycleActivity : ComponentActivity() {
    private val returnBicycleViewModel: ReturnBicycleViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    private var _availableBikes: MutableState<List<Bike>> = mutableStateOf(mutableListOf())
    private var currentStep: MutableState<StepConfigType> =
        mutableStateOf(StepConfigType.SELECT_BIKE)
    private lateinit var joinedCommunityId: String

    private val loadingDialog: AlertDialog by lazy {
        createLoading(R.layout.loading_dialog_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {
                ReturnBicyclePage(
                    viewModel = returnBicycleViewModel,
                    finish = { finish() },
                    communityId = joinedCommunityId
                )
            }
        }

        returnBicycleViewModel.loadingState.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        returnBicycleViewModel.errorState.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        initUI()
        return super.onCreateView(name, context, attrs)
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        returnBicycleViewModel.setInitialStep()
        returnBicycleViewModel.getBikesInUseToReturn(joinedCommunityId)
    }
}
