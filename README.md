# MySimpleHTTPApp
This app reads Earthquake data from the USGS website. It makes an HTTP request using the query format provided by the website.
If a successful connections is established, a JSON response is returned.
The JSON response is then parsed and the data strucuture holding the data is modified accordingly. 
Then we call notifyDataSetChanged() from the recycler view adapter
