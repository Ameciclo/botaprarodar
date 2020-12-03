package app.igormatos.botaprarodar.data.model

import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.network.FirebaseHelperModuleImpl
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

@KoinApiExtension
abstract class Item : KoinComponent {

    // EXTERNALIZAR PARA CAMADA DE REPOSITORY
    open val firebaseHelperModule: FirebaseHelperModule by inject()

    open val path: String = ""

    open var id: String? = null

    open var isAvailable: Boolean = false

    open fun title(): String = ""

    open fun subtitle(): String = ""

    open fun iconPath(): String = ""

    fun toggleAvailability(block: (Boolean) -> Unit) {
        isAvailable = !isAvailable
        saveRemote { block(it) }
    }

    fun saveRemote(block: (Boolean) -> Unit) {
        firebaseHelperModule.saveItem(this, block)
    }

    fun getReadableDate(timestamp: Long): String {
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val inputDate = Date(timestamp)
        return outputFormat.format(inputDate)
    }
}