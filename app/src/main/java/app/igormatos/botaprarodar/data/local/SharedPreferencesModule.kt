package app.igormatos.botaprarodar.data.local

import android.content.Context
import android.content.SharedPreferences
import app.igormatos.botaprarodar.domain.model.community.Community

class SharedPreferencesModule(appContext: Context) {

    private val sharedPrefs: SharedPreferences =
        appContext.getSharedPreferences("BPR-SHARED", Context.MODE_PRIVATE)

    private val COMMUNITY_NAME = "COMMUNITY_NAME"
    private val COMMUNITY_ID = "COMMUNITY_ID"
    private val COMMUNITY_ORG_NAME = "COMMUNITY_ORG_NAME"

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

    fun clear(): Boolean {
        return sharedPrefs.edit().clear().commit()
    }
}