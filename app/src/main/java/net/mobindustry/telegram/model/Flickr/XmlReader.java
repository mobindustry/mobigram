package net.mobindustry.telegram.model.Flickr;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlReader {
    public static PhotosFlickr read(URL url) throws SAXException, IOException {

        return read(url.openStream());

    }

    public static PhotosFlickr read(InputStream stream) throws SAXException, IOException {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            XmlHandler handler = new XmlHandler();
            InputSource input = new InputSource(stream);

            reader.setContentHandler(handler);
            reader.parse(input);

            return handler.getResult();

        } catch (ParserConfigurationException e) {
            throw new SAXException();
        }

    }

    public static PhotosFlickr read(String source) throws SAXException, IOException {
        return read(new ByteArrayInputStream(source.getBytes()));
    }

}
