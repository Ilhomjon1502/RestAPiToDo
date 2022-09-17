package uz.ilhomjon.restapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.ilhomjon.restapi.models.MyPostTodoRequest
import uz.ilhomjon.restapi.models.MyPostTodoResponse
import uz.ilhomjon.restapi.models.MyTodo
import uz.ilhomjon.restapi.repository.TodoRepository
import uz.ilhomjon.restapi.retrofit.ApiClient
import uz.ilhomjon.restapi.utils.Resource

class TodoViewModel(val todoRepository: TodoRepository) : ViewModel(){

    private val liveData = MutableLiveData<Resource<List<MyTodo>>>()
    fun getAllTodo():MutableLiveData<Resource<List<MyTodo>>>{

        viewModelScope.launch {

            liveData.postValue(Resource.loading("loading"))
            try {
                coroutineScope {
                    val list = async {
                        todoRepository.getAllTodo()
                    }.await()
                    liveData.postValue(Resource.success(list))
                }
            }catch (e:Exception){
                liveData.postValue(Resource.error(e.message))
            }
        }

        return liveData
    }

    private val postLiveData = MutableLiveData<Resource<MyPostTodoResponse>>()
    fun addMyTodo(myPostTodoRequest: MyPostTodoRequest) : MutableLiveData<Resource<MyPostTodoResponse>>{
        viewModelScope.launch {
            postLiveData.postValue(Resource.loading("Loading post"))
            try {
                coroutineScope {
                    val response = async {
                        todoRepository.addTodo(myPostTodoRequest)
                    }.await()
                    postLiveData.postValue(Resource.success(response))
                    getAllTodo()
                }
            }catch (e:Exception){
                postLiveData.postValue(Resource.error(e.message))
            }
        }

        return postLiveData
    }

    private val liveDataUpdate = MutableLiveData<Resource<MyPostTodoResponse>>()
    fun updateMyTodo(id:Int, myPostTodoRequest: MyPostTodoRequest):MutableLiveData<Resource<MyPostTodoResponse>>{

        viewModelScope.launch {
            liveDataUpdate.postValue(Resource.loading("Loading update"))
            try {
                coroutineScope {
                    val response = async {
                        todoRepository.updateTodo(id, myPostTodoRequest)
                    }.await()
                    liveDataUpdate.postValue(Resource.success(response))
                    getAllTodo()
                }
            }catch (e:Exception){
                liveDataUpdate.postValue(Resource.error(e.message))
            }
        }

        return liveDataUpdate
    }

    fun deleteTodo(id:Int){
        viewModelScope.launch {

            try {
                coroutineScope {
                   launch {
                        todoRepository.deleteTodo(id)
                    }
                    getAllTodo()
                }
            }catch (e:Exception){

            }
        }
    }
}