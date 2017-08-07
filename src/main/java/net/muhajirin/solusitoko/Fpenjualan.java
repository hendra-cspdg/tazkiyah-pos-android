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
import java.util.ArrayList;

public class Fpenjualan extends Ftransaksi {
    //String sql_get_brg = "SELECT id, name, code, harga_jual, CAST( CONCAT_WS(',',diskon_jual,disc_qty1,disc_amount1,disc_qty2,disc_amount2,disc_qty3,disc_amount3,disc_qty4,disc_amount4) AS char ) AS diskon, gambar FROM barang ORDER BY name";    //mending by name, krn klo BY code dia pastinya udah hafal (ato ada guideline dari kode barcode)

    public EditText Tdibayar;    //must be public to let requestFocus() from retail ...
    EditText Lkembali;
    //JLabel Ldialog_kembali = new JLabel();
    //JLabel Ldialog_dibayar = new JLabel();
    //Object[] msg_dialog_kembali = { "\nDibayar:\n",Ldialog_dibayar, "\n\nKembali:\n",Ldialog_kembali,"\n\n\n\n\n" };   //Object[] msg_dialog_kembali = { "\n",Ldialog_kembali,"\n","\n" }; //ga iso ... label.setSize( new Dimension(600, 650) );
    //blink listener_blink;    clock listener_clock;    Timer timer_title;    Timer timer_total;    Timer timer_kembalian;    //Timer timer_refresh;
    android.os.CountDownTimer timer_kembalian;

    static Fpenjualan form;
    public Fpenjualan() { super(); }
    public static Fpenjualan newInstance(String modul) {
        return newInstance(modul, "") ;
    }
    public static Fpenjualan newInstance(String modul /*, Object parent_*/, final String barcode_init_ ) {
        form = new Fpenjualan();
        Ftransaksi.newInstance(modul, form);
        barcode_init = barcode_init_;
        form.modul = modul;    //"Edit Data Barang";
        return form;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);

        //final String modul = "Penjualan";

        Tdibayar = new EditText(form.getActivity());    Tdibayar.setMinWidth(min_width);    Tdibayar.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Tdibayar.setHint( "Dibayar" );    Tdibayar.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );
        TextInputLayout TL = new TextInputLayout(form.getActivity());    TL.addView( Tdibayar, prms_tv );
        toolbar.addView( TL, prms );
        //footer_panel.addView( TL, prms );
        //Tdibayar.setName("Tdibayar");    //supaya bisa dipanggil getName()nya di retail :p
        Tdibayar.setOnFocusChangeListener(retail.add_ribuan_when_lost_focus);

