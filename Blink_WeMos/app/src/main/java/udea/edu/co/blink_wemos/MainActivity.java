package udea.edu.co.blink_wemos;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity {
    Button encender, apagar, connect;
    EditText ipServer;
    TextView estado;
    boolean socketStatus = false;
    Socket socket;
    MyClientTask myClientTask;
    String address;
    int port = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        encender = (Button)findViewById(R.id.encender);
        encender.setEnabled(false);
        apagar = (Button)findViewById(R.id.apagar);
        apagar.setEnabled(false);
        connect = (Button)findViewById(R.id.connect);
        ipServer = (EditText)findViewById(R.id.ip_server);
        estado = (TextView)findViewById(R.id.estado);

        connect.setOnClickListener(connectOnClickListener);
        encender.setOnClickListener(OnOffLedClickListener);
        apagar.setOnClickListener(OnOffLedClickListener);
    }

    OnClickListener connectOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if(socketStatus)
                Toast.makeText(MainActivity.this,"Already talking to a Socket!! Disconnect and try again!", Toast.LENGTH_LONG).show();
            else {
                socket = null;
                address = ipServer.getText().toString();
                if (address == null)
                    Toast.makeText(MainActivity.this, "Please enter valid address", Toast.LENGTH_LONG).show();
                else {
                    myClientTask = new MyClientTask(address);
                    ipServer.setEnabled(false);
                    connect.setEnabled(false);
                    myClientTask.execute("apagar");
                    encender.setEnabled(true);
                    estado.setText("IP guardada");
                }
            }
        }
    };

    OnClickListener OnOffLedClickListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            String onoff = "";
            if(v==encender){
                onoff="encender";
                encender.setEnabled(false);
                apagar.setEnabled(true);
            }else if (v==apagar){
                onoff="apagar";
                apagar.setEnabled(false);
                encender.setEnabled(true);
            }
            MyClientTask taskEsp = new MyClientTask(address);
            taskEsp.execute(onoff);

        }
    };


    public class MyClientTask extends AsyncTask<String,Void,String>{

        String server;

        MyClientTask(String server){
            this.server = server;
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuffer chaine = new StringBuffer("");

            final String val = params[0];
            final String p = "http://"+ server+"/"+val;

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    estado.setText(p);
                }
            });

            String serverResponse = "";
            try {
                URL url = new URL(p);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }
                inputStream.close();

                System.out.println("chaine: " + chaine.toString());

                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }


}
