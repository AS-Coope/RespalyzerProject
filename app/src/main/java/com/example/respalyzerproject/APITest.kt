package com.example.respalyzerproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class APITest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apitest)

        val dataDisplay = findViewById<TextView>(R.id.tvDisplayData)

        val client = OkHttpClient()

        fun run() {
            val request = Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        dataDisplay.text = "Text Switch"
                        for ((name, value) in response.headers) {
                            println("$name: $value")
                        }

                        println(response.body!!.string())
                    }
                }
            })
        }
    }
}