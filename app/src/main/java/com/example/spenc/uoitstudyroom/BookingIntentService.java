package com.example.spenc.uoitstudyroom;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingIntentService extends IntentService {

    public BookingIntentService() {
        super("BookingIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DataScraper dataScraper = new DataScraper();

        String[] formData = new String[4];
        Intent i = new Intent(SCRAPE_DONE);

        char[] cbuf = dataScraper.getRawHtml();
        Parser parser = new Parser(cbuf);

        // Necessary formData for the API
        formData[0] = "ctl00$ContentPlaceHolder1$Calendar1";
        formData[1] = parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value");
        formData[2] = parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value");
        formData[3] = parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value");

        // Get dates
        ArrayList<Element> elems = parser.select("table", "id", "ContentPlaceHolder1_Calendar1");
        ArrayList<Element> aElems = parser.getElements(elems, "a", "href");

        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> dateStrings = new ArrayList<>();

        for (Element e : aElems) {
            String link = e.getAttribute("href");
            ids.add(e.getID(link));
            dateStrings.add(e.getAttribute("title"));
        }

        // Get all postings by date
        for (String id : ids) {

            ArrayList<Booking> bookingList = new ArrayList<>();

            int idInt = Integer.parseInt(id);

            cbuf = dataScraper.postDate(idInt, formData);
            parser = new Parser(cbuf);
            elems = parser.select("table", "id", "ContentPlaceHolder1_Table1");
            ArrayList<Element> bookingElems = parser.getElements(elems, "a", "href");

            for (Element elem : bookingElems) {
                Booking booking = parseBookingData(elem.getAttribute("href"));
                bookingList.add(booking);
            }
            i.putExtra(id,bookingList);
        }

        i.putExtra("dates",ids);
        i.putExtra("formData",formData);
        i.putExtra("dateStrings", dateStrings);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        //notifyFinished(i);
    }

    Booking parseBookingData(String data) {
        int bookingState = 2;
        if (data.contains("next=book"))
            bookingState = 0;
        else if (data.contains("viewleaveorjoin"))
            bookingState = 1;

        String reg = "\\d{1,2}:..\\s[A|P]M";
        String reg_ = "LIB[a-zA-Z0-9]*";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(data);
        Pattern p_ = Pattern.compile(reg_);
        Matcher m_ = p_.matcher(data);

        m.find();
        m_.find();

        Booking booking = new Booking(m.group(), m_.group(), bookingState);
        return booking;
    }

    public static final String SCRAPE_DONE = "SCRAPE_DONE";

    private void notifyFinished(Intent i) {
        BookingIntentService.this.sendBroadcast(i);
    }
}
