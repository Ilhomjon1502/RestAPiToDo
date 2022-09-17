package uz.ilhomjon.restapi.retrofit

import retrofit2.http.*
import uz.ilhomjon.restapi.models.MyPostTodoRequest
import uz.ilhomjon.restapi.models.MyPostTodoResponse
import uz.ilhomjon.restapi.models.MyTodo

interface ApiService {

    @GET("plan")
    suspend fun getAllTodo():List<MyTodo>

    @POST("plan/")
    suspend fun addTodo(@Body myPostTodoRequest: MyPostTodoRequest):MyPostTodoResponse

    @PUT("plan/{id}/")
    suspend fun updateTodo(@Path("id") id:Int, @Body myPostTodoRequest: MyPostTodoRequest):MyPostTodoResponse

    @DELETE("plan/{id}/")
    suspend fun deleteTodo(@Path("id") id:Int)
}