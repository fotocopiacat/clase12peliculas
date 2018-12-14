package com.example.android.clase12peliculas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.activity_pelicula.*

import okhttp3.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //var v : View = findViewById<View>(R.layout.activity_main)
        var peliBuscar: String = findViewById<EditText>(R.id.editPeli).text.toString()

        btnBuscar.setOnClickListener {
            var url = "http://swapi.co/api/films/"
            val request = Request.Builder().url(url).build()
            val cliente = OkHttpClient()

            cliente.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call?, e: IOException?) {
                }

                override fun onResponse(call: okhttp3.Call?, response: Response?) {
                    val respuesta = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val pelicula = gson.fromJson(respuesta, Pelicula::class.java)
                   val historia = gson.fromJson(respuesta, Results::class.java)

                    runOnUiThread {
                        val adaptador = CustomAdapter(this@MainActivity, R.layout.activity_lista,pelicula.title)
                        lvLista.adapter = adaptador
                    }
                }
            })
        }
    }

    class CustomAdapter(
        var miContexto:Context,
        var miRecurso:Int,
        var miLista: String
    ):
        ArrayAdapter<Pelicula>(miContexto,miRecurso,miLista) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v = LayoutInflater.from(miContexto).inflate(miRecurso, null)
            var buscapelicula = v.findViewById<TextView>(R.id.editPeli).text.toString()
            buscapelicula = miLista[position].toString()
            return v
        }
    }
}
