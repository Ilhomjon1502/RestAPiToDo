package uz.ilhomjon.restapi.repository

import uz.ilhomjon.restapi.models.MyPostTodoRequest
import uz.ilhomjon.restapi.retrofit.ApiService

class TodoRepository(val apiService: ApiService) {
    suspend fun getAllTodo() = apiService.getAllTodo()
    suspend fun addTodo(myPostTodoRequest: MyPostTodoRequest) = apiService.addTodo(myPostTodoRequest)
    suspend fun updateTodo(id:Int, myPostTodoRequest: MyPostTodoRequest) = apiService.updateTodo(id, myPostTodoRequest)
    suspend fun deleteTodo(id:Int) = apiService.deleteTodo(id)
}