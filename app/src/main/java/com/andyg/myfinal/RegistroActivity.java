package com.andyg.myfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import modelos.Usuario;

public class RegistroActivity extends AppCompatActivity {

    //declarar variables

    EditText txtUsuario, txtNombre, txtCorreo, txtDireccion, txtContraseña;
    Button btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtContraseña = findViewById(R.id.txtContraseña);

        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mandar llamar la validacion de datos
                if (
                validarDatos(txtUsuario.getText().toString().trim(),
                        txtNombre.getText().toString().trim(),
                        txtCorreo.getText().toString().trim(),
                        txtDireccion.getText().toString().trim(),
                        txtContraseña.getText().toString())
                )
                {
                   //crear objeto
                   //al llegar a este punto de la ejecucion all is correct

                   Usuario usuario = new Usuario();
                   usuario.setNombre(txtNombre.getText().toString().trim());
                   usuario.setUsuario(txtUsuario.getText().toString().trim());
                   usuario.setCorreo(txtCorreo.getText().toString().trim());
                   usuario.setDireccion(txtDireccion.getText().toString().trim());
                   usuario.setContraseña(txtContraseña.getText().toString());

                   //crear la instancia de la subClase asyncTask para realizar la conexion

                    new AddUser().execute(usuario);
                }


            }
        });
    }




    public boolean validarDatos(String usuario, String nombre, String correo, String direccion, String contraseña){

        //validar que los datos noesten vacios

        if (usuario.isEmpty())
        {
            txtUsuario.setError("Campo vacio");
            txtUsuario.setFocusable(true);
            return false;
        }

        if (nombre.isEmpty())
        {
            txtNombre.setError("Campo vacio");
            txtNombre.setFocusable(true);
            return false;
        }

        if (correo.isEmpty())
        {
            txtCorreo.setError("Campo vacio");
            txtCorreo.setFocusable(true);
            return false;
        }

        if (direccion.isEmpty())
        {
            txtDireccion.setError("Campo vacio");
            txtDireccion.setFocusable(true);
            return false;
        }

        if (contraseña.isEmpty())
        {
            txtContraseña.setError("Campo vacio");
            txtContraseña.setFocusable(true);
            return false;
        }

        return true;
}

    //clase AsyncTask
    //crear un subproseso que no corra en el hilo principal
    //en caso de que colapse no se va cerrar la aplicacion

    //void no se agrega porque se agregara una excepcion
    class AddUser extends AsyncTask <Usuario, Integer, Boolean>{

        //ejecuta todas las instrucciones
        @Override
        protected Boolean doInBackground(Usuario... usuarios) {
            //preparar los datos de la insercion

            //variable para mandar los parametros
            //almacena las variables completas

            String params="";

            params = "user=" + usuarios[0].getUsuario() + "&"+
                    "nombre=" + usuarios[0].getNombre() + "&" +
                    "correo=" + usuarios[0].getCorreo() + "&" +
                    "direccion=" + usuarios[0].getDireccion() + "&" +
                    "password=" + usuarios[0].getContraseña();

            //preparar la conexion
            try {

                URL url = new URL ("http://172.18.26.67/cursoAndroid/vista/Usuario/crearUsuario.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //post para enmascarar los datos
                connection.setRequestMethod("POST");
                //para indicar que se llevaran parametros
                //lleva datos de entrada y salida
                //esto es exclusivo del metodo post
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //es la que lleva el velor de lo elementos que queremos escrbir (archivo, bits)
                OutputStream outputStream = connection.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));

                //escribe los parametros
                writer.write(params);
                //limpia los datos
                writer.flush();
                //cierra la conexion
                writer.close();

                outputStream.close();

                //usar la conexion
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    Log.i("AddUser", "usuario agregado con exito");
                    return true;

                }else {
                    return false;
                }

            }catch (MalformedURLException e){

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
                Toast.makeText(RegistroActivity.this, "Usuario agregado con exito", Toast.LENGTH_SHORT).show();

                finish();
            }else
            {
                Toast.makeText(RegistroActivity.this, "Usuario no agregado, intenta nuevamente", Toast.LENGTH_SHORT).show();
            }





        }
    }
}
