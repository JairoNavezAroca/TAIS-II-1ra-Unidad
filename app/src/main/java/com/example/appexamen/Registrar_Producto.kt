package com.example.appexamen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import com.example.appexamen.firebase.Conexion_Database
import com.example.appexamen.firebase.Conexion_Storage
import com.example.appexamen.modelo.Producto
import java.io.IOException


class Registrar_Producto : AppCompatActivity(){
    var producto: Producto? = null
    var conexion_database: Conexion_Database? = null
    var conexion_storage: Conexion_Storage? = null
    var button: Button? = null
    var txt_descripcion: EditText? = null
    var txt_categoria: EditText? = null
    var txt_stock: EditText? = null
    var txt_precio: EditText? = null
    var image_view: ImageView? = null
    var PICK_IMAGE = 1
    var bitmap: Bitmap? = null
    var archivo_extension: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        txt_descripcion = findViewById(R.id.txt_descripcion)
        txt_categoria = findViewById(R.id.txt_categoria)
        txt_stock = findViewById(R.id.txt_stock)
        txt_precio = findViewById(R.id.txt_precio)
        image_view = findViewById(R.id.image_view)

        producto = Producto()
        conexion_database = Conexion_Database(this)
        conexion_storage = Conexion_Storage(this)

        val bundle = intent.extras
        try {
            var producto_key = bundle?.getString("producto_key")!!
            producto = conexion_database!!.obtener_producto(producto_key){ producto_ ->
                producto = producto_
                mostrar_datos()
            }
        }
        catch(e: Exception){
            producto = Producto()
        }

        mostrar_datos()
        listener_boton_guardar()
        listener_boton_image()
    }

    fun listener_boton_image(){
        image_view?.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath: Uri? = data.data
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                //Configuración del mapa de bits en ImageView
                image_view?.setImageBitmap(bitmap)
                //archivo_extension = filePath.toString()/////////////////////////
                val contentResolver = contentResolver
                val mimeTypeMap = MimeTypeMap.getSingleton()
                archivo_extension = mimeTypeMap.getExtensionFromMimeType(filePath?.let { contentResolver.getType(it) }).toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun mostrar_datos(){
        txt_descripcion?.setText(producto?.descripcion)
        txt_categoria?.setText(producto?.categoria)
        txt_stock?.setText(producto?.stock.toString())
        txt_precio?.setText(producto?.precio.toString())
        producto?.foto?.let {
            if (it != "")
                conexion_storage?.traer_archivo(it){ bitmap ->
                    image_view?.setImageBitmap(bitmap)
                }
        }
    }
    fun listener_boton_guardar(){
        button?.setOnClickListener{
            if (bitmap == null){
                bitmap = createBitmap(1,2,Bitmap.Config.ALPHA_8)
            }
            archivo_extension?.let { it2 ->
                conexion_storage?.enviar_archivo(bitmap!!, it2){ foto ->
                    if (foto != "")
                        producto?.foto = foto
                    producto?.descripcion = txt_descripcion?.text.toString()
                    producto?.categoria = txt_categoria?.text.toString()
                    producto?.stock = txt_stock?.text.toString().toInt()
                    producto?.precio = txt_precio?.text.toString().toDouble()
                    if (producto?.key == "")
                        producto?.let { it1 -> conexion_database!!.registrar_producto(it1) }
                    else
                        producto?.let { it1 -> conexion_database!!.editar_producto(it1) }
                }
            }
        }
    }
}