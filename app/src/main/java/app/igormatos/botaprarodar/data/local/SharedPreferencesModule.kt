package app.igormatos.botaprarodar.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.igormatos.botaprarodar.domain.model.community.Community

class SharedPreferencesModule(appContext: Context) {
    private val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        appContext,
        "BPR-SHARED-ENCRYPTED",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences("BPR-SHARED", Context.MODE_PRIVATE)

    private val COMMUNITY_NAME = "COMMUNITY_NAME"
    private val COMMUNITY_ID = "COMMUNITY_ID"
    private val COMMUNITY_ORG_NAME = "COMMUNITY_ORG_NAME"
    private val USER_TOKEN = "USER_TOKEN"
    private val USER_TOKEN_RENOVATION = "USER_TOKEN_RENOVATION"

    fun saveJoinedCommmunity(community: Community) {
        val editor = sharedPrefs.edit()
        editor.putString(COMMUNITY_NAME, community.name)
        editor.putString(COMMUNITY_ID, community.id)
        editor.putString(COMMUNITY_ORG_NAME, community.org_name)
        editor.apply()
    }

    fun getJoinedCommunity(): Community {
        val communityName = sharedPrefs.getString(COMMUNITY_NAME, "")
        val communityId = sharedPrefs.getString(COMMUNITY_ID, "")
        val communityOrgName = sharedPrefs.getString(COMMUNITY_ORG_NAME, "")
        return Community(
            name = communityName ?: "",
            id = communityId ?: "",
            org_name = communityOrgName ?: ""
        )
    }

    fun isCommunitySelected(): Boolean {
        return sharedPrefs.getString(COMMUNITY_ID, "").isNullOrEmpty().not()
    }

    fun clear() {
        encryptedSharedPrefs.edit().clear().apply()
        sharedPrefs.edit().clear().apply()
    }

    fun getAuthToken(): String? {
        return encryptedSharedPrefs.getString(USER_TOKEN, null)
    }

    fun saveAuthToken(token: String?) {
        val editor = encryptedSharedPrefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getAuthTokenRenovationStatus(): Boolean {
        return sharedPrefs.getBoolean(USER_TOKEN_RENOVATION, true)
    }

    fun saveAuthTokenRenovationStatus(shouldRenew: Boolean) {
        val editor = sharedPrefs.edit()
        editor.putBoolean(USER_TOKEN_RENOVATION, shouldRenew)
        editor.apply()
    }
}