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
                .url("http://192.168.100.73:8080/")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle failure
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }

                    val responseBody = response.body?.string() ?: ""
                    runOnUiThread {
                        dataDisplay.text = responseBody
                    }
                }
            })
        }

        run()
    }
}