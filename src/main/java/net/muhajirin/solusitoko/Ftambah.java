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
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import android.view.Window;
import android.view.Gravity;
import android.content.Context;
import android.widget.ImageView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
//import android.widget.Spinner;
import android.view.KeyEvent;

import java.util.*;
//import java.awt.*;
//import java.awt.event.*;

public class Ftambah extends DialogFragment {
    public AppCompatButton Bsimpan, Bkosongkan;
    protected db_connection db = retail.db; //hanya alias aja:p agar ga kepanjangan
    protected static String modul;    protected String sql;    protected View default_focused_comp;    protected int default_combo_index=0;
    protected android.widget.AdapterView.OnItemClickListener enable_on_change_combo;    //protected android.widget.AdapterView.OnItemSelectedListener enable_on_change_combo;    //ItemListener;
    protected android.widget.CompoundButton.OnCheckedChangeListener enable_on_change_checkbox;
    protected android.text.TextWatcher enable_on_change;
    protected static LinearLayoutCompat panel;
    protected String[] params;
    ArrayList<EditText> mandatory = new ArrayList<EditText>();
    ArrayList<EditText> comp_number = new ArrayList<EditText>();
    ArrayList<View> comps = new ArrayList<View>();
    Map<Integer, Integer> spinner_idx = new HashMap<Integer, Integer>();    //Map<Spinner, Integer> spinner_idx = new HashMap<Spinner, Integer>();
    static ViewGroup view;    static LayoutParams prms;
    static Ftambah form;
    public Ftambah() {}
    public static /*Ftambah */ void newInstance(String title, Ftambah form /*, Object parent_*/ ) {    //ViewGroup or another DialogFragment? :p
        //parent=parent_;
        //form = new Ftambah();
        form.setStyle( DialogFragment.STYLE_NORMAL, R.style.AppTheme );    //flogin    //dia dimasukin ke oncreate() klo menurut ini >> file:///E:/Android/sdk/docs/reference/android/app/DialogFragment.html
        //not exist >> form.setTitle(title);
        modul = title;
        //super(parent, retail.app_name + " - " + title + "   ::   Petugas: " + retail.nama, Dialog.ModalityType.APPLICATION_MODAL);
        form.setCancelable(false);    //defaultnya true
        //?!alert.setCanceledOnTouchOutside(false);    //<- to cancel outside touch
        Ftambah.form=form;
        //return form;
    }
/*
    static android.app.Activity my_Activity;
    static android.app.Activity get_my_Activity() {
        return my_Activity;
    }
*/



    @Override public void onStart() {
        super.onStart();
android.util.Log.e(" start : ", "Bsimpan.isEnabled():" + Bsimpan.isEnabled() );
    }
    @Override public void onResume() {
        super.onResume();
android.util.Log.e(" Resume : ", "Bsimpan.isEnabled():" + Bsimpan.isEnabled() );
    }
    @Override public void onPause() {
        super.onPause();
android.util.Log.e(" Pause : ", "Bsimpan.isEnabled():" + Bsimpan.isEnabled() );
    }
    @Override public void onStop() {
        super.onStop();
android.util.Log.e(" stop : ", "Bsimpan.isEnabled():" + Bsimpan.isEnabled() );
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    //Use onCreateView when the entire view of the dialog is going to be defined via custom XML.
android.util.Log.e("Ftambah: ", "1" );
try{

        align_comps();    //align comps from child class

android.util.Log.e("Ftambah: ", "2" );

//        if( retail.barcode_view!=null ) retail.get_my_Activity().barcode_view.setOnFocusChangeListener(retail.scan_listener);
/*or this? >>  implement OnFocusChangeListener
@Override
public void onFocusChange(View v, boolean hasFocus) {
    switch(v.getId()){
    case r.id.editText1:
    break;

    ...etc
    }
}
*/

        String err_msg = "";

        Bsimpan    = new AppCompatButton(this.getActivity());
        Bsimpan.setText( "simpan" );
        //Bsimpan.setBackgroundColor(android.graphics.Color.GREEN);
        Bsimpan.setCompoundDrawablesWithIntrinsicBounds( retail.icon_save, 0, 0, 0 ) ;
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.5f );
        prms.setMargins( 10+20, 25, 10+20, 0 );    panel.addView( Bsimpan, prms );
        Bsimpan.setEnabled(false);

        Bkosongkan    = new AppCompatButton(this.getActivity());
        Bkosongkan.setText( "kosongkan" );
        //Bkosongkan.setBackgroundColor(0x5500ff00);
        Bkosongkan.setCompoundDrawablesWithIntrinsicBounds( retail.icon_reset, 0, 0, 0 ) ;
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.5f );    //100, 80
        //params.setPaddings(12, 12, 12, 12);
        prms.setMargins( 10+20, 5, 10+20, 0 );    panel.addView( Bkosongkan, prms );
        Bkosongkan.setEnabled(false);
android.util.Log.e("Ftambah: ", "3" );

