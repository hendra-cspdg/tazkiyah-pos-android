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
import java.util.ArrayList;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;

public class Ftransaksi extends Fedit {
    JCdb Ccode_brg, Cname_brg;    ArrayList<Integer> harga_brg;    ArrayList<String> diskon_brg, gambar_brg;
//ItemListener sync_brg;
    //JCdb Ccode_brg  = retail.Ccode_brg;    JCdb Cname_brg  = retail.Cname_brg;    int[] harga_brg  = retail.harga_brg;    int[] diskon_brg = retail.diskon_brg;    String[] gambar_brg = retail.gambar_brg;
    //JCdb Ccode_brg  = new JCdb("");    JCdb Cname_brg  = new JCdb("");    int[] harga_brg  = {};    int[] diskon_brg = {};    String[] gambar_brg = {};

    String no_faktur  = "";    String no_faktur_log  = "";    String jam_faktur = "";
    int last_inserted_id = 0;
    String no_faktur_date_format = retail.user_id + "_%1$td.%<tm.%<ty.%2$tH.%<tM.%<tS";
    //JXDatePicker Dtgl_faktur;

    String sql_get_brg = "SELECT id, name, code, harga_jual, CAST( CONCAT_WS(',',diskon_jual,disc_qty1,disc_amount1,disc_qty2,disc_amount2,disc_qty3,disc_amount3,disc_qty4,disc_amount4) AS char ) AS diskon, gambar FROM barang " +(retail.convert_null(retail.setting.get("Jangan Jual Barang Habis Stok")).toLowerCase().equals("ya") ? "WHERE stok>0" : "" )+ " ORDER BY name";    //mending by name, krn klo BY code dia pastinya udah hafal (ato ada guideline dari kode barcode)

    //must be public to let retail.click_button() ...
    public android.view.MenuItem Bbaru, Bsimpan, Brefresh, Btambah_brg, Bedit_brg;    //Bsimpan must be public utk dipakai di class Bprint ...
    //JBkeluar Bkeluar;

//    public JLabel Lgambar;    //kuset public biar jadi tempat pengalihan focus saat input datang dari barcode scanner

//    public JTable_db table;    //private static JTable_db table;
//    public JMenuItem menu_tambah;
    int last_selected_row = -13;    int agent_id = -13;    int[] diskon_agent;    //int brg_id = retail.brg_id;    //-13;

    int sub_total = 0;    int total = 0;  int total_round = 0;    int banyak_total = 0;    int kembali = 0;
    int ppn = 0;    int diskon = 0;
    final String harga_poin_str = retail.setting.get("Harga Poin Member")==null ? "" : retail.setting.get("Harga Poin Member").replace(retail.digit_separator,"");    //ini kayaknya bikin error deh di andrid lama >> retail.convert_null(retail.setting.get("Harga Poin Member")).replace(retail.digit_separator,"")
    int harga_poin = ( retail.is_number(harga_poin_str) ) ? Integer.valueOf(harga_poin_str) : 15000;
    int poin = 0;    int poin_last_saved = 0;
    int last_Tdiskon = -87654321;    int current_Tdiskon;    int last_Tdiskon_edit = -87654321;    int current_Tdiskon_edit;

    Boolean print_sebelum_simpan = retail.convert_null(retail.setting.get("Print Sebelum Simpan")).toLowerCase().equals("ya");
    Boolean open_drawer_sebelum_simpan = retail.convert_null(retail.setting.get("Open Drawer Sebelum Simpan")).toLowerCase().equals("ya");
    Boolean poin_aktif = retail.convert_null(retail.setting.get("Aktifkan Poin Member")).toLowerCase().equals("ya");
    Boolean print_ulang_aktif = retail.convert_null(retail.setting.get("Aktifkan Print Ulang Transaksi")).toLowerCase().equals("ya");
    Boolean edit_potongan_aktif = retail.convert_null(retail.setting.get("Aktifkan Edit Rupiah Potongan")).toLowerCase().equals("ya");
    byte prosentase_ppn = retail.is_number( retail.convert_null(retail.setting.get("Prosentase PPN")) ) ? Byte.valueOf( retail.convert_null(retail.setting.get("Prosentase PPN")) ) : 0 ;
    int col_banyak_idx = retail.is_number( retail.convert_null(retail.setting.get("Posisi Kolom Banyak di Fitur Transaksi")) ) ? Integer.valueOf( retail.convert_null(retail.setting.get("Posisi Kolom Banyak di Fitur Transaksi")) ) : 3 ;
    Boolean enable_edit = true;
//    JComponent[] temp_col_editor;    int[] temp_col_width;
    Boolean calculate_disc_percent = true;    Boolean calculate_disc_rupiah = true;

    String sound = "sound/terimakasih" + ( retail.convert_null(retail.setting.get("Seragamkan Suara Terimakasih")).toLowerCase().equals("ya") ? "" : retail.user_id ) + ".wav" ;

    //JTextField Tdiskon_edit = new JTextField();
    EditText Tdiskon;
    //JCheckBox CHppn = new JCheckBox("PPN", retail.db.cfg.get("ppn_aktif_secara_default").equals("ya"));
    //JTextField Tppn = new JTextField();
    EditText Tsub_total, Ttotal, Tno_faktur;
    //public JLabel Ltotal_blink = new JLabel();    JLabel Lbanyak_blink = new JLabel("", JLabel.CENTER);
    //JTextField Tpoin;
    //JComboBox Ckontak_agent;
    //AutoCompleteTextView Cname_agent;
    public JCdb Ccode_agent, Cname_agent;    //dibaca di class celleditor untuk otomatis beralih dari scan barcode barang

//    public EditText Tdibayar;    //must be public to let requestFocus() from retail ...
//    EditText Lkembali;
    //JLabel Ldialog_kembali = new JLabel();
    //JLabel Ldialog_dibayar = new JLabel();
    //Object[] msg_dialog_kembali = { "\nDibayar:\n",Ldialog_dibayar, "\n\nKembali:\n",Ldialog_kembali,"\n\n\n\n\n" };   //Object[] msg_dialog_kembali = { "\n",Ldialog_kembali,"\n","\n" }; //ga iso ... label.setSize( new Dimension(600, 650) );
    //blink listener_blink;    clock listener_clock;    Timer timer_title;    Timer timer_total;    Timer timer_kembalian;    //Timer timer_refresh;
//    android.os.CountDownTimer timer_kembalian;
    String last_refresh = "";


