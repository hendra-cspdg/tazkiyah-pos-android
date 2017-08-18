/*
 *
 */

package net.muhajirin.solusitoko;

//import android.support.v7.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.text.InputType;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.Menu;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Types;
//import java.util.ArrayList;

public class Flaporan extends Fedit {
    Date Dstart_date=new Date();          Date Dstop_date=new Date();          String user_selected_name;    int user_selected;
    Date Dstart_date_print;    Date Dstop_date_print;    String user_selected_print;
    Boolean Dstop_is_changed;
    String modul, label;
    JCdb Cuser;

    static Flaporan form;
    public Flaporan() { super(); }
    public static Flaporan newInstance(String modul ) {
        form = new Flaporan();
        Fedit.newInstance(modul, form);
        form.modul = modul;
        return form;
    }

    public String get_sql() {
                                                                       //brg.harga_jual diganti di bawah ini krn harga jual boleh diedit saat penjualan:p
        return   " SELECT f.no_faktur, f.tgl_faktur, brg.code, brg.name, CAST( ROUND( (i.total+i.diskon)/i.banyak ) AS SIGNED ) AS harga_jual, i.banyak, i.diskon, brg.gambar, i.total "
               + "      , f.total, f.ppn, f.diskon AS potongan, f.sub_total "    //ini bs si, tapi lebih manis klo di code ajah:p >> IF(faktur_id=@last_faktur_id, 0, f.total), (@last_faktur_id:=faktur_id)
               + " FROM jual i"
               + " INNER JOIN jual_faktur f ON i.faktur_id=f.id"
               + " LEFT  JOIN barang brg ON i.barang_id=brg.id"
               + " WHERE tgl_faktur BETWEEN '" +new SimpleDateFormat("yyyy-MM-dd").format(this.Dstart_date)+ "' AND '" +new SimpleDateFormat("yyyy-MM-dd").format(this.Dstop_date)+ " 23:59:59' "    //ADDDATE('" +new SimpleDateFormat("yyyy-MM-dd").format(this.Dstop_date)+ "', 1 )
               +         ( user_selected==-13 ? "" : " AND f.user_id = " + user_selected )
               + " ORDER BY i.faktur_id, i.no"    //f.tgl_faktur    //, barang_id
               ;
    }

    public void init_table() {
        db.enable_filter = true;
        db.col_width     = new int[]        {          -140,       -70-30,           -105+27, -120-20/*(scrollpane.getWidth()-5 -140-70-105-60-65-50-0-70  -15)*/,           -60,            -65,           -50,             0,           -70,              0,             0,               0,             0  };   // (-) means not editable:)    //biarin aja kelebaran agar muncul horizontal scrollbar, jelek:p
        db.col_label     = new String[]     {        "code", "updated_at",              "id",                                           "product",       "price",     "quantity",        "disc.",      "gambar",       "total", "total_price",         "ppn", "diskon_faktur",    "sub_total" };    //jgn "tgl._faktur" krn di laporan retur, seharusnya tanggal retur:)
        db.col_type      = new int[]        { Types.VARCHAR,   Types.DATE,     Types.VARCHAR,                                       Types.VARCHAR, Types.INTEGER, Types.SMALLINT, Types.INTEGER, Types.VARCHAR, Types.INTEGER,  Types.TINYINT, Types.TINYINT,   Types.TINYINT, Types.INTEGER  };
    }
    @Override Boolean build_sql() {
android.util.Log.e( "build: ", "1" );
        init_table();
        db.col_editor    = new View [db.col_width.length];
        db.col_precision = new int  [db.col_width.length];
        for( int i=0; i<db.col_width.length; i++ ) {
            db.col_editor[i] = null;
            db.col_precision[i] = 1;
        }
        db.show_table(false,null,false);
        table = new JTable( db, form.getActivity() );        //View view = inflater.inflate(R.layout.fragment_recycler, container, false);

        return true;
    }

