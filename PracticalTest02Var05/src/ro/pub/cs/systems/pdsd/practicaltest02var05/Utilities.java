package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class Utilities {
	public static BufferedReader getReader(Socket socket) throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public static PrintWriter getWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}
	
	public static Long getTime() throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
		ResponseHandler<String> resp = new BasicResponseHandler();
		
		String response = httpClient.execute(httpGet,resp);
		String[] responseArray = response.split("[-T:+]");
		DateTime date = new DateTime(Integer.parseInt(responseArray[0]),Integer.parseInt(responseArray[1]),Integer.parseInt(responseArray[2]),Integer.parseInt(responseArray[3]),Integer.parseInt(responseArray[4]),Integer.parseInt(responseArray[5]));
		
		
		
		return date.toLong();
	}
}
