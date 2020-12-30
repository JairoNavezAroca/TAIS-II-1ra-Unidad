package com.example.appexamen

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appexamen.adaptador.AdaptadorProducto
import com.example.appexamen.adaptador.onProductoItemClickListener
import com.example.appexamen.firebase.Conexion_Database
import com.example.appexamen.modelo.Producto


class Mostrar_producto : AppCompatActivity(), onProductoItemClickListener {

    var conexion_database: Conexion_Database? = null

    var boton_agregar: Button? = null
    var recycler_view: RecyclerView? = null

    override fun onResume() {
        super.onResume()
        conexion_database!!.obtener_productos { productos: ArrayList<Producto> ->
            //var adapter = AdaptadorProducto(productos, this)
            //recycler_view?.adapter = adapter
            //recycler_view?.layoutManager = LinearLayoutManager(this)

            val adapter = AdaptadorProducto(productos, this)
            recycler_view?.setHasFixedSize(true)
            recycler_view?.setAdapter(adapter)
            recycler_view?.setLayoutManager(LinearLayoutManager(this))
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_producto)
        boton_agregar = findViewById(R.id.button3)
        recycler_view = findViewById(R.id.recycler_view)
        conexion_database = Conexion_Database(this)
        listener_boton_agregar()
    }

    fun listener_boton_agregar(){
        //Agregar
        boton_agregar?.setOnClickListener{
            val intent: Intent = Intent(this, Registrar_Producto::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(item: Producto, position: Int, accion: Int) {
        //Editar
        val intent: Intent = Intent(this, Registrar_Producto::class.java)
        intent.putExtra("producto_key", item.key)
        startActivity(intent)
    }

    override fun onLongItemClick(item: Producto, position: Int, accion: Int): Boolean {
        val dialogo1: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogo1.setTitle("Confirmar eliminar")
        dialogo1.setMessage("¿Seguro que desea eliminar este producto?")
        dialogo1.setCancelable(false)
        dialogo1.setPositiveButton("Confirmar", DialogInterface.OnClickListener { dialogo1, id ->
            conexion_database!!.eliminar_producto(item)
        })
        dialogo1.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogo1, id ->
        })
        dialogo1.show()
        return true
    }
}

