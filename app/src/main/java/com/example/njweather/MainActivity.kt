package com.example.njweather

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    //URL погоды для получения JSON
    var weather_url1 = ""
    //API-идентификатор для URL-адреса
    var api_id1 = "6432d1c22b32445a809871688125500b"
    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //свяжите textView, в котором будет отображаться температура
        textView = findViewById(R.id.textView)
        //создать экземпляр Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url1)
        //при нажатии этой кнопки будет вызвана функция получения координат
        btVar1.setOnClickListener {
            Log.e("lat", "onClick")
            //функция для поиска координат последнего местоположения
            obtainLocation()
        }

    }
    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        Log.e("lat", "function")
        //дает последнию локацию
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //получить широту и долготу и создать URL-адрес http
                weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude +"&lon="+ location?.longitude + "&key="+ api_id1
                Log.e("lat", weather_url1.toString())
                //эта функция будет извлекать данные из URL
                getTemp()
            }
    }

    fun getTemp() {
        //Создайте экземпляр RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Запросить строковый ответ с предоставленного URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("lat", response.toString())
                //получить объект JSON
                val obj = JSONObject(response)
                //получить массив из объекта имени - "данные"
                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())
                //получить объект JSON из массива в позиции индекса 0
                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())
                //установить температуру и название города с помощью функции getString ()
                textView.text = obj2.getString("temp")+" град Цельсия в "+obj2.getString("city_name")
            },
            //В случае ошибки
            Response.ErrorListener { textView!!.text = "That didn't work!" })
        queue.add(stringReq)
    }
}
