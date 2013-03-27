package com.damagedapps.UCAD.helper;

import com.damagedapps.UCAD.model.GoogleCalendarEvent;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class CalendarParser extends DefaultHandler {
    GoogleCalendarEvent mGoogleCalendarEventTemp = new GoogleCalendarEvent();
    String mCurrentString = "";
    ArrayList<GoogleCalendarEvent> mGoogleCalendarEvents = new ArrayList<GoogleCalendarEvent>();
    String mResponse;

    public CalendarParser(String response) {
        mResponse = response;
        parseDocument();
    }

    private void parseDocument() {
        // parse
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(mResponse)), this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        if (elementName.equalsIgnoreCase("entry")) {
            mGoogleCalendarEventTemp = new GoogleCalendarEvent();
            mCurrentString = "";
        }
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if(element.equalsIgnoreCase("title"))
            mGoogleCalendarEventTemp.setTitle(mCurrentString);

        if(element.equalsIgnoreCase("content"))
            mGoogleCalendarEventTemp.setDescription(mCurrentString);

        if(element.equalsIgnoreCase("entry"))
            mGoogleCalendarEvents.add(mGoogleCalendarEventTemp);
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        mCurrentString = mCurrentString + new String(ac, i, j);
    }

    public ArrayList<GoogleCalendarEvent> getCalendarEvents() {
        for(GoogleCalendarEvent gce : mGoogleCalendarEvents) {
            gce.setDate(gce.getDescription().split(":")[11].trim());
            gce.setLocation(gce.getDescription().split("\n<br>")[2].replace("Where: ", ""));
            gce.setTitle(gce.getTitle().split(".000Z")[2]);
        }
        return mGoogleCalendarEvents;
    }
}
