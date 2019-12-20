package hnu.example.bttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Connection;

public class Activity2 extends AppCompatActivity implements IBTMsgClient {
    private TextView mTextView1, mTextView2, mTextView3, connection;

    private ImageButton button, button2;

    private boolean b1 = true;
    private String Umdrehung;
    private String Ansauglufttemperatur;
    private String Temperatur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        mTextView1 = (TextView) findViewById(R.id.textView2);
        mTextView2 = (TextView) findViewById(R.id. Ansauglufttemp);
        mTextView3 = (TextView) findViewById(R.id.temp);
        button = (ImageButton) findViewById(R.id.imageButton);
        button2 = (ImageButton) findViewById(R.id.imageButton2);
        connection = (TextView) findViewById(R.id.Connection);
        TextView text;
        connection.setText("Please Connect");


        BTManager.addBTMsgClient(this);
        button.setOnClickListener(
                new ImageButton.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        b1 = false;
                        connection.setText("Connected");

                    }
                }
        );
        button2.setOnClickListener(
                new ImageButton.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        b1 = true;
                        mTextView1.setText("-");
                        mTextView2.setText("-");
                        mTextView3.setText("-");
                        connection.setText("Disconnected");


                    }
                }
        );

    }


    @Override
    public void receiveMessage(final String msg) {


        if (b1 == false) {
            if (msg.length() >= 3) {

                if (msg.startsWith("R")) {
                    mTextView1.setText(msg);
                    Umdrehung = msg.substring(1, msg.length() - 1);
                }
                if (msg.startsWith("A")) {

                    mTextView2.setText(msg);
                    Ansauglufttemperatur = msg.substring(1, msg.length() - 1);
                }
                if (msg.contains("T")) {

                    mTextView3.setText(msg);
                    Temperatur = msg.substring(1, msg.length() - 1);
                }
                Umdrehung = Umdrehung;
                Ansauglufttemperatur = Ansauglufttemperatur;
                Temperatur = Temperatur;
                String f1 = "&field1=";
                String f2 = "&field2=";
                String f3 = "&field3=";
                String url = "https://api.thingspeak.com/update?api_key=7SNVCPWKYOUT5Y9U" + f1 + Umdrehung + f2 + Temperatur + f3 + Ansauglufttemperatur;

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        connection.setText("Übertragung erfolgreich");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        connection.setText("Übertragung fehlgeschlagen");
                    }
                });
                /*Umdrehung = "";
                Temperatur = "";
                Lambda = ""; wir möchten nicht, das die werte mehrfach gesendet werden, erst wenn Sie wieder dran sind sollen diese gesendet werden.
                            Da die Übertragung nicht konstant stattfindet haben wir diesen Punkt ausgelassen*/
                Volley.newRequestQueue(Activity2.this).add(stringRequest);
            }

        } else {
            connection.setText("Keine Werteübertragung");
            mTextView1.setText("-");
            mTextView2.setText("-");
            mTextView3.setText("-");

        }
        


            /*
            if(msg.startsWith("R")) {

                mTextView1.setText(msg);
                mTextView2.setText("-");
                mTextView3.setText("-");
            } else if(msg.contains("L")){
                mTextView1.setText("-");
                mTextView2.setText(msg);
                mTextView3.setText("-");
            } else if(msg.contains("T")){
                mTextView3.setText(msg);
                mTextView1.setText("-");
                mTextView2.setText("-");
            } else{
                mTextView1.setText("-");
                mTextView2.setText("-");
                mTextView3.setText("-");
            }


            */


        //System.out.println("jetzt könnte an ThingSpeak senden");


    }







    @Override
    public void receiveConnectStatus(boolean isConnected) {
    }

    @Override
    public void handleException(Exception e) {
    }
}
