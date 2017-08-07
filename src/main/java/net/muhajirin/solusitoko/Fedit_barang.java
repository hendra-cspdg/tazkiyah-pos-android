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
//import java.io.File;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
//import android.text.InputType;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;


public class Fedit_barang extends Fedit {
    static Fedit_barang form;
    public Fedit_barang() { super(); }
    public static Fedit_barang newInstance(String modul) {
        return newInstance(modul, "") ;
    }
    public static Fedit_barang newInstance(String modul /*, Object parent_*/, final String barcode_init_ ) {
        form = new Fedit_barang();
        Fedit.newInstance(modul, form);
        barcode_init = barcode_init_;
        form.modul = modul;    //"Edit Data Barang";
        return form;
    }


    String modul;
    @Override Boolean build_sql() {
android.util.Log.e( "build_sql: ", "1" );
        Boolean sup_akses = retail.hak_akses.indexOf("'Supplier di Fitur Barang'") >= 0 ;
        Boolean disc_qty  = retail.convert_null(retail.setting.get("Aktifkan Diskon Quantity")).toLowerCase().equals("ya");
        Boolean barcode   = retail.convert_null(retail.setting.get("Prefer Barcode")).toLowerCase().equals("ya");
android.util.Log.e( "build_sql: ", "2" );
        //build_sql masuk di asynctask:p >> JCdb Cagent = JCdb.newInstance(false, "SELECT id, name FROM supplier ORDER BY IF(id=1,-111,name)", form.getActivity());
android.util.Log.e( "build_sql: ", "3" );
        db.col_editor = new View[]       {    null,  null ,     null,          null, null,  Cagent          ,    null   ,     null   ,    null   ,     null   ,     null       ,      null      ,     null       ,      null      ,     null       ,      null      ,     null       ,      null      , null ,  null, null,    null    };
android.util.Log.e( "build_sql: ", "4" );
        db.col_width  = new int[]        {      0 ,    0  , (barcode?105+10:70) ,  170+50,  40 , (sup_akses?150:0),     70    ,      75    ,     70    ,      75    , (disc_qty?65:0), (disc_qty?90:0), (disc_qty?65:0), (disc_qty?90:0), (disc_qty?65:0), (disc_qty?90:0), (disc_qty?65:0), (disc_qty?90:0),  50  ,   50 ,  50 ,    300     };
android.util.Log.e( "build_sql: ", "5" );
        sql =                        "SELECT   id , gambar,     code,          name, stok,  supplier_id     , harga_beli, diskon_beli, harga_jual, diskon_jual,  disc_qty1     ,  disc_amount1  ,  disc_qty2     ,  disc_amount2  ,  disc_qty3     ,  disc_amount3  ,  disc_qty4     ,  disc_amount4  , berat, omset, size, deskripsi FROM barang ORDER BY code";
android.util.Log.e( "build_sql: ", "6" );

        db.editor_barcode_target = 2;
android.util.Log.e( "build_sql: ", "7" );
        /*
        db.col_editor[2] = new EditText(form.getActivity());
        ((EditText)db.col_editor[2]).setInputType( android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_NORMAL );
        db.col_editor[2].setTag("barcode target");
        */

        return true;
    }

    EditText Taset, Tmodal, Tstok, Titem;  //ActionListener summary_actionListener;
    JCdb Cagent;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sql ="pancing dulu sebelum build_sql() ";
        //default >> form.enable_filter = true;
        Cagent = JCdb.newInstance(false, "SELECT id, name FROM supplier ORDER BY IF(id=1,-111,name)", form.getActivity());
        summary = retail.hak_akses.indexOf("'Summary di Fitur Barang'") >= 0;    //used by parent class too
        if( summary ) {
//E9D460
            footer_panel = new LinearLayoutCompat(form.getActivity());
            footer_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);

android.util.Log.e( "editbrg: ", "1" );

            /*this should be replaced later by visibling horizontal scrollbar
            View border = new View(form.getActivity());
            border.setBackgroundResource( android.R.color.black );
            LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, 1 );
            footer_panel.addView( border, prms );
            */

