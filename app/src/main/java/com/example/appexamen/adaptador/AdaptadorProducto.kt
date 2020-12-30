package com.example.appexamen.adaptador

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appexamen.Mostrar_producto
import com.example.appexamen.R
import com.example.appexamen.firebase.Conexion_Storage
import com.example.appexamen.modelo.Producto


class AdaptadorProducto(private val lista_productos: List<Producto>, var clickListener: Mostrar_producto) : RecyclerView.Adapter<AdaptadorProducto.ViewHolder>() {
    companion object{
        var EDITAR: Int = 1
        var ELIMINAR: Int = 2
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val txt_descripcion = itemView.findViewById<TextView>(R.id.txt_descripcion)
        val txt_categoria = itemView.findViewById<TextView>(R.id.txt_categoria)
        val txt_precio = itemView.findViewById<TextView>(R.id.txt_precio)
        val txt_stock = itemView.findViewById<TextView>(R.id.txt_stock)
        val linear_layout = itemView.findViewById<LinearLayout>(R.id.linear_layout)
        val image_view = itemView.findViewById<ImageView>(R.id.image_view)

        fun initialize(item: Producto, action: onProductoItemClickListener){
            linear_layout.setOnClickListener {
                action.onItemClick(item, adapterPosition, AdaptadorProducto.EDITAR)
            }
            linear_layout.setOnLongClickListener{
                action.onLongItemClick(item, adapterPosition, AdaptadorProducto.ELIMINAR)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val elemento_producto = inflater.inflate(R.layout.activity_elemento_producto, parent, false)
        return ViewHolder(elemento_producto)
    }

    override fun getItemCount(): Int {
        return lista_productos.count()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val producto: Producto = lista_productos.get(position)
        val txt_descripcion = viewHolder.txt_descripcion
        txt_descripcion.setText(producto.descripcion)
        val txt_categoria = viewHolder.txt_categoria
        txt_categoria.setText("Categoria: "+producto.categoria)
        val txt_precio = viewHolder.txt_precio
        txt_precio.setText("Precio s/: "+producto.precio.toString())
        val txt_stock = viewHolder.txt_stock
        txt_stock.setText("Stock: "+producto.stock.toString()+" unidades")
        val image_view = viewHolder.image_view
        var conexion_storage: Conexion_Storage? = null
        producto?.foto?.let {
            if (it != "")
                conexion_storage = Conexion_Storage(clickListener)
                conexion_storage?.traer_archivo(it){ bitmap ->
                    image_view?.setImageBitmap(bitmap)
                }
        }
        viewHolder.initialize(lista_productos[position], clickListener)
    }

}

interface onProductoItemClickListener{
    fun onItemClick(item: Producto, position: Int, accion: Int)
    fun onLongItemClick(item: Producto, position: Int, accion: Int): Boolean
}