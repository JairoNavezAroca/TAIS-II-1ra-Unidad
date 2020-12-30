package com.example.appexamen.firebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.example.appexamen.modelo.Producto
import com.example.appexamen.operaciones.Operaciones
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Conexion_Database {
    var firebase: Firebase? = null
    var context: AppCompatActivity? = null

    constructor(context: AppCompatActivity){
        this.context = context
        Firebase.setAndroidContext(context)
        firebase = Firebase("https://examen1raunidad-default-rtdb.firebaseio.com/")
    }

    //firebase!!.child("1").setValue(Clase())
    //firebase!!.child("2").setValue(Clase())
    //var s = firebase!!.key
    //var x = firebase!!.child("1")
    //firebase!!.child("users").child(userId).child("username").setValue(name)

    fun obtener_productos(funcion: (ArrayList<Producto>) -> Unit){
        firebase!!
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var productos: ArrayList<Producto> = ArrayList<Producto>()
                    for (child: DataSnapshot in snapshot.children) {
                        val p: Producto = child.getValue(Producto::class.java)
                        p.key = child.key
                        productos.add(p)
                    }
                    funcion(productos)
                }
                override fun onCancelled(error: FirebaseError?) {
                }
            })
    }

    fun obtener_producto(key: String, funcion: (Producto) -> Unit):Producto {
        //var fire =  firebase!!.child(key)
        //var producto: Producto = dataSnapshot.getValue(Producto::class.java)
        //return producto
        //val xx = 2

        firebase?.child(key)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var producto: Producto = Producto()
                    producto.key = key
                    producto.descripcion = dataSnapshot.child("descripcion").getValue(String::class.java)
                    producto.categoria = dataSnapshot.child("categoria").getValue(String::class.java)
                    producto.stock = dataSnapshot.child("stock").getValue(Int::class.java)
                    producto.precio = dataSnapshot.child("precio").getValue(Double::class.java)
                    producto.foto = dataSnapshot.child("foto").getValue(String::class.java)
                    funcion(producto)
                }
            }
            override fun onCancelled(p0: FirebaseError?) {
                TODO("Not yet implemented")
            }
        })
        return Producto()
    }

    fun registrar_producto(producto: Producto) {
        val formato = "yyyy-MM-dd_HH:mm:ss"
        val key: String = Operaciones.obtenerFechaConFormato(formato, "America/Lima")
        producto.key = key
        firebase!!.child(key).setValue(producto)
        context?.finish()
    }

    fun editar_producto(producto: Producto){
        firebase!!.child(producto.key).setValue(producto)
        context?.finish()
    }

    fun eliminar_producto(producto: Producto){
        firebase!!.child(producto.key).removeValue()
    }
}