            int min_width = 60+20;
            int disabled_background_color = android.graphics.Color.TRANSPARENT ;    //0xff663399
            Taset = new EditText(form.getActivity());    Taset.setEnabled(false);    Taset.setMinWidth(min_width);    Taset.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            Taset.setHint( "Aset Rp." );    Taset.setTextColor( android.graphics.Color.GREEN );    Taset.setBackgroundColor( disabled_background_color );
android.util.Log.e( "editbrg: ", "2" );

            LayoutParams prms_tv = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );    //new LayoutParams( 85, LayoutParams.WRAP_CONTENT );
            TextInputLayout TL = new TextInputLayout(form.getActivity());    TL.addView( Taset, prms_tv );
            LayoutParams prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
            prms.setMargins( 0, 7, 0, 0 );
            footer_panel.addView( TL, prms );

            Tmodal = new EditText(form.getActivity());    Tmodal.setEnabled(false);    Tmodal.setMinWidth(min_width);    Tmodal.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            Tmodal.setHint( "Modal Rp." );    Tmodal.setTextColor( android.graphics.Color.GREEN );    Tmodal.setBackgroundColor( disabled_background_color );
            TL = new TextInputLayout(form.getActivity());    TL.addView( Tmodal, prms_tv );
            footer_panel.addView( TL, prms );

            Tstok = new EditText(form.getActivity());    Tstok.setEnabled(false);    Tstok.setMinWidth(min_width);    Tstok.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            Tstok.setHint( "Stok" );    Tstok.setTextColor( android.graphics.Color.GREEN );    Tstok.setBackgroundColor( disabled_background_color );
            TL = new TextInputLayout(form.getActivity());    TL.addView( Tstok, prms_tv );
            footer_panel.addView( TL, prms );

            Titem = new EditText(form.getActivity());    Titem.setEnabled(false);    Titem.setMinWidth(min_width);    Titem.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            Titem.setHint( "Item" );    Titem.setTextColor( android.graphics.Color.GREEN );    Titem.setBackgroundColor( disabled_background_color );
            TL = new TextInputLayout(form.getActivity());    TL.addView( Titem, prms_tv );
            footer_panel.addView( TL, prms );

            //refresh_summary();
            //panel.addView( footer_panel, prms );
        }

        View ret = super.onCreateView(inflater, container, savedInstanceState);
android.util.Log.e( "editbrg: ", "3" );
        refresh_summary();
android.util.Log.e( "editbrg: ", "4" );
//pindah atas         db.col_editor[2].setTag("barcode target");    //null pointer exception jg walo da dicreate di constructornya. Kayaknya gara2 contexnya yg ga sama deh >> form.table.col[2].setTag("barcode target");    form.table.col[2].setOnClickListener(retail.scan_listener2);    //db.col_editor[2].setOnLongClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_target = (TextView) Tcode;
android.util.Log.e( "editbrg: ", "5" );
        return ret;
    }

    @Override void refresh_summary() {
        if(!summary) return;
        long aset=0, modal=0, stok=0;  int item=-1;
        for( int i=0; i<db.getRowCount(); i++ ) {
            String left_val = db.getStringAt( i, 0 );
            if( left_val.equals("0") || left_val.length()==0 ) continue;
            int stok_i  = db.getIntAt(i, 4 );
            if( stok_i<0 ) stok_i=0;
            stok  += stok_i;
            aset  += stok_i*db.getIntAt(i, 8 );
            modal += stok_i*db.getIntAt(i, 6 );
            item   = i;
        }
        Taset.setText  ( retail.numeric_format.format( aset  ));    //android.util.Log.e("onfooter fail:", String.format("%,d", 15000 ));
        Tmodal.setText ( retail.numeric_format.format( modal ));
        Tstok.setText  ( String.valueOf( stok   ) );
        Titem.setText  ( String.valueOf( item+1 ) );
    }

    @Override void close() {
        super.close();
        retail.reset_brg();
        if( barcode_init.length()>0 ) Ftransaksi.form.refresh_brg();
    }



}