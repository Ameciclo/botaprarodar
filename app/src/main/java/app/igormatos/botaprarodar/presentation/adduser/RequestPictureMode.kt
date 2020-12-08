package app.igormatos.botaprarodar.presentation.adduser

import androidx.annotation.IntDef

@IntDef(
    RequestPictureMode.REQUEST_PROFILE_PHOTO,
    RequestPictureMode.REQUEST_ID_PHOTO,
    RequestPictureMode.REQUEST_RESIDENCE_PHOTO,
    RequestPictureMode.REQUEST_ID_PHOTO_BACK
)
annotation class RequestPictureMode {
    companion object {
        const val REQUEST_PROFILE_PHOTO = 1
        const val REQUEST_ID_PHOTO = 2
        const val REQUEST_RESIDENCE_PHOTO = 3
        const val REQUEST_ID_PHOTO_BACK = 4
        const val REQUEST_NOTHING = 0
    }
}