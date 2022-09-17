package uz.ilhomjon.restapi.utils

data class Resource<out T>(val status: Status, val data:T?, val message:String?){

    companion object{
        fun <T>success(data:T):Resource<T>{
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T>error(message: String?):Resource<T>{
            return Resource(Status.ERROR, null, message)
        }
        fun <T>loading(message: String?):Resource<T>{
            return Resource(Status.LOADING, null, message)
        }
    }
}