android.util.Log.e( "penjual: ", "5"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));
        Tdibayar.addTextChangedListener( new android.text.TextWatcher() {
                public void afterTextChanged( android.text.Editable s ) {    //to me (especially for patternfilter), e.getType().toString() bernilai remove atau insert ... kadang2 dua2nya ... jadi double2 deh:p So, i have to save the last value of this element to compare :p
                        if( !calculate_disc_percent ) return;    //supaya ga circular lock:p
                        String string = s.toString().trim();
                        if( string.length()==0 ) string="0";
                        if( string.length()>9 ) {
android.util.Log.e( "dibayar: ", "string.substring(0,9)="  + string.substring(0,9) );

                            Tdibayar.setText( string.substring(0,9) );    //ini harusnya tugas filter dan validasi ntar:p
                            return;
                        }
                        if( table==null ) return;
                        Boolean enter_pressed = false;
                        /*if(string.length()>0) if( string.substring(0,1).equals("0") ) if( string.equals("0" + fb.getDocument().getText( 0,fb.getDocument().getLength() ) ) ) enter_pressed = true;    //ini hanya akibat aksi dummy setText() dari event dispathcer akibat user tekan enter
                        if( enter_pressed && kembali<0 && Math.abs(kembali)<total_round && total_round>0 ) {
                            JOptionPane.showMessageDialog( Fpenjualan.this, "Maaf, jumlah dibayar lebih kecil dari total belanja!", "Jumlah Dibayar Lebih Kecil", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        */
                        //super.replace(fb, offset, length, string, attr);
                        //if( super.pattern.matcher(super.plain).matches() ) {
                            //Document doc = fb.getDocument();
                            //if( table.isEditing() ) hitung_total();    //table.getCellEditor().stopCellEditing();    //post update ... pingin jaga2 aja
                            //else
                            hitung_kembalian(string);
                            String dibayar_text = string;    //doc.getText(0,doc.getLength());    //di bawah ini aku pake (total_round+kembali) % 100 == 0 walo ga ideal tp aku perlu cegah enter masuk dari barcode reader (misal: setelah user ketik 44) yg akan menampilkan dialog kembali dan langsung ngeprint!!!    //trus aku pilih <=3 krn harus di bawah len_barcode_detection agar cepat kereset sebelum kembali diproses    //// && dibayar_text.substring(dibayar_text.length() - 2, dibayar_text.length()).equals("00")
                            int dibayar = total_round+kembali;
// ga bisa karena bisa aja dia bayar 55.000 biar kembaliannya bulat 20.000 .. ato klo mo repot, cek lagi apa pecahan kembaliannya bakal lebih besar2 ato lebih sedikit?
                            if( ( (total_round+kembali) % 100 == 0 /*|| indexOf_000==0 || indexOf_000>=1*/ ) &&
                              ( total_round>0 && kembali>=0 && dibayar_valid(dibayar) /* && kembali<100000 */  && s.length()>0 || total_round>0 && enter_pressed ) ) {    //!string.isEmpty() >> jika user hanya menghapus:p    //enter_pressed>>klo user ga sengaja scanbarcode di sini, dia jadi kesulitan menghapusnya lagi krn dialog kembali ini muncul terus:)
                                //int dibayar = total_round+kembali;
                                if( dibayar==1000 || dibayar==2000 || dibayar==5000 || dibayar==10000 ) {    //got potential to be 10rb, 20rb, 50rb or 100rb, then give a delay to u to type more :)
                                    if( timer_kembalian==null )
                                        timer_kembalian = new android.os.CountDownTimer(1000, 1000) {
                                                              public void onFinish() {
                                                                timer_kembalian.cancel();
                                                                int dibayar = total_round+kembali;
                                                                if( dibayar==1000 || dibayar==2000 || dibayar==5000 || dibayar==10000 ) show_kembalian( retail.numeric_format.format(dibayar) );
                                                              }
                                                              public void onTick( long millisUntilFinished ) {}
                                                          };
                                    timer_kembalian.start();
                                } else
                                    show_kembalian(dibayar_text);

                            } //else if (kembali>=0 )                JOptionPane.showMessageDialog( Fpenjualan.this, "total_round : kembali " + total_round +" : " + kembali + " retail.input_buffer:" + retail.input_buffer.toString() , "Penyimpanan Data " + modul + " Gagal", JOptionPane.ERROR_MESSAGE);

                        //}
                }
                public void onTextChanged( java.lang.CharSequence s, int start, int before, int count ) {    //This method is called to notify you that, within s, the count characters beginning at start have just replaced old text that had length before.
                }
                public void beforeTextChanged( java.lang.CharSequence s, int start, int count, int after ) {
                }
        });

android.util.Log.e( "penjual: ", "6"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));


        Lkembali = new EditText(form.getActivity());    Lkembali.setText("0");    Lkembali.setEnabled(false);    Lkembali.setMinWidth(min_width);    Lkembali.setGravity( Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        Lkembali.setHint( "Kembali" );    Lkembali.setBackgroundColor( disabled_background_color );    Lkembali.setTextSize(28f);    Lkembali.setTextColor( android.graphics.Color.GREEN );    //0xff060018
        TL = new TextInputLayout(form.getActivity());    TL.addView( Lkembali, prms_tv );
        toolbar.addView( TL, prms );
        //footer_panel.addView( TL, prms );
android.util.Log.e( "penjual: ", "7"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));




android.util.Log.e( "penjual db.col_width: ", ""+db.col_width.length  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));
        if( col_banyak_idx<0 || col_banyak_idx>=db.col_width.length ) col_banyak_idx=3;
        //if( col_banyak_idx!=3 ) db.column_model.moveColumn(3,col_banyak_idx);    //pindah posisi kolom banyak

android.util.Log.e( "penjual: ", "12" );
        if( !db.err_msg.equals("") ) {  error("Pembacaan Data Penjualan Gagal");   return null; }
android.util.Log.e( "penjual: ", "13"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));




android.util.Log.e( "penjual: ", "14"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));
        //init
        Tdibayar.setText("0");    Tdibayar.selectAll();
