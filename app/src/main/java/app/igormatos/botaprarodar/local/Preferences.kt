package app.igormatos.botaprarodar.local

import android.content.Context
import android.content.SharedPreferences
import app.igormatos.botaprarodar.network.Community


class Preferences {

    companion object {

        private const val COMMUNITY_NAME = "COMMUNITY_NAME"
        private const val COMMUNITY_ID = "COMMUNITY_ID"
        private const val COMMUNITY_ORG_NAME = "COMMUNITY_ORG_NAME"
        private const val TRIPS_COUNT = "TRIPS_COUNT"

        fun saveJoinedCommmunity(context: Context, community: Community) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(COMMUNITY_NAME, community.name)
            editor.putString(COMMUNITY_ID, community.id)
            editor.putString(COMMUNITY_ORG_NAME, community.org_name)
            editor.apply()
        }

        fun getJoinedCommunity(context: Context): Community {
            val communityName = getSharedPreferences(context).getString(COMMUNITY_NAME, "")
            val communityId = getSharedPreferences(context).getString(COMMUNITY_ID, "")
            val communityOrgName = getSharedPreferences(context).getString(COMMUNITY_ORG_NAME, "")
            return Community(name = communityName, id = communityId, org_name = communityOrgName)
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("BPR-SHARED", Context.MODE_PRIVATE)
        }

        fun isCommunitySelected(context: Context): Boolean {
            return getSharedPreferences(context).getString(COMMUNITY_ID, "").isNullOrEmpty().not()
        }

        fun incrementTripCount(context: Context) {
            val editor = getSharedPreferences(context).edit()
            val newCount = getTripsCount(context) + 1
            editor.putInt(TRIPS_COUNT, newCount)
            editor.apply()
        }

        fun getTripsCount(context: Context) : Int {
            return getSharedPreferences(context).getInt(TRIPS_COUNT, 0)
        }

    }
}