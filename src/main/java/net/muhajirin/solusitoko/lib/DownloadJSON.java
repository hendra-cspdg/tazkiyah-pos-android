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


    private int CONNECTION_TIMEOUT = 60*1000;    //in milliseconds
    private int READ_TIMEOUT = 2*60*1000;    //in milliseconds
    //private final HttpClient Client = new DefaultHttpClient();
    private android.app.ProgressDialog progress;    // = new ProgressDialog( retail.get_my_app_context() );
    //TextView jsonParsed = (TextView) findViewById(R.id.jsonParsed);
         
    protected void onPreExecute() {    // NOTE: You can call UI Element here.
        progress = android.app.ProgressDialog.show( retail.get_my_app_context(), "Sedang menghubungi server", "Mohon tunggu ...", true, true);    //progress.setIndeterminate(true);        //progress.setMessage("Mohon Tunggu...");        //progress.show();
    }
    protected String doInBackground( String... args ) {    //{ url, Authorization, params }    //"client_name=client_a&api_key=abc123"

if(1==2) return

"{"
+    "\"client_label\":\"Client A\","
+    "\"client_token\":\"bv8bmzhhPBYZTOppVqREIzT-GgZi8pAU\","

+    "\"user_name\":\"Rafinal Rafik\","
+    "\"access_token\":\"B8j4zwBZQjAQ-BcV0n5Go_pUK-ZcSWS3\","



+    "\"status\": 200,"
+    "\"message\": \"Success\","

+    "\"customer\":{\"id\":18,\"username\":\"rafik\",\"phone_number\":\"0893222222\",\"address\":\"jl. heappy nO. 1 & 2\",\"gender\":\"male\"},"
+    "\"order\":{\"id\":123, \"code\":\"xyz\"},"
//+    "\"order\":{\"id\":31,\"customer_id\":null,\"outlet_id\":1,\"code\":\"GTM3E\",\"tax\":\"0.00\",\"discount\":\"0.00\",\"total_price\":\"14000.00\",\"status\":\"ordered\",\"delivery_time\":null,\"note\":\"\",\"created_at\":1503954618,\"updated_at\":1503954619},\"items\":[{\"id\":30,\"order_id\":31,\"product_id\":9,\"product_label\":\"Lemon Tea\",\"product_attribute_id\":null,\"quantity\":\"2.00\",\"discount\":\"0.00\",\"unit_price\":\"7000.00\",\"status\":\"paid\",\"note\":\"\",\"created_at\":1503954619,\"updated_at\":1503954619}]},"


+    "\"customers\":["
+        "{"
        +    "\"id\":1,"
        +    "\"username\":\"Abu Hurairah\","
        +    "\"phone_number\":\"080000000XXX\""
+        "},"
+        "{\"id\":5,\"username\":\"Ali Musthafa\",\"phone_number\":\"080000000XXX\"},{\"id\":2,\"username\":\"Amr Ma'ruf \",\"phone_number\":\"080000000XXX\"},{\"id\":17,\"username\":\"Coba\",\"phone_number\":\"0890909090XXX\"},{\"id\":7,\"username\":\"Hengky Supat\",\"phone_number\":\"080000000XXX\"},{\"id\":8,\"username\":\"Kendal Jennor\",\"phone_number\":\"080000000XXX\"},{\"id\":4,\"username\":\"Kris Bone\",\"phone_number\":\"080000000XXX\"},{\"id\":18,\"username\":\"rafik\",\"phone_number\":\"0893222XXX\"},{\"id\":19,\"username\":\"rafik2\",\"phone_number\":\"3XXX\"},{\"id\":20,\"username\":\"rafik3\",\"phone_number\":\"XXX\"},{\"id\":21,\"username\":\"rafik4\",\"phone_number\":\"XXX\"},{\"id\":22,\"username\":\"rafik5\",\"phone_number\":\"XXX\"},{\"id\":23,\"username\":\"rafik6\",\"phone_number\":\"XXX\"},{\"id\":9,\"username\":\"Sumiati\",\"phone_number\":\"080000000XXX\"},{\"id\":3,\"username\":\"Tengku Wisnu\",\"phone_number\":\"080000000XXX\"},{\"id\":6,\"username\":\"Ummu Kaltsum\",\"phone_number\":\"080000000XXX\"},{\"id\":10,\"username\":\"Uta Likuhamuah\",\"phone_number\":\"080000000XXX\"}"

+    "],"

+    "\"products\": ["
+        "{"
        +    "\"id\": 1,"
        +    "\"label\": \"Nasi Goreng Seafood\","
        +    "\"description\": \"Nasi Goreng Seafood\","
        +    "\"price\": \"18000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": ["
        +        "{"
                +    "\"id\": 2,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Tidak Pedas\""
        +        "},"
        +        "{"
                +    "\"id\": 3,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Sedang\""
        +        "},"
        +        "{"
                +    "\"id\": 4,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Pedas\""
        +        "}"
        +    "]"
+        "},"
+        "{"
        +    "\"id\": 11,"
        +    "\"label\": \"Susu Coklat\","
        +    "\"description\": \"Susu Coklat\","
        +    "\"price\": \"8000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 10,"
        +    "\"label\": \"Cappucino\","
        +    "\"description\": \"Cappucino\","
        +    "\"price\": \"11000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 9,"
        +    "\"label\": \"Lemon Tea\","
        +    "\"description\": \"Lemon Tea\","
        +    "\"price\": \"7000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 8,"
        +    "\"label\": \"Es Jeruk\","
        +    "\"description\": \"Es Jeruk\","
        +    "\"price\": \"8000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 7,"
        +    "\"label\": \"Teh Tarik\","
        +    "\"description\": \"Teh Tarik\","
        +    "\"price\": \"9000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 6,"
        +    "\"label\": \"Pastel\","
        +    "\"description\": \"Pastel\","
        +    "\"price\": \"4500.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 5,"
        +    "\"label\": \"Risoles\","
        +    "\"description\": \"Risoles\","
        +    "\"price\": \"5000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": []"
+        "},"
+        "{"
        +    "\"id\": 4,"
        +    "\"label\": \"Roti Bakar\","
        +    "\"description\": \"Roti Bakar\","
        +    "\"price\": \"8000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": ["
        +        "{"
                +    "\"id\": 21,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Keju\""
        +        "},"
        +        "{"
                +    "\"id\": 22,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Coklat\""
        +        "},"
        +        "{"
                +    "\"id\": 23,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Strawberry\""
        +        "},"
        +        "{"
                +    "\"id\": 24,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Kacang\""
        +        "},"
        +        "{"
                +    "\"id\": 25,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Keju\""
        +        "},"
        +        "{"
                +    "\"id\": 26,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Coklat\""
        +        "},"
        +        "{"
                +    "\"id\": 27,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Strawberry\""
        +        "},"
        +        "{"
                +    "\"id\": 28,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Kacang\""
        +        "}"
        +    "]"
+        "},"
+        "{"
        +    "\"id\": 3,"
        +    "\"label\": \"Roti Maryam\","
        +    "\"description\": \"Roti Maryam\","
        +    "\"price\": \"5500.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": ["
        +        "{"
                +    "\"id\": 7,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Daging Ayam\""
        +        "},"
        +        "{"
                +    "\"id\": 8,"
                +    "\"price\": \"1000.00\","
                +    "\"label\": \"Reguler, Daging Sapi\""
        +        "},"
        +        "{"
                +    "\"id\": 9,"
                +    "\"price\": \"1000.00\","
                +    "\"label\": \"Reguler, Keju\""
        +        "},"
        +        "{"
                +    "\"id\": 10,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler, Sayur\""
        +        "},"
        +        "{"
                +    "\"id\": 11,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Daging Ayam\""
        +        "},"
        +        "{"
                +    "\"id\": 12,"
                +    "\"price\": \"3000.00\","
                +    "\"label\": \"Extra, Daging Sapi\""
        +        "},"
        +        "{"
                +    "\"id\": 13,"
                +    "\"price\": \"3000.00\","
                +    "\"label\": \"Extra, Keju\""
        +        "},"
        +        "{"
                +    "\"id\": 14,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra, Sayur\""
        +        "}"
        +    "]"
+        "},"
+        "{"
        +    "\"id\": 2,"
        +    "\"label\": \"Kentang Goreng\","
        +    "\"description\": \"Kentang Goreng\","
        +    "\"price\": \"8000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": ["
        +        "{"
                +    "\"id\": 5,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Reguler\""
        +        "},"
        +        "{"
                +    "\"id\": 6,"
                +    "\"price\": \"2000.00\","
                +    "\"label\": \"Extra\""
        +        "}"
        +    "]"
+        "},"
+        "{"
        +    "\"id\": 12,"
        +    "\"label\": \"Jus Buah\","
        +    "\"description\": \"Jus Buah\","
        +    "\"price\": \"10000.00\","
        +    "\"position\": 0,"
        +    "\"attributes\": ["
        +        "{"
                +    "\"id\": 15,"
                +    "\"price\": \"1000.00\","
                +    "\"label\": \"Alpukat\""
        +        "},"
        +        "{"
                +    "\"id\": 16,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Jambu\""
        +        "},"
        +        "{"
                +    "\"id\": 17,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Mangga\""
        +        "},"
        +        "{"
                +    "\"id\": 18,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Buah Naga\""
        +        "},"
        +        "{"
                +    "\"id\": 19,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Campuran\""
        +        "},"
        +        "{"
                +    "\"id\": 20,"
                +    "\"price\": \"0.00\","
                +    "\"label\": \"Apel\""
        +        "}"
        +    "]"
+        "}"
+    "],"

/*
E/onapi:  ( 5440): 17 {"status":200,"message":"Return process is done","order":{"id":
33,"customer_id":null,"outlet_id":1,"code":"RCJF9","tax":"0.00","discount":"0.00","to
tal_price":"22000.00","status":"ordered","delivery_time":null,"note":"notes dss","cre
ated_at":1503956863,"updated_at":1503956863},"items":[{"id":32,"order_id":33,"product
_id":10,"product_label":"Cappucino","product_attribute_id":null,"quantity":"2.00","di
scount":"0.00","unit_price":"11000.00","status":"paid","note":"","created_at":1503956
863,"updated_at":1503956863}]}
*/


/*
{"status":200,"orders":[{"id":4,"outlet_id":1,"customer":{"id":
null,"username":""},"items":[{"id":1,"product_id":10,"product_label":"Cappucino","pro
duct_attribute_id":null,"attribute_label":"","quantity":"1.00","discount":"0.00","uni
t_price":"11000.00","note":""},{"id":2,"product_id":7,"product_label":"Teh Tarik","pr
oduct_attribute_id":null,"attribute_label":"","quantity":"1.00","discount":"0.00","un
it_price":"9000.00","note":""}],"code":"739WJ","tax":"0.00","discount":"0.00","total_
price":"20000.00","status":"ordered","note":"","created_at":1502568182,"updated_at":1
502568182},{"id":5,"outlet_id":1,"customer":{"id":null,"username":""},"items":[{"id":
3,"product_id":10,"product_label":"Cappucino","product_attribute_id":null,"attribute_
label":"","quantity":"1.00","discount":"0.00","unit_price":"11000.00","note":""},{"id
":4,"product_id":6,"product_label":"Pastel","product_attribute_id":null,"attribute_la
bel":"","quantity":"1.00","discount":"0.00","unit_price":"4500.00","note":""}],"code"
:"PG4FM","tax":"0.00","discount":"0.00","total_price":"15500.00","status":"ordered","
note":"","created_at":1502568277,"updated_at":1502568278},{"id":6,"outlet_id":1,"cust
omer":{"id":null,"username":""},"items":[{"id":5,"product_id":10,"product_label":"Cap
pucino","product_attribute_id":null,"attribute_label":"","quantity":"3.00","discount"
:"0.00","unit_price":"11000.00","note":""},{"id":6,"product_id":9,"product_label":"Le
mon Tea","product_attribute_id":null,"attribute_label":"","quantity":"4.00","discount
":"0.00","unit_price":"7000.00","note":""}],"code":"XAA3N","tax":"0.00","discount":"0
.00","total_price":"61000.00","status":"ordered","note":"","created_at":1502575875,"u
pdated_at":1502575875},{"id":7,"outlet_id":1,"customer":{"id":4,"username":"Kris Bone
"},"items":[{"id":7,"product_id":11,"product_label":"Susu Coklat","product_attribute_
id":null,"attribute_label":"","quantity":"3.00","discount":"0.00","unit_price":"8000.
00","note":""},{"id":8,"product_id":5,"product_label":"Risoles","product_attribute_id
":null,"attribute_label":"","quantity":"6.00","discount":"0.00","unit_price":"5000.00
","note":""}],"code":"N3DWW","tax":"0.00","discount":"0.00","total_price":"54000.00",
"status":"ordered","note":"","created_at":1502576040,"updated_at":1502576040},{"id":1
0,"outlet_id":1,"customer":{"id":null,"username":""},"items":[{"id":9,"product_id":10
,"product_label":"Cappucino","product_attribute_id":null,"attribute_label":"","quanti
ty":"1.00","discount":"0.00","unit_price":"11000.00","note":""},{"id":10,"product_id"
:5,"product_label":"Risoles","product_attribute_id":null,"attribute_label":"","quanti
ty":"1.00","discount":"0.00","unit_price":"5000.00","note":""}],"code":"PHTAY","tax":
"0.00","discount":"0.00","total_price":"16000.00","status":"ordered","note":"","creat
ed_at":1502579634,"updated_at":1502579635},{"id":11,"outlet_id":1,"customer":{"id":nu
ll,"username":""},"items":[{"id":11,"product_id":10,"product_label":"Cappucino","prod
uct_attribute_id":null,"attribute_label":"","quantity":"1.00","discount":"0.00","unit
_price":"11000.00","note":""},{"id":12,"product_id":11,"product_label":"Susu Coklat",
"product_attribute_id":null,"attribute_label":"","quantity":"1.00","discount":"0.00",
"unit_price":"8000.00","note":""}],"code":"AHMWM","tax":"0.00","discount":"0.00","tot
al_price":"19000.00","status":"ordered","note":"","created_at":1502580133,"updated_at
":1502580133},{"id":12,"outlet_id":1,"customer":{"id":2,"username":"Amr Ma'ruf "},"it
ems":[{"id":13,"product_id":10,"product_label":"Cappucino","product_attribute_id":nul
l,"attribute_label":"","quantity":"1.00","discount":"0.00","unit_price":"11000.00","n
ote":""},{"id":14,"product_id":8,"product_label":"Es Jeruk","product_attribute_id":nu
ll,"attribute_label":"","quantity":"1.00","discount":"0.00","unit_price":"8000.00","n
ote":""}],"code":"6HPNK","tax":"0.00","discount":"0.00","total_price":"8000.00","stat
us":"ordered","note":"","created_at":1502580713,"updated_at":1503396287},{"id":13,"ou
tlet_id":1,"customer":{"id":24,"username":"John Saputra"},"items":[{"id":15,"product_
id":9
*/


+    "\"orders\": ["
+        "{"
        +    "\"id\": 4,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": null,"
            +    "\"username\": \"\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 1,"
                +    "\"product_id\": 10,"
                +    "\"product_label\": \"Cappucino\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"11000.00\","
                +    "\"note\": \"\""
        +        "},"
        +        "{"
                +    "\"id\": 2,"
                +    "\"product_id\": 7,"
                +    "\"product_label\": \"Teh Tarik\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"9000.00\","
                +    "\"note\": \"\""
        +        "}"
        +    "],"
        +    "\"code\": \"739WJ\","
        +    "\"tax\": \"0.00\","
        +    "\"discount\": \"0.00\","
        +    "\"total_price\": \"20000.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"\","
        +    "\"created_at\": 1502568182,"
        +    "\"updated_at\": 1502568182"
+        "},"
+        "{"
        +    "\"id\": 5,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": null,"
            +    "\"username\": \"\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 3,"
                +    "\"product_id\": 10,"
                +    "\"product_label\": \"Cappucino\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"11000.00\","
                +    "\"note\": \"\""
        +        "},"
        +        "{"
                +    "\"id\": 4,"
                +    "\"product_id\": 6,"
                +    "\"product_label\": \"Pastel\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"4500.00\","
                +    "\"note\": \"\""
        +        "}"
        +    "],"
        +    "\"code\": \"PG4FM\","
        +    "\"tax\": \"0.00\","
        +    "\"discount\": \"0.00\","
        +    "\"total_price\": \"15500.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"\","
        +    "\"created_at\": 1502568277,"
        +    "\"updated_at\": 1502568278"
+        "},"
+        "{"
        +    "\"id\": 6,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": null,"
            +    "\"username\": \"\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 5,"
                +    "\"product_id\": 10,"
                +    "\"product_label\": \"Cappucino\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"3.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"11000.00\","
                +    "\"note\": \"\""
        +        "},"
        +        "{"
                +    "\"id\": 6,"
                +    "\"product_id\": 9,"
                +    "\"product_label\": \"Lemon Tea\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"4.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"7000.00\","
                +    "\"note\": \"\""
        +        "}"
        +    "],"
        +    "\"code\": \"XAA3N\","
        +    "\"tax\": \"0.00\","
        +    "\"discount\": \"0.00\","
        +    "\"total_price\": \"61000.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"\","
        +    "\"created_at\": 1502575875,"
        +    "\"updated_at\": 1502575875"
+        "},"
+        "{"
        +    "\"id\": 7,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": 4,"
            +    "\"username\": \"Kris Bone\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 7,"
                +    "\"product_id\": 11,"
                +    "\"product_label\": \"Susu Coklat\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"3.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"8000.00\","
                +    "\"note\": \"\""
        +        "},"
        +        "{"
                +    "\"id\": 8,"
                +    "\"product_id\": 5,"
                +    "\"product_label\": \"Risoles\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"6.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"5000.00\","
                +    "\"note\": \"\""
        +        "}"
        +    "],"
        +    "\"code\": \"N3DWW\","
        +    "\"tax\": \"0.00\","
        +    "\"discount\": \"0.00\","
        +    "\"total_price\": \"54000.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"\","
        +    "\"created_at\": 1502576040,"
        +    "\"updated_at\": 1502576040"
+        "},"
+        "{"
        +    "\"id\": 10,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": null,"
            +    "\"username\": \"Sumiati\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 9,"
                +    "\"product_id\": 10,"
                +    "\"product_label\": \"Cappucino\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"100.00\","
                +    "\"unit_price\": \"11000.00\","
                +    "\"note\": \"capuc\""
        +        "},"
        +        "{"
                +    "\"id\": 10,"
                +    "\"product_id\": 5,"
                +    "\"product_label\": \"Risoles\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"5000.00\","
                +    "\"note\": \"risss\""
        +        "}"
        +    "],"
        +    "\"code\": \"PHTAY\","
        +    "\"tax\": \"10.00\","
        +    "\"discount\": \"200.00\","
        +    "\"total_price\": \"16000.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"note s\","
        +    "\"created_at\": 1502579634,"
        +    "\"updated_at\": 1502579635"
+        "},"
+        "{"
        +    "\"id\": 11,"
        +    "\"outlet_id\": 1,"
        +    "\"customer\": {"
            +    "\"id\": null,"
            +    "\"username\": \"\""
    +        "},"
        +    "\"items\": ["
        +        "{"
                +    "\"id\": 11,"
                +    "\"product_id\": 10,"
                +    "\"product_label\": \"Cappucino\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"11000.00\","
                +    "\"note\": \"\""
        +        "},"
        +        "{"
                +    "\"id\": 12,"
                +    "\"product_id\": 11,"
                +    "\"product_label\": \"Susu Coklat\","
                +    "\"product_attribute_id\": null,"
                +    "\"attribute_label\": \"\","
                +    "\"quantity\": \"1.00\","
                +    "\"discount\": \"0.00\","
                +    "\"unit_price\": \"8000.00\","
                +    "\"note\": \"\""
        +        "}"
        +    "],"
        +    "\"code\": \"AHMWM\","
        +    "\"tax\": \"0.00\","
        +    "\"discount\": \"0.00\","
        +    "\"total_price\": \"19000.00\","
        +    "\"status\": \"ordered\","
        +    "\"note\": \"\","
        +    "\"created_at\": 1502580133,"
        +    "\"updated_at\": 1502580133"
+        "}"
+    "]"



+"}";



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
android.util.Log.e("onapi454s: ", ( args.length>1 ? " args[1]=" + args[1] : "" ) );
            String client_token = retail.db.cfg.get("client_token").trim();
            if( client_token.length()>0 ) {
                byte[] auth=null;
                //auth=args[1].getBytes();
                try { auth = client_token.getBytes( "UTF-8" );    } catch ( java.io.UnsupportedEncodingException ex) { android.util.Log.e("oncharset: ", "error: " + ex.toString() );}
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

            String post_data_ = retail.db.cfg.get("access_token").trim();    if( post_data_.length()>0 ) post_data_ = "access_token=" + post_data_ ;
            post_data_ += args.length>1 && args[1].length()>0 ? ( post_data_.length()>0?"&":"" ) + args[1]: "" ;
            if( post_data_.length()>0 ) {    //ada params yg ingin diPOST
android.util.Log.e("onapi: ", "6 post_data_=" + post_data_ );
                con.setDoOutput(true);
android.util.Log.e("onapi: ", "61");
                byte[] post_data = null;
                try {
                    post_data = post_data_.getBytes( "UTF-8" );    //args[2].getBytes( java.nio.charset.StandardCharsets.UTF_8 );
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
                reader = new BufferedReader( new InputStreamReader( con.getResponseCode()==HttpURLConnection.HTTP_OK ? con.getInputStream() : con.getErrorStream(), "UTF-8" ) );
android.util.Log.e("onapi: ", "11a");
                String line;
                StringBuilder sb = new StringBuilder();
android.util.Log.e("onapi: ", "11b");
                while( (line = reader.readLine()) != null ) {
android.util.Log.e("onapi: ", "line=" + line);
sb.append(line);
android.util.Log.e("onapi: ", "after sb.append" );
}
android.util.Log.e("onapi: ", "12" );
                result = sb.toString();
android.util.Log.e("onapi: ", "13" );
            } else
                result = "Error: Gagal mengakses " + args[0] + "\nRespon: " + con.getResponseMessage();
        } catch( IOException e ) {
            e.printStackTrace();
            return "Error: Gagal mengakses " + args[0] + "\nMohon pastikan internet tersambung!" + "\nRespon: " + e.toString();    // + ( args[0].equals( retail.db.cfg.get("url_user_login") ) ? " dan isian email dan password sudah benar!" : " dan isian data sudah benar!" ) 
        } finally {
android.util.Log.e("onapi: ", "14" );
            if( reader!=null ) try { reader.close(); } catch( IOException e ) { e.printStackTrace(); } 
android.util.Log.e("onapi: ", "15" );
            con.disconnect();
android.util.Log.e("onapi: ", "16" );
        }
android.util.Log.e("onapi: ", "17 "  + result );
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