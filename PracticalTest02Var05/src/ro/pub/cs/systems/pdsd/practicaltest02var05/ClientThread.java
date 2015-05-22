package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {
	private String   address;
	private int      port;
	private String   command;
	private TextView resultsView;
	
	private Socket   socket;
	
	public ClientThread(
			String address,
			int port,
			String command,
			TextView resultsView) {
		this.address                 = address;
		this.port                    = port;
		this.command                    = command;
		this.resultsView         = resultsView;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(command);
				printWriter.flush();

				String resultInfo;
				while ((resultInfo = bufferedReader.readLine()) != null) {
					final String finalizedresultInfo = resultInfo;
					resultsView.post(new Runnable() {
						@Override
						public void run() {
							resultsView.append(finalizedresultInfo + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}
