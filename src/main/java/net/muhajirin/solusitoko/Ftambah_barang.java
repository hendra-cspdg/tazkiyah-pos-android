/*
 * Solusi Toko, Software POS/retail/inventory
 * Copyright (c) 2014-2015. Rafik, http://solusiprogram.com/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */

package net.muhajirin.solusitoko;

//import java.awt.*;
//import java.awt.event.*;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import org.jdesktop.swingx.JXDatePicker;
import android.support.v7.widget.AppCompatButton;
import android.widget.Spinner;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.InputType;


public class Ftambah_barang extends Ftambah {
    //public JBprint Bprint_barcode, Bprint_label_rak;
    //public AppCompatButton Bambil_gambar;
    EditText Tname, Tcode, Tstok, Tsize, Tberat, Tomset, Tdeskripsi, Tharga_beli, Tdiskon_beli, Tharga_jual, Tdiskon_jual, Tqty1, Tdisc_amount1, Tqty2, Tdisc_amount2, Tqty3, Tdisc_amount3, Tqty4, Tdisc_amount4 ;
    //BufferedImage BIgambar = null;
    //String ext = "";
    JCdb Cagent=null;
    //JLabel Lrangkap;    Spinner Ccopies;    JLabel Lskip;    public Spinner Cskip;
    //Boolean printer_barcode   = retail.convert_null(retail.setting.get("Aktifkan Printer Barcode")).toLowerCase().equals("ya");
    //Boolean printer_label_rak = retail.convert_null(retail.setting.get("Aktifkan Printer Label Rak")).toLowerCase().equals("ya");
    //Boolean skip_label_barcode_aktif = retail.convert_null(retail.setting.get("Aktifkan Skip Label di Printer Barcode")).toLowerCase().equals("ya");
    //int barcode_copies_default = retail.is_number( retail.convert_null(retail.setting.get("Default Banyak Rangkap Label di Printer Barcode")) ) ? Integer.valueOf( retail.convert_null(retail.setting.get("Default Banyak Rangkap Label di Printer Barcode")) ) : 3 ;
    Boolean sup_akses = retail.hak_akses.indexOf("'Supplier di Fitur Barang'") >= 0 ;
    String saved_code = "", saved_name = "", saved_harga = "", saved_deskripsi = "";
    int panjang_barcode_masuk_history_tambah_barang;
    static String[] tambah_init;
    static String barcode_init = "";
    static Ftambah_barang form;
    public Ftambah_barang() { super(); }
    public static Ftambah_barang newInstance(String title) {
        return newInstance(title, "", null) ;
    }
    public static Ftambah_barang newInstance(String title, final String barcode_init_ ) {
        return newInstance(title, barcode_init_, null) ;
    }
    public static Ftambah_barang newInstance(String title /*, Object parent_*/, final String barcode_init_, final String[] tambah_init_send ) {
        form = new Ftambah_barang();    //Ftambah.newInstance(title);
        Ftambah.newInstance(title, form);
        barcode_init = barcode_init_;
        if( tambah_init_send!=null ) tambah_init=tambah_init_send;
        return form;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
String debug="";
try{
debug+="1";
android.util.Log.e("Ftambah_barang: ", "1" );

        //View ret = super.onCreateView(inflater, container, savedInstanceState);
        //super(parent, title);
        //setIconImage(retail.icon_app);
        final Boolean disc_qty  = retail.convert_null(retail.setting.get("Aktifkan Diskon Quantity")).toLowerCase().equals("ya");


//TYPE_CLASS_NUMBER|TYPE_NUMBER_FLAG_SIGNED
//ni kayaknya perlu ditest deh >> TYPE_TEXT_VARIATION_FILTER

        Tname = new EditText(form.getActivity());    Tname.setHint( "Nama Barang" );    comps.add(Tname);
        Tname.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tname.setMaxLines(50);    //Tname = new JTextField(50);    //ini tipu aja ... sebenarnya mo ngirim variable maxLength ke super class    //setMaxLines(int)

        Tcode = new EditText(form.getActivity());    Tcode.setHint( "Kode Barang" );    comps.add(Tcode);
        Tcode.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tcode.setMaxLines(20);
        Tcode.setText( barcode_init, TextView.BufferType.EDITABLE );    //initial value from caller
        Tcode.setOnClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_view = (TextView) Tcode;

        Tstok = new EditText(form.getActivity());    Tstok.setHint( "Stok" );    comps.add(Tstok);
        Tstok.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tstok.setMaxLines(4);

        Tsize = new EditText(form.getActivity());    Tsize.setHint( "Ukuran / Size" );    comps.add(Tsize);
        Tsize.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tsize.setMaxLines(30);

        Tberat = new EditText(form.getActivity());    Tberat.setHint( "Berat (gram)" );    comps.add(Tberat);
        Tberat.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tberat.setMaxLines(6);

        Tomset = new EditText(form.getActivity());    Tomset.setHint( "Omset / bln" );    comps.add(Tomset);
        Tomset.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tomset.setMaxLines(4);

        Tdeskripsi = new EditText(form.getActivity());    Tdeskripsi.setHint( "Deskripsi Barang" );    comps.add(Tdeskripsi);
        Tdeskripsi.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tdeskripsi.setMaxLines(255);

        Tharga_beli = new EditText(form.getActivity());    Tharga_beli.setHint( "Harga Beli" );    comps.add(Tharga_beli);
        Tharga_beli.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tharga_beli.setMaxLines(9);
        Tharga_beli.setOnFocusChangeListener(retail.add_ribuan_when_lost_focus);

        Tdiskon_beli = new EditText(form.getActivity());    Tdiskon_beli.setHint( "Diskon Beli" );    comps.add(Tdiskon_beli);
        Tdiskon_beli.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdiskon_beli.setMaxLines(2);

debug+="2";
android.util.Log.e("Ftambah_barang: ", "2" );
        if( sup_akses ) {
            floating_label Lagent = new floating_label(form.getActivity());    //TextView Lagent = new TextView( form.getActivity(), null, 0, com.solusiprogram.solusitoko.R.style.floating_label );    //if( Build.VERSION.SDK_INT<23 ) {    Lagent.setTextAppearance( form.getActivity(), android.R.style.InputLabel );    } else {    Lagent.setTextAppearance( android.R.style.InputLabel );    }
            Lagent.setText("Supplier");    comps.add(Lagent);
            Cagent = JCdb.newInstance(false,"SELECT id, name, code, kontak, diskon FROM supplier ORDER BY IF(id=1,-111,name)", form.getActivity(), 1);    comps.add(Cagent);
debug+="3";
android.util.Log.e("Ftambah_barang: ", "3" );
        }
debug+="4";
android.util.Log.e("Ftambah_barang: ", "4" );

        Tharga_jual = new EditText(form.getActivity());    Tharga_jual.setHint( "Harga Jual (Rp)" );    comps.add(Tharga_jual);
        Tharga_jual.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tharga_jual.setMaxLines(9);
        Tharga_jual.setOnFocusChangeListener(retail.add_ribuan_when_lost_focus);

        Tdiskon_jual = new EditText(form.getActivity());    Tdiskon_jual.setHint( "Diskon Jual" );    comps.add(Tdiskon_jual);
        Tdiskon_jual.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdiskon_jual.setMaxLines(2);

        Tqty1 = new EditText(form.getActivity());    Tqty1.setHint( "Qty1" );    comps.add(Tqty1);
        Tqty1.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tqty1.setMaxLines(4);
        Tdisc_amount1 = new EditText(form.getActivity());    Tdisc_amount1.setHint( "Diskon" );    comps.add(Tdisc_amount1);
        Tdisc_amount1.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdisc_amount1.setMaxLines(9);

        Tqty2 = new EditText(form.getActivity());    Tqty2.setHint( "Qty2" );    comps.add(Tqty2);
        Tqty2.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tqty2.setMaxLines(4);
        Tdisc_amount2 = new EditText(form.getActivity());    Tdisc_amount2.setHint( "Diskon" );    comps.add(Tdisc_amount2);
        Tdisc_amount2.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdisc_amount2.setMaxLines(9);

        Tqty3 = new EditText(form.getActivity());    Tqty3.setHint( "Qty3" );    comps.add(Tqty3);
        Tqty3.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tqty3.setMaxLines(4);
        Tdisc_amount3 = new EditText(form.getActivity());    Tdisc_amount3.setHint( "Diskon" );    comps.add(Tdisc_amount3);
        Tdisc_amount3.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdisc_amount3.setMaxLines(9);

        Tqty4 = new EditText(form.getActivity());    Tqty4.setHint( "Qty4" );    comps.add(Tqty4);
        Tqty4.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tqty4.setMaxLines(4);
        Tdisc_amount4 = new EditText(form.getActivity());    Tdisc_amount4.setHint( "Diskon" );    comps.add(Tdisc_amount4);
        Tdisc_amount4.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdisc_amount4.setMaxLines(9);

        if( !disc_qty ) {
            Tqty1.setVisibility(TextView.GONE);  Tdisc_amount1.setVisibility(TextView.GONE);
            Tqty2.setVisibility(TextView.GONE);  Tdisc_amount2.setVisibility(TextView.GONE);
            Tqty3.setVisibility(TextView.GONE);  Tdisc_amount3.setVisibility(TextView.GONE);
            Tqty4.setVisibility(TextView.GONE);  Tdisc_amount4.setVisibility(TextView.GONE);
        }

debug+="5";
android.util.Log.e("Ftambah_barang: ", "5" );
        mandatory.add(Tname);         mandatory.add(Tcode);         mandatory.add(Tharga_jual);
        //comp_number.add(Tjumlah);
        default_focused_comp = Tname;
        //align_comps();
debug+="6";
//android.util.Log.e("tambah_brg: ", "on create Cagent.getCount() " + Cagent.getCount() );



            } catch (Exception e) { //numpang2 pake db.err_msg :D
android.util.Log.e("Ftambah_barang: ", "error: " + debug+"\nTAMBAH BARANG\n" + e.getMessage() );
                retail.show_error( debug+"\nTAMBAH BARANG\n" + e.getMessage(), "eee" );
            }
debug+="7";
android.util.Log.e("Ftambah_barang: ", "6" );


        return super.onCreateView(inflater, container, savedInstanceState);
        //if( sup_akses ) Cagent = JCdb.newInstance(false,"SELECT id, name, code, kontak, diskon FROM supplier ORDER BY IF(id=1,-111,name)", form.getActivity());    comps.add(Cagent);
    }

