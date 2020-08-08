# MySimpleHTTPApp
This app reads Earthquake data from the USGS website. It makes an HTTP request using the query format provided by the website.
If a successful connections is established, a JSON response is returned.
The JSON response is then parsed and the data strucuture holding the data is modified accordingly. 
Then we call notifyDataSetChanged() from the recycler view adapter <br>

I followed the Udacity course in using the AsyncTask abstract class, as well as designing the layout of the list items. However, I used 
a recycler view instead of a listview. <br>
 