        Bsimpan.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
try{
            if( retail.empty( mandatory.toArray( new EditText[mandatory.size()] ) )) return;
            if( !build_sql() ) return;
android.util.Log.e("Ftambah: ", "sql=" + sql );
            v.requestFocusFromTouch();
            v.setEnabled(false);

            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
android.util.Log.e("Ftambah: ", " before exec sql=" + sql );
                    db.exec(sql, params, true, false/*, true*/);
                    return null;
                }
                @Override protected void onPostExecute(Void v) {
                    //public void run() {
                        if( db.err_msg.equals("") ) {
                            after_save_succeed();
                            Bkosongkan.performClick();    //doClick()
                        } else{
                            retail.show_error( db.err_msg, modul + " Gagal" );
                            Bsimpan.setEnabled(true);
                        }
                    //}
                }

            }.execute();

} catch (Exception ex) {    retail.show_error( "from regfresss\n. Pesan Kesalahan: " + ex, "refress" ); }
        }});
        Bkosongkan.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
            clear_value(panel);
            Bkosongkan.setEnabled(false);    Bsimpan.setEnabled(false);
            if( default_focused_comp!=null ) default_focused_comp.requestFocus();    //klo error, pake ginian >>if( default_focused_comp!=null ) view.post(new Runnable() { @Override public void run() {    default_focused_comp.requestFocus();    }});
        }});

android.util.Log.e("Ftambah: ", "4" );

        //my_Activity = (android.app.Activity) this.getActivity();
        form.getDialog().setOnKeyListener( new android.content.DialogInterface.OnKeyListener() {    //@Override public void onCancel( android.content.DialogInterface dialog ) { }    //doesn't exist >> @Override public void onBackPressed() {}
            @Override public boolean onKey( android.content.DialogInterface dialog, int keyCode, KeyEvent event ) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && form != null && form.getDialog().isShowing() && !retail.scan_cancelled ) {
                    if( !Bsimpan.isEnabled() ) { close(); return false; }
android.util.Log.e("Ftambah: ", "onkeycode 2" );
                    new android.os.AsyncTask<Void, Void, Integer> () {   @Override protected Integer doInBackground( Void... v ) {
                            return retail.show_confirm( "\nPerubahan data ingin disimpan?\n\n\n\n", "Konfirmasi" );    //, get_my_Activity()     //int resp = JOptionPane.showOptionDialog( retail.f, "\nPerubahan data ingin disimpan?\n\n\n\n", "Konfirmasi", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, retail.icon_confirm, retail.Boptions, null);
                        } @Override protected void onPostExecute( Integer resp ) {
                            if( resp==0 ) { //"YA"
                                Bsimpan.performClick();
                                if( !Bsimpan.isEnabled() ) close();
                            } else if( resp==1 ) { //"TIDAK"
                                close();
                            }}}.execute();

                    return true;
                } else if( retail.scan_cancelled ) {
                    retail.scan_cancelled = false;
                    return true;
                }
                return false;
            }
        });


            } catch (Exception e) { //numpang2 pake db.err_msg :D
android.util.Log.e("Ftambah: ", "error: " + "\nFTAMBAH\n" + e.getMessage() );
                //retail.show_error( "\nTAMBAH\n" + e.getMessage(), "eee" );
            }
