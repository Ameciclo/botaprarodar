package app.igormatos.botaprarodar.data.repository

import androidx.annotation.VisibleForTesting
import app.igormatos.botaprarodar.data.model.Admin

interface AdminDataSource {
    suspend fun createAdmin(
        email: String,
        password: String
    ): Admin?
    @VisibleForTesting
    suspend fun authenticateAdmin(email: String, password: String): Admin?
    suspend fun sendPasswordRecoverEmail(email: String)
    suspend fun isAdminRegistered(email: String): Boolean
}