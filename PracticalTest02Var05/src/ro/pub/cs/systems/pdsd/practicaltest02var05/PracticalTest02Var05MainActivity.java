package ro.pub.cs.systems.pdsd.practicaltest02var05;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var05MainActivity extends Activity {

	//Server
	private EditText     serverPortEditText       = null;
	private Button       connectButton            = null;
	
	//Client
	private EditText     clientAddressEditText    = null;
	private EditText     clientPortEditText       = null;
	private EditText 	commandEditText = null;
	private Button 		sendButton = null;
	
	private TextView 	resultTextView = null;
	
	private ServerThread serverThread             = null;
	private ClientThread clientThread             = null;

	private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
	private class ConnectButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			String serverPort = serverPortEditText.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Server port should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			serverThread = new ServerThread(Integer.parseInt(serverPort));
			if (serverThread.getServerSocket() != null) {
				serverThread.start();
			} else {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
			}
			
		}
	}
	
	private SendButtonClickListener sendButtonClickListener = new SendButtonClickListener();
	private class SendButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			
			
			String clientAddress = clientAddressEditText.getText().toString();
			String clientPort    = clientPortEditText.getText().toString();
			if (clientAddress == null || clientAddress.isEmpty() ||
				clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Client connection parameters should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			if (serverThread == null || !serverThread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			
			String command = commandEditText.getText().toString();

			if (command == null || command.isEmpty()) {
				Toast.makeText(
					getApplicationContext(),
					"Parameters from client (command) should be filled!",
					Toast.LENGTH_SHORT
				).show();
				return;
			}
			
			resultTextView.setText("");
			
			clientThread = new ClientThread(
					clientAddress,
					Integer.parseInt(clientPort), 
					command,
					resultTextView);
			clientThread.start();
		}
	}	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_var05_main);
		
		serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
		connectButton = (Button)findViewById(R.id.connect_button);
		connectButton.setOnClickListener(connectButtonClickListener);
		
		clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
		clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
		commandEditText = (EditText)findViewById(R.id.command);
		sendButton = (Button)findViewById(R.id.send_button);
		sendButton.setOnClickListener(sendButtonClickListener);

		resultTextView = (TextView)findViewById(R.id.result_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_var05_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
		}
		super.onDestroy();
	}
}
