package com.example.spenc.uoitstudyroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

/**
 * Scrapes various sorts of needed data from UOIT's library website
 */

//TODO: Open and close sockets more less often to reduce overhead

class DataScraper {

    private Socket socket = null;
    private String sessionID = null;

    private void connect() {
        try {
            // Must connect over a secure socket layer (port 443)
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.socket = ssf.createSocket(InetAddress.getByName("rooms.library.dc-uoit.ca"), 443);
            //System.out.println("Opened socket!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if(socket != null && !socket.isClosed()) {
            try {
                this.socket.close();
                //System.out.println("Closed socket.");
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean probeSocket() {
        return this.socket != null && this.socket.isConnected();
    }

    char[] getRawHtml() {
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

                cbuf = receive();

            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle no connection
            System.out.println("[ERROR] No connection established!");
        }
        return cbuf;
    }

    char[] getRawLoginHtml() {
        char[] cbuf = null;

        connect();

        if(probeSocket()) {
            try {
                // Send a GET header to the HTTP server to receive HTML data of the page we want
                PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
                pw.print("GET /uoit_studyrooms/myreservations.aspx HTTP/1.1\r\n");
                pw.print("Host: rooms.library.dc-uoit.ca\r\n");
                pw.print("Connection: keep-alive\r\n");
                pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n\r\n");
                pw.flush();

                cbuf = receive();

            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle no connection
            System.out.println("[ERROR] No connection established!");
        }
        return cbuf;
    }

    private char[] receive() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        String line;
        int content_length = 0;
        int max_iter = 20;

        // Retrieve header contents
        //System.out.println(br.readLine());
        for(int i = 0; i < max_iter; i++) {
            line = br.readLine();
            if (line.contains("Set-Cookie: ASP.NET_SessionId")) { // scrape session ID cookie
                if(sessionID == null) this.sessionID = "ASP.NET_SessionId" + line.substring(29,54);
                System.out.println(this.sessionID);
            } else if (line.contains("Content-Length:")) {
                String num = line.substring(16);
                content_length = Integer.parseInt(num);
                break;
            }
        }

        // No data was received
        if(content_length == 0)
            System.out.println("[ERROR] No suitable content found.");
        //TODO: inform user of possible connection issue

        char[] cbuf = new char[content_length];

        int total_bytes_read = 0;

        while(content_length > 0) {
            int bytes_read = br.read(cbuf, total_bytes_read, content_length);
            content_length -= bytes_read;
            total_bytes_read += bytes_read;
        }

        br.close();

        return cbuf;
    }

    private char[] sendPayload(String payload, int bookingState) throws IOException {
        char[] cbuf = null;
        String next;
        int cLength = payload.length();

        if(bookingState == 0) // open booking
            next = "book.aspx";
        else if(bookingState == 1) // partial booking
            next = "joinorleave.aspx";
        else if(bookingState == 2) // complete booking
            next = "viewleaveorjoin.aspx";
        else if(bookingState == 3) // login validation
            next = "myreservations.aspx";
        else
            next = "joingroup.aspx";


        connect();

        if(probeSocket()) {
            // Send a GET header to the HTTP server to receive HTML data of the page we want
            PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
            pw.print("POST /uoit_studyrooms/" + next + " HTTP/1.1\r\n");
            pw.print("Host: rooms.library.dc-uoit.ca\r\n");
            pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n");
            pw.print("Origin: https://rooms.library.dc-uoit.ca\r\n");
            pw.print("Referer: https://rooms.library.dc-uoit.ca/uoit_studyrooms/" + next + "\r\n");
            pw.print("Cookie: " + this.sessionID + "\r\n");
            pw.print("Content-Type: multipart/form-data; boundary=----BookingBoundary7MA4YWxkTrZu0gW\r\n");
            pw.print("Content-Length: " + cLength + "\r\n\r\n");

            pw.print(payload);

            pw.flush();

            cbuf = receive();

            disconnect();
        }

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
                pw.print("Origin: https://rooms.library.dc-uoit.ca\r\n");
                pw.print("Content-Type: multipart/form-data; boundary=----BookingBoundary7MA4YWxkTrZu0gW\r\n");
                pw.print("Content-Length: " + cLength + "\r\n\r\n");

                pw.print(payload);

                pw.flush();

                cbuf = receive();

                disconnect();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: Handle no connection
            System.out.println("[ERROR] No connection established!");
        }

        return cbuf;
    }

    char[] selectBooking(String room, String time, int bookingState) {
        char[] cbuf = null;
        String next;

        connect();

        String referer = "Referer: https://rooms.library.dc-uoit.ca/uoit_studyrooms/calendar.aspx\r\n";

        if(bookingState == 0) // open booking
            next = "book.aspx";
        else if(bookingState == 1) // partial booking
            next = "joinorleave.aspx";
        else if(bookingState == 2) // complete booking
            next = "viewleaveorjoin.aspx";
        else {
            next = "joingroup.aspx";
            referer = "Referer: https://rooms.library.dc-uoit.ca/uoit_studyrooms/joinorleave.aspx\r\n";
        }

        if(probeSocket()) {
            try {
                // Send a request to temp.aspx in order to perform server sided booking selection for
                // our session.
                PrintWriter pw = new PrintWriter(this.socket.getOutputStream());
                pw.print("GET /uoit_studyrooms/temp.aspx?starttime=" + time.replace(" ","%20") + "&room=" + room + "&next=" + next + " HTTP/1.1\r\n");
                pw.print("Host: rooms.library.dc-uoit.ca\r\n");
                pw.print("Connection: keep-alive\r\n");
                pw.print(referer);
                pw.print("Cookie: " + this.sessionID + "\r\n");
                pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n\r\n");
                pw.flush();

                cbuf = receive();

                pw.print("GET /uoit_studyrooms/" + next + " HTTP/1.1\r\n");
                pw.print("Host: rooms.library.dc-uoit.ca\r\n");
                pw.print("Connection: keep-alive\r\n");
                pw.print(referer);
                pw.print("Cookie: " + this.sessionID + "\r\n");
                pw.print("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64)\r\n\r\n");
                pw.flush();

                cbuf = receive();

                disconnect();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return cbuf;
    }

    char[] postLogin(HashMap<String, String> postData) {
        char[] cbuf = null;
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + postData.get("evalid") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + postData.get("vstate") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + postData.get("vstategen") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxPassword\"\r\n\r\n" + postData.get("password") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxID\"\r\n\r\n" + postData.get("studentid") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$ButtonListBookings\"\r\n\r\n" + "My Bookings" + cr +
                        "--" + boundary + "--";

        try {
            cbuf = sendPayload(payload, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cbuf;
    }

    char[] postBooking(HashMap<String,String> postData) {
        char[] cbuf = null;
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + postData.get("evalid") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + postData.get("vstate") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + postData.get("vstategen") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$ButtonReserve\"\r\n\r\n" + postData.get("btnreserve") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$RadioButtonListDuration\"\r\n\r\n" + postData.get("duration") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$RadioButtonListInstitutions\"\r\n\r\n" + postData.get("institution") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxGroupCode\"\r\n\r\n" + postData.get("groupcode") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxName\"\r\n\r\n" + postData.get("groupname") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxNotes\"\r\n\r\n" + postData.get("notes") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxPassword\"\r\n\r\n" + postData.get("password") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxStudentID\"\r\n\r\n" + postData.get("studentid") + cr +
                        "--" + boundary + "--";

        try {
            cbuf = sendPayload(payload, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cbuf;
    }

    char[] selectPartialBooking(HashMap<String,String> postData) {
        char[] cbuf;
        String btnPlaceHolder, radioPlaceHolder;
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        if(postData.get("btn").equals("Create or Join a Group")) {
            btnPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$ButtonJoinOrCreate\"\r\n\r\n";
            radioPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$RadioButtonListJoinOrCreateGroup\"\r\n\r\n";
        } else {
            btnPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$ButtonLeave\"\r\n\r\n";
            radioPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$RadiobuttonListLeaveGroup\"\r\n\r\n";
        }

        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + postData.get("evalid") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + postData.get("vstate") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + postData.get("vstategen") + cr +
                        "--" + boundary + cr + cd + btnPlaceHolder + postData.get("btn") + cr +
                        "--" + boundary + cr + cd + radioPlaceHolder + postData.get("radio") + cr +
                        "--" + boundary + "--";
        try {
            sendPayload(payload, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cbuf = selectBooking(postData.get("room"), postData.get("time"), 4);

        return cbuf;
    }

    char[] joinPartialBooking(HashMap<String,String> postData) {
        char[] cbuf = null;
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + postData.get("evalid") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + postData.get("vstate") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + postData.get("vstategen") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxPassword\"\r\n\r\n" + postData.get("password") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxID\"\r\n\r\n" + postData.get("studentid") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$ButtonJoin\"\r\n\r\n" + postData.get("btn") + cr +
                        "--" + boundary + "--";
        try {
            cbuf = sendPayload(payload, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cbuf;
    }

    char[] existingBooking(HashMap<String,String> postData) {
        char[] cbuf = null;
        String btnPlaceHolder;
        String boundary = "----BookingBoundary7MA4YWxkTrZu0gW";
        String cr = "\r\n";
        String cd = "Content-Disposition: form-data; ";

        if(postData.get("btn").equals("Join"))
            btnPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$ButtonJoinGroup\"\r\n\r\n";
        else
            btnPlaceHolder = "name=\"ctl00$ContentPlaceHolder1$ButtonLeaveGroup\"\r\n\r\n";

        String payload =
                "--" + boundary + cr + cd + "name=\"__EVENTVALIDATION\"\r\n\r\n" + postData.get("evalid") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATE\"\r\n\r\n" + postData.get("vstate") + cr +
                        "--" + boundary + cr + cd + "name=\"__VIEWSTATEGENERATOR\"\r\n\r\n" + postData.get("vstategen") + cr +
                        "--" + boundary + cr + cd + btnPlaceHolder + postData.get("btn") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxPassword\"\r\n\r\n" + postData.get("password") + cr +
                        "--" + boundary + cr + cd + "name=\"ctl00$ContentPlaceHolder1$TextBoxStudentID\"\r\n\r\n" + postData.get("studentid") + cr +
                        "--" + boundary + "--";
        try {
            cbuf = sendPayload(payload, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cbuf;


    }

}
