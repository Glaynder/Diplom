package t.stepura.weatherappdiplom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {  // Вспомогательный класс
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb(){ //Функция для открытия базы данных, когда мы ею пользуемся
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String cityName24, String date, String tempstr, String humiditystr, String wind24, String clouds24, String pressurestr){  //прописыаем данные которые передадуться сюда после запроса в классе MainActivity
        ContentValues cv = new ContentValues();  //инициализировали класс
        //Поменял только temp24 на tempstr в двух местах
        //String date - добавить вверху в начале возле tempstr
        cv.put(MyConstants.CITY, cityName24);
        cv.put(MyConstants.DATE, date);        //
        cv.put(MyConstants.TEMP, tempstr);      //
        cv.put(MyConstants.HUM, humiditystr);   //Здесь будет происходить заполнение колонок теми данными которые пришли в функцию insertToDb после запроса в классе MainActivity. Заполняем класс ContentValues (в сокращении cv)
        cv.put(MyConstants.WIND, wind24);      //
        cv.put(MyConstants.CLOUDS, clouds24);  //
        cv.put(MyConstants.PRESS, pressurestr); //
        db.insert(MyConstants.TABLE_NAME, null, cv);  //Теперь необходимо записать всё предыдущее в базу данных. Прописываем имя таблицы и данные (в виде cv) которые мы указывали в класс ContentValues.
    }


    //Считывание для температуры
    public List<String> getFromDb(){  //Функция для считывания данных из базы данных. List будет выводить список всех данных. К примеру одной из колонок
        List<String> tempList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null); //Создаётся курсор для считывания данных из таблицы

        while (cursor.moveToNext()){  //Запускается цикл который будет считывать все данные находящиеся в колонке. Пока все данные не считаются, цикл не закончится.
            String cityy = cursor.getString(cursor.getColumnIndex(MyConstants.CITY));
            String datee = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            String tempp = cursor.getString(cursor.getColumnIndex(MyConstants.TEMP)); //Здесь указывается колонка (TEMP) из которой нужно взять данные и переменная (tempp) в которую сохраняются собранные данные

            String ex = cityy + "-[" + datee + "]-" + "(" + tempp + " C" + ")";

            tempList.add(ex);  //когда цикл заканчивается, переменная (в данном случае tempp) попадает в List

        }

        cursor.close();  //Закрытие курсора.
        return tempList;  //Возвращение списка
    }


    //Считывание для влажности
    public List<String> getFromDb1(){
        List<String> humList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            String cityy1 = cursor.getString(cursor.getColumnIndex(MyConstants.CITY));
            String datee1 = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            String humidityy1 = cursor.getString(cursor.getColumnIndex(MyConstants.HUM));

            String ey = cityy1 + "-[" + datee1 + "]-" + "(" + humidityy1 + " %" + ")";

            humList.add(ey);
        }

        cursor.close();
        return humList;
    }

    //Считывание для скорости ветра
    public List<String> getFromDb2(){
        List<String> windList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            String cityy1 = cursor.getString(cursor.getColumnIndex(MyConstants.CITY));
            String datee1 = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            String windd1 = cursor.getString(cursor.getColumnIndex(MyConstants.WIND));

            String ez = cityy1 + "-[" + datee1 + "]-" + "(" + windd1 + " m/s" + ")";

            windList.add(ez);
        }

        cursor.close();
        return windList;
    }

    //Считывание для облачности
    public List<String> getFromDb3(){
        List<String> cloudsList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            String cityy1 = cursor.getString(cursor.getColumnIndex(MyConstants.CITY));
            String datee1 = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            String cloudss1 = cursor.getString(cursor.getColumnIndex(MyConstants.CLOUDS));

            String eq = cityy1 + "-[" + datee1 + "]-" + "(" + cloudss1 + " %" + ")";

            cloudsList.add(eq);
        }

        cursor.close();
        return cloudsList;
    }

    //Считывание для давления
    public List<String> getFromDb4(){
        List<String> pressList = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            String cityy1 = cursor.getString(cursor.getColumnIndex(MyConstants.CITY));
            String datee1 = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            String presss1 = cursor.getString(cursor.getColumnIndex(MyConstants.PRESS));

            String ew = cityy1 + "-[" + datee1 + "]-" + "(" + presss1 + " hPa" + ")";

            pressList.add(ew);
        }

        cursor.close();
        return pressList;
    }


    public void closeDb(){  //База данных закрывается. Это делается для того, чтобы не нагружать систему.
        myDbHelper.close();
    }

}
