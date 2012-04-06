package step.music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetAddress;
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

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class MusicAsyncTask extends AsyncTask<Integer, Map<String, Genre>, Object>
{
	private Socket sock;
	private DataOutputStream sendStream;
	private DataInputStream recvStream;
	private MusicAsyncTaskCallback callback;
	private MusicContentHandler handler;
	private BlockingQueue<String> queue;
	private InetAddress baseStationAddr;
	
	public MusicAsyncTask(InetAddress addr)
	{
		super();
		this.baseStationAddr = addr;
	}

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
			int port_offset = 0;
			
			while (true)
			{
				try {
					sock = new Socket(baseStationAddr,  port + port_offset);
					sendStream = new DataOutputStream(sock.getOutputStream());
					recvStream = new DataInputStream(sock.getInputStream());
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ConnectException e) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					port_offset = (port_offset + 1) % 10;
					continue;
	
				} catch (IOException e) {
					port_offset = (port_offset + 1) % 10;
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
				String msg = null;
				
				try {
					msg = queue.take();
					sendStream.writeBytes(msg);
				} catch (Exception e) {
					try {
						if (msg != null)
							queue.put(msg);
					} catch (InterruptedException e1) {}
					
					break;
				}
			}
		}
	}
	
	@Override
	protected void onProgressUpdate(Map<String, Genre>... data)
	{
		if (data.length > 0)
			callback.taskGotStations(data[0]);
    }

}
