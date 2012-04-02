package step.music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.util.Log;

public class MusicAsyncTask extends AsyncTask<Integer, Map<String, Station[]>, Object>
{
	private Socket sock;
	private DataOutputStream sendStream;
	private DataInputStream recvStream;
	private MusicAsyncTaskCallback callback;
	private MusicContentHandler handler;
	private BlockingQueue<String> queue;

	public void setCallback(MusicAsyncTaskCallback callback)
	{
		this.callback = callback;
	}
	
	public void sendMessage(String msg)
	{
		queue.add(msg);
	}
	
	@Override
	protected Object doInBackground(Integer... arg0)
	{
		int port = arg0[0];
		handler = new MusicContentHandler();
		queue = new LinkedBlockingQueue<String>();
		
		while (true)
		{
			try {
				sock = new Socket("10.0.50.1",  port);
				sendStream = new DataOutputStream(sock.getOutputStream());
				recvStream = new DataInputStream(sock.getInputStream());
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
				continue;
			}
			
			break;
		}
		
		try {
			sendStream.writeBytes("GENRES");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			StringBuilder sb = new StringBuilder();
			while (true)
			{
				String data = recvStream.readLine().trim();
				sb.append(data);
				
				if (data.compareTo("</categories>") == 0)
					break;
			}
			
			try {
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(handler);
				InputSource is = new InputSource();
				is.setByteStream(new ByteArrayInputStream(sb.toString().getBytes()));
				reader.parse(is);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			publishProgress(handler.getStations());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (true)
		{
			try {
				String msg = queue.take();
				sendStream.writeBytes(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onProgressUpdate(Map<String, Station[]>... data)
	{
		if (data.length > 0)
			callback.taskGotStations(data[0]);
    }

}