android.util.Log.e("Ftambah: ", "last" );


        return (View) view;    //return inflater.inflate(R.layout.flogin, container, false);    //return inflater.inflate(R.layout.dialog, container);
    }
    protected void clear_value( ViewGroup container ) {
        for( int i=0; i<container.getChildCount(); i++) {    //for( Component comp : container.getComponents() )
            View comp = container.getChildAt(i);
            if( comp instanceof JCdb ) {    ((JCdb)comp).setText("");    ((JCdb)comp).setSelection( default_combo_index );    }    //else if( comp instanceof Spinner ) ((Spinner)comp).setSelection(default_combo_index);
            else if( comp instanceof EditText ) ((EditText)comp).setText("");
            else if( comp instanceof ViewGroup ) clear_value( (ViewGroup)comp );
        }
    }

    protected void align_comps() {

android.util.Log.e("Ftambah align_comps: ", "1" );
        LinearLayoutCompat lview = new LinearLayoutCompat(form.getActivity());    //getApplicationContext()
        view = (ViewGroup) lview;

        android.widget.ScrollView scroll_panel = new android.widget.ScrollView( form.getActivity() );    //supaya ga keganggu saat soft keyboard muncul
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        prms.gravity = Gravity.CENTER_VERTICAL;
        view.addView( scroll_panel, prms );

        panel = new LinearLayoutCompat(form.getActivity());
        panel.setOrientation(LinearLayoutCompat.VERTICAL);
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        scroll_panel.addView( panel, prms );

android.util.Log.e("Ftambah align_comps: ", "2" );

        enable_on_change = new android.text.TextWatcher() {
            public void afterTextChanged( android.text.Editable s ) {
                if(Bsimpan==null) return;
                Bsimpan.setEnabled(true);  Bkosongkan.setEnabled(true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        };
android.util.Log.e("Ftambah align_comps: ", "3" );
        enable_on_change_combo = new android.widget.AdapterView.OnItemClickListener() {
            @Override public void onItemClick( android.widget.AdapterView<?> parent, View view, int idx, long id ) {
                if(Bsimpan==null) return;
                int spinner = parent.getId();    //Spinner spinner = (Spinner) parent;
                //Integer index = (Integer) idx;
                if( spinner_idx.get( spinner ) == null ) {
                    spinner_idx.put( spinner, idx );
                    return;
                }
                if( spinner_idx.get( spinner ) == idx ) return;    //di nexianku, sebetulnya klo idxnya masih sama, dia bahkan ga ketangkap listener:p
                spinner_idx.put( spinner, idx );
                Bsimpan.setEnabled(true);    Bkosongkan.setEnabled(true);
            }
            //@Override public void onNothingSelected( android.widget.AdapterView<?> parent ) {}
        };
android.util.Log.e("Ftambah align_comps: ", "4" );
        enable_on_change_checkbox = new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged( android.widget.CompoundButton buttonView, boolean isChecked ) {
                Bsimpan.setEnabled(true);    Bkosongkan.setEnabled(true);
            }
        };

        //int ci=0;   String filter_pre;    //JTextField last_field=null;

        for( View comp : mandatory )
            if( comp instanceof EditText && !( comp instanceof JCdb ) ) {
                EditText tc = (EditText)comp;
                tc.setHint( tc.getHint().toString() + " *" );
            }

android.util.Log.e("Ftambah align_comps: ", "5" );

        LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );    //LayoutParams prms = new LayoutParams( (int) ( LayoutParams.MATCH_PARENT * (90/100)),  LayoutParams.WRAP_CONTENT );     //0, 2, 0, 2;
        prms.setMargins( 10+20, 5, 10+20, 0 );
        for( int i=0; i<comps.size(); i++)  align_comp(comps.get(i),i);

        if( default_focused_comp!=null ) view.post(new Runnable() { @Override public void run() {    default_focused_comp.requestFocus();    }});

    }


    protected void align_comp( View comp, int i ) {
//android.util.Log.e("Ftambah align_comp: ", "1" );
            if( comp instanceof EditText && !( comp instanceof JCdb ) ) {
                EditText tc = (EditText)comp;

//android.util.Log.e("Ftambah align_comp: ", "2" );

/* cukupkah ini? >> TYPE_CLASS_NUMBER

myEditText.setFilters( new InputFilter[] {new PasswordCharFilter(), new InputFilter.LengthFilter(20)});


For the numeric editText, there is also a obscur solution :
DigitsKeyListener MyDigitKeyListener = new DigitsKeyListener(true, true); // first true : is signed, second one : is decimal
editEntryView.setKeyListener( MyDigitKeyListener );


or this

<EditText 
  android:id="@+id/editText"
  android:inputType="text" 
  android:maxLength="10"
  android:digits="0,1,2,3,4,5,6,7,8,9,*,qwertzuiopasdfghjklyxcvbnm" 
  android:hint="Only letters and numbers allowed.."
/>

or this

 InputFilter filter = new InputFilter() {
                public CharSequence filter(CharSequence source, int start, int end,
                        Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetterOrDigit(source.charAt(i))) { // Accept only letter & digits ; otherwise just return
                            Toast.makeText(context,"Invalid Input",Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    return null;
                }

            };

        editText.setFilters(new InputFilter[] { filter });





                AbstractDocument doc = (AbstractDocument) tc.getDocument();
                doc.putProperty("tombol_to_enable", new AppCompatButton[]{Bsimpan, Bkosongkan} );
                if( comp_number.contains(comp) )
                    filter_pre = (retail.convert_null(comp.getName()).equals("enable_sign")?"^-?":"") + "\\d{0,";   //"\\d{1," >> dia lakukan validasi klo disetText("") atau setText(null) dari code
                else
                    filter_pre = ".{0,";    //\\w{0, >> A word character: [\p{Alpha}\p{gc=Mn}\p{gc=Me}\p{gc=Mc}\p{Digit}\p{gc=Pc}]
                doc.setDocumentFilter(new PatternFilter( filter_pre + (int)((JTextField)comp).getColumns() + "}" ));

*/

                tc.addTextChangedListener(enable_on_change);    //doc.addDocumentListener(retail.enable_on_change);
                //tc.setMargin( new Insets( 0, 7, 0, 0 ) );
                //last_field = tc;
            }

            if( comp instanceof JCdb ) ((JCdb)comp).setOnItemClickListener(enable_on_change_combo);    //if( comp instanceof Spinner ) ((Spinner)comp).setOnItemSelectedListener(enable_on_change_combo);
            if( comp instanceof android.widget.CheckBox ) ((android.widget.CheckBox)comp).setOnCheckedChangeListener(enable_on_change_checkbox);

            //if( comp instanceof EditText || comp instanceof Spinner ) {
                if( comp instanceof EditText && !( comp instanceof JCdb ) ) {    //setHintTextColors(???)
                    TextInputLayout TL = new TextInputLayout(form.getActivity());
                    TL.addView( comp, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
                    panel.addView( TL, prms );

                } else
                    panel.addView( comp, prms );

                if( default_focused_comp==null ) default_focused_comp = comp;    //utk dipakai di Bkosongkan
            //}
    }

    Boolean build_sql() {
        return false;
    }
    void after_save_succeed() {
    }
    void close() {
        //try { db.rs.close(); } catch (Exception e) {}
        //unfully tester: never close the retail.db to get permanent connection
        //anyway, subclasses may create their own db (like Fedit_kas) :p, so ...

android.util.Log.e("before  : ", "db.close" );
        if( db!=retail.db ) db.close();

android.util.Log.e("before  : ", "dismiss" );
        //tak ade ... if( retail.barcode_view!=null ) retail.barcode_view.removeCallbacks(retail.scan_listener);
        retail.barcode_target = null;
        dismiss();    //cancel();    //finish();    //dispose();
        retail.modal_result=1;

android.util.Log.e("after  : ", "dismiss" );

    }
}