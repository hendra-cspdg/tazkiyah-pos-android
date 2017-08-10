/*
 * DownloadJSON from api (web service)
 */

package net.muhajirin.solusitoko;



/*
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
 
*/
     


/*
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
*/



/*
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
*/

import java.net.HttpURLConnection;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class DownloadJSON extends android.os.AsyncTask<String, Void, String> {

/*
ArrayList<HashMap<String, String>> arraylist;
static String RANK = "rank";
static String COUNTRY = "country";
static String POPULATION = "population";
static String FLAG = "flag";
*/


    private int CONNECTION_TIMEOUT = 10*1000;    //in milliseconds
    private int READ_TIMEOUT = 2*60*1000;    //in milliseconds
    //private final HttpClient Client = new DefaultHttpClient();
    private android.app.ProgressDialog progress;    // = new ProgressDialog( retail.get_my_app_context() );
    //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
         
    protected void onPreExecute() {    // NOTE: You can call UI Element here.
        progress = android.app.ProgressDialog.show( retail.get_my_app_context(), "Sedang menghubungi server", "Mohon tunggu ...", true, true);    //progress.setIndeterminate(true);        //progress.setMessage("Mohon Tunggu...");        //progress.show();
    }
    protected String doInBackground( String... args ) {    //{ url, Authorization, params }    //"client_name=client_a&api_key=abc123"
        java.net.URL url;
        try {
            url = new java.net.URL(args[0]);
        } catch( java.net.MalformedURLException e ) {
            e.printStackTrace();
            return e.toString();
        }

        HttpURLConnection con;
        try {
android.util.Log.e("onapi: ", "1");




//urlConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            con = (HttpURLConnection) url.openConnection();    //?! con.setUseCaches(true);
            con.setRequestMethod("POST");

            //con.setRequestProperty("Host", "muhajirin.net");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");    //"application/x-www-form-urlencoded;charset=UTF-8"
android.util.Log.e("onapi: ", "4");
            //con.setRequestProperty("charset", "utf-8");
android.util.Log.e("onapi454s: ", " args[1]=" + args[1] + " post data=" + args[2] );
            if( args.length>1 && args[1].length()>0 ) {
                byte[] auth=null;
                //auth=args[1].getBytes();
                try { auth=args[1].getBytes( "UTF-8" );    } catch ( java.io.UnsupportedEncodingException ex) { android.util.Log.e("oncharset: ", "error: " + ex.toString() );}
android.util.Log.e("onapi: ", "auth=" + "Basic " + android.util.Base64.encodeToString( auth, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP ) + "6" );    //DEFAULT
                con.setRequestProperty( "Authorization", "Basic " + android.util.Base64.encodeToString( auth, android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP ) + "6" );    //URL_SAFE//DEFAULT    //"Basic {base64encode(*client_token*)}"
            }
android.util.Log.e("onapi: ", "5");

            //con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setReadTimeout(READ_TIMEOUT);    con.setConnectTimeout(CONNECTION_TIMEOUT);
android.util.Log.e("onapi: ", "2");
            //con.setRequestProperty("Cache-Control", "max-age=0");    //max-age=0: force a cached response to be validated by the server
android.util.Log.e("onapi: ", "3");
            con.setUseCaches(false);
            //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; PPC; en-US; rv:1.3.1)");
            //con.setRequestProperty("User-Agent", "my-rest-app-v0.1");
            //con.setInstanceFollowRedirects(false);


            if( args.length>2 && args[2].length()>0 ) {    //ada params yg ingin diPOST
android.util.Log.e("onapi: ", "6");
                con.setDoOutput(true);
android.util.Log.e("onapi: ", "61");
                byte[] post_data = null;
                //post_data = args[2].getBytes();
                try {
                    post_data = args[2].getBytes( "UTF-8" );    //args[2].getBytes( java.nio.charset.StandardCharsets.UTF_8 );
android.util.Log.e("onapi: ", "7");
                    con.setRequestProperty("Content-Length", Integer.toString( post_data.length ));
android.util.Log.e("onapi: ", "8");
                    java.io.DataOutputStream wr = new java.io.DataOutputStream(con.getOutputStream());    wr.write( post_data );    wr.flush();    //java.io.OutputStreamWriter wr = new java.io.OutputStreamWriter(con.getOutputStream());    wr.write( args[2] );    wr.flush();    
                    //ini sering ga tereksekusi >> con.getOutputStream().write( post_data );
android.util.Log.e("onapi: ", "9");
                } catch ( Exception ex ) {    //java.io.UnsupportedEncodingException ex) 
                    return "Error: Gagal mengakses " + args[0] + "\nRespon: " + ex.toString();
                }
            }


            //con.setDoInput(true); 
android.util.Log.e("onapi: ", "10" );

//android.util.Log.e("onapi: ", "stream: " + con.getInputStream().toString() );



/*
Or you can create a generic method to build key value pattern which is required for application/x-www-form-urlencoded.

private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for(Map.Entry<String, String> entry : params.entrySet()){
        if (first)
            first = false;
        else
            result.append("&");    
        result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
    }    
    return result.toString();
}
*/

        } catch (IOException e1) {
            e1.printStackTrace();
            return e1.toString();
        }

        String result;    //to return
        BufferedReader reader=null;
        try {
            if( con.getResponseCode()==HttpURLConnection.HTTP_OK || con.getResponseCode()==HttpURLConnection.HTTP_BAD_REQUEST ) {    //read response
android.util.Log.e("onapi: ", "11");
//klo password salah, terjadi exception setelah ini
                reader = new BufferedReader( new InputStreamReader( con.getInputStream(), "UTF-8" ) );
                String line;
                StringBuilder sb = new StringBuilder();
                while( (line = reader.readLine()) != null ) {
android.util.Log.e("onapi: ", "line=" + line);
sb.append(line);
android.util.Log.e("onapi: ", "after sb.append" );
}
                                                          //sb.append(line + "
//");
android.util.Log.e("onapi: ", "12" );
                result = sb.toString();
android.util.Log.e("onapi: ", "13" );
            } else
                result = "Error: Gagal mengakses " + args[0] + "\nRespon: " + con.getResponseMessage();
        } catch( IOException e ) {
            e.printStackTrace();
            return "Error: Gagal mengakses " + args[0] + "\nMohon pastikan internet tersambung" + ( args[0].equals( retail.db.cfg.get("url_user_login") ) ? " dan isian email dan password sudah benar!" : "" ) + "\nRespon: " + e.toString();
        } finally {
android.util.Log.e("onapi: ", "13" );
            if( reader!=null ) try { reader.close(); } catch( IOException e ) { e.printStackTrace(); } 
android.util.Log.e("onapi: ", "14" );
            con.disconnect();
android.util.Log.e("onapi: ", "15" );
        }
android.util.Log.e("onapi: ", "16 "  + result );
        return result;    //Pass data to onPostExecute method


/*
	// Create an array
arraylist = new ArrayList<HashMap<String, String>>();
// Retrieve JSON Objects from the given URL address
jsonobject = JSONfunctions
.getJSONfromURL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
 
try {
// Locate the array name in JSON
jsonarray = jsonobject.getJSONArray("worldpopulation");
 
for (int i = 0; i < jsonarray.length(); i++) {
HashMap<String, String> map = new HashMap<String, String>();
jsonobject = jsonarray.getJSONObject(i);
// Retrive JSON Objects
map.put("rank", jsonobject.getString("rank"));
map.put("country", jsonobject.getString("country"));
map.put("population", jsonobject.getString("population"));
map.put("flag", jsonobject.getString("flag"));
// Set the JSON Objects into the array
arraylist.add(map);
}
} catch (JSONException e) {
Log.e("Error", e.getMessage());
e.printStackTrace();
}
*/

/*

        if( con.getResponseCode()==200 ) {    //read response
            InputStreamReader responseBodyReader = new InputStreamReader( con.getInputStream(), "UTF-8" );
            JsonReader jsonReader = new JsonReader( responseBodyReader );
            jsonReader.beginObject(); // Start processing the JSON object
            while( jsonReader.hasNext() ) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                //{
    "client_label": "Client A",
    "client_token": "bv8bmzhhPBYZTOppVqREIzT-GgZi8pAU"
}
                if (key.equals("client_label")) { // Check if desired key
                    // Fetch the value as a String
                    String value = jsonReader.nextString();
                    break; // Break out of the loop
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }
            jsonReader.close();
            con.disconnect();
        } else {
            // Error handling code goes here
        }

*/


    }

    protected void onPostExecute( String result ) {
android.util.Log.e("onpostexec: ", "1");
        progress.dismiss();    //Utils.dismissDialog( progress );
android.util.Log.e("onpostexec: ", "2");

        if( result.startsWith( "Error:" ) ) {
            retail.show_error( "\n" + result + "\n\n\n\n", "Koneksi Gagal" );
            return;
        }

        /* proceed the data ....

//for example ....
        List<DataFish> data = new ArrayList<>();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.fishImage= json_data.getString("fish_img");
                    fishData.fishName= json_data.getString("fish_name");
                    fishData.catName= json_data.getString("cat_name");
                    fishData.sizeName= json_data.getString("size_name");
                    fishData.price= json_data.getInt("price");
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                mRVFishPrice = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterFish(MainActivity.this, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }



//or this example ...

	// Locate the listview in listview_main.xml
listview = (ListView) findViewById(R.id.listview);
// Pass the results into ListViewAdapter.java
adapter = new ListViewAdapter(MainActivity.this, arraylist);
// Set the adapter to the ListView
listview.setAdapter(adapter);




              
//or this example ....                 
             //Start Parse Response JSON Data 
                 
                String OutputData = "";
                JSONObject jsonResponse;
                       
                try {
                       
                     //Creates a new JSONObject with name/value mappings from the JSON string
                     jsonResponse = new JSONObject(Content);
                       
                     //Returns the value mapped by name if it exists and is a JSONArray.
                     //Returns null otherwise. 
                     JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
                       
                     //Process each JSON Node 
 
                     int lengthJsonArr = jsonMainNode.length(); 
   
                     for(int i=0; i < lengthJsonArr; i++) {
                         //Get Object for each JSON node.
                         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                           
                         //Fetch node values
                         String name       = jsonChildNode.optString("name").toString();
                         String number     = jsonChildNode.optString("number").toString();
                         String date_added = jsonChildNode.optString("date_added").toString();
                           
                         
                         OutputData += " Name           : "+ name +"
 "
                                     + "Number      : "+ number +"
 "
                                     + "Time                : "+ date_added +"
 "
                                     +"--------------------------------------------------
";
                         
                          
                    }
                 //End Parse Response JSON Data
                      
                     //Show Parsed Output on screen (activity)
                     jsonParsed.setText( OutputData );
                      
                 } catch (JSONException e) {
                      e.printStackTrace();
                 }
   













//or this example ...
    videos = new ArrayList<Video>();
 
    try {
      JSONObject json = new JSONObject(res);
      JSONObject dataObject = json.getJSONObject("data");
      JSONArray items = dataObject.getJSONArray("items");
 
      for (int i = 0; i < items.length(); i++) {
        JSONObject videoObject = items.getJSONObject(i);
        Video video = new Video(videoObject.getString("title"),
                                videoObject.getString("description"),
                                videoObject.getJSONObject("player")
                                           .getString("default"),
                                videoObject.getJSONObject("thumbnail")
                                           .getString("sqDefault"));
 
        videos.add(video);
      }
 
    } catch (JSONException e) {
      // manage exceptions
    }
 
    mVideosAdapter = new VideosListAdapter(MainActivity.this, videos);
    mVideosLv.setAdapter(mVideosAdapter);
    // dismiss progress dialog




*/


    }    
}