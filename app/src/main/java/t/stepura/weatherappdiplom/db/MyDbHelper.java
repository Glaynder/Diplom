package t.stepura.weatherappdiplom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);  //Конструктор, который создаёт базу данных с названием и версией базы данных из класса MyConstants
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  //Эта функция запускается после того, как база данных будет создана. То есть после выполнения предыдущего метода
        db.execSQL(MyConstants.TABLE_STRUCTURE);  //Идёт создание таблицы внутри базы данных.  "db" - это наша база данных
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstants.DROP_TABLE);  //После смены версии базы данных она будет перезаписана
        onCreate(db);  //Запустится этот метод и создастся новая таблица
    }
}
