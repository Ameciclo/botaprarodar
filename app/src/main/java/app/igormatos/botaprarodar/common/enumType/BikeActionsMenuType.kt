package app.igormatos.botaprarodar.common.enumType

import app.igormatos.botaprarodar.R


enum class BikeActionsMenuType(val stringId: Int, val icon: Int) {
    BORROW (R.string.borrow_bike, R.drawable.ic_borrow_bike),
    RETURN (R.string.return_bike, R.drawable.ic_return_bike),
}
