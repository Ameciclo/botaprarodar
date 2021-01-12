package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BprViewModel<T> : ViewModel() {
    protected val _state = MutableLiveData<T>()
    val state: LiveData<T>
        get() = _state
}