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
import android.text.InputType;
import android.widget.CheckBox;

public class Fconfig extends Ftambah {
    //EditText Tname, Tcode, Tstok, Tsize, Tberat, Tomset, Tdeskripsi, Tharga_beli, Tdiskon_beli, Tharga_jual, Tdiskon_jual, Tqty1, Tdisc_amount1, Tqty2, Tdisc_amount2, Tqty3, Tdisc_amount3, Tqty4, Tdisc_amount4 ;
    static Fconfig form;
    public Fconfig() { super(); }
    public static Fconfig newInstance(String title) {
        form = new Fconfig();
        Ftambah.newInstance(title, form);
        return form;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final EditText Tdatabase = new EditText(form.getActivity());    Tdatabase.setHint( "Database" );    Tdatabase.setText( retail.db.cfg.get("database"), TextView.BufferType.EDITABLE );    comps.add(Tdatabase);
        Tdatabase.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tdatabase.setMaxLines(50);

        final EditText Tuser = new EditText(form.getActivity());    Tuser.setHint( "User" );    Tuser.setText( retail.db.cfg.get("user"), TextView.BufferType.EDITABLE );    comps.add(Tuser);
        Tuser.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tuser.setMaxLines(50);

        final EditText Ppassword = new EditText(form.getActivity());    Ppassword.setHint( "Password" );    try{ Ppassword.setText( retail.db.decrypt( retail.db.cfg.get("password"), retail.db.encryptionKey ), TextView.BufferType.EDITABLE ); } catch (Exception e) {}    comps.add(Ppassword);
        Ppassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );    Ppassword.setMaxLines(50);

        final EditText Thost = new EditText(form.getActivity());    Thost.setHint( "Host" );    Thost.setText( retail.db.cfg.get("host"), TextView.BufferType.EDITABLE );    comps.add(Thost);
        Thost.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Thost.setMaxLines(50);

        final EditText Tsocket = new EditText(form.getActivity());    Tsocket.setHint( "Socket" );    Tsocket.setText( retail.db.cfg.get("socket"), TextView.BufferType.EDITABLE );    comps.add(Tsocket);
        Tsocket.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tsocket.setMaxLines(50);

        final EditText Tdbms = new EditText(form.getActivity());    Tdbms.setHint( "Dbms" );    Tdbms.setText( retail.db.cfg.get("dbms"), TextView.BufferType.EDITABLE );    comps.add(Tdbms);
        Tdbms.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tdbms.setMaxLines(50);

        final EditText Tuser_default = new EditText(form.getActivity());    Tuser_default.setHint( "User Login Default" );    Tuser_default.setText( retail.db.cfg.get("user_login_default"), TextView.BufferType.EDITABLE );    comps.add(Tuser_default);
        Tuser_default.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tuser_default.setMaxLines(50);

