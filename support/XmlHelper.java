package com.ru.gacklash.fluff.support;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnack_000 on 05.12.2015.
 */
public class XmlHelper {
    public static List readQuotes(InputStream str) throws XmlPullParserException, IOException {

        XmlPullParser parser = Xml.newPullParser();

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(str, "UTF-8");

        List quotes = new ArrayList();
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "root");

        while (parser.next() != XmlPullParser.END_TAG ) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("quote")) {
               /* while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String nameQuote = parser.getName();
                    // Starts by looking for the entry tag
                    if (nameQuote.equals("quote")) {*/
                if (parser.next() == XmlPullParser.TEXT) {
                    quotes.add(parser.getText());
                    parser.nextTag();
                }
               /*     } else {
                        skip(parser);
                    }
                }*/
            } else {
                skip(parser);
            }
        }
        return quotes;
    }
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