    EditText Tdiskon, Tppn, Tsub_total, Ttotal, Tdisc_item, Dstart;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sql              = "";
        label = modul.substring( modul.lastIndexOf( " " )+1 ).trim();

android.util.Log.e( "penjual: ", "1" );

        footer_panel = new LinearLayoutCompat(form.getActivity());
        footer_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);

android.util.Log.e( "penjual: ", "2" + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

            /*this should be replaced later by visibling horizontal scrollbar
            View border = new View(form.getActivity());
            border.setBackgroundResource( android.R.color.black );
            LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, 1 );
            footer_panel.addView( border, prms );
            */

        int min_width = 60;
        int disabled_background_color = android.graphics.Color.TRANSPARENT;    // ;    //0xff663399

        LayoutParams prms_tv = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );    //new LayoutParams( 85, LayoutParams.WRAP_CONTENT );
        LayoutParams prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        prms.setMargins( 0, 7, 0, 0 );


/*
        Dstart = new EditText(form.getActivity());    Dstart.setMinWidth(min_width);    Dstart.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //ga iso >> Dstart.setEnabled(false);    Dstart.setClickable(true);    Dstart.setFocusable(true);    Dstart.setFocusableInTouchMode(true);
        Dstart.setText( new SimpleDateFormat("dd-MM-yyyy").format(Dstart_date) );    Dstart.setHint( "Tanggal" );
        TextInputLayout TL = new TextInputLayout(form.getActivity());    TL.addView( Dstart, prms_tv );
        footer_panel.addView( TL, prms );

        final EditText Dstop = new EditText(form.getActivity());    Dstop.setMinWidth(min_width);    Dstop.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Dstop.setText( new SimpleDateFormat("dd-MM-yyyy").format(Dstop_date) );    Dstop.setHint( "  s.d." );
        TL = new TextInputLayout(form.getActivity());    TL.addView( Dstop, prms_tv );
        footer_panel.addView( TL, prms );
*/
        //final android.support.v7.widget.AppCompatButton Blihat = new android.support.v7.widget.AppCompatButton(form.getActivity());
        //Blihat.setText( "lihat" );    Blihat.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //footer_panel.addView( Blihat, prms_tv );



        Tdiskon = new EditText(form.getActivity());    Tdiskon.setEnabled(false);    Tdiskon.setMinWidth(min_width+25);    Tdiskon.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tdiskon.setHint( "Potongan" );    Tdiskon.setBackgroundColor( disabled_background_color );    Tdiskon.setTextSize(18f);    Tdiskon.setTextColor( android.graphics.Color.GREEN );    //R.drawable.edittext_text_color    //0xff060018
        TextInputLayout TL = new TextInputLayout(form.getActivity());    TL.addView( Tdiskon, prms_tv );
        footer_panel.addView( TL, prms );

        Tppn = new EditText(form.getActivity());    Tppn.setEnabled(false);    Tppn.setMinWidth(min_width);    Tppn.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tppn.setHint( "PPN" );    Tppn.setBackgroundColor( disabled_background_color );    Tppn.setTextSize(18f);    Tppn.setTextColor( android.graphics.Color.GREEN );    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Tppn, prms_tv );
        footer_panel.addView( TL, prms );

        Tsub_total = new EditText(form.getActivity());    Tsub_total.setEnabled(false);    Tsub_total.setMinWidth(min_width+25);    Tsub_total.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tsub_total.setHint( "Sub Total" );    Tsub_total.setBackgroundColor( disabled_background_color );    Tsub_total.setTextSize(18f);    Tsub_total.setTextColor( android.graphics.Color.GREEN );    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Tsub_total, prms_tv );
