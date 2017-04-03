# UOITStudyRoom
An Android application that displays, manages and books study room sessions at UOIT. Without any external libraries, the application interacts with UOIT/DC's library study room management system by manually sending HTTP requests via SSL sockets.

[View Domain Model](https://github.com/SpencerCBryson/UOITStudyRoom/wiki)

## Acknowledgements
This application was designed by three second-year Computer Science students as a course project for Software Systems Development & Integration at UOIT.

Use of this application is subject to the [terms and conditions](https://rooms.library.dc-uoit.ca/uoit_studyrooms/instruction.aspx), this application is no way endorsed by UOIT or DC.

## Data Scraper
Handles all connections to UOIT's library study room booker, and scrapes the needed non-parsed data in order to build a list of bookings.
SSL sockets are used to send HTTP headers to the webserver to fetch the raw HTML that is passed off to the Parser class to extract needed data. The scraper also provides the functionality for posting new bookings.
### Current features
- GETs [Calender.aspx](https://rooms.library.dc-uoit.ca/uoit_studyrooms/calendar.aspx) in order to gather available dates.
- POSTs the required ASP.NET form-data in order to display the bookings for the available dates.
- GETs Book.aspx to gather required data to create a booking.
- POSTs Book.aspx with given data in order to create a booking.

### Future
- Handle joining a booking
- Handle managing bookings

## Parser
Robust HTML parser coded from scratch based on the needs of the application. It gathers all the required data about date, times and bookings from the raw HTML fetched from the data scraper.

## User Interface
The user interface was designed to be very minimalistic, and straight to the point. This allows for a very fast and robust alternative to the outdated browser based booking system.
### Current components
- **Login activity:** can be skippped to view bookings, however it is mandatory for posting a booking
- **Main activity:** allows for a valid date to be selected, and displays all rooms with their corresponding number of open or partial bookings
- **Room activity:** displays all available open or partial bookings
- **Join activity:** allows the user to create a booking or join an existing partial booking

### Possible future components
- A way to manage/leave your existing bookings
- A toggle to view taken bookings, with the corresponding group name (it is already booked, so why join?)