        final EditText Ppassword_default = new EditText(form.getActivity());    Ppassword_default.setHint( "password_default" );    try{ Ppassword_default.setText( retail.db.decrypt( retail.db.cfg.get("password_login_default"), retail.db.encryptionKey ), TextView.BufferType.EDITABLE ); } catch (Exception e) {}    comps.add(Ppassword_default);
        Ppassword_default.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );    Ppassword_default.setMaxLines(50);

        final EditText Tbarcode_delay = new EditText(form.getActivity());    Tbarcode_delay.setHint( "Barcode Delay" );    Tbarcode_delay.setText( retail.db.cfg.get("barcode_delay") );    //comps.add(Tbarcode_delay);
        Tbarcode_delay.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tbarcode_delay.setMaxLines(50);

        final EditText Tpanjang_barcode_masuk_history_tambah_barang = new EditText(form.getActivity());    Tpanjang_barcode_masuk_history_tambah_barang.setHint( "Panjang Barcode Masuk History Tambah Barang" );    Tpanjang_barcode_masuk_history_tambah_barang.setText( retail.db.cfg.get("panjang_barcode_masuk_history_tambah_barang") );    comps.add(Tpanjang_barcode_masuk_history_tambah_barang);
        Tpanjang_barcode_masuk_history_tambah_barang.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tpanjang_barcode_masuk_history_tambah_barang.setMaxLines(50);

        final EditText Tdirektori_gambar = new EditText(form.getActivity());    Tdirektori_gambar.setHint( "Direktori File Gambar" );    Tdirektori_gambar.setText( retail.db.cfg.get("direktori_gambar"), TextView.BufferType.EDITABLE );    comps.add(Tdirektori_gambar);
        Tdirektori_gambar.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tdirektori_gambar.setMaxLines(50);

        final EditText Tfont_di_tabel_transaksi = new EditText(form.getActivity());    Tfont_di_tabel_transaksi.setHint( "Font di Tabel Transaksi" );    Tfont_di_tabel_transaksi.setText( retail.db.cfg.get("font_di_tabel_transaksi"), TextView.BufferType.EDITABLE );    comps.add(Tfont_di_tabel_transaksi);
        Tfont_di_tabel_transaksi.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tfont_di_tabel_transaksi.setMaxLines(50);

        final EditText Ttinggi_baris_di_tabel_transaksi = new EditText(form.getActivity());    Ttinggi_baris_di_tabel_transaksi.setHint( "Tinggi Baris di Tabel Transaksi" );    Ttinggi_baris_di_tabel_transaksi.setText( retail.db.cfg.get("tinggi_baris_di_tabel_transaksi"), TextView.BufferType.EDITABLE );    comps.add(Ttinggi_baris_di_tabel_transaksi);
        Ttinggi_baris_di_tabel_transaksi.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Ttinggi_baris_di_tabel_transaksi.setMaxLines(50);

        final EditText Tlebar_kode_di_tabel_transaksi = new EditText(form.getActivity());    Tlebar_kode_di_tabel_transaksi.setHint( "Lebar Kode di Tabel Transaksi" );    Tlebar_kode_di_tabel_transaksi.setText( retail.db.cfg.get("lebar_kode_di_tabel_transaksi"), TextView.BufferType.EDITABLE );    comps.add(Tlebar_kode_di_tabel_transaksi);
        Tlebar_kode_di_tabel_transaksi.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tlebar_kode_di_tabel_transaksi.setMaxLines(50);

        final EditText Tpembulatan = new EditText(form.getActivity());    Tpembulatan.setHint( "Pembulatan" );    Tpembulatan.setText( retail.db.cfg.get("pembulatan"), TextView.BufferType.EDITABLE );    comps.add(Tpembulatan);
        Tpembulatan.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tpembulatan.setMaxLines(50);

        final EditText Tprint_barcode_skala_horizontal = new EditText(form.getActivity());    Tprint_barcode_skala_horizontal.setHint( "Print Barcode Skala Horizontal" );    Tprint_barcode_skala_horizontal.setText( retail.db.cfg.get("print_barcode_skala_horizontal"), TextView.BufferType.EDITABLE );    //comps.add(Tprint_barcode_skala_horizontal);
        Tprint_barcode_skala_horizontal.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_barcode_skala_horizontal.setMaxLines(50);

        final EditText Tprint_label_rak_skala_horizontal = new EditText(form.getActivity());    Tprint_label_rak_skala_horizontal.setHint( "Print Label Rak Skala Horizontal" );    Tprint_label_rak_skala_horizontal.setText( retail.db.cfg.get("print_label_rak_skala_horizontal"), TextView.BufferType.EDITABLE );    //comps.add(Tprint_label_rak_skala_horizontal);
        Tprint_label_rak_skala_horizontal.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_label_rak_skala_horizontal.setMaxLines(50);


        final CheckBox CHsound_active = new CheckBox(form.getActivity());    CHsound_active.setText( "Aktifkan Suara" );    CHsound_active.setChecked( retail.db.cfg.get("aktifkan_suara").equals("ya") );    comps.add(CHsound_active);

        final CheckBox CHppn_active   = new CheckBox(form.getActivity());    CHppn_active.setText( "PPN aktif secara default" );    CHppn_active.setChecked( retail.db.cfg.get("ppn_aktif_secara_default").equals("ya") );    comps.add(CHppn_active);
        final CheckBox CHppn_visible  = new CheckBox(form.getActivity());    CHppn_visible.setText( "PPN ditampilkan" );    CHppn_visible .setChecked( retail.db.cfg.get("ppn_ditampilkan").equals("ya") );    comps.add(CHppn_visible );

        //ini taruh dibawah krn mo dimanual setbound nya :p
        final EditText Tfont_size_di_tabel_transaksi = new EditText(form.getActivity());    Tfont_size_di_tabel_transaksi.setHint( "Font Size di Tabel Transaksi" );    Tfont_size_di_tabel_transaksi.setText( retail.db.cfg.get("font_size_di_tabel_transaksi"), TextView.BufferType.EDITABLE );    comps.add(Tfont_size_di_tabel_transaksi);
        Tfont_size_di_tabel_transaksi.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tfont_size_di_tabel_transaksi.setMaxLines(50);

        final CheckBox CHfont_bold = new CheckBox(form.getActivity());    CHfont_bold.setText( "Bold" );    CHfont_bold.setChecked( retail.db.cfg.get("font_bold_di_tabel_transaksi").equals("ya") );    comps.add(CHfont_bold);
        final CheckBox CHfont_italic = new CheckBox(form.getActivity());    CHfont_italic.setText( "Italic" );    CHfont_italic.setChecked( retail.db.cfg.get("font_italic_di_tabel_transaksi").equals("ya") );    comps.add(CHfont_italic);

        final EditText Tprint_barcode_skala_vertical = new EditText(form.getActivity());    Tprint_barcode_skala_vertical.setHint( "Print Barcode Skala Vertical" );    Tprint_barcode_skala_vertical.setText( retail.db.cfg.get("print_barcode_skala_vertical"), TextView.BufferType.EDITABLE );    comps.add(Tprint_barcode_skala_vertical);
        Tprint_barcode_skala_vertical.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_barcode_skala_vertical.setMaxLines(50);

        final EditText Tprint_barcode_lebar_kolom = new EditText(form.getActivity());    Tprint_barcode_lebar_kolom.setHint( "Print Barcode Lebar Kolom" );    Tprint_barcode_lebar_kolom.setText( retail.db.cfg.get("print_barcode_lebar_kolom"), TextView.BufferType.EDITABLE );    comps.add(Tprint_barcode_lebar_kolom);
        Tprint_barcode_lebar_kolom.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_barcode_lebar_kolom.setMaxLines(50);

        final EditText Tprint_barcode_tinggi_baris = new EditText(form.getActivity());    Tprint_barcode_tinggi_baris.setHint( "Print Barcode Tinggi Baris" );    Tprint_barcode_tinggi_baris.setText( retail.db.cfg.get("print_barcode_tinggi_baris"), TextView.BufferType.EDITABLE );    comps.add(Tprint_barcode_tinggi_baris);
        Tprint_barcode_tinggi_baris.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_barcode_tinggi_baris.setMaxLines(50);

        final EditText Tprint_label_rak_skala_vertical = new EditText(form.getActivity());    Tprint_label_rak_skala_vertical.setHint( "Print Label Rak Skala Vertical" );    Tprint_label_rak_skala_vertical.setText( retail.db.cfg.get("print_label_rak_skala_vertical"), TextView.BufferType.EDITABLE );    comps.add(Tprint_label_rak_skala_vertical);
        Tprint_label_rak_skala_vertical.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_label_rak_skala_vertical.setMaxLines(50);

        final EditText Tprint_label_rak_lebar_kolom = new EditText(form.getActivity());    Tprint_label_rak_lebar_kolom.setHint( "Print Label Rak Lebar Kolom" );    Tprint_label_rak_lebar_kolom.setText( retail.db.cfg.get("print_label_rak_lebar_kolom"), TextView.BufferType.EDITABLE );    comps.add(Tprint_label_rak_lebar_kolom);
        Tprint_label_rak_lebar_kolom.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_label_rak_lebar_kolom.setMaxLines(50);

        final EditText Tprint_label_rak_tinggi_baris = new EditText(form.getActivity());    Tprint_label_rak_tinggi_baris.setHint( "Print Label Rak Tinggi Baris" );    Tprint_label_rak_tinggi_baris.setText( retail.db.cfg.get("print_label_rak_tinggi_baris"), TextView.BufferType.EDITABLE );    comps.add(Tprint_label_rak_tinggi_baris);
        Tprint_label_rak_tinggi_baris.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tprint_label_rak_tinggi_baris.setMaxLines(50);


        //mandatory.add(Tname);         mandatory.add(Tcode);         mandatory.add(Tharga_jual);
        //comp_number.add(Tjumlah);
        default_focused_comp = Tdatabase;
        //align_comps();
        View ret = super.onCreateView(inflater, container, savedInstanceState);


        Bsimpan.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
                ((android.support.v7.widget.AppCompatButton) v).setEnabled(false);
                //String to_write = ""; for (int i = 0; i < n; i++) to_write += panel.getComponent(n).getText().toString() + "  :  "
                String newline = "\r\n"; //"\\n";  //kok unix version semua :p >> java.lang.Character newline = java.lang.Character.LINE_SEPARATOR;    //System.lineSeparator()
                try {
                    java.io.FileWriter writer = new java.io.FileWriter( retail.db.cfg.get("file_konfigurasi") ); // creates or overrides existing file
                    //byte[] cipher = retail.db.encrypt( (new String(Ppassword.getPassword())).getBytes(), "just rafikoi@yahoo.com");
                    //byte[] cipher = retail.db.encrypt( Ppassword.getPassword(), retail.db.encryptionKey );
                    //String pass = "";
                    //for (int i=0; i<cipher.length; i++) pass += new String(cipher[i]);
                    writer.append(
                             "# File Konfigurasi .... " + newline + "// Author : Rafik, http://solusiprogram.com" + newline + newline + newline    //rafikoi@yahoo.com
                           + "Database : " + Tdatabase.getText().toString().trim() + newline
                           + "User     : " + Tuser.getText().toString().trim()     + newline
                           + "Password : \"" + retail.db.encrypt( Ppassword.getText().toString().toCharArray(), retail.db.encryptionKey) + "\"" + newline
                           + "Host     : " + Thost.getText().toString().trim()     + newline
                           + "Socket   : " + Tsocket.getText().toString().trim()   + newline
                           + "DBMS     : " + Tdbms.getText().toString().trim()     + newline
                           + newline
                           //+ "# Login Default"                          + newline
                           //+ "----------------------------------------" + newline
                           + "User Login Default     : " + Tuser_default.getText().toString().trim()       + newline
                           + "Password Login Default : \"" + retail.db.encrypt( Ppassword_default.getText().toString().toCharArray(), retail.db.encryptionKey) + "\"" + newline
                           //+ "----------------------------------------" + newline
                           + newline
                           + "Direktori Gambar     : " + Tdirektori_gambar.getText().toString().trim()     + newline
                           + "Barcode Delay        : " + Tbarcode_delay.getText().toString().trim().replace(retail.digit_separator, "") + newline
                           + "Panjang Barcode Masuk History Tambah Barang : " + Tpanjang_barcode_masuk_history_tambah_barang.getText().toString().trim().replace(retail.digit_separator, "") + newline

                           + "Font di Tabel Transaksi         : " + Tfont_di_tabel_transaksi.getText().toString().trim()     + newline
                           + "Font Size di Tabel Transaksi    : " + Tfont_size_di_tabel_transaksi.getText().toString().trim().replace(retail.digit_separator, "") + newline
                           + "Font Bold di Tabel Transaksi    : " + (CHfont_bold.isChecked() ?"ya":"tidak")  + newline
                           + "Font Italic di Tabel Transaksi  : " + (CHfont_italic.isChecked() ?"ya":"tidak")  + newline
                           + "Tinggi Baris di Tabel Transaksi : " + Ttinggi_baris_di_tabel_transaksi.getText().toString().trim().replace(retail.digit_separator, "") + newline
                           + "Lebar Kode di Tabel Transaksi   : " + Tlebar_kode_di_tabel_transaksi.getText().toString().trim().replace(retail.digit_separator, "") + newline

                           + "Pembulatan Rupiah    : " + Tpembulatan.getText().toString().trim().replace(retail.digit_separator, "") + newline

                           + "Print Barcode Skala Horizontal      : " + Tprint_barcode_skala_horizontal.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Barcode Skala Vertical        : " + Tprint_barcode_skala_vertical.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Barcode Lebar Kolom           : " + Tprint_barcode_lebar_kolom.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Barcode Tinggi Baris          : " + Tprint_barcode_tinggi_baris.getText().toString().trim().replace(retail.digit_separator, "")  + newline

                           + "Print Label Rak Skala Horizontal    : " + Tprint_label_rak_skala_horizontal.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Label Rak Skala Vertical      : " + Tprint_label_rak_skala_vertical.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Label Rak Lebar Kolom         : " + Tprint_label_rak_lebar_kolom.getText().toString().trim().replace(retail.digit_separator, "")  + newline
                           + "Print Label Rak Tinggi Baris        : " + Tprint_label_rak_tinggi_baris.getText().toString().trim().replace(retail.digit_separator, "")  + newline

                           +  newline
                           + "Aktifkan Suara       : " + (CHsound_active.isChecked() ?"ya":"tidak")  + newline

                           + ( CHppn_active.getVisibility()==View.VISIBLE ?
                              newline
                              + "PPN aktif secara default : " + (CHppn_active.isChecked() ?"ya":"tidak")  + newline
                              + "PPN ditampilkan          : " + (CHppn_visible.isChecked()?"ya":"tidak")  + newline
                           : "" )
                    );
                    writer.flush();
                    writer.close();
                    retail.db.read_cfg(); //need to refresh the config vars!
                    Flogin.form.Tuser.setText( Tuser_default.getText().toString().trim(), TextView.BufferType.EDITABLE );
                    Flogin.form.Ppassword.setText( Ppassword_default.getText().toString().trim(), TextView.BufferType.EDITABLE );
                } catch (Exception ex) {
                    ((android.support.v7.widget.AppCompatButton) v).setEnabled(true);
                    retail.show_error( "Penulisan File '" +retail.db.cfg.get("file_konfigurasi")+ "' Gagal!\n\n\n" + ex, "Penulisan File Gagal!" );
                //} finally { br.close();
                }
        }});

        Bkosongkan.setText( "test koneksi" );
        Bkosongkan.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
            Bkosongkan.setEnabled(false);
            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
                    java.sql.Connection con = null;    db.err_msg = "";
                    try {
                        con = java.sql.DriverManager.getConnection( "jdbc:" +Tdbms.getText().toString().trim()+ "://" +Thost.getText().toString().trim()+ ":" +Tsocket.getText().toString().trim()+ "/" +Tdatabase.getText().toString().trim()+ "" + "?user=" +Tuser.getText().toString().trim()+ "&password=" + Ppassword.getText().toString() );
                        con.close();  con = null;
                    } catch (Exception ex) {
                        if( ex.getMessage().indexOf("Access denied") >= 0) db.err_msg = ex.getMessage() + "\n\nPastikan konfigurasi koneksi sudah benar!\n\n\n";
                        else                                               db.err_msg = ex.getMessage() + "\n\nPastikan server database sudah dijalankan!\n\n\n";
                        try{ if( !con.isClosed() ) con.close(); } catch(Exception x) {};  //con.close();  con = null;
                    }
                    return null;
                }
                @Override protected void onPostExecute(Void v) {
                    if( db.err_msg.equals("") ) {
                        retail.show_error( "Koneksi ke Database Berhasil!\n\n\n", "Koneksi Berhasil!" );  //belum tentu dia ubah:p >> Klik \"Simpan\" untuk menyimpan konfigurasi!\n
                        //con.close();  con = null;
                    } else {
                        retail.show_error( db.err_msg, "Koneksi ke Database Gagal!" );
                        //try{ if( !con.isClosed() ) con.close(); } catch(Exception x) {};  //con.close();  con = null;
                    }
                    Bkosongkan.setEnabled(true);
                }
            }.execute();

        }});


        Bkosongkan.setEnabled(true);
        return ret;
    }
}