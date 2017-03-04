package com.example.spenc.uoitstudyroom;

import android.os.AsyncTask;
import java.io.IOException;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

public class ScrapeBookingsTask extends AsyncTask<DataScraper, Void, Integer> {

    @Override
    protected Integer doInBackground(DataScraper... param) {
        DataScraper dataScraper = (DataScraper) param[0];

        char[] cbuf = dataScraper.getRawHtml();

        // Raw HTML in cbuf will be passed to another class to parse it

        return cbuf.length;
    }

    protected void onPostExecute(Integer result)
    {
        System.out.println("Bytes Read: " + result);
    }

}