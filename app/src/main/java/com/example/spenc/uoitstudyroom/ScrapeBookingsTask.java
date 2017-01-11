package com.example.spenc.uoitstudyroom;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Scrapes raw HTML data from website, needing to be parsed
 */

public class ScrapeBookingsTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {

        try {
            System.out.println("STARTING WEB SCRAPE\n");

            System.setProperty("javax.net.ssl.trustStore", "clienttrust");
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            Socket sock = ssf.createSocket(InetAddress.getByName("rooms.library.dc-uoit.ca"), 443);

            PrintWriter pw = new PrintWriter(sock.getOutputStream());
            pw.print("GET /uoit_studyrooms/calendar.aspx HTTP/1.1\r\n");
            pw.print("Host: rooms.library.dc-uoit.ca\r\n");
            pw.print("Connection: keep-alive\r\n");
            pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36\r\n\r\n");
            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String t;
            while((t = br.readLine()) != null) System.out.println(t);
            br.close();

            // Raw HTML will be passed to another class to parse it

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}