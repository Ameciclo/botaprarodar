package app.igormatos.botaprarodar.presentation.adapter

import app.igormatos.botaprarodar.domain.model.User

interface UserRecyclerViewItemDelegate {
    fun onTouchUserItem(user: User? = null)
}