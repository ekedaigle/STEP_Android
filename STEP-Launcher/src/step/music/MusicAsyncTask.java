package step.music;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class MusicAsyncTask extends AsyncTask<Integer, String, Object>
{
	private Socket sendSocket;
	private Socket recvSocket;
	private DataOutputStream sendStream;
	private DataInputStream recvStream;
	private MusicAsyncTaskCallback callback;

	@Override
	protected Object doInBackground(Integer... arg0)
	{
		int recvPort = arg0[0];
		int sendPort = arg0[1];
		
		while (true)
		{
			try {
				sendSocket = new Socket("10.0.50.1",  recvPort);
				sendStream = new DataOutputStream(sendSocket.getOutputStream());
				recvSocket = new Socket("10.0.50.1", sendPort);
				recvStream = new DataInputStream(sendSocket.getInputStream());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConnectException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				continue;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		
		while (true)
		{
			try {
				sendStream.writeBytes("g\n");
				String data = recvStream.readLine();
				publishProgress(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onProgressUpdate(String... data)
	{
		if (data.length > 0)
			callback.taskGotData(data[0]);
    }

}
