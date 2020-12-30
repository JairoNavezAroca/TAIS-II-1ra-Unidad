package com.example.appexamen.operaciones

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Operaciones {
    companion object{
        @SuppressLint("SimpleDateFormat")
        fun obtenerFechaConFormato(formato: String?, zonaHoraria: String?): String {
            val calendar: Calendar = Calendar.getInstance()
            val date: Date = calendar.getTime()
            val sdf: SimpleDateFormat
            sdf = SimpleDateFormat(formato)
            sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
            return sdf.format(date)
        }
    }
}