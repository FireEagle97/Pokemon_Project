package com.example.pokemongame.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Connecter {
   suspend fun connect(url: URL) = withContext(Dispatchers.IO){
       val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
       try {
           val input: InputStream = BufferedInputStream(urlConnection.getInputStream())
           readStream(input)
       } finally {
           urlConnection.disconnect()
       }
    }
}