//        footer_panel.addView( TL, prms );

        Ttotal = new EditText(form.getActivity());    Ttotal.setEnabled(false);    Ttotal.setMinWidth(min_width+25);    Ttotal.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Ttotal.setHint( "Total" );    Ttotal.setBackgroundColor( disabled_background_color );    Ttotal.setTextSize(18f);    Ttotal.setTextColor( android.graphics.Color.GREEN );    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Ttotal, prms_tv );
        footer_panel.addView( TL, prms );

        Tdisc_item = new EditText(form.getActivity());    Tdisc_item.setEnabled(false);    Tdisc_item.setMinWidth(min_width+25);    Tdisc_item.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tdisc_item.setHint( "Disc. Item" );    Tdisc_item.setBackgroundColor( disabled_background_color );    Tdisc_item.setTextSize(18f);    Tdisc_item.setTextColor( android.graphics.Color.GREEN );    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Tdisc_item, prms_tv );
        footer_panel.addView( TL, prms );



        View ret = super.onCreateView(inflater, container, savedInstanceState);


        user_selected = retail.user_id;    user_selected_name = retail.nama;


/*
        android.app.DatePickerDialog.OnDateSetListener date_listener = new android.app.DatePickerDialog.OnDateSetListener() {
            public void onDateSet( android.widget.DatePicker datePicker, int year, int month, int day ) {    //ini emang baca Dstart.getDate() biar datenya tervalidasi dulu (otherwise u did ur own validation:) ), maka user emang perlu press enter dulu baru Blihat enable:p
android.util.Log.e( "OnDateSetListener: ", "1" );

                Date Dnew = java.sql.Date.valueOf( year+"-"+ (month+1) +"-"+ day  );    //ribet >> new java.util.GregorianCalendar( year, month, day ).getGregorianChange();
                if( !Dstop_is_changed && Dnew==Dstart_date || Dstop_is_changed && Dnew==Dstop_date ) return;    //if( temp_filter_tgl_faktur.equals( filter_tgl_faktur ) ) return;    //ga ngefek, masa dia beraksi dua kali ?! bahkan hanya saat focus berubah?! :p >> if( !Dstart.COMMIT_KEY.equals("datePickerCommit") ) return;    //if( !Dstart.COMMIT_KEY.equals(e.getActionCommand()) ) return;    //Lgambar.setText( Lgambar.getText() + "1");

                Date Dstart_temp=Dstart_date, Dstop_temp=Dstop_date;
                //String date_str = Tchanged.getText().toString();
                //date_str = date_str.substring(6,10) +"-"+ date_str.substring(3,5) +"-"+ date_str.substring(0,2);
                if( Dstop_is_changed )  Dstop_temp = Dnew;
                else                   Dstart_temp = Dnew;

                if( Dstop_temp.before(Dstart_temp) )  {
                    retail.show_error( "\nMaaf, \"Tanggal Mulai\" tidak boleh melampaui \"Tanggal Akhir\"!\n\n\n\n\n", "Rentang tanggal tidak sesuai" );
                    if( Dstop_is_changed ) {
                        //luweh, samain aja tanggalnya :D
                        Dstart_temp = Dnew;
                        Dstart.setText( new SimpleDateFormat("dd-MM-yyyy").format(Dnew) );    Dstart.requestFocus();
                    } else {
                        Dstop_temp = Dnew;
                        Dstop.setText( new SimpleDateFormat("dd-MM-yyyy").format(Dnew) );    Dstop.requestFocus();
                    }    //Dstop.getEditor().selectAll() does not work    //requestFocus() jg memanggil listener ini krn sepertinya itu dilakukan datepicker:p    //seharusnya si ga boleh exit dari control ini:p
                    //return;
                }
                Dstart_date = Dstart_temp;    Dstop_date = Dstop_temp;
                EditText Tchanged     = Dstop_is_changed ? Dstop  : Dstart;
                Tchanged.setText( new SimpleDateFormat("dd-MM-yyyy").format(Dnew) );

                lihat();    //Blihat.setEnabled(true);
            }
        };


        final java.util.Calendar c = java.util.Calendar.getInstance();
        int year = c.get(java.util.Calendar.YEAR), month = c.get(java.util.Calendar.MONTH), day = c.get(java.util.Calendar.DAY_OF_MONTH);
        final android.app.DatePickerDialog date_dialog = new android.app.DatePickerDialog( form.getActivity(), date_listener, year, month, day);  //Dstart.setFont(new Font( "Book Antigua", Font.PLAIN, 12 ));   Dstart.getEditor().setMargin( new Insets( 0, 7, 0, 0 ) );
        View.OnClickListener date_click_listener = new View.OnClickListener() {    @Override public void onClick( final View v ) {
            //new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
android.util.Log.e( "click: ", "11" );
             String date_str = ((TextView)v).getText().toString();
android.util.Log.e( "click: ", "12" );
             int year  = Integer.valueOf( date_str.substring(6,10)   );
android.util.Log.e( "click: ", "13" );
             int month = Integer.valueOf( date_str.substring(3,5) );
android.util.Log.e( "click: ", "14" );
             int day   = Integer.valueOf( date_str.substring(0,2) );
android.util.Log.e( "click: ", "15" );
             Dstop_is_changed = ((TextInputLayout)v.getParent()).getHint().toString().contains("s.d.");    //api level 14:p >> date_dialog.getDatePicker().setTag( ((EditText)v).getHint() );
android.util.Log.e( "click: ", "16 " + day +"-"+month +"-"+ year);
             date_dialog.updateDate( year, month-1, day );
android.util.Log.e( "click: ", "17" );
             date_dialog.show();
android.util.Log.e( "click: ", "18" );
             //}}, 200);
        }};
        Dstart.setOnClickListener(date_click_listener);
        Dstop.setOnClickListener(date_click_listener);

*/


