package com.example.pokemongame.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Connector {
   suspend fun connect(url: URL) = withContext(Dispatchers.IO){
       val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
       try {
           //InputStream
           val inputStream: InputStream = urlConnection.inputStream
           //buffered reader
           val reader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
           //StringBuilder
           val stringBuilder: StringBuilder = java.lang.StringBuilder()
           var line: String? = reader.readLine()
           while (line != null) {
               stringBuilder.append(line)
               line = reader.readLine()
           }
           //JSON string
           return@withContext stringBuilder.toString()
       } catch (e: Exception) {
           println(e.printStackTrace())
       } finally {
           urlConnection.disconnect()
       }
    }
}