package uz.ilhomjon.restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import uz.ilhomjon.restapi.adapters.TodoAllAdapter
import uz.ilhomjon.restapi.databinding.ActivityMainBinding
import uz.ilhomjon.restapi.databinding.ItemDialogBinding
import uz.ilhomjon.restapi.models.MyPostTodoRequest
import uz.ilhomjon.restapi.models.MyTodo
import uz.ilhomjon.restapi.repository.TodoRepository
import uz.ilhomjon.restapi.retrofit.ApiClient
import uz.ilhomjon.restapi.retrofit.ApiService
import uz.ilhomjon.restapi.utils.Status
import uz.ilhomjon.restapi.viewmodel.MyViewModelFactory
import uz.ilhomjon.restapi.viewmodel.TodoViewModel

class MainActivity : AppCompatActivity(), TodoAllAdapter.RvClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoViewModel: TodoViewModel
    private val TAG = "MainActivity"
    private lateinit var todoAllAdapter: TodoAllAdapter
    private lateinit var todoRepository: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoRepository = TodoRepository(ApiClient.getApiService())
        todoViewModel = ViewModelProvider(this, MyViewModelFactory(todoRepository)).get(TodoViewModel::class.java)
        todoAllAdapter = TodoAllAdapter(rvClick = this)
        binding.rv.adapter = todoAllAdapter

        todoViewModel.getAllTodo()
            .observe(this){
                when(it.status){
                    Status.LOADING ->{
                        Log.d(TAG, "onCreate: Loading")
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR->{
                        Log.d(TAG, "onCreate: Error ${it.message}")
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS ->{
                        Log.d(TAG, "onCreate: ${it.data}")
                        todoAllAdapter.list = it?.data!!
                        todoAllAdapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
            dialog.setView(itemDialogBinding.root)

            itemDialogBinding.apply {

                btnSave.setOnClickListener {
                    val myPostTodoRequest = MyPostTodoRequest(
                        spinnerStatus.selectedItem.toString(),
                        edtAbout.text.toString().trim(),
                        edtDeadline.text.toString().trim(),
                        edtTitle.text.toString().trim()
                    )

                    todoViewModel.addMyTodo(myPostTodoRequest).observe(this@MainActivity){
                        when(it.status){
                            Status.LOADING ->{
                                progressPost.visibility = View.VISIBLE
                                linerDialog.isEnabled = false
                            }
                            Status.ERROR->{
                                Toast.makeText(this@MainActivity,
                                    "Xatolik, ${it.message}",
                                    Toast.LENGTH_SHORT).show()
                                progressPost.visibility = View.GONE
                                linerDialog.isEnabled = true
                            }
                            Status.SUCCESS->{
                                Toast.makeText(this@MainActivity,
                                    "${it.data?.sarlavha} ${it.data?.id} ga saqlandi",
                                    Toast.LENGTH_SHORT).show()
                                dialog.cancel()
                            }
                        }
                    }
                }

            }

            dialog.show()
        }
    }

    override fun menuCLick(imageView: ImageView, myTodo: MyTodo) {
        val popup = PopupMenu(this, imageView)
        popup.inflate(R.menu.todo_menu)

        popup.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.menu_edit-> {
                    editTodo(myTodo)
                }
                R.id.menu_delete->{
                    deleteTodo(myTodo)
                }
            }

            true
        }
        popup.show()
    }

    private fun editTodo(myTodo: MyTodo){
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
        itemDialogBinding.apply {
            edtTitle.setText(myTodo.sarlavha)
            edtAbout.setText(myTodo.matn)
            edtDeadline.setText(myTodo.oxirgi_muddat)

            when(myTodo.holat){
                "Yangi"->spinnerStatus.setSelection(0)
                "Bajarilmoqda"->spinnerStatus.setSelection(1)
                "Tugallandi"->spinnerStatus.setSelection(2)
            }

            btnSave.setOnClickListener {

                val myPostTodoRequest = MyPostTodoRequest(
                    spinnerStatus.selectedItem.toString(),
                    edtAbout.text.toString().trim(),
                    edtDeadline.text.toString().trim(),
                    edtTitle.text.toString()
                )

                todoViewModel.updateMyTodo(myTodo.id, myPostTodoRequest)
                    .observe(this@MainActivity){
                        when(it.status){
                            Status.ERROR->{
                                progressPost.visibility = View.INVISIBLE
                                linerDialog.isEnabled = true
                                Toast.makeText(this@MainActivity,
                                    "Error ${it.message}",
                                    Toast.LENGTH_LONG).show()
                            }
                            Status.LOADING->{
                                progressPost.visibility = View.VISIBLE
                                linerDialog.isEnabled = false
                            }
                            Status.SUCCESS->{
                                Toast.makeText(this@MainActivity,
                                    "${it.data?.sarlavha} ${it.data?.id} bilan saqlandi",
                                    Toast.LENGTH_SHORT).show()
                                dialog.cancel()
                            }
                        }
                    }
            }

        }

        dialog.setView(itemDialogBinding.root)
        dialog.show()
    }

    private fun deleteTodo(myTodo: MyTodo){
        todoViewModel.deleteTodo(myTodo.id)
    }
}