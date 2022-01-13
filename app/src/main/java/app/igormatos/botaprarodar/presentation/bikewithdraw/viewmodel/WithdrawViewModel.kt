package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.bikewithdraw.GetAvailableBikesException
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class WithdrawViewModel(
    private val bikeHolder: BikeHolder,
    private val getAvailableBikes: GetAvailableBikes,
    private val userHolder: UserHolder,
    private val stepperAdapter: WithdrawStepper,
    private val usersUseCase: UsersUseCase,
    private val validateUserWithdraw: ValidateUserWithdraw,
    private val sendBikeWithdraw: SendBikeWithdraw,
) : ViewModel() {
    private val _availableBikes = MutableLiveData<List<Bike>>()
    val availableBikes: LiveData<List<Bike>>
        get() = _availableBikes

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun getBikeList(communityId: String) {
        viewModelScope.launch {
            try {
                _availableBikes.value = getAvailableBikes.execute(communityId)
            } catch (e: GetAvailableBikesException) {

            }
        }
    }

    fun setBike(bike: Bike) {
        bikeHolder.bike = bike
        _bike.value = bike
    }

    private val _userList = MutableLiveData<List<User>>()
    private val _originalList = arrayListOf<User>()
    val userList: LiveData<List<User>>
        get() = _userList

    fun getUserList(communityId: String) {
        viewModelScope.launch {
            when (val result = usersUseCase.getAvailableUsersByCommunityId(communityId)) {
                is SimpleResult.Success -> {
                    result.data.forEach {
                        validateUserWithdraw(it)
                    }
                    if (!result.data.isNullOrEmpty()) {
                        _userList.postValue(result.data!!)
                        _originalList.run {
                            this.clear()
                            this.addAll(result.data)
                        }
                    } else {
                        _userList.postValue(arrayListOf())
                    }
                }
                is SimpleResult.Error -> {
                    throw Exception()
                }
            }
        }
    }

    fun setUser(user: User) {
        userHolder.user = user
        _user.value = user
    }

    private suspend fun validateUserWithdraw(user: User): Boolean {
        return validateUserWithdraw.execute(user)
    }

    fun filterBy(word: String) {
        val lista =
            _originalList.filter { user ->
                user.name!!.lowercase().contains(word.lowercase())
            }
        _userList.value = lista!!
    }

    private val _uiState = MutableLiveData<BikeWithdrawUiState>()
    val uiState: LiveData<BikeWithdrawUiState>
        get() = _uiState

    val uiStepConfig = stepperAdapter.currentStep.asLiveData()

    private val date = Calendar.getInstance(Locale("pt", "BR")).time
    val withdrawDate = formattedDate("dd MMM yyyy").format(date).replace(" ", " de ")
    private val withdrawDateBase = formattedDate().format(date)

    val bikeImageUrl = MutableLiveData<String>()
    val userImageUrl = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val bikeOrderNumber = MutableLiveData<String>()
    val bikeSeriesNumber = MutableLiveData<String>()
    val bikeName = MutableLiveData<String>()

    private val _bike: MutableLiveData<Bike> = MutableLiveData<Bike>()
    private val _user: MutableLiveData<User> = MutableLiveData<User>()
    var bike: LiveData<Bike?> = _bike
    var user: LiveData<User?> = _user

    init {
        bikeImageUrl.value = bikeHolder.bike?.photoPath ?: ""
        userImageUrl.value = userHolder.user?.profilePicture ?: ""
        userName.value = userHolder.user?.name ?: ""
        bikeOrderNumber.value = bikeHolder.bike?.orderNumber?.toString() ?: ""
        bikeSeriesNumber.value = bikeHolder.bike?.serialNumber ?: ""
        bikeName.value = bikeHolder.bike?.name ?: ""
        _bike.value = bikeHolder.bike
        _user.value = userHolder.user
    }

    fun confirmBikeWithdraw(onFinished: () -> Unit) {
        _uiState.value = BikeWithdrawUiState.Loading

        viewModelScope.launch {
            try {
                when (sendBikeWithdraw.execute(withdrawDateBase, bikeHolder, userHolder)) {
                    is SimpleResult.Success -> {
                        _uiState.postValue(BikeWithdrawUiState.Success)
                        onFinished()
                    }
                    is SimpleResult.Error -> {
                        _uiState.postValue(BikeWithdrawUiState.Error(DEFAULT_WITHDRAW_ERROR_MESSAGE))
                    }
                }
            } catch (e: java.lang.Exception) {
                _uiState.postValue(BikeWithdrawUiState.Error(DEFAULT_WITHDRAW_ERROR_MESSAGE))
            }
        }
    }

    fun navigateToPrevious() {
        stepperAdapter.navigateToPrevious()
    }

    fun backToInitialState() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
    }

    fun setFinishAction() {
        stepperAdapter.setCurrentStep(StepConfigType.FINISHED_ACTION)
    }
}