package com.example.nsrin.tirupatibalaji;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        new AccessTokenFetcher().execute(getString(R.string.server)+"/session/token"); //Get the access token.
        Button sign_in=findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email_address=(EditText) findViewById(R.id.email_address);
                EditText password=(EditText) findViewById(R.id.password);

                //Let's do the validation, its going to be fun.
                boolean validation_ok=validateCredentials(email_address,password);

                if(validation_ok==true){
                    //Let's the call the webservice call.
                    pd= ProgressDialog.show(LoginActivity.this,"","Attempting Login ...", true);
                    pd.show();
                    new RequestDrupalLogin().execute(getString(R.string.server)+"/user/login/?_format=json",email_address.getText().toString(),password.getText().toString());
                }




            }
        });
    }

    public boolean validateCredentials(EditText email_address, EditText password){

        String email=email_address.getText().toString();
        String pass=password.getText().toString();

        if(email.length()==0){
            email_address.requestFocus(); //to show the error.
            email_address.setError("Username can't be empty !"); //Showing the error message.
            return false;
        }
        if(pass.length()==0){
            password.requestFocus();
           password.setError("Password can't be empty !");
           return false;
        }

         return true;
    }

    private class RequestDrupalLogin extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... params) {

            HttpURLConnection connection = null;
            String s_pref=getStoredPref();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Authorization","Bearer "+s_pref);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Cache-Control", "no-cache");
                String body="{\"name\":\""+params[1]+"\",\"pass\":\""+params[2]+"\"}";
                byte[] outputInBytes = body.getBytes("UTF-8");
                OutputStream os = connection.getOutputStream();
                os.write( outputInBytes );
                os.close();
                connection.connect();
                int statusCode = connection.getResponseCode();
                //##################################
                //#For handling good request.#######
                //#Login Success####################
                if(statusCode==200) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer login_json = new StringBuffer();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        login_json.append(line);
                    }
                    return login_json.toString()+"--200";
                }
                //#########################################
                //#For handling bad request.###############
                //#Login failed message to be shown here###
                else{
                    InputStream inputStream = connection.getErrorStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer login_json = new StringBuffer();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        login_json.append(line);
                    }

                    return login_json.toString()+"--400";
                }



            } catch (MalformedURLException ex){
                return "Padmavati";

            } catch (IOException ex) {

               return "Tirupati";

            } finally {
                if (connection != null)
                    connection.disconnect();
            }

        }

        public void ShowLoginError(String ErrorJson){

            try {
                JSONObject reader = new JSONObject(ErrorJson);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Failed !");
                builder.setMessage(reader.getString("message"));
                //builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setIcon(android.R.drawable.btn_dialog);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                });
                builder.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Store the value in SQLITE database.
            String json_result[]=result.split("--");
            if(json_result[1].contains("200")) {
                //showToastMessage(json_result[0].toString());
                Intent myIntent = new Intent(LoginActivity.this, ProfileActivity.class);
                myIntent.putExtra("user_data", json_result[0].toString()); //Optional parameters
                LoginActivity.this.startActivity(myIntent);
                startActivity(myIntent);
                pd.dismiss();

            }else {
                pd.dismiss();
                ShowLoginError(json_result[0].toString());
            }


        }



    } //Class for login ends here.



    public void showToastMessage(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
    public String getStoredPref() {
        SharedPreferences s_pref = getSharedPreferences("srinivas_drupal",0);
        String stringPref = s_pref.getString("access_token","");
        return stringPref;
    }

    private class AccessTokenFetcher extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... params) {


            HttpURLConnection httpURLConnection=null;

            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("USER-AGENT", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10 (.NET CLR 3.5.30729)");
                httpURLConnection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.connect();



                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer access_token= new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    access_token.append(line);
                }

                return access_token.toString();

            } catch (MalformedURLException ex){
                ex.printStackTrace();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }finally {
                if(httpURLConnection !=null)
                   httpURLConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Store the value in shared preference.
            SharedPreferences.Editor editor=getSharedPreferences("srinivas_drupal",MODE_PRIVATE).edit();
            editor.putString("access_token",result.toString());
            editor.commit(); //Very important, if you don't commit then shared preferences will not work.
        }

    }//Inner class ends here.
} //Class end here.
