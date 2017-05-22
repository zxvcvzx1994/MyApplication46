package com.example.kh.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void process(View v){
        String[] a= getResources().getStringArray(R.array.array);
        new DownloadImage().execute(a);
    }

    public class DownloadImage extends AsyncTask<String, Integer, String>{
        private static final String TAG ="DU Lieu" ;
        Dialog dialog;
        int so;
        int phantram;
        @Override
        protected String doInBackground(String... params) {
            try {
                for(int i =  0; i< params.length; i++){
                    URL url = new URL(params[i]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int length=    connection.getContentLength();
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder string = new StringBuilder();
                    so  = 0;
                    byte[] bytes  =new byte[1024];
                    Uri uri = Uri.parse(params[i]);
                    double total=0;
                    File f= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/n"+uri.getLastPathSegment());
                    FileOutputStream fileOutputStream = new FileOutputStream(f);
                    Log.d(TAG, "doInBackground: "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/n"+uri.getLastPathSegment());
                    while((so = inputStream.read(bytes))!=-1){
                        fileOutputStream.write(bytes, 0, so);
                        Log.d(TAG, "doInBackground: "+so);
                        total+= so;
                        phantram = (int) (total*100/length);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Complete";
        }
        ProgressDialog progressDialog;
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(phantram);

        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Download in process...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.hide();
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

        }
    }
}
