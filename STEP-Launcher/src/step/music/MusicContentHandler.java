package step.music;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MusicContentHandler extends DefaultHandler {
	
	private Map<String, Genre> stations = new HashMap<String, Genre>();
	private Genre genre = null;
	private String genre_name = null;
	private Station station = null;
	private ArrayList<Station> station_list = null;
	private String station_name = null;
	private String station_description = null;
	private String text = null;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.compareTo("category") == 0)
		{
			genre = new Genre();
			genre.id = Integer.parseInt(atts.getValue("id"));
		}
		
		else if (localName.compareTo("station") == 0)
		{
			station = new Station();
			station.id = Integer.parseInt(atts.getValue("id"));
		}
		
		else if (localName.compareTo("stations") == 0)
		{
			station_list = new ArrayList<Station>();
		}
	}
	
	@Override
	public void characters (char buf [], int offset, int len) throws SAXException
	{
		text = new String(buf, offset, len);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.compareTo("category") == 0) {
			stations.put(genre_name, genre);
			genre = null;
			genre_name = null;
		}
		
		else if (localName.compareTo("name") == 0)
		{
			if (station == null) // name for genre
				genre_name = text;
			else // name for station
				station_name = text;
		}
		
		else if (localName.compareTo("description") == 0)
		{
			station_description = text.toString();
		}
		
		else if (localName.compareTo("station") == 0)
		{
			station.description = station_description;
			station.name = station_name;
			station_list.add(station);
			station = null;
		}
		
		else if (localName.compareTo("stations") == 0)
		{
			genre.stations = new Station[station_list.size()];
			station_list.toArray(genre.stations);
			station_list = null;
		}
	}
	
	public Map<String, Genre> getStations()
	{
		return stations;
	}
}
