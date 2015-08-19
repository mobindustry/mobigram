package net.mobindustry.mobigram.model.flickr;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XmlHandler extends DefaultHandler {
    private PhotosFlickr photosFlickr;
    private PhotoFlickr photoFlickr;
    private StringBuilder stringBuilder;

    @Override
    public void startDocument() {
        photosFlickr = new PhotosFlickr();
    }

    public PhotosFlickr getResult() {
        return photosFlickr;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        stringBuilder = new StringBuilder();

        if (qName.equals("photo") && photosFlickr != null) {
            photoFlickr = new PhotoFlickr();
            photoFlickr.setPhotoId(attributes.getValue("id"));
            photoFlickr.setOwner(attributes.getValue("owner"));
            photoFlickr.setSecret(attributes.getValue("secret"));
            photoFlickr.setServer(attributes.getValue("server"));
            photoFlickr.setFarm(attributes.getValue("farm"));
            photoFlickr.setCheck(false);
            photoFlickr.setLink("");
            photosFlickr.addPhotoFlickr(photoFlickr);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (photosFlickr != null && photoFlickr == null) {
            // Parse feed properties

            try {
                if (qName != null && qName.length() > 0) {
                    String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                    Method method = photosFlickr.getClass().getMethod(methodName, String.class);
                    try {
                        method.invoke(photosFlickr, stringBuilder.toString());
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }

        } else if (photoFlickr != null) {
            // Parse item properties
            try {
                if (qName.equals("content:encoded"))
                    qName = "content";
                String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                Method method = photoFlickr.getClass().getMethod(methodName, String.class);
                method.invoke(photoFlickr, stringBuilder.toString());
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }
}
