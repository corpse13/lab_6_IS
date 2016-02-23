package lab_6_IS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class XMLHandler {
	
	static final String BOOK = "book",
			LEFT = "left", 
			PRICE = "price", 
			YEAR = "year", 
			ID = "id", 
			ENTITLE = "englishTitle", 
			ORTITLE = "originalTitle", 
			AUTHOR = "author", 
			COUNTRY = "country", 
			GENRE = "genre";
	
	@SuppressWarnings("unchecked")
	public static List<BookBean> readXML(File file){
		List <BookBean> books = new ArrayList<BookBean>();
		try{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(file);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			
			BookBean book = new BookBean();
			
			while(eventReader.hasNext()){
				XMLEvent event = eventReader.nextEvent();
				if(event.isStartElement()){
					StartElement startElement = event.asStartElement();
					if(startElement.getName().getLocalPart() == (BOOK)){
						book = new BookBean();
						Iterator<Attribute> attributes = startElement.getAttributes();
						while(attributes.hasNext()){
							Attribute attribute = attributes.next();
							if(attribute.getName().toString().equalsIgnoreCase(GENRE)){
								book.setGenre(attribute.getValue());
							}
						}
						
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(ID)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							System.out.println("Field ID is empty");
							break;
						}
						book.setID(Integer.parseInt(event.toString()));
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + ID);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart() == (BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(ENTITLE)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							book.setEnglishTitle("");
							continue;
						}
						book.setEnglishTitle(event.toString());
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + ENTITLE);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart() == (BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(ORTITLE)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							System.out.println("Field Original title is empty");
							break;
						}
						book.setOriginalTitle(event.toString());
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + ORTITLE);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(AUTHOR)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							book.setAuthor("");
							continue;
						}
						book.setAuthor(event.asCharacters().getData());
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + AUTHOR);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(YEAR)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							book.setYear(0);
							continue;
						}
						book.setYear(Integer.parseInt(event.asCharacters().getData()));
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + YEAR);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(COUNTRY)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							book.setCountry("");
							continue;
						}
						book.setCountry(event.asCharacters().getData());
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + COUNTRY);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(PRICE)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							System.out.println("Field Price is empty");
							break;
						}
						Double.parseDouble(event.asCharacters().getData());
						book.setPrice(event.asCharacters().getData());
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + PRICE);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}else if(startElement.getName().getLocalPart().equalsIgnoreCase(LEFT)){
						event = eventReader.nextEvent();
						if(event.isEndElement()){
							System.out.println("Field Left is empty");
							break;
						}
						book.setLeft(Integer.parseInt(event.asCharacters().getData()));
						event = eventReader.nextEvent();
						if(event.isEndElement() == false){
							System.out.println("No close tag " + LEFT);
						}else if (event.isEndElement()){
							if(event.asEndElement().getName().getLocalPart().equalsIgnoreCase(BOOK)){
								System.out.println("unexpected product close tag");
								break;
							}
						}
						continue;
					}
					}else if(event.isEndElement()){
						EndElement endElement = event.asEndElement();
						if(endElement.getName().getLocalPart() == (BOOK)){
							books.add(book);
						}
				}
			}
		}catch(FileNotFoundException | XMLStreamException e){
			e.printStackTrace();
		}catch(NumberFormatException e){
			System.out.println("Not a number in field");
			e.printStackTrace();
		}
		return books;
	}
	

	
	public static void writeXML(File file, JTable table, String[] colNames){
		try {
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(file));
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			
			StartDocument startDocument = eventFactory.createStartDocument();
		    eventWriter.add(startDocument);
		    
		    StartElement bookstoreStartElement = eventFactory.createStartElement("", "", "bookstore");
		    eventWriter.add(bookstoreStartElement);
		    eventWriter.add(end);
		    
		    for(int i = 0; i < table.getRowCount(); i++){
		    	StartElement bookStartElement = eventFactory.createStartElement("", "", BOOK);
		    	eventWriter.add(bookStartElement);
		    	eventWriter.add(eventFactory.createAttribute("genre", table.getValueAt(i, table.getColumnModel().getColumnIndex("genre")).toString()));
		    	eventWriter.add(end);
		    	for(int j = 0; j < table.getColumnCount(); j++){
		    		if(colNames[j].equalsIgnoreCase("genre") == false){
		    			Object value = table.getValueAt(i, j);
		    			if(value != null)
		    				createNode(eventWriter, colNames[j], value.toString());
		    			else if(value == null)
		    				createNode(eventWriter, colNames[j], "");
		    		}
		    			
		    	}
		    	eventWriter.add(eventFactory.createEndElement("", "", BOOK));
		    }
		    eventWriter.add(eventFactory.createEndDocument());
		    eventWriter.close();
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	 private static void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {

		    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		    XMLEvent end = eventFactory.createDTD("\n");
		    XMLEvent tab = eventFactory.createDTD("\t");
		    // create Start node
		    StartElement sElement = eventFactory.createStartElement("", "", name);
		    eventWriter.add(tab);
		    eventWriter.add(sElement);
		    // create Content
		    Characters characters = eventFactory.createCharacters(value);
		    eventWriter.add(characters);
		    // create End node
		    EndElement eElement = eventFactory.createEndElement("", "", name);
		    eventWriter.add(eElement);
		    eventWriter.add(end);

		  }

}