    static Ftransaksi form;
    //public Ftransaksi() { super(); }
    public static void newInstance(String modul, Ftransaksi form ) {
        Fedit.newInstance(modul, form);
        Ftransaksi.form=form;
        //if( form instanceof Fpembelian ) {
        //    modul_table = "beli";    agent = "supplier";    stok_sign = "1*";
        //} else {
            modul_table = "jual";    agent = "Customer";   stok_sign = "-1*";    //pelanggan
        //} 
    }
    static String modul_table, agent, stok_sign;
    String sql_hapus_item;
    //di JTable aja, pukul rata semua ... tidak hanya di transaksi:p >> final int row_height    = retail.is_number( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) && Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) > 16 ? Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) : 38;    //defaultnya adalah 16    //32
    int code_width;    //   = retail.is_number( db.cfg.get("lebar_kode_di_tabel_transaksi") ) ? Integer.valueOf( db.cfg.get("lebar_kode_di_tabel_transaksi") ) : 100+15;
    void build_col() {
android.util.Log.e( "build: ", "5" );
        //barcode sebagai code tu lebar ... db.col_width     = new int[]        {           100, Ttotal.getX()+Ttotal.getWidth()-15 -5 -100-100-75-70-130,          -100,             75,           -70,          -130  ,             0  };   // (-) means not editable:)
        //db.col_width     = new int[]        {         125+20+10, retail.screen_width-15 -5 -125-20-10- 90-75-70-100,           ( retail.hak_akses.indexOf("'Edit Harga di Fitur Penjualan'") >= 0 ? 1 : -1 ) * 90,                  75,           -70,          -100  ,             0  };   // (-) means not editable:)

        db.col_width     = new int[]        {     code_width, 130 /*blm bisa klo item panjang, dia melebar sendiri, padahal header kagak:p 10000*/,           ( form instanceof Fpenjualan && retail.hak_akses.indexOf("'Edit Harga di Fitur Penjualan'") >= 0 ? 1 : -1 ) * 90,                  75,           -70,          -100  ,             0  };   // (-) means not editable:)
        db.col_editor    = new View[]       {      Ccode_brg,                                                   Cname_brg,          null,                null,          null,          null  ,          null  };
        db.col_label     = new String[]     {         "kode",                                                      "item",       "harga",            "banyak",      "diskon",       "total"  ,       "gambar" };    //don't contact the db, just create my own RowSetMetaData:)
        db.col_type      = new int[]        { Types.SMALLINT,                                              Types.SMALLINT, Types.INTEGER,      Types.SMALLINT, Types.INTEGER, Types.INTEGER  , Types.VARCHAR  };
        db.col_precision = new int[]        {              3,                                                           3,             9,                   5,             9,             9  ,           255  };    //ini menentukan jumlah karakter diizinkan

        /* if( col_banyak_idx<0 || col_banyak_idx>=db.col_width.length ) col_banyak_idx=3;
        if( col_banyak_idx!=3 ) {    //pindah posisi kolom banyak
            int        t_col_width     = db.col_width    [col_banyak_idx];    db.col_width    [col_banyak_idx] = db.col_width    [3];    db.col_width    [3] = t_col_width;
            JComponent t_col_editor    = db.col_editor   [col_banyak_idx];    db.col_editor   [col_banyak_idx] = db.col_editor   [3];    db.col_editor   [3] = t_col_editor;
            String     t_col_label     = db.col_label    [col_banyak_idx];    db.col_label    [col_banyak_idx] = db.col_label    [3];    db.col_label    [3] = t_col_label;
            int        t_col_type      = db.col_type     [col_banyak_idx];    db.col_type     [col_banyak_idx] = db.col_type     [3];    db.col_type     [3] = t_col_type;
            int        t_col_precision = db.col_precision[col_banyak_idx];    db.col_precision[col_banyak_idx] = db.col_precision[3];    db.col_precision[3] = t_col_precision;
        }
        */
    }
    @Override Boolean build_sql() {
android.util.Log.e( "build: ", "1" );

    /*private db_connection*/ db = new db_connection() {    //indahnya inherit :)        //private static db_connection db = retail.db;
        int bid=0;    JCdb Ceditor;    //utk dipakai juga di setvalue diskon sebelum update total_i
        @Override public void setValueAt(final Object val, final int row_idx, final int col_idx) {
            setValueAt(true, val, row_idx, col_idx);
        }
        @Override public void setValueAt(final Boolean async, final Object val, final int row_idx, final int col_idx) {
android.util.Log.e( "setval: ", "1" );
            //Ltotal_blink.setFont( new Font("Kartika", Font.BOLD, 16) );      Ltotal_blink.setText( Ltotal_blink.getText() + " '" + val.toString() + "'");
            //JOptionPane.showMessageDialog( null, "\nval=" + val.toString() + "\ntext=" + ((JTextComponent) Ccode_brg.getEditor().getEditorComponent()).getText() + "\nselectedindex=" + Ccode_brg.getSelectedIndex() , "title", JOptionPane.ERROR_MESSAGE);
            //System.out.println( " row_idx, col_idx =" + row_idx + " , "+ col_idx);
            //System.out.println( " getValueAt(row_idx, col_idx).toString() = " +getValueAt(row_idx, col_idx).toString() );
            if( row_idx<0 ) return;    //ini bisa terjadi saat posting code yg blm ada lalu user pilih tambah item baru yg pada gilirannya akan melakukan posting kedua dengan item baru tsb
            if( to_str( col_idx, getValueAt(row_idx, col_idx).toString().trim() ).equals( to_str(col_idx, val.toString().trim()) ) ) return;
            if( col_idx==3 && Integer.valueOf(val.toString()) < 1 ) {
                retail.show_error( "Banyak barang harus lebih besar dari 0.\nPerubahan dibatalkan!", "Kesalahan" );
                //table.requestFocus();
                return;
            }
            int brg_id = retail.brg_id;

            //berbeda dengan versi desktop, retail.brg_id diset duluan oleh listener sync_brg namun setvaluenya dia lakukan terakhir di asyncthread
            if( (col_idx==0 || col_idx==1) && brg_id>-12 ) {
                //Ceditor = col_idx==0 ? Ccode_brg : Cname_brg;
                bid = brg_id;    //Ceditor.getSelectedItemPosition() ;    //ini jika Ccode/Cnama boleh diketik >> Ceditor.my_index_of(val.toString());    
            }
android.util.Log.e( "kelompok: ", "brg_id="+ brg_id  );
            if( retail.convert_null(retail.setting.get("Kelompokkan Item Transaksi Yg Sama Ke Satu Baris")).toLowerCase().equals("ya")
              && (col_idx==0 || col_idx==1) && brg_id>-12 && table.getRowCount()>1 ) { //    jika kolom kode ato nama brg, dan hanya terjadi sekali di akhir aja     !Ccode_brg.isPopupVisible()  isShowing  isVisible()  && !table.isEditing()
android.util.Log.e( "kelompok: ", "1 col_idx=" + col_idx  + "   Ccode_brg.getCount()=" + Ccode_brg.getCount()  );
                String code = col_idx==0 ? val.toString() : Ccode_brg.getItemAt( Ccode_brg.getSelectedItemPosition() ).toString();    //Ccode_brg.getSelectedItemPosition() masih dalam keadaan terfilter ... //Ccode_brg.getItemAt(bid).toString();    //krn di asyncthread, ini belum terset >> db.getValueAt(row_idx,0).toString()).toLowerCase();
android.util.Log.e( "kelompok: ", "getIntAt(row_idx, 3)=" + (getIntAt(row_idx, 3))  );
                int banyak = getIntAt(row_idx, 3);    if( banyak<1 ) banyak=1;    //default banyak barang = 1;
android.util.Log.e( "kelompok: ", "banyak=" +banyak );
                //if( getIntAt(row_idx, 5)>0 )    //ribet banget, di android baru kok malah ga masuk sini .. brg_id>-12
                for( int i=0; i<table.getRowCount(); i++ ) {
                    if( i==row_idx ) continue;
                    android.util.Log.e( "kelompok: ", "2 i=" + i + " code=" + code + " db.getValueAt(i,0)=" + db.getValueAt(i,0).toString().toLowerCase() );
                    if( !code.equals( db.getValueAt(i,0).toString().toLowerCase() ) ) continue;
android.util.Log.e( "kelompok: ", "3 setbanyak = " + banyak + " + " + getIntAt(i, 3)  );
                    //if( table.isEditing() ) table.getCellEditor().cancelCellEditing();    //cancelll update ... i don't know exactly why but tanpa ini dia lock circular
                    db.removeRow(false, row_idx);    //db.removeRow(table.convertRowIndexToModel(row_idx));  //harus dilakukan kalau2 row ini sebelumnya da ada valuenya    //hapus baris yg lama 
android.util.Log.e( "kelompok: ", "4"  );
                    setValueAt( false, banyak + getIntAt(i, 3), i, 3 );    //ga pake super supaya nyang di bawah (saat user update banyak barang) jalan
                    //if( row_idx==table.getRowCount()-1 ) db.addRow();    //always add the empty lastrow
//untested: pindah atas agar hitung_sub_total gak ngitung dua kali?? db.removeRow(false, row_idx);    //db.removeRow(table.convertRowIndexToModel(row_idx));  //harus dilakukan kalau2 row ini sebelumnya da ada valuenya    //hapus baris yg lama 
android.util.Log.e( "kelompok: ", "5"  );
                    //ga jadi ah >> pindah atas krn remove_row ada dalam asyncthread>>
                    //not again :) >>
  if( row_idx==table.getRowCount() ) db.addRow(false);    //always add the empty lastrow
android.util.Log.e( "kelompok: ", "6"  );
                    table.getLayoutManager().scrollToPosition(i);
android.util.Log.e( "kelompok: ", "7"  );
//                    ((Table_adapter)table.getAdapter()).selected_row = i;
                    table.setRowSelectionInterval(i, i);    //pindah ke baris di mana ia ditemukan
android.util.Log.e( "kelompok: ", "8"  );
                    //int col_selected = retail.convert_null(retail.setting.get("Prefer Barcode")).toLowerCase().equals("ya") ? 3 : 0 ;    //table.convertColumnIndexToView(3)    //default edit banyak barang:p
                    //table.setColumnSelectionInterval(col_selected, col_selected);    //default edit banyak barang:p
                    //table.scrollRectToVisible( table.getCellRect( i, col_selected, true ) );    //supaya scrollbarpun ikut naik:p
                    //table.requestFocus();
                    return;    //break;
                }
            }

android.util.Log.e( "all set: ", "col_idx=" + col_idx +  "   val=" + val);
            super.setValueAt(async, val, row_idx, col_idx);
            if( (col_idx==0 || col_idx==1) && /*ribet banget, di android baru kok malah ga masuk sini ..*/ brg_id>-12 ) { //    jika kolom kode ato nama brg, set harga, diskon, dan default banyak   dan hanya terjadi sekali di akhir aja     !Ccode_brg.isPopupVisible()  isShowing  isVisible()  && !table.isEditing()
android.util.Log.e( "setval: ", "2" );
                //JCdb Ceditor = col_idx==0 ? Ccode_brg : Cname_brg;
                int banyak = getIntAt(row_idx, 3);    //ini gagal .... int banyak = ((Integer)getValueAt(row_idx, 3)).intValue();
android.util.Log.e( "setval: ", "2a" );
                //bid = Ceditor.my_index_of(val.toString());    //table.isEditing() && !val.toString().isEmpty() ? Ceditor.getSelectedIndex() : Ceditor.my_index_of(val.toString())  ;    //val.toString().isEmpty() bisa terjadi klo user mengosongkan isian kode, so jgn baca index Ccode yg lama    //table.isEditing() perlu dicek saat setvalue dari code program (contoh: saat refresh data barang)... int bid = col_idx==0 ? Ccode_brg.getSelectedIndex() : Cname_brg.getSelectedIndex()  ;    //int bid = (JComboBox)(table.getCellEditor(row_idx, col_idx).get_editor()).getSelectedIndex();    //Ccode_brg.getSelectedIndex();    //table.getCellEditor(row, col ).getTableCellEditorComponent( table, ((JComboBox)ed).getSelectedItem(), true, row, col );
                if( banyak<1 /* || banyak>1 && bid==-1  unfully tested .. ini aku lupa dulu apa alasannya ini diperlukan ... but anyway, ini jadi mereset banyak barang jadi 1 saat banyak barang=2 dan user menekan refresh (di method refresh_brg ada pula pancingan yg mengeset nama_barang="" ) dan kebetulan baris itu ada perubahan harga */ ) { banyak=1; super.setValueAt( false, banyak, row_idx, 3 ); }    //default banyak barang = 1;
android.util.Log.e( "setval: ", "2b" );
                //di bawah aja >> int diskon = bid<0 ? 0 : retail.get_diskon( diskon_brg.get(bid), harga_brg.get(bid), banyak );    //saat refresh, perlu ngosongin value2 :p
                //di bawah aja >> super.setValueAt( diskon, row_idx, 4 );
                int harga = bid<0 ? 0 : harga_brg.get(bid);    //saat refresh, perlu ngosongin value2 :p
android.util.Log.e( "setval: ", "3 harga=" + harga + "  row_idx=" + row_idx + "  bid=" + bid + "  banyak=" + banyak );
                setValueAt( async, harga, row_idx, 2 );    //ga pake super supaya total nanti keupdate juga:)   //setValueAt(  String.format( "%,d", harga_brg.get(bid) ), row_idx, 2 );
android.util.Log.e( "setval: ", "4" );
            } else if( (col_idx==2 || col_idx==3) && ( !async || getValueAt(row_idx, 0).toString().length()>0 || getIntAt(row_idx, 5)>0 ) )  { //set total_i = harga * (100 - diskon)     * banyak                   ) /100

android.util.Log.e( "setval: ", "col_idx=" + col_idx );
                String val_str = val.toString().trim();
                int val_int = val_str.length()==0 ? 0 : Integer.valueOf(val_str);
                int harga  = col_idx==2 ? val_int : getIntAt(row_idx, 2);    //ini diperlukan karena super.setvalue() ada dalam asyncthread sehingga tabel belum terupdate >> col_idx==2?val_int:getIntAt(row_idx, 2)
                int banyak = col_idx==3 ? val_int : getIntAt(row_idx, 3);
                if(col_idx==3) bid = Ccode_brg.my_filtered_index_of( getValueAt(row_idx, 0).toString() );    //Ccode_brg.my_index_of( getValueAt(row_idx, 0).toString() )  ;    //user ngedit manual ...
                int diskon = bid<0 ? 0 : retail.get_diskon( diskon_brg.get(bid), harga, banyak );
                super.setValueAt( false, diskon, row_idx, 4 );    //jika harga dan banyak berubah, diskon harus berubah juga
                int total_i = harga * banyak - diskon ;
                //int total_i = retail.round( (long) getIntAt(row_idx, 2) * (100 - getIntAt(row_idx, 4)) * getIntAt(row_idx, 3) , 100 );
                if( total_i<0 ) {   //exceed int max: 2.147.483.647  .. sebetulnya ini blm bs detect klo dia pas 2.147.483.647 krn total_i akan samadengan 0 :D .. luweh
                    retail.show_error( "Total harga terlalu besar.", "Kesalahan" );
                    total_i=0;
                }               //false, perlu agar jangan bikin thread baru agar hitung_sub_total valid
                super.setValueAt( false, total_i, row_idx, 5 );       //super.setValueAt( Integer.valueOf( super.getValueAt(row_idx, 2).toString()) * (1- Integer.valueOf(super.getValueAt(row_idx, 4).toString().replace("%","")) /100)  * Integer.valueOf(super.getValueAt(row_idx, 3).toString()), row_idx, 5 );
android.util.Log.e( "setval: ", "going to hitung_sub_total   col_idx=" + col_idx );
                hitung_sub_total(true);
                //if( total_i==0 || sub_total==0 ) table.requestFocus();

            } //else if( col_idx==6 ) if( table.isEditing() || row_idx==table.convertRowIndexToModel( table.getSelectedRow() ) ) load_gambar(val.toString());    //unfully tested:) Ada if tambahan utk mencegah load_gambar jika setvalue dilakukan dari code... Trus col ini hanya diupdate oleh itemlistener di class retail >> load_gambar( brg_id==-1 ? "" : gambar_brg[brg_id] );

            //((Table_adapter)table.getAdapter()).notifyDataSetChanged();    //db.fireTableDataChanged();
        }
        /*void super_setValueAt(Boolean async, Object val, int row_idx, int col_idx) {
            super.setValueAt(async, val, row_idx, col_idx);
        }*/
    };


android.util.Log.e( "build: ", "2" );
        db.enable_filter = false;
        db.is_editable = true;

android.util.Log.e( "build: ", "3" );

//            new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {


        //db.column_model = table.getColumnModel();
//ini ga boleh dipindah deh >> //pindah bawah krn build_sql sudah di async thread, begitu juga get_brg >>
        if( modul_table.equals("jual") ) retail.get_brg(table, sql_get_brg);  else retail.get_brg(table);

        //new android.os.Handler().post(new Runnable() { public void run() {    //nyang di atas ada db.exec(sql)nya sehingga ada di dalam asyncthread, suck android api developer


    //luweh, get_brg() dlm thread baru >> if( !retail.db.err_msg.equals("") ) {  error("Pembacaan Master Data Barang Gagal");    return; }        //((JComponent)retail.Ccode_brg).putClientProperty(JLabel.LABELED_BY_PROPERTY, table);    //JComponent.NEXT_FOCUS  //retail.tbl_brg = table;

android.util.Log.e( "build: ", "4" );
        Ccode_brg  = retail.Ccode_brg;    Cname_brg  = retail.Cname_brg;    harga_brg  = retail.harga_brg;    diskon_brg = retail.diskon_brg;    gambar_brg = retail.gambar_brg;

        code_width   = retail.is_number( db.cfg.get("lebar_kode_di_tabel_transaksi") ) ? Integer.valueOf( db.cfg.get("lebar_kode_di_tabel_transaksi") ) : 100+15;
        build_col();

android.util.Log.e( "build: ", "6" );
        db.show_table(false,null);

android.util.Log.e( "build: ", "66" );
        db.editor_barcode_target = 0;    //db.col_editor[0].setTag("barcode target");    //null pointer exception jg walo da dicreate di constructornya. Kayaknya gara2 contexnya yg ga sama deh >> form.table.col[2].setTag("barcode target");    form.table.col[2].setOnClickListener(retail.scan_listener2);    //db.col_editor[2].setOnLongClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_target = (TextView) Tcode;

//                return null;
//            }}.execute();


android.util.Log.e( "build: ", "7 db==null" + (db==null));
        table = new JTable( db, form.getActivity() );        //View view = inflater.inflate(R.layout.fragment_recycler, container, false);
android.util.Log.e( "build: ", "8" );
        retail.tbl_brg = table;    //utk ngereset klo tbl_brg terakhir dipakai di Fpembelian

android.util.Log.e( "build: ", "9 Cname_brg==null" + ( Cname_brg==null) );
        //untested >> harusnya kan udah di get_brg?! >>
 Cname_brg.setOnItemClickListener(retail.sync_brg);    Ccode_brg.setOnItemClickListener(retail.sync_brg);

android.util.Log.e( "build: ", "10" );


//}});

        sql_hapus_item = " UPDATE barang, " +modul_table+ "             \n"   //update barang.stok
                                   +  "     SET stok = stok + " +stok_sign+ " IF(stok<0,0,banyak)    \n"
                                   +  "     WHERE barang_id = barang.id \n"    //ga perlu krn yg nyimpan pasti form (user) ini >> + " AND user_id= '" + retail.user_id + "'
                                   +  "       AND faktur_id = @id   ;\n"    //(SELECT @id:=id FROM jual_faktur WHERE no_faktur = @no_faktur)    //ga perlu krn yg nyimpan pasti form (user) ini >> + " AND user_id= '" + retail.user_id + "'

                                   +  (modul_table.equals("jual") && poin_aktif ?
                                      " UPDATE " +agent+ ", " +modul_table+ "_faktur   \n"   //update pelanggan.poin
                                   +  "     SET poin = poin - FLOOR(total / " +harga_poin+ ") \n"
                                   +  "     WHERE " +agent+ "_id = " +agent+ ".id   \n"
                                   +  "       AND LOWER(" +agent+ ".name)<>'umum' \n"
                                   +  "       AND " +modul_table+ "_faktur.id = @id   ;\n"
                                   :  "" )

                                   +  " DELETE FROM " +modul_table+ " WHERE faktur_id = @id ;\n" ;             //" DELETE FROM jual WHERE faktur_id = (SELECT @id:=id FROM jual_faktur WHERE no_faktur = " +no_faktur+ ") ;\n" ;   //ga perlu krn yg nyimpan pasti form (user) ini >> + " AND user_id= '" + retail.user_id + "'


        return true;
    }