android.util.Log.e( "penjual: ", "17"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));


        //Blihat.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
        //    lihat();
        //}});

        new android.os.Handler().post(new Runnable() { @Override public void run() {    //Ccode_brg.post(new Runnable() { @Override public void run() {
            lihat();    //Blihat.performClick();
        }});

        return ret;
    }

    long disc_item, sub_total_faktur, potongan, ppn, total_faktur;
    byte disc_sign;
    void lihat() {
        disc_item=0;  sub_total_faktur=0;  potongan=0;  ppn=0;  total_faktur=0;
        final String api_table="orders";
        new DownloadJSON(){
            @Override protected void onPostExecute( String result ) {
                super.onPostExecute(result);
android.util.Log.e(api_table+": ", "1");
                if( result.startsWith( "Error:" ) ) return;
android.util.Log.e(api_table+": ", "2");

                Boolean record_found=false;
String debug="";
                try {
                    org.json.JSONObject data = new org.json.JSONObject(result);
android.util.Log.e(api_table+": ", "3");

                    if( !data.has( api_table ) ) {
                        retail.show_error( "\n" + data.getString("message") + "\n\n\n\n", "Koneksi Gagal" );
                        return;
                    }
android.util.Log.e(api_table+": ", "4");


debug+="1";
                    db.rows.clear();
debug+="3";
debug+="4";
                    //Dstart_date_print = Dstart_date;    Dstop_date_print = Dstop_date;    user_selected_print = user_selected_name;
debug+="5";



                    org.json.JSONArray jArray = new org.json.JSONArray( data.getString(api_table) );
android.util.Log.e(api_table+": ", "10");
                    record_found = jArray.length()>0;

                    Object[] rs_row = new Object[db.col_width.length];
                    int[] col_type  = new int   [db.col_width.length];
debug+="8";
debug+="9";
                    String last_no_faktur = "";  //long disc_item=0;  long sub_total_faktur=0;  long potongan=0;  long ppn=0;  long total_faktur=0;
                    disc_sign        = (byte) -1 ;
                    for( int i=0; i<jArray.length(); i++ ) {
                        org.json.JSONObject jtable = jArray.getJSONObject(i);
                        for( int c=0; c<db.col_width.length; c++ ) {  //klo ga gini, kebanyakan try{} ntar
android.util.Log.e("lihat:", "1 c=" + c );
                            if(col_type[c]==0) col_type[c] = db.metaData.getColumnType(i+1);
                            String field = db.col_label[c];
android.util.Log.e("lihat:", "2 c=" + c + " field="+field );
                            if( field.equals("code") || field.equals("unit_price") || field.equals("discount") ) rs_row[c] = jtable.getString( field );
                            else if( field.equals("ppn") ) rs_row[c] = jtable.getInt( "tax" );
                            else if( field.equals("updated_at") ) rs_row[c] = new SimpleDateFormat("d MMM yyyy").format( jtable.getLong( field )  * 1000L );
                            else if( field.equals("product") ) {
android.util.Log.e("lihat:", "3 c=" + c + " field="+field );
                                org.json.JSONArray jArray2 = new org.json.JSONArray( jtable.getString("items") );
                                for( int i2=0; i2<jArray2.length(); i2++ ) {
android.util.Log.e("lihat:", "4 c=" + c + " i2="+i2 );
                                    org.json.JSONObject jtable2 = jArray2.getJSONObject(i2);
android.util.Log.e("lihat:", "5 c=" + c + " i2="+i2 );
                                    rs_row[c-1] = jtable2.getString( "product_id" );
                                    rs_row[c]   = jtable2.getString( "product_label" );
                                    rs_row[c+1] = jtable2.getInt( "unit_price" );
                                    rs_row[c+2] = jtable2.getInt( "quantity" );
                                    rs_row[c+3] = jtable2.getInt( "discount" );
                                    rs_row[c+5] = jtable2.getInt("unit_price") * jtable2.getInt("quantity");
android.util.Log.e("lihat:", "6 c=" + c + " i2="+i2 );
                                    db.addRow(false, db.getRowCount(), rs_row,col_type, false);
android.util.Log.e("lihat:", "7 c=" + c + " i2="+i2 );
                                }
                            }

                            //if(db.metaData.getColumnType(i+1)==Types.DATE) rs_row[c] = new SimpleDateFormat("d MMM yyyy").format( jtable.getInt( field ) );
                            //else                                           rs_row[c] = jtable.getString( field );
                        }

debug+="11";
                        //calculating the summary
                        disc_item += jtable.getLong("discount");    //retail.round( (long) db.rs.getInt(5) * db.rs.getInt("diskon") * db.rs.getInt(6) , 100 ) ;    //ada si resiko kecil banget jika pembulatannya pas tengah2:p ... tp masa ada harga kelipatan di bawah 100?! ... ada aja si klo di supermarket:D
debug+="12" ;
                        if( !last_no_faktur.equals( jtable.getString( "code" ) ) /*|| jtable2.getLong( "unit_price" )==-1 */ ) {    //bisa berisi no_faktur atau faktur.id
debug+="13";
                            last_no_faktur    = jtable.getString( "code" );
debug+="14";
                            sub_total_faktur += jtable.getInt( "total_price" );
debug+="15";
                            potongan         += jtable.getInt( "discount" );    //db.rs.getLong(9)==-1 ? db.rs.getLong(12)    : retail.round( (long) db.rs.getLong(13) * db.rs.getLong(12) , 100 ) ;    //db.rs.getInt(9)==-1  utk laporan kas:p  //ada si resiko kecil banget jika pembulatannya pas tengah2:p ... tp masa ada harga kelipatan di bawah 100?! ... ada aja si klo di supermarket:D
debug+="16";
                            ppn              += /*jtable.getLong( "unit_price" )==-1 ? jtable.getInt( "ppn" ) : */ retail.round( (long) ( jtable.getLong( "total_price" )+jtable.getLong( "discount" )) * jtable.getInt( "tax" ) , 100 ) ;    //ada si resiko kecil banget jika pembulatannya pas tengah2:p ... tp masa ada harga kelipatan di bawah 100?! ... ada aja si klo di supermarket:D
debug+="17";
                            total_faktur     += jtable.getInt( "total_price" ) ;
debug+="18";
                            //if( jtable.getLong( "unit_price" )==-1 ) disc_sign = (byte) 1 ;
debug+="19";
                        } else if( ((TextInputLayout)Ttotal.getParent()).getHint().equals("Laba Rugi") )
                            total_faktur     += jtable.getInt( "total_price" ) ;    //cappe deeeh:)
                    }
debug+="21";
                    //db.rs.close();
debug+="22";
                    last_selected_row = -1;    //ngumpanin ListSelectionListener() ... agar tetap load_gambar



                } catch( Exception e ) {    //org.json.JSONException 
                    db.err_msg += debug+"<debug\nMaaf, Data \"" +api_table+ "\" gagal diinisialisasi!\n\n\n(" + e.toString() + ")";
                    retail.show_error( db.err_msg, "Pembacaan Data \"" +api_table+ "\"" );
                    android.util.Log.e("get " +api_table+ " error: ", "e.toString()="+ e.toString() );
                }

                if( !record_found )      {
android.util.Log.e("lihat:", "6 db==nnull " + (db==null) );
                    //reset_gambar();               //perlu klo db.rs.size() <= 0 :p

                    Tdisc_item.setText("0");  Tsub_total.setText("0");  Tdiskon.setText("0");  Tppn.setText("0");  Ttotal.setText("0");    //perlu klo db.rs.size() <= 0 :p
                    db.adapter.notifyDataSetChanged();    //perlu klo db.rs.size() <= 0 :p
android.util.Log.e("lihat:", "6a");
                    retail.show_error( "\nMaaf, Data "+label+" tidak ditemukan!\n\n\n\n\n", "Data tidak ditemukan" );
android.util.Log.e("lihat:", "6b");
                    //Dstart.requestFocus();
android.util.Log.e("lihat:", "6c");        
                    return;
                }
                //print the summary
                Tdisc_item.setText ( String.format("%,d", disc_sign*disc_item     ) );
                Tsub_total.setText ( String.format("%,d", sub_total_faktur ) );
                Tdiskon.setText    ( String.format("%,d", (((TextInputLayout)Tdiskon.getParent()).getHint().equals("Potongan") ? 1 : -1) * potongan      ) );
                Tppn.setText       ( String.format("%,d", ppn              ) );
                Ttotal.setText     ( String.format("%,d", total_faktur     ) );

                db.adapter.notifyDataSetChanged();    //perlu di sini agar yg bawah ini kagak error

                //new android.os.Handler().postDelayed(new Runnable() { public void run() {

android.util.Log.e("lihat:", "28 table.getLayoutManager()==null " + (table.getLayoutManager()==null) );

                    //kenapa ini null?! >>
 table.getLayoutManager().scrollToPosition( table.getRowCount()-1 );
android.util.Log.e("lihat:", "29 table.getLayoutManager()==null " + (table.getLayoutManager()==null) );
                    table.setRowSelectionInterval(table.getRowCount()-1,table.getRowCount()-1);    //table.setColumnSelectionInterval(form.db.col_width.length-1,form.db.col_width.length-1);    table.scrollRectToVisible( table.getCellRect( table.getSelectedRow(), table.getSelectedColumn(), true ) );    //load_gambar( rs_row[6].toString() );
                    //Bprint.setEnabled(true);  Bexport_xls.setEnabled(true);
android.util.Log.e("lihat:", "30 table.getLayoutManager()==null " + (table.getLayoutManager()==null) );
                    //table.requestFocus();
                //}}, 200);





                }
            }.execute( retail.db.cfg.get( "url_order_index" ) );





    }

}