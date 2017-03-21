package com.example.spenc.uoitstudyroom;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

class ScrapeBookingsTask extends AsyncTask<DataScraper, Void, Integer> {

    private ProgressDialog dialog;
    ArrayList<Booking> bookingList = new ArrayList<>();
    DataScraper dataScraper = new DataScraper();

    ScrapeBookingsTask(MainActivity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
//        dialog.setMessage("Loading bookings... please wait.");
//        dialog.show();
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        System.out.println("Bytes Read: " + result);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected Integer doInBackground(DataScraper... param) {
        DataScraper dataScraper = param[0];
        String[] formData = new String[4];

        long startTime = System.currentTimeMillis();

        char[] cbuf = dataScraper.getRawHtml();
        Parser parser = new Parser(cbuf);

        formData[0] = "ctl00$ContentPlaceHolder1$Calendar1";
        formData[1] = parser.select("input", "name", "__VIEWSTATE").get(0).getAttribute("value");
        formData[2] = parser.select("input", "name", "__VIEWSTATEGENERATOR").get(0).getAttribute("value");
        formData[3] = parser.select("input", "name", "__EVENTVALIDATION").get(0).getAttribute("value");

        // Get dates

        ArrayList<Element> elems = parser.select("table", "id", "ContentPlaceHolder1_Calendar1");
        ArrayList<Element> aElems = parser.getElements(elems, "a", "href");

        ArrayList<String> ids = new ArrayList<>();

        for (Element e : aElems) {
            String link = e.getAttribute("href");
            ids.add(e.getID(link));
        }

        // Get all postings by date
        for (String id : ids) {
            System.out.println(id);
            int idInt = Integer.parseInt(id);

            cbuf = dataScraper.postDate(idInt, formData);
            parser = new Parser(cbuf);
            elems = parser.select("table", "id", "ContentPlaceHolder1_Table1");
            ArrayList<Element> bookingElems = parser.getElements(elems, "a", "href");

            for (Element elem : bookingElems) {
                Booking booking = parseBookingData(elem.getAttribute("href"));
                bookingList.add(booking);
            }
        }

//        dataScraper.postDate(6289,formData);
//
//        //TODO: Scrape updated eventvalidation and viewstate for posting a certain booking
//        cbuf = dataScraper.selectBooking("LIB305","8:30 PM",0);
//
//        //TODO: Post booking with retrieved data
//        dataScraper.postBooking(formData);

        long totalTime = System.currentTimeMillis() - startTime;

        //System.out.println(new String(cbuf));
        System.out.println("Scraped bookings in " + totalTime + " ms.");

        return cbuf.length;
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
}