    //EditText Taset, Tmodal, Tstok, Titem;  //ActionListener summary_actionListener;
    int min_width = (int) ( 80 * retail.scale_width ), disabled_background_color = android.graphics.Color.TRANSPARENT ;    //0xff663399
    LayoutParams prms_tv, prms;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        new android.os.Handler().postDelayed(new Runnable() { public void run() {        }}, 200);



        //min_width = (int) ( min_width * retail.scale_width );
        sql ="";
android.util.Log.e( "penjual: ", "1" );
        form.enable_filter = false;

//E9D460
        footer_panel = new LinearLayoutCompat(form.getActivity());
        footer_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);

android.util.Log.e( "penjual: ", "2" + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));



            /*this should be replaced later by visibling horizontal scrollbar
            View border = new View(form.getActivity());
            border.setBackgroundResource( android.R.color.black );
            LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, 1 );
            footer_panel.addView( border, prms );
            */

        prms_tv = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );    //new LayoutParams( 85, LayoutParams.WRAP_CONTENT );
        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        prms.setMargins( 0, 7, 0, 0 );


        Tsub_total = new EditText(form.getActivity());    Tsub_total.setText("0");    Tsub_total.setEnabled(false);    Tsub_total.setMinWidth(min_width+20);    Tsub_total.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tsub_total.setHint( "Sub Total" );    Tsub_total.setBackgroundColor( disabled_background_color );    Tsub_total.setTextSize(18f);    Tsub_total.setTextColor( android.graphics.Color.LTGRAY );    //0xff060018
        TextInputLayout TL = new TextInputLayout(form.getActivity());    TL.addView( Tsub_total, prms_tv );
        footer_panel.addView( TL, prms );

        Tdiskon = new EditText(form.getActivity());    Tdiskon.setMinWidth(min_width);    Tdiskon.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tdiskon.setHint( "Potongan" );    Tdiskon.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    //Tdiskon.setBackgroundColor( disabled_background_color );
        TL = new TextInputLayout(form.getActivity());    TL.addView( Tdiskon, prms_tv );
