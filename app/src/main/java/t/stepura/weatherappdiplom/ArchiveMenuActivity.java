package t.stepura.weatherappdiplom;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import t.stepura.weatherappdiplom.db.MyDbManager;

public class ArchiveMenuActivity extends AppCompatActivity {

    private MyDbManager myDbManager;  //Создаём объект myDbManager
    TextView tvArchive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_menu);
        myDbManager = new MyDbManager(this);
        tvArchive = findViewById(R.id.tvArchive);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
    }



    public void TempArchive(View view) {  //Кнопка вывода сохранённых данных о температуре на экран
        tvArchive.setTextColor(Color.rgb(68, 134, 199));
        tvArchive.setText("");
        for(String ex : myDbManager.getFromDb()){
            tvArchive.append(ex);
            tvArchive.append("\n");
        }
    }

    public void HumArchive(View view) {  //Кнопка вывода сохранённых данных о влажности на экран
        tvArchive.setText("");
        for(String ey : myDbManager.getFromDb1()){
            tvArchive.append(ey);
            tvArchive.append("\n");
        }
    }

    public void WindArchive(View view) {  //Кнопка вывода сохранённых данных о скорости ветра на экран
        tvArchive.setText("");
        for(String ez : myDbManager.getFromDb2()){
            tvArchive.append(ez);
            tvArchive.append("\n");
        }

    }

    public void CloudsArchive(View view) {  //Кнопка вывода сохранённых данных о облачности на экран
        tvArchive.setText("");
        for(String eq : myDbManager.getFromDb3()){
            tvArchive.append(eq);
            tvArchive.append("\n");
        }
    }

    public void PressArchive(View view) {  //Кнопка вывода сохранённых данных о давлении на экран
        tvArchive.setText("");
        for(String ew : myDbManager.getFromDb4()){
            tvArchive.append(ew);
            tvArchive.append("\n");
        }
    }
}
