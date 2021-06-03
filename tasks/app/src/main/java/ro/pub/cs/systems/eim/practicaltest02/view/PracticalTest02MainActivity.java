package ro.pub.cs.systems.eim.practicaltest02.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private Button sendRequest = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        startServer();

        sendRequest = (Button)findViewById(R.id.sendRequest);
        sendRequest.setOnClickListener(sendRequestListener);
    }

    private SendRequestListener sendRequestListener = new SendRequestListener();

    private class SendRequestListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(Constants.SERVER_HOST, Constants.SERVER_PORT);
            clientThread.start();
        }
    }

    private void startServer() {
        int serverPort = Constants.SERVER_PORT;
        serverThread = new ServerThread(serverPort);

        if (serverThread.getServerSocket() == null) {
            Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
            return;
        }

        serverThread.start();
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}