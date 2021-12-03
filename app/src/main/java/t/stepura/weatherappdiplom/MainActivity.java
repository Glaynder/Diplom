package t.stepura.weatherappdiplom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import android.os.Handler;

import t.stepura.weatherappdiplom.db.MyDbManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;  //Объявляем элменты из activity_main
    TextView tvResult;

    private final String url = "http://api.openweathermap.org/data/2.5/weather"; //Создаём переменные запроса (url) и ключа (appid)
    private final String url2 = "http://api.openweathermap.org/data/2.5/forecast";  //Переменная запроса на получение прогноза на 5 дней
    private final String appid = "64a6d4b786aa5b620f0e8bd23c6413c4";
    DecimalFormat df = new DecimalFormat( "#");    //Задаём формат десятичных чисел

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);              //Инициализируем элементы
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        myDbManager = new MyDbManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();  //Когда запустится activity откроется база данных
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";  //Определяем строку для удерживания полного URL-адреса
        String city = etCity.getText().toString().trim();   //Получаем текст из текстовых полей
        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("Поле не может быть пустым!");  //Показывает текст ошибки, если поле "city" пустое
        }else{
            if(!country.equals("")){
                tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;  //Или если поле "country" не пустое. Если да, то url продолжается так
            }else{
                tempUrl = url + "?q=" + city + "&appid=" + appid;  //Если нет, то так
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {  //создаём экземпляр объекта строковго запроса который является типом HTTP-запроса                @Override
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);  //создаём оператор для печати ответа в журнал логов
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        tvResult.setTextColor(Color.rgb(68, 134, 199));
                        output += " Текущая погода"
                                + "\n " + cityName + " (" + countryName + ")"
                                + "\n"
                                + "\n Температура: " + "--------------- " + df.format(temp) + " C"
                                + "\n Ощущается как: " + "----------- "  + df.format(feelsLike) + " C"
                                + "\n Влажность: " + "------------------ " + humidity + "%"
                                + "\n Скорость ветра: " + "----------- " + wind + "m/s"
                                + "\n Облачность: " + "---------------- " + clouds + "%"
                                + "\n Давление: " + "-------------- " + pressure + " hPa";
                        tvResult.setText(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();  //делаем сообщение об ошибке в виде всплывающего уведомления
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());  //создаём экземпляр очереди запросов
            requestQueue.add(stringRequest);  //добавляем строковый запрос в очередь запросов
        }
    }


    public void getWeatherDetailsonfivedays(View view) {
        String tempUrl2 = "";  //Определяем строку для удерживания полного URL-адреса
        String city2 = etCity.getText().toString().trim();   //Получаем текст из текстовых полей
        String country2 = etCountry.getText().toString().trim();
        if(city2.equals("")){
            tvResult.setText("Поле не может быть пустым!");  //Показывает текст ошибки, если поле "city" пустое
        }else{
            if(!country2.equals("")){
                tempUrl2 = url2 + "?q=" + city2 + "," + country2 + "&appid=" + appid;  //Или если поле "country" не пустое. Если да, то url продолжается так
            }else{
                tempUrl2 = url2 + "?q=" + city2 + "&appid=" + appid;  //Если нет, то так
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl2, new Response.Listener<String>() {  //создаём экземпляр объекта строковго запроса который является типом HTTP-запроса                @Override
                @Override
                public void onResponse(String response) {
                    Log.d("response", response);  //создаём оператор для печати ответа в журнал логов
                    String output1 = "";
                    try {

                        JSONObject jsonResponse1 = new JSONObject(response);
                        JSONObject CityFiveDays = jsonResponse1.getJSONObject("city");
                        String CountryNameFiveDays = CityFiveDays.getString("country");
                        String CityNameFiveDays = CityFiveDays.getString("name");
                        JSONArray ListFiveDays = jsonResponse1.getJSONArray("list");

                        // 0
                        JSONObject json0FiveDays0 = ListFiveDays.getJSONObject(0);
                        JSONObject MainFiveDays0 = json0FiveDays0.getJSONObject("main");
                        double TempFiveDays0 = MainFiveDays0.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays0 = MainFiveDays0.getDouble("feels_like") - 273.15;
                        float PressureFiveDays0 = MainFiveDays0.getInt("pressure");
                        int HumidityFiveDays0 = MainFiveDays0.getInt("humidity");
                        JSONObject WindFiveDays0 = json0FiveDays0.getJSONObject("wind");
                        String SpeedFiveDays0 = WindFiveDays0.getString("speed");
                        JSONObject CloudsFiveDays0 = json0FiveDays0.getJSONObject("clouds");
                        int AllFiveDays0 = CloudsFiveDays0.getInt("all");
                        String DateFiveDays0 = json0FiveDays0.getString("dt_txt");
                        String subStrDate0 = DateFiveDays0.substring(0, 10);
                        String subStrTime0 = DateFiveDays0.substring(11, 16);

                        // 1
                        JSONObject json0FiveDays1 = ListFiveDays.getJSONObject(1);
                        JSONObject MainFiveDays1 = json0FiveDays1.getJSONObject("main");
                        double TempFiveDays1 = MainFiveDays1.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays1 = MainFiveDays1.getDouble("feels_like") - 273.15;
                        float PressureFiveDays1 = MainFiveDays1.getInt("pressure");
                        int HumidityFiveDays1 = MainFiveDays1.getInt("humidity");
                        JSONObject WindFiveDays1 = json0FiveDays1.getJSONObject("wind");
                        String SpeedFiveDays1 = WindFiveDays1.getString("speed");
                        JSONObject CloudsFiveDays1 = json0FiveDays1.getJSONObject("clouds");
                        int AllFiveDays1 = CloudsFiveDays1.getInt("all");
                        String DateFiveDays1 = json0FiveDays1.getString("dt_txt");
                        String subStrDate1 = DateFiveDays1.substring(0, 10);
                        String subStrTime1 = DateFiveDays1.substring(11, 16);

                        //2
                        JSONObject json0FiveDays2 = ListFiveDays.getJSONObject(2);
                        JSONObject MainFiveDays2 = json0FiveDays2.getJSONObject("main");
                        double TempFiveDays2 = MainFiveDays2.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays2 = MainFiveDays2.getDouble("feels_like") - 273.15;
                        float PressureFiveDays2 = MainFiveDays2.getInt("pressure");
                        int HumidityFiveDays2 = MainFiveDays2.getInt("humidity");
                        JSONObject WindFiveDays2 = json0FiveDays2.getJSONObject("wind");
                        String SpeedFiveDays2 = WindFiveDays2.getString("speed");
                        JSONObject CloudsFiveDays2 = json0FiveDays2.getJSONObject("clouds");
                        int AllFiveDays2 = CloudsFiveDays2.getInt("all");
                        String DateFiveDays2 = json0FiveDays2.getString("dt_txt");
                        String subStrDate2 = DateFiveDays2.substring(0, 10);
                        String subStrTime2 = DateFiveDays2.substring(11, 16);

                        //3
                        JSONObject json0FiveDays3 = ListFiveDays.getJSONObject(3);
                        JSONObject MainFiveDays3 = json0FiveDays3.getJSONObject("main");
                        double TempFiveDays3 = MainFiveDays3.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays3 = MainFiveDays3.getDouble("feels_like") - 273.15;
                        float PressureFiveDays3 = MainFiveDays3.getInt("pressure");
                        int HumidityFiveDays3 = MainFiveDays3.getInt("humidity");
                        JSONObject WindFiveDays3 = json0FiveDays3.getJSONObject("wind");
                        String SpeedFiveDays3 = WindFiveDays3.getString("speed");
                        JSONObject CloudsFiveDays3 = json0FiveDays3.getJSONObject("clouds");
                        int AllFiveDays3 = CloudsFiveDays3.getInt("all");
                        String DateFiveDays3 = json0FiveDays3.getString("dt_txt");
                        String subStrDate3 = DateFiveDays3.substring(0, 10);
                        String subStrTime3 = DateFiveDays3.substring(11, 16);

                        //4
                        JSONObject json0FiveDays4 = ListFiveDays.getJSONObject(4);
                        JSONObject MainFiveDays4 = json0FiveDays4.getJSONObject("main");
                        double TempFiveDays4 = MainFiveDays4.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays4 = MainFiveDays4.getDouble("feels_like") - 273.15;
                        float PressureFiveDays4 = MainFiveDays4.getInt("pressure");
                        int HumidityFiveDays4 = MainFiveDays4.getInt("humidity");
                        JSONObject WindFiveDays4 = json0FiveDays4.getJSONObject("wind");
                        String SpeedFiveDays4 = WindFiveDays4.getString("speed");
                        JSONObject CloudsFiveDays4 = json0FiveDays4.getJSONObject("clouds");
                        int AllFiveDays4 = CloudsFiveDays4.getInt("all");
                        String DateFiveDays4 = json0FiveDays4.getString("dt_txt");
                        String subStrDate4 = DateFiveDays4.substring(0, 10);
                        String subStrTime4 = DateFiveDays4.substring(11, 16);

                        //5
                        JSONObject json0FiveDays5 = ListFiveDays.getJSONObject(5);
                        JSONObject MainFiveDays5 = json0FiveDays5.getJSONObject("main");
                        double TempFiveDays5 = MainFiveDays5.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays5 = MainFiveDays5.getDouble("feels_like") - 273.15;
                        float PressureFiveDays5 = MainFiveDays5.getInt("pressure");
                        int HumidityFiveDays5 = MainFiveDays5.getInt("humidity");
                        JSONObject WindFiveDays5 = json0FiveDays5.getJSONObject("wind");
                        String SpeedFiveDays5 = WindFiveDays5.getString("speed");
                        JSONObject CloudsFiveDays5 = json0FiveDays5.getJSONObject("clouds");
                        int AllFiveDays5 = CloudsFiveDays5.getInt("all");
                        String DateFiveDays5 = json0FiveDays5.getString("dt_txt");
                        String subStrDate5 = DateFiveDays5.substring(0, 10);
                        String subStrTime5 = DateFiveDays5.substring(11, 16);

                        //6
                        JSONObject json0FiveDays6 = ListFiveDays.getJSONObject(6);
                        JSONObject MainFiveDays6 = json0FiveDays6.getJSONObject("main");
                        double TempFiveDays6 = MainFiveDays6.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays6 = MainFiveDays6.getDouble("feels_like") - 273.15;
                        float PressureFiveDays6 = MainFiveDays6.getInt("pressure");
                        int HumidityFiveDays6 = MainFiveDays6.getInt("humidity");
                        JSONObject WindFiveDays6 = json0FiveDays6.getJSONObject("wind");
                        String SpeedFiveDays6 = WindFiveDays6.getString("speed");
                        JSONObject CloudsFiveDays6 = json0FiveDays6.getJSONObject("clouds");
                        int AllFiveDays6 = CloudsFiveDays6.getInt("all");
                        String DateFiveDays6 = json0FiveDays6.getString("dt_txt");
                        String subStrDate6 = DateFiveDays6.substring(0, 10);
                        String subStrTime6 = DateFiveDays6.substring(11, 16);

                        //7
                        JSONObject json0FiveDays7 = ListFiveDays.getJSONObject(7);
                        JSONObject MainFiveDays7 = json0FiveDays7.getJSONObject("main");
                        double TempFiveDays7 = MainFiveDays7.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays7 = MainFiveDays7.getDouble("feels_like") - 273.15;
                        float PressureFiveDays7 = MainFiveDays7.getInt("pressure");
                        int HumidityFiveDays7 = MainFiveDays7.getInt("humidity");
                        JSONObject WindFiveDays7 = json0FiveDays7.getJSONObject("wind");
                        String SpeedFiveDays7 = WindFiveDays7.getString("speed");
                        JSONObject CloudsFiveDays7 = json0FiveDays7.getJSONObject("clouds");
                        int AllFiveDays7 = CloudsFiveDays7.getInt("all");
                        String DateFiveDays7 = json0FiveDays7.getString("dt_txt");
                        String subStrDate7 = DateFiveDays7.substring(0, 10);
                        String subStrTime7 = DateFiveDays7.substring(11, 16);

                        //8
                        JSONObject json0FiveDays8 = ListFiveDays.getJSONObject(8);
                        JSONObject MainFiveDays8 = json0FiveDays8.getJSONObject("main");
                        double TempFiveDays8 = MainFiveDays8.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays8 = MainFiveDays8.getDouble("feels_like") - 273.15;
                        float PressureFiveDays8 = MainFiveDays8.getInt("pressure");
                        int HumidityFiveDays8 = MainFiveDays8.getInt("humidity");
                        JSONObject WindFiveDays8 = json0FiveDays8.getJSONObject("wind");
                        String SpeedFiveDays8 = WindFiveDays8.getString("speed");
                        JSONObject CloudsFiveDays8 = json0FiveDays8.getJSONObject("clouds");
                        int AllFiveDays8 = CloudsFiveDays8.getInt("all");
                        String DateFiveDays8 = json0FiveDays8.getString("dt_txt");
                        String subStrDate8 = DateFiveDays8.substring(0, 10);
                        String subStrTime8 = DateFiveDays8.substring(11, 16);

                        //9
                        JSONObject json0FiveDays9 = ListFiveDays.getJSONObject(9);
                        JSONObject MainFiveDays9 = json0FiveDays9.getJSONObject("main");
                        double TempFiveDays9 = MainFiveDays9.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays9 = MainFiveDays9.getDouble("feels_like") - 273.15;
                        float PressureFiveDays9 = MainFiveDays9.getInt("pressure");
                        int HumidityFiveDays9 = MainFiveDays9.getInt("humidity");
                        JSONObject WindFiveDays9 = json0FiveDays9.getJSONObject("wind");
                        String SpeedFiveDays9 = WindFiveDays9.getString("speed");
                        JSONObject CloudsFiveDays9 = json0FiveDays9.getJSONObject("clouds");
                        int AllFiveDays9 = CloudsFiveDays9.getInt("all");
                        String DateFiveDays9 = json0FiveDays9.getString("dt_txt");
                        String subStrDate9 = DateFiveDays9.substring(0, 10);
                        String subStrTime9 = DateFiveDays9.substring(11, 16);

                        //10
                        JSONObject json0FiveDays10 = ListFiveDays.getJSONObject(10);
                        JSONObject MainFiveDays10 = json0FiveDays10.getJSONObject("main");
                        double TempFiveDays10 = MainFiveDays10.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays10 = MainFiveDays10.getDouble("feels_like") - 273.15;
                        float PressureFiveDays10 = MainFiveDays10.getInt("pressure");
                        int HumidityFiveDays10 = MainFiveDays10.getInt("humidity");
                        JSONObject WindFiveDays10 = json0FiveDays10.getJSONObject("wind");
                        String SpeedFiveDays10 = WindFiveDays10.getString("speed");
                        JSONObject CloudsFiveDays10 = json0FiveDays10.getJSONObject("clouds");
                        int AllFiveDays10 = CloudsFiveDays10.getInt("all");
                        String DateFiveDays10 = json0FiveDays10.getString("dt_txt");
                        String subStrDate10 = DateFiveDays10.substring(0, 10);
                        String subStrTime10 = DateFiveDays10.substring(11, 16);

                        //11
                        JSONObject json0FiveDays11 = ListFiveDays.getJSONObject(11);
                        JSONObject MainFiveDays11 = json0FiveDays11.getJSONObject("main");
                        double TempFiveDays11 = MainFiveDays11.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays11 = MainFiveDays11.getDouble("feels_like") - 273.15;
                        float PressureFiveDays11 = MainFiveDays11.getInt("pressure");
                        int HumidityFiveDays11 = MainFiveDays11.getInt("humidity");
                        JSONObject WindFiveDays11 = json0FiveDays11.getJSONObject("wind");
                        String SpeedFiveDays11 = WindFiveDays11.getString("speed");
                        JSONObject CloudsFiveDays11 = json0FiveDays11.getJSONObject("clouds");
                        int AllFiveDays11 = CloudsFiveDays11.getInt("all");
                        String DateFiveDays11 = json0FiveDays11.getString("dt_txt");
                        String subStrDate11 = DateFiveDays11.substring(0, 10);
                        String subStrTime11 = DateFiveDays11.substring(11, 16);

                        //12
                        JSONObject json0FiveDays12 = ListFiveDays.getJSONObject(12);
                        JSONObject MainFiveDays12 = json0FiveDays12.getJSONObject("main");
                        double TempFiveDays12 = MainFiveDays12.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays12 = MainFiveDays12.getDouble("feels_like") - 273.15;
                        float PressureFiveDays12 = MainFiveDays12.getInt("pressure");
                        int HumidityFiveDays12 = MainFiveDays12.getInt("humidity");
                        JSONObject WindFiveDays12 = json0FiveDays12.getJSONObject("wind");
                        String SpeedFiveDays12 = WindFiveDays12.getString("speed");
                        JSONObject CloudsFiveDays12 = json0FiveDays12.getJSONObject("clouds");
                        int AllFiveDays12 = CloudsFiveDays12.getInt("all");
                        String DateFiveDays12 = json0FiveDays12.getString("dt_txt");
                        String subStrDate12 = DateFiveDays12.substring(0, 10);
                        String subStrTime12 = DateFiveDays12.substring(11, 16);

                        //13
                        JSONObject json0FiveDays13 = ListFiveDays.getJSONObject(13);
                        JSONObject MainFiveDays13 = json0FiveDays13.getJSONObject("main");
                        double TempFiveDays13 = MainFiveDays13.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays13 = MainFiveDays13.getDouble("feels_like") - 273.15;
                        float PressureFiveDays13 = MainFiveDays13.getInt("pressure");
                        int HumidityFiveDays13 = MainFiveDays13.getInt("humidity");
                        JSONObject WindFiveDays13 = json0FiveDays13.getJSONObject("wind");
                        String SpeedFiveDays13 = WindFiveDays13.getString("speed");
                        JSONObject CloudsFiveDays13 = json0FiveDays13.getJSONObject("clouds");
                        int AllFiveDays13 = CloudsFiveDays13.getInt("all");
                        String DateFiveDays13 = json0FiveDays13.getString("dt_txt");
                        String subStrDate13 = DateFiveDays13.substring(0, 10);
                        String subStrTime13 = DateFiveDays13.substring(11, 16);

                        //14
                        JSONObject json0FiveDays14 = ListFiveDays.getJSONObject(14);
                        JSONObject MainFiveDays14 = json0FiveDays14.getJSONObject("main");
                        double TempFiveDays14 = MainFiveDays14.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays14 = MainFiveDays14.getDouble("feels_like") - 273.15;
                        float PressureFiveDays14 = MainFiveDays14.getInt("pressure");
                        int HumidityFiveDays14 = MainFiveDays14.getInt("humidity");
                        JSONObject WindFiveDays14 = json0FiveDays14.getJSONObject("wind");
                        String SpeedFiveDays14 = WindFiveDays14.getString("speed");
                        JSONObject CloudsFiveDays14 = json0FiveDays14.getJSONObject("clouds");
                        int AllFiveDays14 = CloudsFiveDays14.getInt("all");
                        String DateFiveDays14 = json0FiveDays14.getString("dt_txt");
                        String subStrDate14 = DateFiveDays14.substring(0, 10);
                        String subStrTime14 = DateFiveDays14.substring(11, 16);

                        //15
                        JSONObject json0FiveDays15 = ListFiveDays.getJSONObject(15);
                        JSONObject MainFiveDays15 = json0FiveDays15.getJSONObject("main");
                        double TempFiveDays15 = MainFiveDays15.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays15 = MainFiveDays15.getDouble("feels_like") - 273.15;
                        float PressureFiveDays15 = MainFiveDays15.getInt("pressure");
                        int HumidityFiveDays15 = MainFiveDays15.getInt("humidity");
                        JSONObject WindFiveDays15 = json0FiveDays15.getJSONObject("wind");
                        String SpeedFiveDays15 = WindFiveDays15.getString("speed");
                        JSONObject CloudsFiveDays15 = json0FiveDays15.getJSONObject("clouds");
                        int AllFiveDays15 = CloudsFiveDays15.getInt("all");
                        String DateFiveDays15 = json0FiveDays15.getString("dt_txt");
                        String subStrDate15 = DateFiveDays15.substring(0, 10);
                        String subStrTime15 = DateFiveDays15.substring(11, 16);

                        //16
                        JSONObject json0FiveDays16 = ListFiveDays.getJSONObject(16);
                        JSONObject MainFiveDays16 = json0FiveDays16.getJSONObject("main");
                        double TempFiveDays16 = MainFiveDays16.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays16 = MainFiveDays16.getDouble("feels_like") - 273.15;
                        float PressureFiveDays16 = MainFiveDays16.getInt("pressure");
                        int HumidityFiveDays16 = MainFiveDays16.getInt("humidity");
                        JSONObject WindFiveDays16 = json0FiveDays16.getJSONObject("wind");
                        String SpeedFiveDays16 = WindFiveDays16.getString("speed");
                        JSONObject CloudsFiveDays16 = json0FiveDays16.getJSONObject("clouds");
                        int AllFiveDays16 = CloudsFiveDays16.getInt("all");
                        String DateFiveDays16 = json0FiveDays16.getString("dt_txt");
                        String subStrDate16 = DateFiveDays16.substring(0, 10);
                        String subStrTime16 = DateFiveDays16.substring(11, 16);

                        //17
                        JSONObject json0FiveDays17 = ListFiveDays.getJSONObject(17);
                        JSONObject MainFiveDays17 = json0FiveDays17.getJSONObject("main");
                        double TempFiveDays17 = MainFiveDays17.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays17 = MainFiveDays17.getDouble("feels_like") - 273.15;
                        float PressureFiveDays17 = MainFiveDays17.getInt("pressure");
                        int HumidityFiveDays17 = MainFiveDays17.getInt("humidity");
                        JSONObject WindFiveDays17 = json0FiveDays17.getJSONObject("wind");
                        String SpeedFiveDays17 = WindFiveDays17.getString("speed");
                        JSONObject CloudsFiveDays17 = json0FiveDays17.getJSONObject("clouds");
                        int AllFiveDays17 = CloudsFiveDays17.getInt("all");
                        String DateFiveDays17 = json0FiveDays17.getString("dt_txt");
                        String subStrDate17 = DateFiveDays17.substring(0, 10);
                        String subStrTime17 = DateFiveDays17.substring(11, 16);

                        //18
                        JSONObject json0FiveDays18 = ListFiveDays.getJSONObject(18);
                        JSONObject MainFiveDays18 = json0FiveDays18.getJSONObject("main");
                        double TempFiveDays18 = MainFiveDays18.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays18 = MainFiveDays18.getDouble("feels_like") - 273.15;
                        float PressureFiveDays18 = MainFiveDays18.getInt("pressure");
                        int HumidityFiveDays18 = MainFiveDays18.getInt("humidity");
                        JSONObject WindFiveDays18 = json0FiveDays18.getJSONObject("wind");
                        String SpeedFiveDays18 = WindFiveDays18.getString("speed");
                        JSONObject CloudsFiveDays18 = json0FiveDays18.getJSONObject("clouds");
                        int AllFiveDays18 = CloudsFiveDays18.getInt("all");
                        String DateFiveDays18 = json0FiveDays18.getString("dt_txt");
                        String subStrDate18 = DateFiveDays18.substring(0, 10);
                        String subStrTime18 = DateFiveDays18.substring(11, 16);

                        //19
                        JSONObject json0FiveDays19 = ListFiveDays.getJSONObject(19);
                        JSONObject MainFiveDays19 = json0FiveDays19.getJSONObject("main");
                        double TempFiveDays19 = MainFiveDays19.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays19 = MainFiveDays19.getDouble("feels_like") - 273.15;
                        float PressureFiveDays19 = MainFiveDays19.getInt("pressure");
                        int HumidityFiveDays19 = MainFiveDays19.getInt("humidity");
                        JSONObject WindFiveDays19 = json0FiveDays19.getJSONObject("wind");
                        String SpeedFiveDays19 = WindFiveDays19.getString("speed");
                        JSONObject CloudsFiveDays19 = json0FiveDays19.getJSONObject("clouds");
                        int AllFiveDays19 = CloudsFiveDays19.getInt("all");
                        String DateFiveDays19 = json0FiveDays19.getString("dt_txt");
                        String subStrDate19 = DateFiveDays19.substring(0, 10);
                        String subStrTime19 = DateFiveDays19.substring(11, 16);

                        //20
                        JSONObject json0FiveDays20 = ListFiveDays.getJSONObject(20);
                        JSONObject MainFiveDays20 = json0FiveDays20.getJSONObject("main");
                        double TempFiveDays20 = MainFiveDays20.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays20 = MainFiveDays20.getDouble("feels_like") - 273.15;
                        float PressureFiveDays20 = MainFiveDays20.getInt("pressure");
                        int HumidityFiveDays20 = MainFiveDays20.getInt("humidity");
                        JSONObject WindFiveDays20 = json0FiveDays20.getJSONObject("wind");
                        String SpeedFiveDays20 = WindFiveDays20.getString("speed");
                        JSONObject CloudsFiveDays20 = json0FiveDays20.getJSONObject("clouds");
                        int AllFiveDays20 = CloudsFiveDays20.getInt("all");
                        String DateFiveDays20 = json0FiveDays20.getString("dt_txt");
                        String subStrDate20 = DateFiveDays20.substring(0, 10);
                        String subStrTime20 = DateFiveDays20.substring(11, 16);

                        //21
                        JSONObject json0FiveDays21 = ListFiveDays.getJSONObject(21);
                        JSONObject MainFiveDays21 = json0FiveDays21.getJSONObject("main");
                        double TempFiveDays21 = MainFiveDays21.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays21 = MainFiveDays21.getDouble("feels_like") - 273.15;
                        float PressureFiveDays21 = MainFiveDays21.getInt("pressure");
                        int HumidityFiveDays21 = MainFiveDays21.getInt("humidity");
                        JSONObject WindFiveDays21 = json0FiveDays21.getJSONObject("wind");
                        String SpeedFiveDays21 = WindFiveDays21.getString("speed");
                        JSONObject CloudsFiveDays21 = json0FiveDays21.getJSONObject("clouds");
                        int AllFiveDays21 = CloudsFiveDays21.getInt("all");
                        String DateFiveDays21 = json0FiveDays21.getString("dt_txt");
                        String subStrDate21 = DateFiveDays21.substring(0, 10);
                        String subStrTime21 = DateFiveDays21.substring(11, 16);

                        //22
                        JSONObject json0FiveDays22 = ListFiveDays.getJSONObject(22);
                        JSONObject MainFiveDays22 = json0FiveDays22.getJSONObject("main");
                        double TempFiveDays22 = MainFiveDays22.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays22 = MainFiveDays22.getDouble("feels_like") - 273.15;
                        float PressureFiveDays22 = MainFiveDays22.getInt("pressure");
                        int HumidityFiveDays22 = MainFiveDays22.getInt("humidity");
                        JSONObject WindFiveDays22 = json0FiveDays22.getJSONObject("wind");
                        String SpeedFiveDays22 = WindFiveDays22.getString("speed");
                        JSONObject CloudsFiveDays22 = json0FiveDays22.getJSONObject("clouds");
                        int AllFiveDays22 = CloudsFiveDays22.getInt("all");
                        String DateFiveDays22 = json0FiveDays22.getString("dt_txt");
                        String subStrDate22 = DateFiveDays22.substring(0, 10);
                        String subStrTime22 = DateFiveDays22.substring(11, 16);

                        //23
                        JSONObject json0FiveDays23 = ListFiveDays.getJSONObject(23);
                        JSONObject MainFiveDays23 = json0FiveDays23.getJSONObject("main");
                        double TempFiveDays23 = MainFiveDays23.getDouble("temp") - 273.15;
                        double FeelsLikeFiveDays23 = MainFiveDays23.getDouble("feels_like") - 273.15;
                        float PressureFiveDays23 = MainFiveDays23.getInt("pressure");
                        int HumidityFiveDays23 = MainFiveDays23.getInt("humidity");
                        JSONObject WindFiveDays23 = json0FiveDays23.getJSONObject("wind");
                        String SpeedFiveDays23 = WindFiveDays23.getString("speed");
                        JSONObject CloudsFiveDays23 = json0FiveDays23.getJSONObject("clouds");
                        int AllFiveDays23 = CloudsFiveDays23.getInt("all");
                        String DateFiveDays23 = json0FiveDays23.getString("dt_txt");
                        String subStrDate23 = DateFiveDays23.substring(0, 10);
                        String subStrTime23 = DateFiveDays23.substring(11, 16);

                        tvResult.setTextColor(Color.rgb(68, 134, 199));
                        output1 += " Прогноз на 5 дней"
                                + "\n " + CityNameFiveDays + " (" + CountryNameFiveDays + ")"
                                + "\n"
                                + "\n Дата: " + subStrDate0
                                + "\n Время: " + subStrTime0
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays0) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays0) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays0 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays0 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays0 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays0 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate1
                                + "\n Время: " + subStrTime1
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays1) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays1) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays1 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays1 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays1 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays1 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate2
                                + "\n Время: " + subStrTime2
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays2) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays2) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays2 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays2 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays2 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays2 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate3
                                + "\n Время: " + subStrTime3
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays3) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays3) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays3 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays3 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays3 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays3 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate4
                                + "\n Время: " + subStrTime4
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays4) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays4) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays4 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays4 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays4 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays4 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate5
                                + "\n Время: " + subStrTime5
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays5) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays5) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays5 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays5 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays5 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays5 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate6
                                + "\n Время: " + subStrTime6
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays6) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays6) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays6 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays6 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays6 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays6 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate7
                                + "\n Время: " + subStrTime7
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays7) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays7) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays7 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays7 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays7 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays7 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate8
                                + "\n Время: " + subStrTime8
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays8) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays8) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays8 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays8 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays8 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays8 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate9
                                + "\n Время: " + subStrTime9
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays9) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays9) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays9 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays9 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays9 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays9 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate10
                                + "\n Время: " + subStrTime10
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays10) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays10) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays10 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays10 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays10 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays10 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate11
                                + "\n Время: " + subStrTime11
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays11) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays11) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays11 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays11 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays11 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays11 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate12
                                + "\n Время: " + subStrTime12
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays12) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays12) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays12 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays12 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays12 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays12 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate13
                                + "\n Время: " + subStrTime13
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays13) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays13) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays13 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays13 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays13 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays13 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate14
                                + "\n Время: " + subStrTime14
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays14) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays14) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays14 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays14 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays14 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays14 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate15
                                + "\n Время: " + subStrTime15
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays15) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays15) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays15 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays15 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays15 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays15 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate16
                                + "\n Время: " + subStrTime16
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays16) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays16) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays16 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays16 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays16 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays16 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate17
                                + "\n Время: " + subStrTime17
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays17) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays17) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays17 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays17 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays17 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays17 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate18
                                + "\n Время: " + subStrTime18
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays18) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays18) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays18 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays18 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays18 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays18 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate19
                                + "\n Время: " + subStrTime19
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays19) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays19) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays19 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays19 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays19 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays19 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate20
                                + "\n Время: " + subStrTime20
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays20) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays20) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays20 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays20 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays20 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays20 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate21
                                + "\n Время: " + subStrTime21
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays21) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays21) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays21 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays21 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays21 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays21 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate22
                                + "\n Время: " + subStrTime22
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays22) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays22) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays22 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays22 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays22 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays22 + " hPa"
                                + "\n"
                                + "\n Дата: " + subStrDate23
                                + "\n Время: " + subStrTime23
                                + "\n Температура: " + "--------------- " + df.format(TempFiveDays23) + " C"
                                + "\n Ощущается как: " + "----------- " + df.format(FeelsLikeFiveDays23) + " C"
                                + "\n Влажность: " + "------------------ " + HumidityFiveDays23 + "%"
                                + "\n Скорость ветра: " + "----------- " + SpeedFiveDays23 + "m/s"
                                + "\n Облачность: " + "---------------- " + AllFiveDays23 + "%"
                                + "\n Давление: " + "-------------- " + PressureFiveDays23 + " hPa"
                        ;
                        tvResult.setText(output1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();  //делаем сообщение об ошибке в виде всплывающего уведомления
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());  //создаём экземпляр очереди запросов
            requestQueue.add(stringRequest);  //добавляем строковый запрос в очередь запросов
        }
    }







    public void StartTimer(View view) {  //Кнопка которая запускает метод Runnable (Кнопка запуска таймера)
        mToastRunnable.run();
    }

    public void StopTimer(View view) {               //Кнопка которая останавливает метод Runnable (Кнопка остановки таймера)
        mHandler.removeCallbacks(mToastRunnable);
    }

    public void Archive(View view) {
        Intent i = new Intent(this, ArchiveMenuActivity.class);  //Кнопка запуска следующей страницы
        startActivity(i);
    }


    private Handler mHandler = new Handler();  // Создаём переменную для таймера
    private MyDbManager myDbManager;  //Создаём объект myDbManager



    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {  //Метод, который запускается по нажатию кнопки StartTimer и останавливается по нажатию кнопки StopTimer

            String tempUrl3 = "";  //Определяем строку для удерживания полного URL-адреса
            String city3 = etCity.getText().toString().trim();   //Получаем текст из текстовых полей
            String country3 = etCountry.getText().toString().trim();
            if(city3.equals("")){
                tvResult.setText("Поле не может быть пустым!");  //Показывает текст ошибки, если поле "city" пустое
            }else{
                if(!country3.equals("")){
                    tempUrl3 = url + "?q=" + city3 + "," + country3 + "&appid=" + appid;  //Или если поле "country" не пустое. Если да, то url продолжается так
                }else{
                    tempUrl3 = url + "?q=" + city3 + "&appid=" + appid;  //Если нет, то так
                }


                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl3, new Response.Listener<String>() {  //создаём экземпляр объекта строковго запроса который является типом HTTP-запроса                @Override
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);  //создаём оператор для печати ответа в журнал логов
                        try {
                            JSONObject jsonResponse24 = new JSONObject(response);
                            JSONArray jsonArray24 = jsonResponse24.getJSONArray("weather");
                            JSONObject jsonObjectWeather24 = jsonArray24.getJSONObject(0);
                            JSONObject jsonObjectMain24 = jsonResponse24.getJSONObject("main");
                            double temp24 = jsonObjectMain24.getDouble("temp") - 273.15;
                            double feelsLike24 = jsonObjectMain24.getDouble("feels_like") - 273.15;
                            float pressure24 = jsonObjectMain24.getInt("pressure");
                            int humidity24 = jsonObjectMain24.getInt("humidity");
                            JSONObject jsonObjectWind24 = jsonResponse24.getJSONObject("wind");
                            String wind24 = jsonObjectWind24.getString("speed");
                            JSONObject jsonObjectClouds24 = jsonResponse24.getJSONObject("clouds");
                            String clouds24 = jsonObjectClouds24.getString("all");
                            JSONObject jsonObjectSys24 = jsonResponse24.getJSONObject("sys");
                            String countryName24 = jsonObjectSys24.getString("country");
                            String cityName24 = jsonResponse24.getString("name");
                            tvResult.setTextColor(Color.rgb(68, 134, 199));

                            Date currentDate = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());  //Текущая дата
                            String dateText = dateFormat.format(currentDate);

                            DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault()); //Текущее время
                            String timeText = timeFormat.format(currentDate);

                            String date = dateText + "-" + timeText;

                            String tempstr = df.format(temp24);
                            String humiditystr = Double.toString(humidity24);  //Перевод в тип string
                            String pressurestr = Float.toString(pressure24);




                            myDbManager.insertToDb(cityName24, date, tempstr, humiditystr, wind24, clouds24, pressurestr);  //Происходит сбор переменных по погодным данным
                                                                                                                            //и они отправляеются в класс MyDbManager в функцию insertToDb



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();  //делаем сообщение об ошибке в виде всплывающего уведомления
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());  //создаём экземпляр очереди запросов
                requestQueue.add(stringRequest);  //добавляем строковый запрос в очередь запросов

            }
            mHandler.postDelayed(this, 30000);  //Таймер с временной задержкой. Метод зациклен на работу через определённое количество времени указанное здесь.
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();  //Закрытие базы данных
    }


}

