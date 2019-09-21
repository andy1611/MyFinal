package com.andyg.myfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    String user, password;
    EditText txtUser, txtPass;
    Button btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FORZAR POSICION
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //tomar control de las variables
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);
        btnMaps = findViewById(R.id.btnMaps);



    }

    public void agregar (View view){
        Intent inte = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(inte);
    }

    public void mapa (View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    //llamar el metodo a travez del boton
    public void Login (View view){

        user = txtUser.getText().toString().trim();
        password = txtPass.getText().toString();

       
        //validar datos de la interfaz (usuario, password)

        if (TextUtils.isEmpty(user))
        {
            txtUser.setError("Usuario vacio");
            txtUser.setFocusable(true);
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            txtPass.setError("Contraseña vacia");
            txtPass.setFocusable(true);
            return;
        }

        //nueva instancia de LoginRest
        new LoginRest().execute(user, password);
    }


    //clase AsyncTask
    //crear un subproseso que no corra en el hilo principal

    class LoginRest extends AsyncTask<String , Integer, String>{

        //integer progreso de la tarea en segundo plano
        //variable de peticion de conexion
        URLConnection connection = null;
        //variable para el resultado
        String result = "0";

        //... el tipo de variable que esta enfrente es un arreglo
        @Override
        protected String doInBackground(String... strings) {

            try {
                connection = new URL("http://172.18.26.67/"
                        + "cursoAndroid/vista/usuario/"
                        + "iniciarSesion.php? "
                        + "usuario="+strings[0]
                        +"&password="+strings[1]).openConnection();
                InputStream inputStream= (InputStream)connection.getContent();

                //respuesta del URL conexion
                byte[] buffer= new byte[10000];
                //regresa el tamaño completo del set de datos
                //cuantos son datos dentro de la cadena de respuesta
                int size = inputStream.read(buffer);

                result = new String(buffer, 0, size);

                //para que imprima el resultado en la consola
                Log.i("result", result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        //recibe el string result
        //metodo que se ejecuta despues deldoInBackgroud
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            //cuando se comparan objetos o tipos nativos
            if (s.equals("1"))
            {
                //dado que no tiene contexto hay que llamar a la super clase
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
