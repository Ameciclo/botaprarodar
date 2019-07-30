package app.igormatos.botaprarodar.network

interface RequestListener<T> {
    fun onChildChanged(result: T)

    fun onChildAdded(result: T)

    fun onChildRemoved(result: T)

}