package app.igormatos.botaprarodar.authentication

import app.igormatos.botaprarodar.Fixtures
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.repository.AdminDataSource
import kotlin.reflect.KFunction0

class TestAdminRemoteDataSource(
    var createAdminStub: (KFunction0<Admin?>)? = null,
    var authenticateAdmin: (KFunction0<Admin?>)? = null,
    var sendPasswordRecoverEmailStub: (() -> Unit)? = null,
    var isAdminRegisteredStub: (KFunction0<Boolean>)? = null,
) : AdminDataSource {

    override suspend fun createAdmin(email: String, password: String): Admin? {
        return createAdminStub?.invoke() ?: Fixtures.adminUser
    }

    override suspend fun authenticateAdmin(email: String, password: String): Admin? {
        return authenticateAdmin?.invoke() ?: Fixtures.adminUser
    }

    override suspend fun sendPasswordRecoverEmail(email: String) {
        return sendPasswordRecoverEmailStub?.invoke() ?: Unit
    }

    override suspend fun isAdminRegistered(email: String): Boolean {
        return isAdminRegisteredStub?.invoke() ?: true
    }
}