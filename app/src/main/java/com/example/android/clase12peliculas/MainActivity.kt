package com.example.android.clase12peliculas

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import com.google.gson.GsonBuilder

import okhttp3.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBuscar.setOnClickListener {
            var url = "https://swapi.co/api/films/?format=json"
            val request = Request.Builder().url(url).build()
            val cliente = OkHttpClient()

            cliente.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call?, e: IOException?) {
                }

                override fun onResponse(call: okhttp3.Call?, response: Response?) {
                    val respuesta = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val movie_response = gson.fromJson(respuesta, MovieApiResponse::class.java)
                //    val pelicula = gson.fromJson(respuesta, Pelicula::class.java)


                    runOnUiThread {
                        val adaptador = CustomAdapter(this@MainActivity,
                            R.layout.activity_pelicula,movie_response.results)
                        lvLista.adapter = adaptador
                    }
                }

            })
        }
    }

    class CustomAdapter(
        var miContexto: Context,
        var miRecurso:Int,
        var miLista: ArrayList<Pelicula>):
        ArrayAdapter<Pelicula>(miContexto,miRecurso,miLista) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var v = LayoutInflater.from(miContexto).inflate(miRecurso, null)
            var pelicula = v.findViewById<TextView>(R.id.lblTitulo)
            pelicula.text = miLista[position].title.toString()
            return v
        }
    }
}
