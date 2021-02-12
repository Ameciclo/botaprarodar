package app.igormatos.botaprarodar.common.components

import java.io.Serializable

interface DialogListener : Serializable {
    fun primaryOnClickListener()
    fun secondaryOnClickListener()
}