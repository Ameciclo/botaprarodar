//package app.igormatos.botaprarodar.local
//
//import app.igormatos.botaprarodar.data.model.User
//import io.realm.Realm
//import org.jetbrains.anko.doAsync
//import org.jetbrains.anko.uiThread
//
//
//object RealmHelper {
//
//    private fun getDefaultRealmInstance(): Realm {
//        return Realm.getDefaultInstance()
//    }
//
//    fun insertUser(user: User) {
//        run {
//            realm.beginTransaction()
//            realm.insertOrUpdate(user)
//            realm.commitTransaction()
//        }
//    }
//
//    fun getUsers(callback: (List<User>) -> Unit) {
//        doAsync {
//            val query = realm.where(User::class.java)
//            val result = query.findAll()
//
//            result.addChangeListener { results ->
//                uiThread {
//                    callback(results.toList())
//                }
//            }
//        }
//    }
//
//
//    var realm = getDefaultRealmInstance()
//}