package app.igormatos.botaprarodar.screens.addbicycle

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Bicycle
import kotlinx.android.synthetic.main.activity_add_bike.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AddBikeViewModel(val bicycle: Bicycle) : ViewModel() {

    private var _successLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val successLiveData: LiveData<Boolean> = _successLiveData

    private var _bicycleMapperLiveData: MutableLiveData<BicycleMapper> = MutableLiveData()
    val bicycleMapperLiveData: LiveData<BicycleMapper> = _bicycleMapperLiveData

    fun saveBicycle(bicycleMapper: BicycleMapper) {
        bicycle.apply {
            name = bicycleMapper.name
            serial_number = bicycleMapper.serial_number
            order_number = bicycleMapper.order_number
            photo_path = bicycleMapper.photo_path
            photo_thumbnail_path = bicycleMapper.photo_thumbnail_path
        }
        bicycle.saveRemote { success ->
            _successLiveData.value = success
        }
    }

    fun uploadImage(imagePath: String) {
        bicycle.firebaseHelperModule.uploadImage(imagePath) { success, path, thumbnailPath ->
            if (success) {
                val bicycleMapper = BicycleMapper().apply {
                    if (path != null) {
                        photo_path = path
                    }
                    if (thumbnailPath != null) {
                        photo_thumbnail_path = thumbnailPath
                    }
                    this.success = success
                }
                _bicycleMapperLiveData.value = bicycleMapper
            }
        }
    }

}