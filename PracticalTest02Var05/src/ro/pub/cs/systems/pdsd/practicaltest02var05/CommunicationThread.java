package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CommunicationThread extends Thread {
	private ServerThread serverThread;
	private Socket       socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String command = bufferedReader.readLine();
					List<String> commandList = Arrays.asList(command.split(","));
					String operation = commandList.get(0);
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] OPERATION + " + operation);
					String key = commandList.get(1);
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] KEY + " + key);
					String value = null;
					if(operation.equals("put")) {
						value = commandList.get(2);
						Log.i(Constants.TAG, "[COMMUNICATION THREAD] VALUE + " + value);
					}
					
					HashMap<String, Timestamp> data = serverThread.getData();
					String finalResult = null;
					
					if(operation.equals("put")) {
						Timestamp t = new Timestamp();
						t.setValue(value);
						t.setTime(Utilities.getTime());
						
						Timestamp result = data.put(key, t);
						if (result == null) {
							finalResult = "inserted";
						}
						else {
							finalResult = "modified";
						}
						

					}
					
					if(operation.equals("get")) {
						Timestamp t2 = data.get(key);
						if(t2 == null) {
							finalResult = "NONE";
						} else {
							Long currentTime = Utilities.getTime();
							if(currentTime - t2.getTime() >= 60) {
								finalResult = "NONE";
							} else {
								finalResult = t2.getValue();
							}
						}
					}
					
					printWriter.println(finalResult);
					printWriter.flush();
					
					

				} else {
					Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			}

		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}
	

}