android.util.Log.e( "penjual: ", "15"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));
        agent_id = -13;    //biar yg di bawah in action
        //if( Cname_agent.getItemCount()>0 ) Cname_agent.setSelectedIndex(0);  else Tdiskon_edit.setText("0");    //utk menginisiasi Tdiskon_edit.getDocument().getProperty("plain_num") dan sekalian label2 via hitung_total();
        Tdiskon.setText("0");    Tdiskon.selectAll();    //utk menginisiasi label2 via hitung_total();
        //di construtor aja >> CHppn.setSelected(true);
        //hitung_total();

android.util.Log.e( "penjual: ", "16"  + ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ));

        set_enable_tombol2(false);

android.util.Log.e( "penjual: ", "17"  /*+ ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) ) */ );
//( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 )indowToken(), 0 );



//pindah atas         db.col_editor[0].setTag("barcode target");    //null pointer exception jg walo da dicreate di constructornya. Kayaknya gara2 contexnya yg ga sama deh >> form.table.col[2].setTag("barcode target");    form.table.col[2].setOnClickListener(retail.scan_listener2);    //db.col_editor[2].setOnLongClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_target = (TextView) Tcode;
android.util.Log.e( "penjual: ", "18"  /*+ ( form.getActivity().getCurrentFocus()==null ? "" : ""+ ((android.view.inputmethod.InputMethodManager) form.getActivity().getSystemService( android.app.Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(form.getActivity().getCurrentFocus().getWindowToken(), 0 ) )*/);

        //Lkembali.performClick();    //form.table.col[0].requestFocus();    //Tdibayar.requestFocus();    retail.hideSoftKeyboard( (android.app.Activity) form.getActivity() );    //entah kenapa softkeyboardnya muncul:p
        //form.view.postDelayed(new Runnable() { @Override public void run() {    retail.hideSoftKeyboard( form.getActivity() );    }},500);    //already give me a lot of problem

        return ret;
    }


    @Override void hitung_sub_total( final Boolean sender_is_setValue ) {
        if( sub_total==0 && kembali!=0 ) Tdibayar.setText("0");    //ini krn ga jadi kureset di Bbaru.doClick()
        super.hitung_sub_total( sender_is_setValue );
    }

    @Override void hitung_total( Boolean sender_is_setValue ) {
        super.hitung_total( sender_is_setValue );
        hitung_kembalian( Tdibayar.getText().toString() );
        poin = (int) Math.floor( total_round / harga_poin );
        //show_poin();
        Tdibayar.setEnabled( total_round>0 );    //aku pinter ga ya:)
    }

    void hitung_kembalian( String dibayar ) {
        dibayar=dibayar.trim();
        if( dibayar.length()==0 ) dibayar="0";
        kembali = Integer.valueOf( dibayar.replace(retail.digit_separator,"") ) - total_round;
        if( table.getRowCount()==0 ) return;    //biar masi ada sisa di layar utk dicek petugas. Sebaiknya si form dialog kembali jangan showmodal sehingga ga perlu ditutup saat user ngeprint:p

        if( kembali<0 )
            Lkembali.setText( "-" ) ;    //kembali=0;
        else
            Lkembali.setText( retail.numeric_format.format(kembali) ) ;
    }

    void show_kembalian(String dibayar_text) {
android.util.Log.e("show_kembalian:", "dibayar_text =" + dibayar_text );
        Tdibayar.setEnabled(false);    //block immediatelly
        int limit_banyak_simpan = retail.is_number( retail.convert_null(retail.setting.get("Default Simpan jika banyak kurang dari")) ) ? Integer.valueOf( retail.convert_null(retail.setting.get("Default Simpan jika banyak kurang dari")) ) : 0 ;
        int limit_total_simpan = retail.is_number( retail.convert_null(retail.setting.get("Default Simpan jika total kurang dari")) ) ? Integer.valueOf( retail.convert_null(retail.setting.get("Default Simpan jika total kurang dari")) ) : 0 ;

        //untuk simpan semua, jgn set kedua setting di atas, atau set ke 0
        int tombol_default = (banyak_total<limit_banyak_simpan || limit_banyak_simpan==0) && (total_round<limit_total_simpan || limit_total_simpan==0) /*&& Ccode_agent.getSelectedIndex()<=0 */&& retail.hak_akses.indexOf("'Default Tombol Simpan di Dialog Kembali'") >= 0 ? 1 : 0 ;          //shift_mask dipencet maka tombol angka ga keluar >> retail.modifiers_pub==Event.SHIFT_MASK
//System.out.println( "(banyak_total<limit_banyak_simpan || limit_banyak_simpan==0)= " + (banyak_total<limit_banyak_simpan || limit_banyak_simpan==0) );
        final Boolean otomatis_print_simpan = retail.hak_akses.indexOf("'Otomatis Print/Simpan di Dialog Kembali'") >= 0 ;
        //retail.in_progress = true;
//        if( otomatis_print_simpan ) {
//            toolkit=Toolkit.getDefaultToolkit();
//            scroll_lock = toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
//        } else {
//            if( kembali>=0 ) {
//                Ldialog_kembali.setText( " " + String.format( "%,d", kembali ) + "    " );     //+ "\n\\n" + "\r\n" + "r n " + java.lang.Character.LINE_SEPARATOR + "LINE_SEPARATOR" );   //\n ga ngaruh di JLabel:p
//                Ldialog_dibayar.setText( "     " + dibayar_text );
//            } else {
//                Ldialog_kembali.setText( " -" + "    " + "    " + "    " );
//                Ldialog_dibayar.setText( "     -" + "    " + "    " );
//            }
//        }
        final int resp = tombol_default;    //otomatis_print_simpan ? ( scroll_lock ? (tombol_default+1)%2 : tombol_default ) : JOptionPane.showOptionDialog( Fpenjualan.this, msg_dialog_kembali, "Kembali", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, retail.icon_smile, retail.Boptions3, retail.Boptions3[tombol_default] );
        //if( scroll_lock ) toolkit.setLockingKeyState(KeyEvent.VK_SCROLL_LOCK, false);
        //SwingUtilities.invokeLater(new Runnable(){ @Override public void run() {    //utk otomatis_print sebenarnya
            if( resp==0 ) {        //"Print"
                //Bprint.doClick();    retail.play_wav(sound);
            } else if( resp==1 ) { //"Simpan"
                simpan();   //retail.play_wav(sound);
            } else               {
                Tdibayar.requestFocus();    Tdibayar.selectAll();    //table.requestFocus();
            }
/*
            if( otomatis_print_simpan && retail.input_buffer_first.toString().matches("[0]{1,}") )    //{2,}    //<< ato baris ini ga perlu?!
                //UNTESTED ni yg di bawah. Rada jorok pula:P >> SwingUtilities.invokeLater(new Runnable(){ @Override public void run() {
                new javax.swing.Timer(1000, new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
                    retail.in_progress = false;    //otherwise, keyboard ga bereaksi saat user ketik banyak
                    retail.input_buffer_first = new StringBuilder() ;    retail.input_buffer = new StringBuilder();    //UNTESTED: ini agar tidak nongol angka bekas user tahan saat dibayar
                    if( table.isEditing() ) table.getCellEditor().cancelCellEditing();
                    ((javax.swing.Timer)e.getSource()).stop();
                }}).start();
                //}});
            else
                retail.in_progress = false;
*/

        //}});
    }



    int[] pecahan_uang = { 100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100 };
    Boolean dibayar_valid( int dibayar ) {
        if( kembali==0 ) return true;
        if( kembali>=pecahan_uang[0] ) return false;    //kembali tidak boleh lebih besar dari 100.000
        //int kembali_ribuan = (int) Math.floor( kembali / 1000 ) ;
        //jgn sampai pecahan uang yg dibayar dikembalikan pula lagi :)
        int left = kembali;    //_ribuan ;
        java.util.ArrayList<Integer> pecahan_kembali = new java.util.ArrayList<Integer>();
        for( int pecahan : pecahan_uang ) {
            while( left>=pecahan ) {
                left -= pecahan ;
                pecahan_kembali.add(pecahan);
            }
            if( left==0 ) break;
        }

        //dibayar = (int) Math.floor( dibayar / 1000 ) ;
        left = dibayar;
        //int total = (int) Math.floor( total_round / 1000 ) ;
        Boolean Continue=false;
/* ga jadi nehhh, biar safety aja deh:p
if jika pecahan yg akan dibayar sudah melebihi total tapi masih ada sisa left, ada dua kemungkinan:
   cek if pecahan.contains!!, jika tidak ada, berarti dia emang kasi lebih krn kasir ga punya kembalian then left -= pecahan
   else if contains then set contain=true, mungkin pecahannya emang lain, abaikan dulu pecahan ini, lanjutin loop ke pecahan lebih kecil

else jika pecahan yg akan dibayar sudah melebihi total tapi left==0 return true

*/
        String debug="";
        java.util.ArrayList<Integer> left_contains_pecahan_kembali = new java.util.ArrayList<Integer>();
        left_contains_pecahan_kembali.add(dibayar-total_round);    //supaya ga ada kembalian yg ga perlu:)
        for( int i=0; i<pecahan_uang.length; i++ ) {    //for( int pecahan : pecahan_uang ) {
            if( i==0 ) debug="";
            int pecahan=pecahan_uang[i];
            int pecahan_sum=0;
            while( left>=pecahan ) {    //utk test >>> contoh1: belanja 35000. dibayar 55000, kembali 20000   contoh2: 149rb dibayar 160rb pake uang 100+20*3
//JOptionPane.showMessageDialog( null, "dibayar-left + pecahan " + dibayar + " - " + left + " + " + pecahan + "   " + (dibayar-left + pecahan) + "  >= " + total + "    pecahan_kembali_contains=" + left_contains_pecahan_kembali.contains(left-pecahan), "refress", JOptionPane.ERROR_MESSAGE);
                //if( dibayar-left + pecahan >= total ) {    //&& pecahan_kembali_contains    //do not pas total twice (jika pecahan yg akan dibayar sudah melebihi total tapi masih ada sisa left)
                pecahan_sum+=pecahan;
//if( left>pecahan && left_contains_pecahan_kembali.contains(left-pecahan) ) JOptionPane.showMessageDialog( null, "left_contains_pecahan_kembali.contains(left/*-pecahan*/)   left=" + left + "  pecahan= " + pecahan, "refress", JOptionPane.ERROR_MESSAGE);
                if( left>pecahan && left_contains_pecahan_kembali.contains(left-pecahan) ) {    //-pecahan means prevent it one step before:)    //&& pecahan_kembali_contains    //do not pas total twice (jika pecahan yg akan dibayar sudah melebihi total tapi masih ada sisa left)
                    //nop, just try next pecahan >> left = dibayar;
                    break;
                } else if( pecahan_kembali.contains(pecahan_sum) /*fail saat belanja 45000 dibayar 46000 >> || pecahan_kembali.contains(left-pecahan) */ ) {    //agar mendeteksi pecahan yg merupakan faktornya juga:p  ... maksudnya juga mendeteksi kelipatannye ... misal: (10.000+10.000) juga terdetect klo 20.000nya ada di kembalian :p
//JOptionPane.showMessageDialog( null, "pecahan_kembali.contains(pecahan_sum)   pecahan = " + pecahan + "   posisi=" + (dibayar-left) + "   left=" + left +"    ulangi   ", "refress", JOptionPane.ERROR_MESSAGE);
                    if( left + pecahan_sum-pecahan < dibayar ) {    //if( dibayar-left + pecahan >= total ) {    //if( left==dibayar ) return false;    //artinya, pada percobaan pertama, sudah contains!
                        if( pecahan_sum==pecahan ) left_contains_pecahan_kembali.add(left);
                        i=-1;    //jgn, krn ga tau juga pecahan mana yg terakhir diambil i-=2;    left += dibayar;    //supaya ntar ngulang loop ke 0 lagi:D
                        Continue=true;
                    }
                    left = dibayar;
                    break;
                }
//JOptionPane.showMessageDialog( null, "he = " + ( left>pecahan && left_contains_pecahan_kembali.contains(left-pecahan)), "refress", JOptionPane.ERROR_MESSAGE);

                //}
                left -= pecahan;
//debug+="  "+ pecahan;    if( debug.length()%80<=3 ) debug+="\n";    if( left==0 ) JOptionPane.showMessageDialog( null, "debug = " + debug + "\nleft_contains_pecahan_kembali=" + left_contains_pecahan_kembali.toString(), "refress", JOptionPane.ERROR_MESSAGE);
//JOptionPane.showMessageDialog( null, "dibayar-left  = " + ( dibayar-left ) , "refress", JOptionPane.ERROR_MESSAGE);
                if( left==0 ) return true;
                //fail saat belanja 5000 dibayar 6000 >> else if( dibayar-left == total_round ) return false;    //ga tau pasti apa ini perlu:D, but safety first:p
            }
            if( Continue ) {    Continue=false;    continue;    }
            //if( left==0 ) return true;
        }
        return false;
    }

    @Override void close_action() {
        if( timer_kembalian!=null ) timer_kembalian.cancel();    timer_kembalian = null;
        super.close_action();
    }
}