package com.example.spenc.uoitstudyroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

/**
 * Scrapes various sorts of needed data from UOIT's library website
 */

class DataScraper {

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
        if(socket != null && !socket.isClosed()) {
            try {
                this.socket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean probeSocket() {
        return this.socket != null && this.socket.isConnected();
    }

    public char[] getRawHtml() {
        char[] cbuf = null;

        connect();

        if(probeSocket()) {
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

            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle no connection
            System.out.println("[ERROR] No connection established!");
        }

        disconnect();

        return cbuf;
    }

    char[] postDate(int dateID, String[] formData) {
        // ASP.NET form-data
        String eTarget = formData[0];
        String vState = formData[1];
        String vStateGen = formData[2];
        String eValidation = formData[3];
        String eArgument = Integer.toString(dateID);
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        // Create data payload for ASP.NET form
        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTTARGET\"\r\n\r\n" + eTarget + cr +
                "--" + boundary + cr + cd + "name=\"__EVENTARGUMENT\"\r\n\r\n" + eArgument + cr +
                "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + vState + cr +
                "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + vStateGen + cr +
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + eValidation + cr +
                "--" + boundary + "--";

        int cLength = payload.length();

        char[] cbuf = null;

        connect();

        if(probeSocket()) {
            try {
                // Send a GET header to the HTTP server to receive HTML data of the page we want
                PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
                pw.print("POST /uoit_studyrooms/calendar.aspx HTTP/1.1\r\n");
                pw.print("Host: rooms.library.dc-uoit.ca\r\n");
                pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n");
                pw.print("Content-Type: multipart/form-data; boundary=----BookingBoundary7MA4YWxkTrZu0gW\r\n");
                pw.print("Content-Length: " + cLength + "\r\n\r\n");

                pw.print(payload);

                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

                String line;
                int content_length = 0;
                int max_iter = 30;

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
