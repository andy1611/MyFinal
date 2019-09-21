package com.andyg.myfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import modelos.Tienda;

public class MenuActivity extends AppCompatActivity {

    ListView lst;
    MyAdapterStore myAdapterStore;
    ArrayList<Tienda> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lst = findViewById(R.id.lstTiendas);

        //definir mi array
        //arrayList = new ArrayList<Tienda>();
        /*agregar registros
        arrayList.add(new Tienda("tiendaUno", "descrip1"));
        arrayList.add(new Tienda("tiendaDos", "descrip2"));
        myAdapterStore = new MyAdapterStore(this, arrayList);
        lst.setAdapter(myAdapterStore);
        /*/

        registerForContextMenu(lst);
        new ConsultarTiendas().execute();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()){

            case R.id.itemContextUpdate:
                //objeto complejo de Tenda
                Toast.makeText(getApplicationContext(), "Actualizar" + arrayList.get(info.position).getId(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), FormStore.class);

                Tienda tiendita = new Tienda();

                tiendita.setId(arrayList.get(info.position).getId());
                tiendita.setNombreTienda(arrayList.get(info.position).getNombreTienda());
                tiendita.setDireccion(arrayList.get(info.position).getDireccion());
                tiendita.setDescripcion(arrayList.get(info.position).getDescripcion());
                tiendita.setLatitud(arrayList.get(info.position).getLatitud());
                tiendita.setLongitud(arrayList.get(info.position).getLongitud());


                intent.putExtra("myObject", tiendita);

                startActivity(intent);

                return true;

            case R.id.itemContextDelete:
                Toast.makeText(getApplicationContext(), "Eliminar", Toast.LENGTH_SHORT).show();

                Tienda store1 = new Tienda();
                store1.setId(arrayList.get(info.position).getId());

                new DeleteStore().execute(store1);
                return true;

            case R.id.itemContextNotificar:
                //notificacion para versiones nuevas
                int nNotificationId = 1;

                String channelID = "my_channel_01";
                //construye la notificacion
                NotificationCompat.Builder noti = new NotificationCompat.Builder(getApplication(), null);

                //gestionar como va a aparecer la notificacion y cuanto
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent1 = new Intent(getApplicationContext(), MenuActivity.class);
                //cuando no esta en ejecucion
                PendingIntent pendingIntent = PendingIntent.getActivity(MenuActivity.this, 0,intent1,0);

                //configurar notificaciones para las versiones igual o superior a android Oreo
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    //cadena donde muestra el nombre de la notificacion
                    CharSequence name="myName";
                    String description = "MyDescrip";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel nChannel = new NotificationChannel(channelID, name, importance);

                    nChannel.setDescription(description);
                    nChannel.enableLights(true);
                    nChannel.setLightColor(Color.BLUE);
                    nChannel.enableVibration(true);
                    nChannel.setVibrationPattern(new long[]{100,200,300,400});
                    nm.createNotificationChannel(nChannel);

                    noti = new NotificationCompat.Builder(getApplicationContext(), channelID);
                }

                noti.setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("myTitle").setContentText("myText").setContentIntent(pendingIntent);
                noti.setChannelId(channelID);

                nm.notify(nNotificationId, noti.build());

                return true;

                default:
                    return super.onContextItemSelected(item);
        }

    }

    class ConsultarTiendas extends AsyncTask<Void, Integer, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {

            URLConnection conection = null;
            JSONArray jsonArray = null;

            try {
                conection = new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/obtenerTiendas.php").openConnection();

                InputStream inputStream = (InputStream) conection.getContent();
                //numero de cavidades que tiene el arreglo para guardar la informacion
                byte[] buffer = new byte[10000];
                //leer la cantidad de caracteres del imputStream
                int size = inputStream.read(buffer);

                jsonArray = new JSONArray(new String(buffer, 0, size));

                //imprimimr resultados (regresar el array)


                //Exeption captura todas las clases de errores
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            Tienda mySotore = null;

            arrayList= new ArrayList<Tienda>();

            //leer los obejetos
            for (int i=0; i<jsonArray.length();i++){
                try {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    mySotore = new Tienda(jsonObject.getInt("idtienda"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("direccion"),
                            jsonObject.getDouble("latitud"),
                            jsonObject.getDouble("longitud"),
                            jsonObject.getString("descripcion"));

                    Log.i("data", jsonObject.getInt("idtienda") + "");

                    //metar la lista al objeto
                    arrayList.add(mySotore);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
           //llenar el adaptador con el contexto y la lista
            myAdapterStore =new MyAdapterStore(MenuActivity.this, arrayList);
            //llenar el listView
            lst.setAdapter(myAdapterStore);
        }
    }


    class DeleteStore extends AsyncTask<Tienda, Integer, Boolean>{


        @Override
        protected Boolean doInBackground(Tienda... tiendas) {

            String params = "idtienda="+ tiendas[0].getId();

            try {
                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/eliminarTienda.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(params);
                writer.flush();
                writer.close();

                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    return true;
                }else
                {
                    return false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean)
            {
                Toast.makeText(MenuActivity.this, "Tienda eliminada con exito", Toast.LENGTH_SHORT).show();
                finish();
            }else
            {
                Toast.makeText(MenuActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
