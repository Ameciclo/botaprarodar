package app.igormatos.botaprarodar.presentation.addbicycle

import app.igormatos.botaprarodar.presentation.ResponseViewCustom
import app.igormatos.botaprarodar.presentation.STATUS

class BicycleResponseView(

    errorMessage: String?,
    status: STATUS?
) : ResponseViewCustom(errorMessage, status)