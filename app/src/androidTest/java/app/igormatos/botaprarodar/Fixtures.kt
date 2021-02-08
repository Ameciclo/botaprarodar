package app.igormatos.botaprarodar

import android.net.Uri
import android.os.Parcel
import com.google.android.gms.internal.firebase_auth.zzey
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*

object Fixtures {

    val adminUser = object : FirebaseUser(){
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            TODO("Not yet implemented")
        }

        override fun getUid(): String {
            return "admin_id"
        }

        override fun getProviderId(): String {
            TODO("Not yet implemented")
        }

        override fun getDisplayName(): String? {
            TODO("Not yet implemented")
        }

        override fun getPhotoUrl(): Uri? {
            TODO("Not yet implemented")
        }

        override fun getEmail(): String? {
            return "admin@admin.com"
        }

        override fun getPhoneNumber(): String? {
            TODO("Not yet implemented")
        }

        override fun isEmailVerified(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isAnonymous(): Boolean {
            TODO("Not yet implemented")
        }

        override fun zza(): MutableList<String> {
            TODO("Not yet implemented")
        }

        override fun zza(p0: MutableList<out UserInfo>): FirebaseUser {
            TODO("Not yet implemented")
        }

        override fun zza(p0: zzey) {
            TODO("Not yet implemented")
        }

        override fun getProviderData(): MutableList<out UserInfo> {
            TODO("Not yet implemented")
        }

        override fun zzb(): FirebaseUser {
            TODO("Not yet implemented")
        }

        override fun zzb(p0: MutableList<zzy>?) {
            TODO("Not yet implemented")
        }

        override fun zzc(): FirebaseApp {
            TODO("Not yet implemented")
        }

        override fun zzd(): String? {
            TODO("Not yet implemented")
        }

        override fun zze(): zzey {
            TODO("Not yet implemented")
        }

        override fun zzf(): String {
            TODO("Not yet implemented")
        }

        override fun zzg(): String {
            TODO("Not yet implemented")
        }

        override fun getMetadata(): FirebaseUserMetadata? {
            TODO("Not yet implemented")
        }

        override fun zzh(): zzz {
            TODO("Not yet implemented")
        }

    }
}