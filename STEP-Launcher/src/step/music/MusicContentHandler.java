package step.music;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MusicContentHandler extends DefaultHandler {
	
	private ArrayList<ArrayList<String>> categories;
	private ArrayList<String> category;
	private Map<String, Station[]> stations = new HashMap<String, Station[]>();
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.compareTo("categories") == 0)
			categories = new ArrayList<ArrayList<String> >();
		else if (localName.compareTo("category") == 0)
		{
			category = new ArrayList<String>();
			category.add(atts.getValue("name"));
		}
		else if (localName.compareTo("genre") == 0)
			category.add(atts.getValue("name"));
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.compareTo("category") == 0) {
			categories.add(category);
			category = null;
		}
	}
	
	public Map<String, Station[]> getStations()
	{
		return stations;
	}
}
