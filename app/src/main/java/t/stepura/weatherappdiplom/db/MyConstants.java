package t.stepura.weatherappdiplom.db;

public class MyConstants {
    public static final String DB_NAME = "my_db.db";  //Имя базы данных, в которой будет хранится таблица
    public static final String TABLE_NAME = "my_table";  //Имя таблицы
    public static final int DB_VERSION = 4;  //Версия базы данных, нужна, если нужно изменить структуру таблицы. Затем вресию нужно увеличить на единицу
    public static final String _ID = "_id";        //
    public static final String CITY = "city";      //
    public static final String DATE = "date";      //
    public static final String TEMP = "temp";      //
    public static final String HUM = "hum";        //Названия колонок таблицы
    public static final String WIND = "wind";      //
    public static final String CLOUDS = "clouds";  //
    public static final String PRESS = "press";    //
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +                       //
            TABLE_NAME + " (" + CITY + " TEXT," + _ID + " INTEGER PRIMARY KEY," + DATE + " TEXT," +    //Тип колонок таблицы
            TEMP + " INTEGER," + HUM + " INTEGER," + WIND + " INTEGER," + CLOUDS +                     //
            " INTEGER," + PRESS + " INTEGER)";                                                         //

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;    //Переменная необходимая для сброса данных таблицы
}
