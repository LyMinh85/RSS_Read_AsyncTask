package com.example.rss_read_asynctask;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RssHandler extends DefaultHandler {
    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LINK = "link";
    private boolean inItem = false;

    private List<RssFeedItem> rssFeedItems;
    private StringBuilder elementValue;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        //...
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case CHANNEL:
                rssFeedItems = new ArrayList<>();
                break;
            case ITEM:
                rssFeedItems.add(new RssFeedItem());
                inItem = true;
                break;
            case TITLE:
                elementValue = new StringBuilder();
                break;
            case DESCRIPTION:
                elementValue = new StringBuilder();
                break;
            case LINK:
                elementValue = new StringBuilder();
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (inItem) {
            switch (qName) {
                case TITLE:
                    latestItem().setTitle(elementValue.toString());
                    break;
                case DESCRIPTION:
                    latestItem().setDescription(elementValue.toString());
                    break;
                case LINK:
                    latestItem().setLink(elementValue.toString());
                    break;
            }
        }
        if (Objects.equals(qName, ITEM)) {
            inItem = false;
        }
    }

    private RssFeedItem latestItem() {
        int latestRssItem = rssFeedItems.size() - 1;
        return rssFeedItems.get(latestRssItem);
    }

    public List<RssFeedItem> getListItem() {
        return rssFeedItems;
    }
}

