package app.igormatos.botaprarodar.presentation.adduser

sealed class AddUserViewState() {
    object HideLoading : AddUserViewState()
    object ShowLoading : AddUserViewState()
    object Finish : AddUserViewState()
    data class ImageFullScream(val path: String) : AddUserViewState()
    data class ShowDialog(val title: Int, val message: Int, val image: Int) : AddUserViewState()
    data class TakePicture(val code: Int) : AddUserViewState()
    data class ShowToast(val message: Int) : AddUserViewState()
}