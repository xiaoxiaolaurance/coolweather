package com.example.xiaoxiao.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xiaoxiao on 2018/1/16.
 */

public class HttpActivity extends Activity implements View.OnClickListener{

    Button btn;
    TextView textView;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_layout);
        btn =(Button)findViewById(R.id.send_request);
        textView =(TextView)findViewById(R.id.response_text);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.send_request:
               // sendRequestWithHttpUrlConnection();
                postData();
                break;
        }
    }

    public void sendRequestWithHttpUrlConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection =null;
                InputStream inputStream;
                BufferedReader bufferedReader =null ;
                try {
                    URL url = new URL("https:www.baidu.com");
                    connection =(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(4000);
                    connection.setUseCaches(true);
                    inputStream =connection.getInputStream();
                    bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder =new StringBuilder();
                    String line;
                    Log.d("http/xx","stringbuilder: "+stringBuilder);
                    if((line =bufferedReader.readLine())!=null){
                        stringBuilder.append(line);
                    }
                    Log.d("http/xx","stringbuilder: "+stringBuilder);
                    updateTextView(stringBuilder);
                }catch (MalformedURLException e){
                    Log.d("http/xx","e: "+e.getMessage());
                    e.printStackTrace();
                }catch (IOException e){
                    Log.d("http/xx","e: "+e.getMessage());
                    e.printStackTrace();
                }finally {

                    try {
                        if(bufferedReader !=null){
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(connection !=null){
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }

    public void updateTextView(final StringBuilder stringBuilder){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(stringBuilder.toString());
            }
        });

    }
    public void postData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection =null;
                DataOutputStream dataOutputStream =null;
                try {
                    URL url =new URL("https://www.baidu.com");
                    httpURLConnection =(HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(8000);
                    dataOutputStream =new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes("username=admin&password=123456");
                    String data =httpURLConnection.getResponseMessage();
                    Log.d("http/xx","message: "+data);
                    Message message =new Message();
                    Bundle bundle =new Bundle();
                    bundle.putString("data",data);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(dataOutputStream !=null){

                        try {
                            dataOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    httpURLConnection.disconnect();

                }
            }
        }).start();

    }

    Handler handler =new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle =msg.getData();
            String data =bundle.getString("data");
            Toast.makeText(HttpActivity.this,data,Toast.LENGTH_SHORT).show();
        }
    };
}
