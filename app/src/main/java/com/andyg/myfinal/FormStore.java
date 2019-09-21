package com.andyg.myfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import modelos.Tienda;

public class FormStore extends AppCompatActivity implements View.OnClickListener {

    EditText txtidTienda, txtNombreTienda, txtDireccionTienda,
            txtLatitudTienda, txtLongitudTienda, txtDescripcionTienda;

    Button btnAceptar, btnCancelar;

    Tienda tiendaIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_store);

      txtidTienda = findViewById(R.id.txtidTienda);
        txtNombreTienda = findViewById(R.id.txtNombreTienda);
        txtDireccionTienda = findViewById(R.id.txtDireccionTienda);
        txtLatitudTienda = findViewById(R.id.txtLatitudTienda);
        txtLongitudTienda = findViewById(R.id.txtLongitudTienda);
        txtDescripcionTienda = findViewById(R.id.txtDescripcionTienda);

        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        txtidTienda.setEnabled(false);

        //int id = getIntent().getExtras().getInt("idUpdate");

        tiendaIntent = (Tienda) getIntent().getExtras().getSerializable("myObject");

        txtidTienda.setText(tiendaIntent.getId()+"");
        txtNombreTienda.setText(tiendaIntent.getNombreTienda()+"");
        txtDescripcionTienda.setText(tiendaIntent.getDescripcion()+"");
        txtDireccionTienda.setText(tiendaIntent.getDireccion()+"");
        txtLatitudTienda.setText(tiendaIntent.getLatitud() +"");
        txtLongitudTienda.setText(tiendaIntent.getLongitud() + "");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAceptar:

                Tienda myStore = new Tienda();
                myStore.setId(Integer.parseInt(txtidTienda.getText().toString().trim()));
                myStore.setNombreTienda(txtNombreTienda.getText().toString().trim());
                myStore.setDireccion(txtDireccionTienda.getText().toString().trim());
                myStore.setLatitud(Double.parseDouble(txtLatitudTienda.getText().toString().trim()));
                myStore.setLongitud(Double.parseDouble(txtLongitudTienda.getText().toString().trim()));
                myStore.setDescripcion(txtDescripcionTienda.getText().toString().trim());

                new UpdateStore().execute(myStore);

                break;
            case R.id.btnCancelar:
                break;
        }
    }

    class UpdateStore extends AsyncTask<Tienda, Integer, Boolean>{


        @Override
        protected Boolean doInBackground(Tienda... tiendas) {

            String params = "nombre=" + tiendas[0].getNombreTienda() + "&"+
                    "direccion=" + tiendas[0].getDireccion() + "&" +
                    "latitud=" + tiendas[0].getLatitud() + "&" +
                    "longitud=" + tiendas[0].getLongitud() + "&" +
                    "descripcion=" + tiendas[0].getDescripcion() + "&" +
                    "idtienda=" + tiendas[0].getId();

            try {
                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/modificarTienda.php");
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
                Toast.makeText(FormStore.this, "Tienda actualizada con exito", Toast.LENGTH_SHORT).show();
                finish();
            }else
            {
                Toast.makeText(FormStore.this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
