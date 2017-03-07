# UOITStudyRoom
Displays, manages and books study room sessions at UOIT.
[View Domain Model](https://github.com/SpencerCBryson/UOITStudyRoom/wiki)

## Data Scraper
Connects to UOIT's library study room booker, and scrapes the needed non-parsed data in order to build a list of bookings.
SSL sockets are used to send HTTP headers to the webserver to fetch the raw HTML that is passed off to the Parser class to extract needed data.
### Current
- GETs [Calender.aspx](https://rooms.library.dc-uoit.ca/uoit_studyrooms/calendar.aspx) in order to gather available dates.
- POSTs the required ASP.NET form-data in order to display the pages for the available dates.

### Future
- POSTs user login information to web server in order to create, join and manage bookings.
- Scrapes viewjoinorleave.aspx to retrieve specific booking information, such as the groups who have signed up.

## Parser
To be completed.
