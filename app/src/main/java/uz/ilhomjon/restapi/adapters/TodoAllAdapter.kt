package uz.ilhomjon.restapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.ilhomjon.restapi.databinding.ItemRvBinding
import uz.ilhomjon.restapi.models.MyTodo

class TodoAllAdapter(var list:List<MyTodo> = emptyList(), val rvClick: RvClick): RecyclerView.Adapter<TodoAllAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding):RecyclerView.ViewHolder(itemRvBinding.root){

        fun onBind(myTodo: MyTodo, position: Int){
            itemRvBinding.itemTvName.text = myTodo.sarlavha
            itemRvBinding.itemTvAbout.text = myTodo.matn
            itemRvBinding.itemTvHolati.text = myTodo.holat
            itemRvBinding.itemTvMuddat.text = myTodo.oxirgi_muddat

            itemRvBinding.imageMore.setOnClickListener {
                rvClick.menuCLick(itemRvBinding.imageMore, myTodo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface RvClick{
        fun menuCLick(imageView: ImageView, myTodo: MyTodo)
    }
}