android.util.Log.e( "penjual: ", "8 edit_potongan_aktif" + edit_potongan_aktif  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

        if( edit_potongan_aktif ) {
            footer_panel.addView( TL, prms );
            Tdiskon.addTextChangedListener( new android.text.TextWatcher() {
                public void afterTextChanged( android.text.Editable s ) {    //to me (especially for patternfilter), e.getType().toString() bernilai remove atau insert ... kadang2 dua2nya ... jadi double2 deh:p So, i have to save the last value of this element to compare :p
                    if( !calculate_disc_percent ) return;    //supaya ga circular lock:p
                    String o = s.toString().trim();
                    if( o.length()==0 ) o="0";
                    if( o!=null ) {
                        current_Tdiskon = Integer.valueOf(o);
                        if( current_Tdiskon==last_Tdiskon ) return;
                    }
                    last_Tdiskon = current_Tdiskon;
                    int percent = sub_total==0 ? 0 : Math.abs( retail.round( (long) 10000 * current_Tdiskon / sub_total , 100 ) );
                    //Object percent_view = Tdiskon_edit.getDocument().getProperty("plain_num");
                    //if( percent_view != null ) if( percent == (Integer)percent_view ) {
                    if( percent<=100 ) {
                        calculate_disc_rupiah=false;    //supaya ga circular lock:p
                        hitung_total();
                        calculate_disc_rupiah=true;
                        return;
                    }
                    if( percent>100 ) {
                        final int backup_Tdiskon = last_Tdiskon;
                        //SwingUtilities.invokeLater( new Runnable() { public void run() {
                            //doc2.removeDocumentListener( listener );
                            calculate_disc_percent=false;    //supaya ga circular lock:p
                            //attempt to mutate in notification >>
Tdiskon.setText( retail.numeric_format.format( -1* Math.abs(backup_Tdiskon) ) );
                            //System.out.println(" hh " + backup_Tdiskon );
                            calculate_disc_percent=true;    //supaya ga circular lock:p
                            //doc2.addDocumentListener( listener );
                        //}});
                        //mbohlah, ini bikin error klo barcode masuk sini >> 
                        //if( retail.input_buffer.length()<=3 || retail.input_buffer.indexOf("000")==0 ) {
                            retail.show_error( "Potongan harus lebih kecil dari 100%!\n\n\n\n\n\n", "Potongan Harus Dikurangi" );
                            //error (circular lock): attempt to mutate in notification .... hitung_total();    //Tdiskon.setText( String.format("%,d", -1*diskon) ) ;
                            Tdiskon.selectAll();    Tdiskon.requestFocus();
                        //}
                        return;
                    }
                    //calculate_disc_rupiah=false;    //supaya ga circular lock:p
                    //Tdiskon_edit.setText(String.valueOf(Math.abs(percent))) ;
                    //calculate_disc_rupiah=true;
                }
                public void onTextChanged( java.lang.CharSequence s, int start, int before, int count ) {    //This method is called to notify you that, within s, the count characters beginning at start have just replaced old text that had length before.
                }
                public void beforeTextChanged( java.lang.CharSequence s, int start, int count, int after ) {
                }
            });
        } //else {    Tdiskon.setEnabled(false);  Tdiskon.setBackgroundColor( disabled_background_color );    }

android.util.Log.e( "penjual: ", "9" + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

        Tno_faktur = new EditText(form.getActivity());    Tno_faktur.setMinWidth(min_width);    Tno_faktur.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //Tno_faktur.setHint( "No. Faktur" );
        //TL = new TextInputLayout(form.getActivity());    TL.addView( Tno_faktur, prms_tv );
        //footer_panel.addView( TL, prms );


        View ret = super.onCreateView(inflater, container, savedInstanceState);


//        final String modul = "Penjualan";
        //final int row_height    = retail.is_number( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) && Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) > 16 ? Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) : 38;    //defaultnya adalah 16    //32






        Ttotal = new EditText(form.getActivity());    Ttotal.setText("0");    Ttotal.setEnabled(false);     Ttotal.setMinWidth(min_width+40);    //Ttotal.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Ttotal.setHint( "Total" );    Ttotal.setTextColor( android.graphics.Color.GREEN );    Ttotal.setBackgroundColor( disabled_background_color );    Ttotal.setTextSize(28f);    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Ttotal, prms_tv );
        prms.setMargins( 0, 7, 0, 0 );
        toolbar.addView( TL, prms );


        /*LayoutParams prms_right = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        prms_right.setMargins( 0, 7, 0, 0 );
        prms_right.gravity = Gravity.RIGHT;
        */

        LinearLayoutCompat jp_sub = new LinearLayoutCompat(form.getActivity());
        jp_sub.setOrientation(LinearLayoutCompat.VERTICAL);

/*
        floating_label Lname_agent = new floating_label(form.getActivity());
        Lname_agent.setText(agent);    Lname_agent.setTextColor( android.graphics.Color.LTGRAY );
        LinearLayoutCompat.LayoutParams prms_sub = new LinearLayoutCompat.LayoutParams( 150, 12 );    //setPreferredSize(new Dimension(115,115));
        jp_sub.addView( Lname_agent, prms_sub );
*/

        Cname_agent = JCdb.newInstance(false, "", form.getActivity());    /*new AutoCompleteTextView(form.getActivity());*/    Cname_agent.setMinWidth(min_width+40);    //Cname_agent.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Cname_agent.setHint( agent );    Cname_agent.setTextColor( android.graphics.Color.YELLOW );    Cname_agent.setTextSize(18f);
        Cname_agent.setGravity( Gravity.TOP );
        //Cname_agent.setAdapter( new ArrayAdapter<String>( form.getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>() ) );
        LinearLayoutCompat.LayoutParams prms_sub = new LinearLayoutCompat.LayoutParams( 150, 30 );
        //jp_sub.addView( Cname_agent, prms_sub );

        footer_panel.addView( Cname_agent, prms );    //footer_panel.addView( jp_sub, prms );


        jp_sub = new LinearLayoutCompat(form.getActivity());
        jp_sub.setOrientation(LinearLayoutCompat.VERTICAL);

/*
        floating_label Lcode_agent = new floating_label(form.getActivity());
        Lcode_agent.setText("Id");    Lcode_agent.setTextColor( android.graphics.Color.LTGRAY );
        prms_sub = new LinearLayoutCompat.LayoutParams( 150, 12 );
        jp_sub.addView( Lcode_agent, prms_sub );
*/

        Ccode_agent = JCdb.newInstance(false, "", form.getActivity());    Ccode_agent.setMinWidth(min_width+10);    //Ccode_agent.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Ccode_agent.setHint( "Id" );    Ccode_agent.setTextColor( android.graphics.Color.YELLOW );    Ccode_agent.setTextSize(18f);
        Ccode_agent.setGravity( Gravity.TOP );
        prms_sub = new LinearLayoutCompat.LayoutParams( 150, 30 );
        //jp_sub.addView( Ccode_agent, prms_sub );

        footer_panel.addView( Ccode_agent, prms );    //footer_panel.addView( jp_sub, prms );


        android.widget.AdapterView.OnItemClickListener sync_agent = new android.widget.AdapterView.OnItemClickListener() { @Override public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {    //public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
android.util.Log.e("onlistener:", "aa1");
            if( agent_id>-13 ) return;  //supaya tidak listen setSelectedIndex yg dilakukan program.
android.util.Log.e("onlistener:", "2");

            int Ccode_agent_idx = Ccode_agent.my_key_of( Ccode_agent.getText().toString().trim() );    //Ccode_agent.my_index_of( Ccode_agent.getText().toString().trim() );
            int Cname_agent_idx = Cname_agent.my_key_of( Cname_agent.getText().toString().trim() );
/*
            int Cname_agent_idx = -1;
            for( int i=0; i < ((ArrayAdapter) Cname_agent.getAdapter()).getCount() ; i++ )
                if( Cname_agent.getText().toString().trim().equals( ((ArrayAdapter) Cname_agent.getAdapter()).getItem(i).toString().trim() ) )
                    Cname_agent_idx = (int) ((ArrayAdapter) Cname_agent.getAdapter()).getItemId(i);

android.util.Log.e("onlistener:", "Cname_agent_idx=" + Cname_agent_idx + "   Ccode_agent_idx=" + Ccode_agent_idx + "  Ccode_agent.getText()=" + Ccode_agent.getText().toString().trim()    + "  Cname_agent.getText()=" + Cname_agent.getText().toString().trim()   );
*/
            if( Cname_agent_idx==Ccode_agent_idx ) return;  //if( Cname_agent.getListSelection()==Ccode_agent.getListSelection() ) return;  //&& Cname_agent.getSelectedIndex()==Ckontak_agent.getSelectedIndex()     //supaya tidak ngulang ampe 2 kali:p

            AutoCompleteTextView Csrc, Cdst;
            if( parent.getAdapter()==Ccode_agent.getAdapter()  ) {  agent_id = Ccode_agent_idx;    Csrc = Ccode_agent;    Cdst = Cname_agent;  } else {  agent_id = Cname_agent_idx;    Csrc = Cname_agent;    Cdst = Ccode_agent;  }
            //agent_id = (int) id;    //position;

            //Csrc.setListSelection(position);
android.util.Log.e("onlistener:", "position=" + position + "   id=" + id + "  Cname_agent.getListSelection()=" + Cname_agent.getListSelection()   + "  Ccode_agent.getListSelection()=" + Ccode_agent.getListSelection()  );

            /*
            if( (AutoCompleteTextView)parent == Ccode_agent && agent_id == -1 ) {
                JTextComponent text_editor = (JTextComponent) Ccode_agent.getEditor().getEditorComponent();
                String text = text_editor.getText().trim();    //text_editor ga pernah lakukan setselectedindex otomatis:(
                agent_id = Ccode_agent.my_index_of(text);
                if( agent_id == -1 ) {
                    if( retail.hak_akses.indexOf("'Tambah Pelanggan di Fitur Transaksi'") >= 0 ) {
                        if( 0 == JOptionPane.showOptionDialog( table, "\nMaaf, Pelanggan dengan Kode '" +text+ "' tidak ditemukan!\n\nApakah anda ingin menambahkan data pelanggan baru?" + "\n\n\n\n\n\n", "Pelanggan tidak ditemukan", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, retail.icon_confirm, retail.Boptions2, null ) )  tambah_agn(text);
                    } else
                        JOptionPane.showMessageDialog( table, "Maaf, Pelanggan dengan Kode '" +text+ "' tidak ditemukan!\nSilahkan ketikkan kode/nama pelanggan untuk mencari atau jalankan Modul Tambah Pelanggan." + "\n\n\n", "Pelanggan tidak ditemukan", JOptionPane.ERROR_MESSAGE ) ;
                    Ccode_agent.setListSelection(Cname_agent.getListSelection());    //balikin    //text_editor.setText( Ccode_agent.getItemAt( Cname_agent.getSelectedIndex() ).toString() );    //text_editor.setText("");
                } else
                    Ccode_agent.setListSelection(agent_id);
            }
            */
android.util.Log.e("onlistener:", "3");

            if( agent_id == -1 ) {
                agent_id = -13;
                return;  //force it to selectable index
            }
android.util.Log.e("onlistener:", "4");
            Cdst.setText( ((ArrayAdapter) Cdst.getAdapter()).getItem( agent_id ).toString()     );

//            if( parent != Cname_agent.getAdapter()   ) {    Cname_agent.setSelection   ( agent_id );    Cname_agent.setText( ((ArrayAdapter) Cname_agent.getAdapter()).getItem( agent_id ).toString()     );    }
android.util.Log.e("onlistener:", "5");
//            if( parent != Ccode_agent.getAdapter()   ) {    Ccode_agent.setSelection   ( agent_id );    Ccode_agent.setText( ((ArrayAdapter) Ccode_agent.getAdapter()).getItem( agent_id ).toString()     );    }
android.util.Log.e("onlistener:", "6");
            //if( e.getItemSelectable() != Ckontak_agent ) Ckontak_agent.setSelection ( agent_id );
            //Tdiskon_edit.setText( String.valueOf(diskon_agent[agent_id]) );
            //show_poin();
            agent_id = -13;
        }};	
        //Ccode_agent.setSelection(-1);    Cname_agent.setSelection(-1);    //Ckontak_agent.setSelectedIndex(-1);    //supaya bisa langsung in action at the first time saat di bawah diset Cname_agent.setSelectedIndex(0)
        Cname_agent.setOnItemClickListener(sync_agent);  Ccode_agent.setOnItemClickListener(sync_agent);  //Ckontak_agent.addItemListener(sync_agent);

        //if( retail.hak_akses.indexOf("'Tambah Pelanggan di Fitur Transaksi'") >= 0 ) {
            //Cname_agent.setSize(Cname_agent.getWidth()-73, Cname_agent.getHeight());
            final android.support.v7.widget.AppCompatButton Btambah_agn    = new android.support.v7.widget.AppCompatButton(form.getActivity());
            //Btambah_agn.setText( "Tambah " + agent );
            //Btambah_agn.setHint( "Tambah " + agent );
            Btambah_agn.setMinWidth(min_width);    //Btambah_agn.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            LayoutParams prms2 = new LayoutParams( 50, 50 );
            //prms.setMargins( 10, 7, 10, 0 );
            prms2.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL ;
            footer_panel.addView( Btambah_agn, prms2 );

            //Btambah_agn.setBackgroundColor(0x5500ff00);
            //Btambah_agn.setCompoundDrawablesWithIntrinsicBounds( 0, icon_path, 0, 0 ) ;
            Btambah_agn.setBackgroundResource( R.drawable.symbol_add_1 );
            Btambah_agn.setOnClickListener( new View.OnClickListener() { @Override public void onClick(View v) {
                tambah_agn("");
            }});

        //}






android.util.Log.e( "penjual db.col_width: ", ""+db.col_width.length  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));
        if( col_banyak_idx<0 || col_banyak_idx>=db.col_width.length ) col_banyak_idx=3;
        //if( col_banyak_idx!=3 ) db.column_model.moveColumn(3,col_banyak_idx);    //pindah posisi kolom banyak

android.util.Log.e( "penjual: ", "12" );
        if( !db.err_msg.equals("") ) {  error("Pembacaan Data " +modul+ " Gagal");   return null; }
android.util.Log.e( "penjual: ", "13"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

        /*
        String font_face = db.cfg.get("font_di_tabel_transaksi");
        int font_size    = retail.is_number( db.cfg.get("font_size_di_tabel_transaksi") ) ? Integer.valueOf( db.cfg.get("font_size_di_tabel_transaksi") ) : 14 ;
        int font_style   = Font.PLAIN ;
        if( retail.db.cfg.get("font_bold_di_tabel_transaksi").equals("ya") ) font_style = Font.BOLD ;
        if( retail.db.cfg.get("font_italic_di_tabel_transaksi").equals("ya") ) font_style = (font_style==Font.PLAIN?0:font_style) + Font.ITALIC ;
        header.setFont( new Font( font_face, Font.PLAIN, font_size) );    //Arial    //header.setFont( header.getFont().deriveFont(14f) );
        table.setTableHeader(header);
        table.setFont( new Font(font_face, font_style, font_size) );    //Font.BOLD    //Arial    //table.setFont( table.getFont().deriveFont(14f) );
        */

        /*
        Bprint = new JBprint(this);
        Bprint.setBounds( Bkeluar.getX(), panel_dibayar.getY()-Bkeluar.getHeight()-8, Bkeluar.getWidth(), Bkeluar.getHeight() );  panel.add(Bprint);
        //Bprint.setBounds( Bkeluar.getX(), Bkeluar.getY()-Bkeluar.getHeight()-50, Bkeluar.getWidth(), Bkeluar.getHeight() );  panel.add(Bprint);
        Insets margin_b = Bprint.getMargin();
        Bprint.setMargin( new Insets( margin_b.top, margin_b.left+6, margin_b.bottom, margin_b.right+6 ));    Bprint.setHorizontalAlignment(SwingConstants.LEFT);
        */

        /*
        Bhapus_faktur = new JButton("F9 - hapus faktur", retail.icon_delete_master );  Bhapus_faktur.setAlignmentX( Bkeluar.getAlignmentX() );  Bhapus_faktur.setIconTextGap( Bkeluar.getIconTextGap() );
        Bhapus_faktur.setBounds( Bkeluar.getX(), Bsimpan.getY()-Bkeluar.getHeight()-1, Bkeluar.getWidth(), Bkeluar.getHeight() );  panel.add(Bhapus_faktur);
        Bhapus_faktur.setMnemonic('U');  Bhapus_faktur.addFocusListener(retail.onHover);    Bhapus_faktur.setMargin(Bprint.getMargin());    Bhapus_faktur.setHorizontalAlignment(Bprint.getHorizontalAlignment());
        Boolean Bhapus_faktur_visible = !retail.convert_null(retail.setting.get("Buka Faktur Baru Setelah Simpan")).toLowerCase().equals("ya") ;
        Bhapus_faktur.setVisible( Bhapus_faktur_visible );    Bhapus_faktur.setEnabled(false);

        Bbaru.setBounds( Bkeluar.getX(), ( Bsimpan.getY()-Bkeluar.getHeight()-1 ) - ( Bhapus_faktur_visible ? Bkeluar.getHeight()-1 : 0 ), Bkeluar.getWidth(), Bkeluar.getHeight() );  panel.add(Bbaru);
        Bbaru.setMnemonic('B');  Bbaru.addFocusListener(retail.onHover);    Bbaru.setMargin(Bprint.getMargin());    Bbaru.setHorizontalAlignment(Bprint.getHorizontalAlignment());
        if( retail.convert_null(retail.setting.get("Aktifkan Multi Form Transaksi")).toLowerCase().equals("ya") ) Bbaru.setToolTipText("[Shift+F5] - Buka Window Baru | [Alt+F5] - Pindah Antar Window");
        */

        form.menu.add( Menu.NONE, 10,   6, "Simpan" );    Bsimpan = form.menu.getItem(0);
        form.menu.add( Menu.NONE,  9,   7, "Baru" );    Bbaru = form.menu.getItem(1);
        if( retail.hak_akses.indexOf("'Refresh Barang di Fitur Transaksi'") >= 0 ) {  form.menu.add( Menu.NONE,  8,    8, "Refresh" );    Brefresh = form.menu.getItem(2);  }
        if( retail.hak_akses.indexOf("'Edit Barang di Fitur Transaksi'")    >= 0 ) {  form.menu.add( Menu.NONE,  7,    9, "Edit Barang" );    Bedit_brg = form.menu.getItem(3);  }
        if( retail.hak_akses.indexOf("'Tambah Barang di Fitur Transaksi'")  >= 0 ) {  form.menu.add( Menu.NONE,  6,   10, "Tambah Barang" );    Btambah_brg = form.menu.getItem(4);  }




android.util.Log.e( "penjual: ", "14"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

        set_enable_tombol2(false);

android.util.Log.e( "penjual: ", "17"  /*+ ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ) */ );
//( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 )indowToken(), 0 );



//pindah atas         db.col_editor[0].setTag("barcode target");    //null pointer exception jg walo da dicreate di constructornya. Kayaknya gara2 contexnya yg ga sama deh >> form.table.col[2].setTag("barcode target");    form.table.col[2].setOnClickListener(retail.scan_listener2);    //db.col_editor[2].setOnLongClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_target = (TextView) Tcode;
android.util.Log.e( "penjual: ", "18"  /*+ ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) )*/);

        //Lkembali.performClick();    //form.table.col[0].requestFocus();    //Tdibayar.requestFocus();    retail.hideSoftKeyboard( (android.app.Activity) form.getActivity() );    //entah kenapa softkeyboardnya muncul:p

        new android.os.Handler().post(new Runnable() { @Override public void run() {    //Ccode_brg.post(new Runnable() { @Override public void run() {
            refresh_agn();
        }});


        return ret;
    }

    void simpan() {
        simpan( modul_table.equals("jual") && Bsimpan.getTitle().toString().indexOf("tunggu")<0 &&  retail.convert_null(retail.setting.get("Buka Faktur Baru Setelah Simpan")).toLowerCase().equals("ya") );
    }
    void simpan( final Boolean baru_action ) {
        //baru_action = false;
        if( table==null ) return;
        if( !Bsimpan.isEnabled() ) return;   //krn bisa dipanggil arbitrarly (salah ya spellingnya:p) oleh method lain
            //if( retail.empty( new JTextComponent[]{ Tno_faktur }, new String[]{ Lno_faktur.getText() } ) ) return;    //table.requestFocus() dulu?!
            //if( table.isEditing() ) table.getCellEditor().stopCellEditing();    //post update ... pingin jaga2 aja
            //if( !Bsimpan.isEnabled() ) {    //mungkin Bsimpan.setEnabled(false) oleh stopCellEditing() di atas ?!
            //    JOptionPane.showMessageDialog( Fpenjualan.this, "Belum ada data yang bisa disimpan!", "Penyimpanan Data " + modul + " Gagal", JOptionPane.ERROR_MESSAGE);
            //    return;
            //}
            //if( Ccode_agent.getSelectedIndex() == -1 ) {
            //    JOptionPane.showMessageDialog( Fpenjualan.this, "Mohon simpan ulang!", "Penyimpanan Data " + modul + " Gagal", JOptionPane.ERROR_MESSAGE);
            //    return;
            //}

        Bsimpan.setEnabled(false);    //ini harus segera karena bisa aja di Tdibayar user ketik 60 lalu klik simpan ... Otomatis retail.add_ribuan akan nyimpan lagi untuk kedua kalinya!!!
            try {
        //ga sahlah, harus dibalas lagi ntar di swingutilities invoke later >> retail.in_progress = true;
        Ttotal.setEnabled(true);    Ttotal.requestFocusFromTouch();    Ttotal.setEnabled(false);    //((View)Bsimpan).requestFocus();    //FromTouch();    //to let add_ribuan when harga or Tdibayar lost focus

            } catch (Exception e) {
                  retail.show_error( "Terjadi Error: " + e.getMessage() + "\n\nMohon Hubungi Rafik!\n\n\n\n", "Error!" );
            }
        /*Bsimpan.setEnabled(false);*/    final String no_faktur_text = Tno_faktur.getText().toString();
android.util.Log.e( "simpan 1st: ", " Tno_faktur.getText()=" + no_faktur_text);

        new android.os.AsyncTask<Void, Void, String> () {
            @Override protected String doInBackground( Void... v ) {
android.util.Log.e( "simpan: ", "1");

android.util.Log.e( "simpan: ", "5");

                //if( open_drawer_sebelum_simpan ) retail.draw_open();

                String no_faktur_new   = retail.escape( no_faktur_text.trim() );    //Tno_faktur.getText().toString()
                String agent_id        = "'" + "1" /*((jcdb_item)Ccode_agent.getSelectedItem()).get_id()*/ + "'";
                jam_faktur             = new SimpleDateFormat("HH:mm:ss").format(new Date());    //nanti akan dipakai saat print
                String tgl_faktur      = "'" + new SimpleDateFormat("yyyy-MM-dd").format( new Date() /*Dtgl_faktur.getDate()*/ ) + " " + jam_faktur + "'";    //String tgl_faktur      = "CONCAT( '" + new SimpleDateFormat("yyyy-MM-dd").format( Dtgl_faktur.getDate() ) + " ',  CURRENT_TIME )";
                String diskon          = (-1 * Math.abs( Integer.valueOf( Tdiskon.getText().toString().replace(retail.digit_separator,"") ))) + "";    //klo - gmn:p >> "-" + Tdiskon.getDocument().getProperty("plain_num").toString().replace("-","");    //Tdiskon_edit
                String sql_insert_faktur = "";    //manfaatin autoincrement aja, toh records table ini ga pernah didelete kan?
                //String sql = " SET @succeed=0, @id = null, @no_faktur =  " + (no_faktur.length()==0?"''":no_faktur) + ", @no_faktur_new =  " +no_faktur_new+ "  ;\n" ;   //SET @id sebenarnya ga perlu amat krn gimanapun jg, @id kan kereset di bawah ini.   //ga iso >> " DECLARE id INT DEFAULT NULL ;\n";    //jika @id tetap is null, query berikutnya gak jalan (Column 'faktur_id' cannot be null)
                String[] params = {no_faktur_new};
                if( no_faktur.length()==0 ) {    //utk dapat last_inserted_id, sementara ini kudu no params :p
                    sql_insert_faktur = " INSERT INTO " +modul_table+ "_faktur (  id  , no_faktur ,    " +agent+ "_id    ,    tgl_faktur    ,    total          ,       ppn                                           ,    diskon    ,    sub_total    ,           user_id    )  \n"
                                           + "                     VALUES      ( null ,     ?     ,    " +agent_id+ "    , " +tgl_faktur+ " , " +total_round+ " , " +"0" /*(CHppn.isSelected()?prosentase_ppn:0)*/+ " , " +diskon+ " , " +sub_total+ " , " +retail.user_id+ " )";    // ;\n  ga iso:) .. " SET id   = (SELECT IFNULL( MAX(id), 0) + 1 FROM jual_faktur)  ;\n"  ;       //+  " SET id   = LAST_INSERT_ID(id+1)"
                                           //+ " SET @succeed=(SELECT IF(MAX(id)=@id,1,0) FROM jual_faktur) ;\n"   //klo mo aman ya pake ini aja (walopun sepertinya lebih lambat) >> " SELECT @succeed:=1 FROM jual_faktur WHERE id=@id ;\n"    //bikin lambat? ... hmm    //ini ga iso krn autoincrementnya harusnya diisi null:p >> " SET @succeed=IF(LAST_INSERT_ID()=@id,1,0) ;\n"  ;  //berat g ya :p
                                           //+ " SET @id=IF(@succeed=1,@id,null) ;\n" ;   //klo mo aman ya pake ini aja (walopun sepertinya lebih lambat) >> " SELECT @succeed:=1 FROM jual_faktur WHERE id=@id ;\n"    //bikin lambat? ... hmm    //ini ga iso krn autoincrementnya harusnya diisi null:p >> " SET @succeed=IF(LAST_INSERT_ID()=@id,1,0) ;\n"  ;  //berat g ya :p
                                           //cannot issue select on executed batch :p >> + " SELECT @succeed:=IF(MAX(id)=@id,1,0), @id:=IF(@succeed=1,@id,null) FROM jual_faktur ;\n" ;   //klo mo aman ya pake ini aja (walopun sepertinya lebih lambat) >> " SELECT @succeed:=1 FROM jual_faktur WHERE id=@id ;\n"    //bikin lambat? ... hmm    //ini ga iso krn autoincrementnya harusnya diisi null:p >> " SET @succeed=IF(LAST_INSERT_ID()=@id,1,0) ;\n"  ;  //berat g ya :p
                                           //+ " SELECT @id:=IF(@succeed=1,@id,null) ;\n"  ;    //" SET @id=IF(@succeed=1,@id,null) ;\n"
                } else {    //berarti sudah pernah disimpan, hapus dulu data penjualan yg sudah ada utk faktur ini, lalu update jual_faktur.
                    //the best shoot will be the trigger which call stored procedure ... but trigger only enable on > 5.0 version anyway:p and I need to download it first.
                    //I have 6.0 (not supported) version but it doesn't match jdbc driver:p ... I have tried to copy proc and help* table from 6.0 to 5.0 also ... but stored procedure doesn't work either
                    sql_insert_faktur =
                    //untested (kadang2 @id tak pasti!!!) >>   sql_hapus_item +
                           " UPDATE " +modul_table+ "_faktur"
                        +  "     SET   no_faktur      = '" +no_faktur_new+ "'"
                        +  "         , " +agent+ "_id = " + agent_id
                        +  "         , tgl_faktur     = " + tgl_faktur
                        +  "         , total          = " + total_round
                        +  "         , ppn          = " + "0"  //(CHppn.isSelected()?prosentase_ppn:0)
                        +  "         , diskon       = " + diskon
                        +  "         , sub_total    = " + sub_total
                        +  "         , user_id      = " + retail.user_id
                        +  "     WHERE id = (@id:=" +last_inserted_id+ ") ;\n"    //LAST_INSERT_ID();    //@id + (@succeed:=1) -1 
                        +  sql_hapus_item;
                    params = null;    //utk batch file, no params:p
                }

android.util.Log.e( "simpan: ", "6");
                db.exec(sql_insert_faktur, params, true);
                try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}

android.util.Log.e( "simpan: ", "7 db.err_msg=" + db.err_msg + "   sql=" + sql_insert_faktur+ "   no_faktur=" + no_faktur_new);

                //saat simpan no_faktur, anyway tetap hrs tangani duplicate dengan autoinsert dengan cara nambahin auto number krn dia boleh diedit user!
                //The value of LAST_INSERT_ID() is not changed if you update the AUTO_INCREMENT column of a row with a non-magic value (that is, a value that is not NULL and not 0). >> sql += " SET @id = IF( @id=LAST_INSERT_ID(id+1)  ;\n" ;
                if( db.err_msg.indexOf("sudah ada" ) >= 0)    //loop ini hanya ngulang klo duplicate jual_faktur.id
                    for( int i=0; i<1000; i++ ) {
                        String no_faktur_loop = no_faktur_new + "#" + String.valueOf(i+1) ;    //no_faktur_new.substring( 0, no_faktur_new.length()-1 ) + "#" + String.valueOf(i+1) + "'";
                        //sql = " SET @no_faktur_new = " +no_faktur_loop+ "  ;\n" + sql_insert_faktur ;
                        params = new String[]{no_faktur_loop};
                        db.exec(sql_insert_faktur, params, true);
                        try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
                        if( db.err_msg.indexOf("sudah ada") < 0 ) { //berhasil insert tanpa duplikasi
                            no_faktur_new     = no_faktur_loop  ;   //to use next
                            //???ret="sudah ada>"+no_faktur_new;
                            //Tno_faktur.setText(no_faktur_new);
                            //Bsimpan.setEnabled(false);    //gara2 di atas:)
                            break;
                        }
                    }   //end of for loop


android.util.Log.e( "simpan: ", "8");

                String sql="";
                if( db.err_msg.equals("") )   // {    //the best way should be db compound or procedure statement
                    if( no_faktur.length()==0 ) last_inserted_id = db.last_inserted_id;    //mencoba mengambil jalan paling aman aja :p

android.util.Log.e( "simpan: ", "9");

                //sql tetap dibuild agar bisa masuk ke error.log walaupun tidak dieksekusi
                sql += " SET @id = " +last_inserted_id+ "  ;\n" ;
                if( modul_table.equals("jual") && poin_aktif && !agent_id.equals("'1'") )  sql += " UPDATE pelanggan \n "   //update pelanggan.poin
                                                                                               +  "     SET poin = poin + " +poin+ "   \n"
                                                                                               +  "     WHERE id = " +agent_id+ " ;\n"  ;  // AND @succeed=1    //@id IS NOT NULL    //ini beresiko tapi daripada lelet, lebih baik pelanggan 'umum' ini dideteksi:p
                //gmn klo hanya ada satu record dan itu masih default (tak perlu diinsert)?  << Bsimpan dah ga enabled:)
                int r=0;    String banyak_cases="";    String barang_ids="";    //sql_stock="";
                for( int i=0; i<db.getRowCount(); i++ ) {
                    String barang_id = db.getValueAt(i,0).toString();
                    if( barang_id.length()==0 ) continue;  //user batal nginsert row
                    barang_id = db.to_db( 0, barang_id );
                    String banyak = db.to_db( 3, db.getValueAt(i,3) );
                    if(r==0) sql +=  " INSERT INTO " +modul_table+ " ( faktur_id,      no      ,     barang_id    ,                           total          ,     banyak       ,                      diskon            ) VALUES \n" ;    //jika @id is null query berikut gak jalan (Column 'faktur_id' cannot be null) >> sql =  " SET @id='fail'    ;\n"  //ah, ternyata dia tetap insert dengan @id=0 ... so biarin ajaahhhh, paling2 datanya banjir :) //biar ga lanjut klo query sebelum ini fail.
                    else     sql +=  " , " ;
                    sql +=           "                  (       @id, " + (i+1) + ", " + barang_id + ", " + db.to_db( 5, db.getValueAt(i,5) ) + ", " + banyak + ", " + db.to_db( 4, db.getValueAt(i,4) ) + " )  \n"  ;         //sql +=  "(@id, " + (1+Ccode_brg.getSelectedItemPosition()) + ", " + (Integer)db.getValueAt(i,5) + ", " + (Integer)db.getValueAt(i,3) + ", " + (Integer)db.getValueAt(i,4) + ")  \n"  ;
                    //tapi apa iya ada yg bawa barang ke kasir, padahal stoknya ga ada:) ... anyway, barang.stok need to NOT unsigned >> dari Fpenjualan, sebaiknya si cek dulu apa stok cukup ... klo dari form interface, kurang perfect deh:)
                    //sql_stock += " UPDATE barang SET stok = stok - IF(stok<0,0," +banyak+ ") WHERE id = " + barang_id + " ;\n" ;
                    banyak_cases += " WHEN " + barang_id + " THEN " + banyak ;
                    if( r>0 ) barang_ids += ", " ;
                    barang_ids += barang_id;
                    r++;
                }
                if( r>0 )  sql += " ;\n UPDATE barang SET stok = stok + " +stok_sign+ " IF( stok<0, 0, CASE id " +banyak_cases+ " ELSE 0 END ) WHERE id IN (" + barang_ids + ") ;\n" ;    //sql_stock;

android.util.Log.e( "simpan: ", "10");
                if( db.err_msg.equals("") ) {
android.util.Log.e( "simpan: ", "11");
                    db.exec(sql, null, true);
                    try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
android.util.Log.e( "simpan: ", "12 db.err_msg=" + db.err_msg + "   no_faktur=" + no_faktur_new);

                    if( db.err_msg.equals("") ) {
                        no_faktur=no_faktur_new;
                        poin_last_saved=poin;    //utk dipakai di poin_actual() jika simpan dilakukan setelah print!    //bikin ribet sebenarnya ni, apalagi klo tetap aja simpan dulu baru draw_open kan? Lambatnya ngeprint terjadi karena print menunggu draw_open selesai dulu!
                        //Bhapus_faktur.setEnabled(true);
                        //Bkosongkan.doClick();

                        //if( modul_table.equals("jual") && Bsimpan.getTitle().toString().indexOf("tunggu")<0 &&  retail.convert_null(retail.setting.get("Buka Faktur Baru Setelah Simpan")).toLowerCase().equals("ya") )
                            //baru_action = true;    //Bbaru.doClick();    //jika textnya tunggu, berarti printing akan jalan setelah ini dan abis itu baru si Bprint yang akan jalanin Bbaru.doClick();

                        //else
                            //Bbaru.requestFocus();
                        //supaya form bisa dibuka banyak, letakkan ini di method print. Lambat? later to check:p
                        //tapi tetap ga bisa juga. Mungkin krn lambat?! ... retail.draw_init();
                    //    if( !open_drawer_sebelum_simpan ) retail.draw_open();    //if( Bsimpan.getText().indexOf("tunggu")<0 ) retail.draw_open();    //otherwise, Bprint yg akan mbuka drawer!!!    //kayaknya ini deh yg bikin lambat banget klo ia diproses sebelum printing ... tp klo kutaruh sebelum simpan, jadi ga baik krn data ga kesimpan tapi laci kok da kebuka?!
                        //tapi tetap ga bisa juga. Mungkin krn lambat?! ... retail.draw_close();
                    } else {
                        //justru sekarang kudu insert terus krn semua query dah ga jalan klo yg pertama gagal (krn duplikat nomor faktur) >> if( no_faktur.isEmpty() ) no_faktur=no_faktur_new;    //hanya biar ga insert melulu .... banjir ntar
                        String msg=db.err_msg;
                        if( msg.indexOf("'faktur_id' cannot be null")>=0 ) msg = "\nMaaf, data gagal diupdate!\nPesan kesalahan: " + "Penyimpanan faktur gagal (kemungkinan karena duplikat Nomor Faktur)" + ".\nCoba perbaiki Nomor Faktur!\n\n\n\n\n";    //berarti gagal pas insert jual gara2 insert jual_faktur juga gagal:)
                        if( no_faktur.length()>0 || !no_faktur_new.equals(no_faktur_log) ) {
                            no_faktur_log = no_faktur_new;
                            retail.log_error( "Modul: " + modul + ". Penyimpanan Data " + modul + " Gagal setelah penyimpan nomor faktur berhasil! Pesan: " + msg.replace("\n", " ") + " HATI-HATI JIKA INGIN MENGULANG QUERY BERIKUT INI KARENA MUNGKIN SAJA SEBAGIAN SUDAH BERHASIL TEREKSEKUSI: \r\n" + sql );    //log error dan tambahkan  + "\r\n" supaya sql setelahnya bisa dieksekusi later :p
                        }
                        db.exec( " REPAIR TABLE " +modul_table+ "_faktur, " +modul_table+ ", barang", null, true);    //just try to repair ... table jual juga sering corrupt saat server mati mendadak.
                        try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
                        db.err_msg = msg;
                        return "Penyimpanan Data " + modul + " Gagal";
                        //retail.show_error( msg, "Penyimpanan Data " + modul + " Gagal" );
                        //Bsimpan.setEnabled(true);
                    }

                } else {
                    if( no_faktur.length()>0 || !no_faktur_new.equals(no_faktur_log) ) {
                        no_faktur_log = no_faktur_new;
                        //sql_insert_faktur = db.cmdp.toString();    //EXCEPTION: com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after statement closed.
                        //sql_insert_faktur = sql_insert_faktur.substring( sql_insert_faktur.indexOf(":") + 1 );
                        for( String param : params ) sql_insert_faktur = sql_insert_faktur.replaceFirst( "\\?", "'"+retail.escape(param)+"'" );   //seharusnya escape juga "?" yang ada di dalam quote atau didahului "/":p
                        retail.log_error( "Modul: " + modul + ". Penyimpanan nomor faktur gagal! Pesan: " + db.err_msg.replace("\n", " ")  + "\r\n" + sql_insert_faktur + " ;\n" + sql );    //log error dan tambahkan  + "\r\n" supaya sql setelahnya bisa dieksekusi later :p
                    }
                    db.exec( " REPAIR TABLE " +modul_table+ "_faktur, " +modul_table, null, true);    //just try to repair ... table jual_faktur sering corrupt saat server mati mendadak.
                    try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
                    db.err_msg = "Penyimpanan nomor faktur gagal!\nMohon ulangi!\n" + db.err_msg;
                    return "Penyimpanan Data " + modul + " Gagal";
                    //retail.show_error( "Penyimpanan nomor faktur gagal!\nMohon ulangi!\n" + db.err_msg, "Penyimpanan Data " + modul + " Gagal" );
                    //Bsimpan.setEnabled(true);
                }
                //table.requestFocus();
                return "";
            }

            @Override protected void onPostExecute( String ret ) {
                if( ret.length()==0 ) {
                    Tno_faktur.setText(no_faktur);    //Tno_faktur.setText( ret.startsWith("sudah ada>") ? ret.substring( "sudah ada>".length() ) : ret );
android.util.Log.e( "simpan berhasil: ", " Tno_faktur.getText()=" + Tno_faktur.getText().toString() );
                    Bsimpan.setEnabled(false);    //gara2 di atas:)
                } else {
                    retail.show_error( "\n" +db.err_msg+ "\n\n\n\n\n", ret );
                    Bsimpan.setEnabled(true);
                    return;
                }
                if( baru_action ) baru_action();
            }
        }.execute();

    }


    void baru() {
        if( !Bsimpan.isEnabled() ) {  baru_action();     return;  }
        new android.os.AsyncTask<Void, Void, Integer> () {   @Override protected Integer doInBackground( Void... v ) {
                //table.requestFocus();
                return retail.show_confirm( "\nTransaksi terakhir ingin disimpan?\n\n\n\n", "Konfirmasi" );    //, get_my_Activity()     //int resp = JOptionPane.showOptionDialog( retail.f, "\nPerubahan data ingin disimpan?\n\n\n\n", "Konfirmasi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, retail.icon_confirm, retail.Boptions, null);
            } @Override protected void onPostExecute( Integer resp ) {
                if( resp==0 ) { //"YA"
                    simpan(true);
                    //suck android OS developer >> if( !Bsimpan.isEnabled() ) baru_action();
                } else if( resp==1 ) { //"TIDAK"
                     baru_action();
                }
            }
        }.execute();
    }

    void baru_action() {
        if( table==null ) return;
        //if( table.isEditing() ) table.getCellEditor().stopCellEditing();    //post update ... pingin jaga2 aja            
        //refresh dulu poin di combo agent
            /*
            if( poin_aktif && poin>0 ) {
                int poin_from_combo = Integer.valueOf( Ckontak_agent.getSelectedItem().toString() );
                int selected_idx = Ccode_agent.getSelectedIndex();    //java bug? : he gives the first found the same item >> Ckontak_agent.getSelectedIndex();
                if( selected_idx > 0 ) {
                    Ckontak_agent.removeItemAt( selected_idx );
                    Ckontak_agent.insertItemAt( poin_from_combo + poin, selected_idx);
                    Ckontak_agent.setSelectedIndex(selected_idx);
                }
            }
            */
        db.rows.clear();

        //notify (bind) harus selalu di akhir >> ((Table_adapter)table.getAdapter()).notifyDataSetChanged();    //db.fireTableDataChanged();
            /*
            if(db.col_width[0]<0) {    //bekas dipakai Blihat (fitur print ulang)
                System.arraycopy( temp_col_editor, 0, db.col_editor, 0, db.col_editor.length );
                System.arraycopy( temp_col_width,  0, db.col_width,  0, db.col_width.length  );
                CHppn.setSelected(false);    //ribet ah, serius2 amat:p
            }
            Ccode_agent.setSelectedIndex(0);
            */

            if( retail.setting.get("Auto Refresh Delay Item & Barang Di Transaksi") == null ) {
                baru_action2();
                return;
            }

            //refresh data barang!!! ..  hmm apa tidak sebaiknya sebelum firetabledatachanged ya? ... tapi beresiko.... user masih tau aplikasi blm ready utk transaksi baru karna belum addrow:) !!!
                //ntar terjadi inkonsistensi antar form:p >> if( !Ccode_agn.isVisible() ) return;    //untested: form tidak sedang nonaktif krn user pindah antrian form :p
                //final int delay = 1000 * Integer.valueOf( retail.setting.get("Auto Refresh Delay Item & Barang Di Transaksi") ) ;
                //timer_refresh = new javax.swing.Timer(delay, new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
                    //if( retail.in_progress ) return;
                    //if( table==null ) return;
                    //if( table.isEditing() ) return;    //untested >> ini deh kayaknya yg bikin nama item sering hilang dari layar
                    //if( last_refresh.length()==0 ) last_refresh = "'" +retail.now_from_db()+ "'";    //don't rely on client clock >> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "'";
                    final String async_last_refresh = last_refresh.length()==0 ? "now()" : last_refresh ;
                    //android.util.Log.e( " onrefresh: ", " last_refresh="+last_refresh );
                    new android.os.AsyncTask<Void, Void, Void> () {
                        @Override protected Void doInBackground( Void... v ) {
                            db.exec( "SELECT table_name, now() FROM last_modified WHERE table_name IN ('barang','" +agent+ "') AND last_modified>" + async_last_refresh , null, true );    //do only when needed :)    //+ " ORDER BY last_modified DESC"
                            return null;
                        }
                        @Override protected void onPostExecute(Void v) {
                            if( !db.err_msg.equals("") )  android.util.Log.e( " Error saat refresh di Bbaru.doClick: ", db.err_msg );    //return;    //keep silent:p >> error("Pembacaan Data last_modified");
                            else {
                                String last_refresh_temp="";
                                Boolean need_to_refresh_brg = false;
                                Boolean need_to_refresh_agn = false;
                                try {
                                    //if( db.rs.size()==0 ) { db.rs.close();    return; }
                                    //((javax.swing.Timer)e.getSource()).stop();
                                    //if( db.rs.size()>0 )
                                        while( db.rs.next() ) {
                                                 if( db.rs.getString(1).equals("barang" ) ) need_to_refresh_brg = true;    //perlu disimpan ke variable dulu krn object db ini jg dipakai di refresh_ :p
                                            else if( db.rs.getString(1).equals( agent   ) ) need_to_refresh_agn = true;
                                            last_refresh_temp = db.rs.getString(2);
                                        }
                                    db.rs.close();
                                } catch (Exception er) {    //keep silent:p >> db.err_msg += "\nMaaf, Data \"last_modified\" gagal diinisiasi!\n\n\n(" + er + ")";    //keep silent:p >> error("Pembacaan Data last_modified");
                                    //return;
                                }

                                if( need_to_refresh_brg ) refresh_brg();
                                if( need_to_refresh_agn ) refresh_agn();
                                if( need_to_refresh_brg || need_to_refresh_agn ) {
                                     int dot_pos = last_refresh_temp.lastIndexOf(".");    //ada .0 nya di belakang
                                     last_refresh = "'" +last_refresh_temp.substring( 0, dot_pos )+ "'";    //in case the time on terminal was differs from db
                                }
                            }    //from else if( !db.err_msg.equals("")

                            baru_action2();
                        }
                    }.execute();
    }

        void baru_action2() {
            //(unfully tested:) )jgn, karena Fadly pingin last faktur bisa langsung mudah diprint ulang:p >> Tno_faktur.setText( String.format( no_faktur_date_format, Dtgl_faktur.getDate(), new Date() ) ) ;
            //Dtgl_faktur.setDate(new Date());
            //reset_gambar();
            //jgn, karena di desain saat ini, "dialog kembali" ditutup sebelum ngeprint >> Tdibayar.setText("");
            //Tdiskon_edit.setText("0");
            Tdiskon.setText("0");    //unfully tested:) >> entah kenapa ini ga ikut ngereset:)

            hitung_sub_total();
            retail.add_row(table);    //db.addRow();
            no_faktur="";
            //Bhapus_faktur.setEnabled(false);
            set_enable_tombol2(false);
            int col_selected = retail.convert_null(retail.setting.get("Prefer Barcode")).toLowerCase().equals("ya") ? 3 : 0 ;    //default edit banyak barang:p
            table.requestFocus();    table.setRowSelectionInterval(0,0);    //table.setColumnSelectionInterval(col_selected, col_selected);    //default edit banyak barang:p

            enable_edit=true;
            set_enable_control2(true);
            //Bhapus.setEnabled(true);    //menu_hapus.setEnabled(true);    //krn sehabis lihat faktur, lalu klik faktur baru, tombol ini hanya dienable jika user bergerak ke baris berikut:D
            //Blihat.setEnabled(true);    //agar bisa langsung lihat faktur yg terakhir :p

            ((Table_adapter)table.getAdapter()).notifyDataSetChanged();    //db.fireTableDataChanged();
        }

    void set_enable_tombol2( Boolean enable ) {    //berdasarkan isi table: kosong ato berisi
        Bsimpan.setEnabled(enable && enable_edit);
        //Bprint.setEnabled(enable);    Bexport_xls.setEnabled(enable);
        Bbaru.setEnabled(enable);
        //not here ... Bhapus_faktur.setEnabled(enable);
    }
    void set_enable_control2( Boolean enable ) {    //enable_edit sebenarnya
/*
        Dtgl_faktur.setEnabled(enable);
        Ccode_agent.setEnabled(enable);    Cname_agent.setEnabled(enable);    Ckontak_agent.setEnabled(enable);
        Tdiskon_edit.setEnabled(enable);    //?Tdiskon.setEnabled(enable);
        CHppn.setEnabled(enable);
        //sama dengan Bsimpan, they will be depend on wheter total>0:p >> Tdibayar.setEnabled(enable);
        Tpoin.setVisible(enable);
        Tdiskon.setEditable(enable && edit_potongan_aktif);
        if( enable && edit_potongan_aktif ) Tdiskon.setBackground(Tdiskon_edit.getBackground());
        else                                Tdiskon.setBackground(Ttotal.getBackground());
*/
    }

    @Override void hapus_final() {
        //reset_gambar();
        hitung_sub_total();
        if( diskon>0 ) {
            //Tdiskon_edit.setText( "0" ) ;    //mbohlah:) ... ini krn tadinya ga kereset kayaknya gara2 aku pingin pertahankan kembalian?!
            Tdiskon.setText( "0" ) ;    //mbohlah:) ... ini krn tadinya ga kereset kayaknya gara2 aku pingin pertahankan kembalian?!
            hitung_total();
        }
        set_enable_tombol2(false);
    }

    void tambah_agn( final String barcode_init ) {
        //kan emg dimungkinakan mendahului table creation >> if( table==null ) return;
        final Ftambah_pelanggan fc = Ftambah_pelanggan.newInstance(retail.app_name + "- Tambah Customer", barcode_init, this );    //Fedit_barang fc = new Fedit_barang( Fpenjualan.this, barcode );
android.util.Log.e("ftransaksi:", "tambah_agn 2" );
        fc.show(retail.fm, "Ftambah_pelanggan");
android.util.Log.e("ftransaksi:", "tambah_agn 3" );
        //let tambah_pelanggan.simpan do this or just merely modify Ccode_agn and Cname_agn there >> refresh_agn(barcode_init);    //Brefresh.doClick();    //Fpenjualan.this.setVisible(true);

        //fc=null;
    }

    void refresh_agn() {
        refresh_agn("");
    }
    void refresh_agn(final String barcode_init) {
        //fill Ccode_agent dan Cname_agent with http://api.muhajirin.net/v1/customer/index
        //kan emg dimungkinakan mendahului table creation >> if( table==null ) return;
        final String table="customers";
        new DownloadJSON(){
            @Override protected void onPostExecute( String result ) {
                super.onPostExecute(result);
android.util.Log.e(table+": ", "1");
                if( result.startsWith( "Error:" ) ) return;
android.util.Log.e(table+": ", "2");

                try {
                    org.json.JSONObject data = new org.json.JSONObject(result);
android.util.Log.e(table+": ", "3");

                    if( !data.has( table ) ) {
                        retail.show_error( "\n" + data.getString("message") + "\n\n\n\n", "Koneksi Gagal" );
                        return;
                    }
android.util.Log.e(table+": ", "4");
                    agent_id = 0;    //supaya itemlistener tidak listen setSelectedIndex yg dilakukan program di bawah ini.
                    int temp_selectedItem = Cname_agent.getListSelection();
                    if( temp_selectedItem<0 ) temp_selectedItem=0;
android.util.Log.e(table+": ", "5");
                    ArrayAdapter Cname_agent_adapter = (ArrayAdapter) Cname_agent.getAdapter();
                    Ccode_agent.items.clear();
android.util.Log.e(table+": ", "6");
    Cname_agent_adapter.clear();
android.util.Log.e(table+": ", "7");
                    int init_idx = -1;
                    Cname_agent.items.add( new jcdb_item( 0, "" ) );    //Cname_agent_adapter.add( "" );
android.util.Log.e(table+": ", "8");
                    Ccode_agent.items.add( new jcdb_item( 0, "" ) );    //sebaiknya di table customer ada minimal 1 customers yaitu: umum    //( -1, "" )    //as long as I remember, I never use Ccode_agent.get_id() and Cname_agent.get_id(), then I can use it to save originate position while filtering:p
android.util.Log.e(table+": ", "9");
                    org.json.JSONArray jArray = new org.json.JSONArray( data.getString(table) );
android.util.Log.e(table+": ", "10");
                    for( int i=0; i<jArray.length(); i++ ) {
                        org.json.JSONObject jtable = jArray.getJSONObject(i);
                        Cname_agent.items.add( new jcdb_item( jtable.getInt("id"), jtable.getString("username") ) );    //Cname_agent_adapter.add( jtable.getString( "username" ) );
                        String code = jtable.getString( "id" );    //harusnya barcode
                        if( init_idx<0 && code.equals(barcode_init) ) init_idx = i+1;
                        Ccode_agent.items.add( new jcdb_item( jtable.getInt("id"), code ) );    //i+1
                    }

                    Cname_agent_adapter.notifyDataSetChanged();    ((ArrayAdapter)Ccode_agent.getAdapter()).notifyDataSetChanged();
android.util.Log.e("ongetagent:", " 42 name_agent.getCount()" + Cname_agent_adapter.getCount() + " name_agent.items.getCount()" + Cname_agent_adapter.getCount()  + " name_agent.getAdapter().getCount()" + Cname_agent_adapter.getCount() );
                    if( init_idx>=0 ) Cname_agent.setSelection(init_idx);    else Cname_agent.setSelection(temp_selectedItem);
                    agent_id = -13;
android.util.Log.e("ongetagent:", "   after setselection "   + "    Ccode_agent.getCount()" + Ccode_agent.getCount()  );
                    } catch( Exception e ) {    //org.json.JSONException 
                        db.err_msg += "\nMaaf, Data \"" +table+ "\" gagal diinisialisasi!\n\n\n(" + e.toString() + ")";
                        retail.show_error( db.err_msg, "Pembacaan Data Barang" );
                        android.util.Log.e("get " +table+ " error: ", "e.toString()="+ e.toString() );
                    }
                }
            }.execute(
                       db.cfg.get( "url_customer_index" )    //url to call
                     , db.cfg.get( "client_token" )    //auth header to send
                     , "access_token=" + retail.db.cfg.get( "access_token" )    //post params
                     );    //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // we target SDK > API 11



    }


/*
    void refresh_brg() {
        new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
            refresh_brg_sync();
            return null;
        }}.execute();
    }
*/
    void refresh_brg() {
        if( table==null ) return;
        //if( Brefresh!=null ) Brefresh.setEnabled(false);

//better flashing a cute animation with circle progression here :)  with text "Refreshing data. Mohon tunggu!"

        retail.reset_brg();
        if( modul_table.equals("jual") ) retail.get_brg(table, sql_get_brg);  else retail.get_brg(table);



    }
//        new android.os.Handler().postDelayed(new Runnable() { public void run() {    //nyang di atas ada db.exec(sql)nya sehingga ada di dalam asyncthread, suck android api developer
    void after_get_brg() {





android.util.Log.e("after_get_brg: ", "1");
    if( !db.err_msg.equals("") ) {  /*if( Brefresh!=null ) Brefresh.setEnabled(true);*/    return;  }
/*        if( last_refresh.length()==0 ) 
            new android.os.AsyncTask<Void, Void, Void> () { @Override protected Void doInBackground( Void... v ) {
                last_refresh = "'" +retail.now_from_db()+ "'";    //emang resikonya last_refresh tidak keupdate jika user klik refresh:) >>    don't rely on client clock >> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "'";
                return null;
            }}.execute();
*/
android.util.Log.e("after_get_brg: ", "2 a   Code_brg.getCount()" + Ccode_brg.getCount() );
        Ccode_brg  = retail.Ccode_brg;    Cname_brg  = retail.Cname_brg;    harga_brg  = retail.harga_brg;    diskon_brg = retail.diskon_brg;    gambar_brg = retail.gambar_brg;
android.util.Log.e("after_get_brg: ", "2 b " +  "    Code_brg.getCount()" + Ccode_brg.getCount()  );
        db.col_editor[0] = (View)Ccode_brg;    db.col_editor[1] = (View)Cname_brg;    //pengaruh di db.to_db() saat simpan :p
android.util.Log.e("after_get_brg: ", "2 c"   + "    Code_brg.getCount()" + Ccode_brg.getCount() );
        //((combo_db_CellEditor)table.getColumnModel().getColumn(table.convertColumnIndexToView(0)).getCellEditor()).set_editor( (JComboBox)Ccode_brg );    //table.getColumn(0).setCellEditor(new combo_db_CellEditor( (JComboBox)Ccode_brg ));
        //((combo_db_CellEditor)table.getColumnModel().getColumn(table.convertColumnIndexToView(1)).getCellEditor()).set_editor( (JComboBox)Cname_brg );    //table.getColumn(1).setCellEditor(new combo_db_CellEditor( (JComboBox)Cname_brg ));
        for( int i=0; i<table.getRowCount(); i++ ) {
            //if( table.isEditing() && table.getEditingRow()==i ) continue;    //untested >> jgn bikin kacau saat dipanggil oleh timer_refresh padahal user sedang tengah2 proses editing!!
android.util.Log.e("after_get_brg: ", "2 i=" + i);
            String code = db.getValueAt(i,0).toString();
            if( code.length()==0 ) continue;
            String name = db.getValueAt(i,1).toString();
android.util.Log.e("after_get_brg: ", "3");
            int code_id = Ccode_brg.my_filtered_index_of(code);
android.util.Log.e("after_get_brg: ", "4");
            int name_id = Cname_brg.my_filtered_index_of(name);
            if( name_id != code_id && !( code_id >= 0 && name_id >= 0 && retail.convert_null(retail.setting.get("Duplikasi Nama Barang")).toLowerCase().equals("ya") ) )    //don't check if allowing duplicate name    //jika index nama barang dan index code barang tidak sama (termasuk jika salah satu indexnya = -1)
                     if( name_id >= 0 ) db.setValueAt( ( Ccode_brg.getItemAt(name_id).toString() ), i, 0 );    //jika nama barang valid, tapi code barang tidak valid, update code barang
                else if( code_id >= 0 ) db.setValueAt( ( Cname_brg.getItemAt(code_id).toString() ), i, 1 );    //jika code barang valid, tapi name barang tidak valid, update nama barang
            if( name_id<0 && code_id<0 ) {    //jika keduanya (nama dan code barang) tidak valid, kosongin ... ga usah dihapus biar usernya nyadar ada yg ilang!! lagian toh dia akan otomatis kehapus jika user klik row di atasnya!
                                              //Bhapus.setSelected(true);    //ini cuman buat ngirim variable biar ga muncul dialog konfirmasi hapus :p
                                              //Bhapus.doClick(1);    Bhapus.setSelected(false);
                db.setValueAt( "", i, 1 );    //kolom name kudu diupdate duluan ... ini trik utk menolkan total_i:p
                db.setValueAt(  0, i, 3 );    //nolkan kolom banyak, ni harus di atas set code di bawah agar lbanyak_blink ikut kereset
                db.setValueAt( "", i, 0 );
            }
            int id = code_id >= 0 ? code_id:name_id;    //name_id jadi tidak valid kalo duplikasi nama barang diizinkan >>   name_id >= 0 ? name_id:code_id;
            if( ( name_id == code_id || code_id >= 0 && retail.convert_null(retail.setting.get("Duplikasi Nama Barang")).toLowerCase().equals("ya") ) && name_id >= 0 )    //cek perubahan harga dan diskon :p
                if( harga_brg.get(id) != db.getIntAt(i,2)  ||  retail.get_diskon( diskon_brg.get(id), db.getIntAt(i, 2), db.getIntAt(i, 3) ) != db.getIntAt(i,4) )  {
                    db.setValueAt(false, "", i, 1 );    //pancing dulu, tp ini membuat banyak barang jadi 1!!!
                    db.setValueAt(false, Cname_brg.getItemAt(id).toString(), i, 1 );    //paksa baris ini terupdate ....
                }
android.util.Log.e("after_get_brg: ", "5"   + "    Code_brg.getCount()" + Ccode_brg.getCount() );

            //ah, biarlah sekalian pastiin aja gambar kerefresh:p >> if( ( name_id != code_id ) || ( name_id<0 && code_id<0 ) ) {    //update value gambar, ga perlu dilakukan jika yg diupdate user hanya gambarnya doang tanpa ngupdate name atau code:p
            //String new_val = id==-1 ? "":gambar_brg.get(id);
            //if( !new_val.equals( db.getValueAt(i,6).toString() ) )  db.setValueAt(false, new_val, i, 6 );
            //}
        }

android.util.Log.e("after_get_brg: ", "6");

        //ini harusnya jgn, krn bikin user kaget dan bisa error klo user (contoh Rita dulu) sedang ngetik TDibayar >> table.requestFocus();
        //if( backup_editing_row_idx > -1 ) table.editCellAt(backup_editing_row_idx,backup_editing_col_idx);

        //} catch (Exception ex) {    JOptionPane.showMessageDialog( null, "from regfresss\n. Pesan Kesalahan: " + ex, "refress", JOptionPane.ERROR_MESSAGE); }
        //if( Brefresh!=null ) Brefresh.setEnabled(true);


//                        }},1000);

    }

    void edit_brg() {
        //if( table==null ) return;
        final String barcode_init = table.getSelectedRow()>=0 ? db.getStringAt(table.getSelectedRow(), 0) : "";
//        new android.os.AsyncTask<Void, Void, Void> () {
//            @Override protected Void doInBackground( Void... v ) {
                final Fedit_barang fc = Fedit_barang.newInstance(retail.app_name + "- Edit Barang", barcode_init);
/*
                retail.modal_result=-13;
                form.getActivity().runOnUiThread(new Runnable() { @Override public void run() {
*/
                    fc.show(retail.fm, "Fedit_barang");
/*
                }});
                try { while( retail.modal_result<0 ) java.lang.Thread.sleep(100);   } catch( InterruptedException e ) {}
                return null;
            }
            @Override protected void onPostExecute( Void v ) {
*/
//tetap harus dipanggil dari fc!!! >> refresh_brg();
//            }
//        }.execute();
        //fc=null;
    }

    String[] tambah_init;
    void tambah_brg( final String barcode_init ) {
        if( table==null ) return;    //closing in progress (to prevent this triggered at the same time as Bkeluar.doClick() via mnemonic and or mouse click:p ... this is why they said that swing was not a thread safe) so far, I thought this is the simple way to protect :)
        //if( table.isEditing() /*biar di tangani di setValue() aja krn ini lebih beresiko >> && barcode_init.isEmpty() */ ) table.getCellEditor().cancelCellEditing();    //cancelll update ... ini sebetulnya dah ada di Brefresh.doClick(); tp sebaiknya tetap dipastiin aja dieksekusi di awal
//        new android.os.AsyncTask<Void, Void, String[]> () {
//            @Override protected String[] doInBackground( Void... v ) {
android.util.Log.e("ftransaksi:", "tambah_brg 1" );
                final Ftambah_barang fc = Ftambah_barang.newInstance(retail.app_name + "- Tambah Barang", barcode_init, tambah_init);    //Fedit_barang fc = new Fedit_barang( Fpenjualan.this, barcode );
android.util.Log.e("ftransaksi:", "tambah_brg 2" );
                retail.modal_result=-13;
//                form.getActivity().runOnUiThread(new Runnable() { @Override public void run() {
                    fc.show(retail.fm, "Ftambah_barang");
android.util.Log.e("ftransaksi:", "tambah_brg 3" );
//                }});
//                  try { while( retail.modal_result<0 ) java.lang.Thread.sleep(100);   } catch( InterruptedException e ) {}
//                return fc.tambah_init;
//            }
//            @Override protected void onPostExecute( String[] tambah_init_ ) {
//                tambah_init = tambah_init_ ;

/* tetap harus dipanggil dari fc!!!
                tambah_init = fc.tambah_init ;

//        new android.os.Handler().postDelayed(new Runnable() { public void run() {    //nyang di atas ada db.exec(sql)nya sehingga ada di dalam asyncthread, suck android api developer
                //try { while( retail.modal_result<0 ) java.lang.Thread.sleep(100);   } catch( InterruptedException e ) {}
                refresh_brg();    //Brefresh.doClick();    //Fpenjualan.this.setVisible(true);
android.util.Log.e("ftransaksi:", "tambah_brg 4" );
//        }},3000);
*/


//            }
//        }.execute();
        //fc=null;
    }


    //public boolean super_onMenuItemClick( android.view.MenuItem item ) {
    //    return super.onMenuItemClick(item);
    //}
    @Override public boolean onMenuItemClick( final android.view.MenuItem item ) {
//        new android.os.Handler().postDelayed(new Runnable() { public void run() {    //biar removeeditor dll kerja dulu, otherwise saat klik baru dan Ya di konfirmasi simpan, proses simpan keganggu oleh thread remove editor
            if( super.onMenuItemClick(item) ) return true;    //jika sudah terhandle oleh super class
            switch( item.getItemId() ) {
                case 10: simpan();          return true;
                case  9: baru();            return true;
                case  8: refresh_brg();     return true;
                case  7: edit_brg();        return true;
                case  6: tambah_brg("");    return true;
                default: return true;
            }
//        }},200);
        //return true;
    }

    void hitung_sub_total() {
        hitung_sub_total(false);
    }
    void hitung_sub_total( final Boolean sender_is_setValue ) {
        if( table==null ) return;
try{

//        new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {

//        if( sub_total==0 && kembali!=0 ) Tdibayar.setText("0");    //ini krn ga jadi kureset di Bbaru.doClick()
        //if( table.isEditing() && !sender_is_setValue ) table.getCellEditor().stopCellEditing();    //!sender_is_setValue utk mencegah setvalue dua kali oleh perintah stopcellediting ini
android.util.Log.e("hitung:", "sub_total =" + sub_total + "    table.getRowCount()=" + table.getRowCount()  );
        if( sub_total==0 && table.getRowCount()<=2 ) {    //untested: di versi android, di sini dah jadi dua row krn aku otomatis addrow() via super.setvalue() >> if( sub_total==0 && table.getRowCount()==1 ) {    //ini krn ga jadi kureset di Bbaru.doClick()
            String left_val = db.getValueAt( 0, 0 ).toString();
            if( !left_val.equals("0") && left_val.length()>0 || retail.brg_id!=-13 )  {    //listener Cname_brg sedang kerja tapi belum mengupdate left_val!!!
android.util.Log.e("hitung:", " Tno_faktur.setText =" + String.format( no_faktur_date_format, new Date(), new Date() )  );
                Tno_faktur.setText( String.format( no_faktur_date_format, new Date(), new Date() ) ) ;
                //Blihat.setEnabled(false);
            }

        }

        sub_total=0;    banyak_total = 0;
        for( int i=0; i<table.getRowCount(); i++ ) {
            //String left_val = db.getValueAt( i, 0 ).toString();
            //if( left_val.equals("0") || left_val.length()==0 ) continue;
android.util.Log.e("hitung:", " db.getIntAt(" +i+ ", 5)=" + db.getStringAt(i, 5)  );
            sub_total += db.getIntAt(i, 5);       //temp += (long)( getIntAt(i, 2) * (100 - getIntAt(i, 4) ) * getIntAt(i, 3) );
android.util.Log.e("hitung:", " db.getIntAt(" +i+ ", 3)=" + db.getStringAt(i, 3)  );
            banyak_total += db.getIntAt(i, 3);
            if( sub_total<0 ) {   //exceed int max: 2.147.483.647
android.util.Log.e("hitung:", "Total harga terlalu besar." );
                retail.show_error( "Total harga terlalu besar.", "Kesalahan" );
                sub_total=0;
                break;
            }
        }

android.util.Log.e("hitung:", "1" );
        Tsub_total.setText( retail.numeric_format.format(sub_total) );
android.util.Log.e("hitung:", "2" );

        set_enable_tombol2(true);
android.util.Log.e("hitung:", "3" );
        hitung_total(sender_is_setValue);
android.util.Log.e("hitung:", "4" );

//            return null;
//        }}.execute();

                } catch (Exception ex) {
                    retail.show_error( "hitung sub error: " + ex.getMessage(), "Kesalahan" );
                    return;
                }

    }

    @Override void refresh_summary() {
        hitung_sub_total();
    }


    void hitung_total() {
        hitung_total(false);
    }
    void hitung_total( Boolean sender_is_setValue ) {
android.util.Log.e("hitung_total:", "1" );
        if( table==null ) return;
        //if( table.isEditing() && !sender_is_setValue ) table.getCellEditor().stopCellEditing();    //post update ... ini penting
        //int diskon_by_percent = Math.abs( retail.round( (long) sub_total * ((Integer)Tdiskon_edit.getDocument().getProperty("plain_num")) , 100 ) ) ;    //((Float)( sub_total * ((Integer)doc.getProperty("plain_num")) / 100 ) ).intValue();
        //Object diskon_obj = Tdiskon.getDocument().getProperty("plain_num");
android.util.Log.e("hitung_total:", "2" );
        diskon = Math.abs( Integer.valueOf(Tdiskon.getText().toString().replace(retail.digit_separator,"")) );    //diskon_obj==null ? 0 : Math.abs( (Integer)diskon_obj );
android.util.Log.e("hitung_total:", "3" );
        calculate_disc_percent=false;

//JOptionPane.showMessageDialog( Fpenjualan.this,  "sub_total"+sub_total + "diskon"+diskon + "diskon_obj==null"+(diskon_obj==null) , "Kembali", JOptionPane.INFORMATION_MESSAGE );
        if( sub_total==0 ) if( diskon==0 ) /*if( diskon_obj==null )*/ Tdiskon.setText( "0" ) ;
/*
        if( sub_total!=0 ) if(calculate_disc_rupiah) if( diskon_by_percent != diskon ) {
                                                         Tdiskon.setText( String.format("%,d", -1*diskon_by_percent) ) ;
                                                         diskon = diskon_by_percent ;
                                                     }
*/
        calculate_disc_percent=true;
android.util.Log.e("hitung_total:", "4" );

        total = sub_total - diskon;
android.util.Log.e("hitung_total:", "5" );

        ppn = 0;    //CHppn.isSelected() ? retail.round( (long)total*prosentase_ppn, 100 ) : 0 ;
        //Tppn.setText( String.format("%,d", ppn) ) ;

        int pembulatan = 100;
        try { pembulatan = Integer.valueOf( db.cfg.get("pembulatan_rupiah") );    } catch( Exception e ) {}
android.util.Log.e("hitung_total:", "6" );

        total += ppn ;    //total = Tdiskon.getText().equals("") ? 0 : Integer.valueOf(Tdiskon.getText());
        total_round = retail.round( (long)total, pembulatan) * pembulatan;    //total_round = Math.round(2/3) * 100; //(total/100)
        Ttotal.setText( retail.numeric_format.format(total_round) ) ;
        //if(1==1) return;
android.util.Log.e("hitung_total:", "7" );

/*
        Ltotal_blink.setText( " " + String.format("%,d", total_round) );
        Lbanyak_blink.setText( String.valueOf(banyak_total) );
*/
    
        Bsimpan.setEnabled( total_round>0 );    
/* di Fpenjualan
        hitung_kembalian( Tdibayar.getText().toString() );
        poin = (int) Math.floor( total_round / harga_poin );
        //show_poin();
        //Bsimpan.setEnabled( total_round>0 );
        Tdibayar.setEnabled( total_round>0 );    //aku pinter ga ya:)
*/
    }

    @Override void close() {
        if( !Bsimpan.isEnabled() ) {  close_action();     return;  }
        new android.os.AsyncTask<Void, Void, Integer> () {   @Override protected Integer doInBackground( Void... v ) {
                //table.requestFocus();
                return retail.show_confirm( "\nTransaksi terakhir ingin disimpan?\n\n\n\n", "Konfirmasi" );    //, get_my_Activity()     //int resp = JOptionPane.showOptionDialog( retail.f, "\nPerubahan data ingin disimpan?\n\n\n\n", "Konfirmasi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, retail.icon_confirm, retail.Boptions, null);
            } @Override protected void onPostExecute( Integer resp ) {
                if( resp==0 ) { //"YA"
                    simpan();
                    if( !Bsimpan.isEnabled() ) close_action();
                } else if( resp==1 ) { //"TIDAK"
                    close_action();
                }
            }}.execute();
    }
    void close_action() {
        //if( timer_kembalian!=null ) timer_kembalian.cancel();    timer_kembalian = null;
        //kan sudah diclose saat show_table >> try { db.rs.close(); } catch (Exception e) {}
        super.close();
    }

}