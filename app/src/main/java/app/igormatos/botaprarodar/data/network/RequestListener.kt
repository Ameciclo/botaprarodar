package app.igormatos.botaprarodar.data.network

interface RequestListener<T> {
    fun onChildChanged(result: T)

    fun onChildAdded(result: T)

    fun onChildRemoved(result: T)

}