    @Override protected void align_comp( final View comp, final int i ) {
android.util.Log.e("Ftambah_brg align_comp: ", "1" );
        super.align_comp(comp,i);
        panjang_barcode_masuk_history_tambah_barang = retail.is_number( retail.db.cfg.get("panjang_barcode_masuk_history_tambah_barang") ) ? Integer.valueOf( retail.db.cfg.get("panjang_barcode_masuk_history_tambah_barang") ) : 1000 ;
        if( tambah_init==null || barcode_init.length()<panjang_barcode_masuk_history_tambah_barang || comp==Tcode ) return;
android.util.Log.e("Ftambah_brg align_comp: ", (comp==null) + " << comp==null     i=" + i+ " tambah_init[i]=" + tambah_init[i] + "  comp instanceof JCdb=" + (comp instanceof JCdb)  + "  comp instanceof EditText=" + (comp instanceof EditText)  );
        if( comp instanceof JCdb ) {    final int idx = Integer.valueOf(tambah_init[i]); 

                    //comp.postDelayed(new Runnable() { @Override public void run() {
   
android.util.Log.e("Ftambah_brg align_comp: ", "idx=" +idx + "((JCdb)comp).getCount()=" + ((JCdb)comp).getCount() );
((JCdb)comp).setText( ((JCdb)comp).getItemAt(idx).toString() );    ((JCdb)comp).setSelection(idx);

                    //}},500);


    }    //((Spinner)comp).setSelection( Integer.valueOf(tambah_init[i]) );
        else if( comp instanceof EditText ) ((EditText)comp).setText(tambah_init[i]);
android.util.Log.e("Ftambah_brg align_comp: ", "3" );
        //malah error >> super.align_comp(comp,i);
    }
    @Override Boolean build_sql() {
        sql = " INSERT INTO barang (            id         , name, code, berat, stok, omset, size, deskripsi, harga_beli, diskon_beli, harga_jual, diskon_jual, gambar, supplier_id, disc_qty1, disc_amount1, disc_qty2, disc_amount2, disc_qty3, disc_amount3, disc_qty4, disc_amount4 ) "  //field gambar ini minimal digunakan utk cek saat view Fedit agar ga nyari terus ke harddisk:)
            + " SELECT              IFNULL( MAX(id), 0) + 1,   ? ,   ? ,   ?  ,   ?  ,   ?  ,   ? ,     ?    ,      ?    ,      ?     ,     ?     ,       ?    ,    ?  ,    ?       ,     ?    ,      ?      ,     ?    ,      ?      ,     ?    ,      ?      ,     ?    ,      ?          FROM barang" ;

android.util.Log.e("tambah: ", " in build_sql ");
android.util.Log.e("tambah: ", " in build_sql Cagent.getCount() " + Cagent.getCount()  + "   ((jcdb_item)Cagent.getSelectedItem()).get_id()=" + ((jcdb_item)Cagent.getSelectedItem()).get_id() );
        int maximum_tambah_barang_berurutan = retail.convert_number( retail.setting.get("Maximum Tambah Barang Berurutan"), 0 );
        String code = Tcode.getText().toString() ;
        if( code.indexOf("-") > 0 && maximum_tambah_barang_berurutan > 0 ) {
            String[] codes = code.split("-") ;
            codes[0] = codes[0].replace(retail.digit_separator,"").trim();    codes[1] = codes[1].replace(retail.digit_separator,"").trim();

            if( retail.is_number(codes[0]) && retail.is_number(codes[1]) && codes[0].length()>0 ) {
                long count = 0;
                try {
                    count = Long.valueOf(codes[1]) - Long.valueOf(codes[0]) ;
                    String err="";
                    if( count<=0 )                                     err = "Range Kode Barang Salah!\nKode Akhir harus lebih besar dari Kode Awal!";
                    else if( count > maximum_tambah_barang_berurutan ) err = "Jumlah Kode Barang yang ingin ditambahkan: " +count+ ".\nMaksimal hanya: " + maximum_tambah_barang_berurutan;
                    if( err.length()>0 ) {
                        retail.show_error( err + "\n\nMohon Perbaiki Kode Barang!\n\n\n\n\n", "Range Kode Barang Salah" );
                        return false;
                    }
                } catch( Exception e ) {
                    retail.show_error( "Terjadi kesalahan saat menghitung range kode barang.\nPesan Kesalahan:" + e.getMessage(), "Kesalahan" );
                    return false;
                }
                code = codes[0];
                sql = sql.replace( "INSERT INTO"
                                 , "INSERT IGNORE INTO"
                  ).replace(   "IFNULL( MAX(id), 0) + 1,   ? ,   ? "
                             , "id+count, CONCAT( ?, LEFT( (@code:=?), 0 ) ) , CAST( LPAD( @code+count-1,LENGTH(@code),'0' ) AS CHAR)"
                  ).replace(   "FROM barang"
                             , "FROM "
                             + "  ( "
                             + "    SELECT IFNULL( MAX(id), 0) + 1 AS id "
                             + "    FROM barang "
                             + "  ) barang "
                             + ", ( "
                             + "    SELECT n5.num*10000 + n4.num*1000 + n3.num*100 + n2.num*10 + n1.num + 1 AS count " // thks to http://stackoverflow.com/users/1051674/igor-kryltsov
                             + "    FROM "
                             + "            ( SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 ) n1, "
                             + "            ( SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 ) n2, "
                             + "            ( SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 ) n3, "
                             + "            ( SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 ) n4, "
                             + "            ( SELECT 0 AS num UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 ) n5  "
                             + "  ) count "
                             + "WHERE count <= " +count
                  );

android.util.Log.e("tambah: ", " build_sql = " + sql );
            }

        }

        String gambar_filename = "";
        //String[] params_ = { Tname.getText().toString(), code, Tberat.getText().toString(), Tstok.getText().toString(), Tomset.getText().toString(), Tsize.getText().toString(), Tdeskripsi.getText().toString(), Tharga_beli.getText().toString(), Tdiskon_beli.getText().toString(), Tharga_jual.getText().toString(), Tdiskon_jual.getText().toString(), gambar_filename, (sup_akses?String.valueOf(((jcdb_item)Cagent.getSelectedItem()).get_id()):"0"), Tqty1.getText().toString(), Tdisc_amount1.getText().toString()  , Tqty2.getText().toString(), Tdisc_amount2.getText().toString()  , Tqty3.getText().toString(), Tdisc_amount3.getText().toString()  , Tqty4.getText().toString(), Tdisc_amount4.getText().toString() };
        //this.params=params_;
        return true;
    }
    @Override void after_save_succeed() {
android.util.Log.e("after_save_succeed: ", " 1");
        retail.reset_brg();
android.util.Log.e("after_save_succeed: ", " 2");

        if( barcode_init.length()>0 ) Ftransaksi.form.refresh_brg();
        if( barcode_init.length()==0 || Tcode.getText().length()<panjang_barcode_masuk_history_tambah_barang ) return;    //if( barcode_init.length()==0 || Tcode.getText().length()<=14 || retail.hak_akses.indexOf("'Init Barang Beda Imei di Fitur Tambah Barang'") < 0 ) return;
android.util.Log.e("after_save_succeed: ", " 3");
        if( tambah_init==null ) tambah_init = new String[comps.size()] ;    //send to Fpembelian to send to next Ftambah
android.util.Log.e("after_save_succeed: ", " 4");
        for( int i=0; i<comps.size(); i++) {
if( comps.get(i) instanceof JCdb     )  android.util.Log.e("after_save_succeed: ", " ((JCdb)comps.get(i)).getSelectedItemPosition(): " +  ((JCdb)comps.get(i)).getSelectedItemPosition()  );
                 if( comps.get(i) instanceof JCdb     ) tambah_init[i] = ((JCdb)comps.get(i)).getSelectedItemPosition()+"";
            else if( comps.get(i) instanceof EditText ) tambah_init[i] = ((EditText)comps.get(i)).getText().toString();
        }
        Ftransaksi.form.tambah_init = tambah_init ;

android.util.Log.e("after_save_succeed: ", " 5");
        super.close();    //langsung keluar klo ada barcode_init heeh heee
android.util.Log.e("after_save_succeed: ", " 6");
    }

    /*
    @Override void clear_value( ViewGroup container ) {
        //Bhapus_gambar.doClick();
        super(panel);
    }
    */
}