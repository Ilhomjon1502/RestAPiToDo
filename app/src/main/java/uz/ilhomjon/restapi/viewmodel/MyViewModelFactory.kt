package uz.ilhomjon.restapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.ilhomjon.restapi.repository.TodoRepository
import java.lang.IllegalArgumentException

class MyViewModelFactory (val todoRepository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TodoViewModel::class.java)){
            return TodoViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Error")
    }
}