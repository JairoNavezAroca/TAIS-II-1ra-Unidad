package com.example.appexamen.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import com.example.appexamen.operaciones.Operaciones
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


public class Conexion_Storage{
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    constructor(context: AppCompatActivity){
        //https://code.tutsplus.com/es/tutorials/firebase-for-android-file-storage--cms-27376#
        storage = FirebaseStorage.getInstance()
    }

    fun enviar_archivo(bitmap: Bitmap, archivo_extension: String, funcion: (String) -> Unit){
        if (archivo_extension == ""){
            funcion("")
            return
        }
        val outputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val data: ByteArray = outputStream.toByteArray()

        //var extension_archivo = ruta_archivo
        //extension_archivo = extension_archivo.split(".").last()

        val formato = "yyyy-MM-dd_HH:mm:ss"
        val nombre_archivo: String = Operaciones.obtenerFechaConFormato(formato, "America/Lima")+"."+archivo_extension

        storageReference = storage!!.getReferenceFromUrl("gs://examen1raunidad.appspot.com").child(nombre_archivo)

        val uploadTask = storageReference!!.putBytes(data)
        uploadTask.addOnFailureListener {
            var s = 2
        }.addOnSuccessListener {
            funcion(nombre_archivo)
        }
    }

    fun traer_archivo(nombre_archivo:String, funcion: (Bitmap) -> Unit){
        storageReference = storage!!.getReferenceFromUrl("gs://examen1raunidad.appspot.com").child(nombre_archivo)
        try {
            val localFile: File = File.createTempFile("images", "jpg")
            storageReference!!.getFile(localFile)
                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
                    val bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
                    funcion(bitmap)
                }).addOnFailureListener(OnFailureListener { })
        } catch (e: IOException) {
        }
    }
}

