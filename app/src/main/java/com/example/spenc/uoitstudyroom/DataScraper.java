package com.example.spenc.uoitstudyroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Spenc on 2017-03-03.
 */

public class DataScraper {

    private Socket socket = null;

    private void connect() {
        try {
            // Must connect over a secure socket layer (port 443)
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.socket = ssf.createSocket(InetAddress.getByName("rooms.library.dc-uoit.ca"), 443);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if(!socket.isClosed()) {
            try {
                this.socket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean probeSocket() {
        if(this.socket != null)
            return this.socket.isConnected();
        else
            return false;
    }

    public char[] getRawHtml() {
        char[] cbuf = null;

        connect();

        if(this.socket.isConnected()) {
            try {
                // Send a GET header to the HTTP server to receive HTML data of the page we want
                PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
                pw.print("GET /uoit_studyrooms/calendar.aspx HTTP/1.1\r\n");
                pw.print("Host: rooms.library.dc-uoit.ca\r\n");
                pw.print("Connection: keep-alive\r\n");
                pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n\r\n");
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

                String line;
                int content_length = 0;
                int max_iter = 15;

                // Get length of content being received
                for(int i = 0; i < max_iter; i++) {
                    line = br.readLine();
                    if (line.contains("Content-Length:")) {
                        String num = line.substring(16);
                        content_length = Integer.parseInt(num);
                        break;
                    }
                }

                // No data was received
                if(content_length == 0)
                    System.out.println("[ERROR] No suitable content found.");
                //TODO: inform user of possible connection issue

                cbuf = new char[content_length];

                int total_bytes_read = 0;

                while(content_length > 0) {
                    int bytes_read = br.read(cbuf, total_bytes_read, content_length);
                    content_length -= bytes_read;
                    total_bytes_read += bytes_read;
                }

                br.close();

                //System.out.println("Bytes Read: " + total_bytes_read);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle no connection
            System.out.println("[ERROR] No connection established!");
        }

        return cbuf;
    }


}
