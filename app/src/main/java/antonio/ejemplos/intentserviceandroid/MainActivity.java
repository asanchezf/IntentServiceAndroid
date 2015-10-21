package antonio.ejemplos.intentserviceandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    /**
     * Text views para los datos
     */
    private TextView memoryUsageText;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener las etiquetas
        memoryUsageText = (TextView) findViewById(R.id.memory_ava_text);
        progressText = (TextView) findViewById(R.id.progress_text);

        // Obtener bot�n de monitoreo de memoria
        ToggleButton button = (ToggleButton) findViewById(R.id.toggleButton);

        // Setear escucha de acci�n
        button.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Intent intentMemoryService = new Intent(
                                getApplicationContext(), MemoryService.class);
                        if (isChecked) {
                            startService(intentMemoryService); //Iniciar servicio
                        } else {
                            stopService(intentMemoryService); // Detener servicio
                        }
                    }
                }
        );


        // Filtro de acciones que ser�n alertadas
        IntentFilter filter = new IntentFilter(
                Constants.ACTION_RUN_ISERVICE);
        filter.addAction(Constants.ACTION_RUN_SERVICE);
        filter.addAction(Constants.ACTION_MEMORY_EXIT);
        filter.addAction(Constants.ACTION_PROGRESS_EXIT);

        // Crear un nuevo ResponseReceiver
        ResponseReceiver receiver =
                new ResponseReceiver();
        // Registrar el receiver y su filtro
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                filter);
    }

    /**
     * M�todo onClick() personalizado para {@code turn_intent_service}
     * @param v View presionado
     */
    public void onClickTurnIntentService(View v) {
        Intent intent = new Intent(this, ProgressIntentService.class);
        intent.setAction(Constants.ACTION_RUN_ISERVICE);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Broadcast receiver que recibe las emisiones desde los servicios
    private class ResponseReceiver extends BroadcastReceiver {

        // Sin instancias
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constants.ACTION_RUN_SERVICE:
                    memoryUsageText.setText(intent.getStringExtra(Constants.EXTRA_MEMORY));
                    break;

                case Constants.ACTION_RUN_ISERVICE:
                    progressText.setText(intent.getIntExtra(Constants.EXTRA_PROGRESS, -1) + "");
                    break;

                case Constants.ACTION_MEMORY_EXIT:
                    memoryUsageText.setText("Memoria");
                    break;

                case Constants.ACTION_PROGRESS_EXIT:
                    progressText.setText("Progreso");
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
