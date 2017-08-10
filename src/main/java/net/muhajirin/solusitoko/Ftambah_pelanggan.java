/*
 *
 */

package net.muhajirin.solusitoko;

import android.support.v7.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.InputType;

public class Ftambah_pelanggan extends Ftambah {
    EditText Tname, Tcode, Tkontak, Tno_hp, Tno_telp, Tkota, Talamat, Tdiskon ;
    static Object parent;
    static Ftambah_pelanggan form;
    public Ftambah_pelanggan() { super(); }
    public static Ftambah_pelanggan newInstance(String title) {
        return newInstance(title, "", null) ;
    }
    public static Ftambah_pelanggan newInstance(String title, final String barcode_init_ ) {
        return newInstance(title, barcode_init_, null) ;
    }
    public static Ftambah_pelanggan newInstance(String title, final String barcode_init_, final Object parent_ ) {
        form = new Ftambah_pelanggan();    //Ftambah.newInstance(title);
        Ftambah.newInstance(title, form);
        //barcode_init = barcode_init_;
        parent=parent_;
        return form;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
String debug="";
try{
debug+="1";
android.util.Log.e("Ftambah_pelanggan: ", "1" );

        Tname = new EditText(form.getActivity());    Tname.setHint( "Nama Customer" );    comps.add(Tname);
        Tname.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tname.setMaxLines(50);    //Tname = new JTextField(50);    //ini tipu aja ... sebenarnya mo ngirim variable maxLength ke super class    //setMaxLines(int)

        Tcode = new EditText(form.getActivity());    Tcode.setHint( "Kode Customer" );    comps.add(Tcode);
        Tcode.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tcode.setMaxLines(20);
        Tcode.setOnClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_view = (TextView) Tcode;

        Tkontak = new EditText(form.getActivity());    Tkontak.setHint( "Kontak" );    comps.add(Tkontak);
        Tkontak.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tkontak.setMaxLines(4);

        Tno_hp = new EditText(form.getActivity());    Tno_hp.setHint( "No. HP" );    comps.add(Tno_hp);
        Tno_hp.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tno_hp.setMaxLines(30);

        Tno_telp = new EditText(form.getActivity());    Tno_telp.setHint( "No. Telp" );    comps.add(Tno_telp);
        Tno_telp.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tno_telp.setMaxLines(6);

        Tkota = new EditText(form.getActivity());    Tkota.setHint( "Kota" );    comps.add(Tkota);
        Tkota.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tkota.setMaxLines(4);

        Talamat = new EditText(form.getActivity());    Talamat.setHint( "Alamat" );    comps.add(Talamat);
        Talamat.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Talamat.setMaxLines(255);

        Tdiskon = new EditText(form.getActivity());    Tdiskon.setHint( "Diskon" );    comps.add(Tdiskon);
        Tdiskon.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL );    Tdiskon.setMaxLines(2);

debug+="2";
android.util.Log.e("Ftambah_pelanggan: ", "2" );

debug+="5";
android.util.Log.e("Ftambah_pelanggan: ", "5" );
        mandatory.add(Tname);         mandatory.add(Tcode);
        default_focused_comp = Tname;
debug+="6";

            } catch (Exception e) { //numpang2 pake db.err_msg :D
android.util.Log.e("Ftambah_pelanggan: ", "error: " + debug+"\nTAMBAH pelanggan\n" + e.getMessage() );
                retail.show_error( debug+"\nTAMBAH pelanggan\n" + e.getMessage(), "eee" );
            }
debug+="7";
android.util.Log.e("Ftambah_pelanggan: ", "6" );

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override Boolean build_sql() {
android.util.Log.e("Ftambah_pelanggan: ", "7" );
        sql = " INSERT INTO pelanggan (            id      , code, name, kontak, no_hp, no_telp, kota, alamat, diskon ) "
            + " SELECT             IFNULL( MAX(id), 0) + 1,   ? ,   ?  ,   ?  ,   ?  ,     ?  ,   ? ,    ?  ,    ?    FROM pelanggan" ;
        String[] params_ = { Tcode.getText().toString(), Tname.getText().toString(), Tkontak.getText().toString(), Tno_hp.getText().toString(), Tno_telp.getText().toString(), Tkota.getText().toString(), Talamat.getText().toString(), Tdiskon.getText().toString() };
 android.util.Log.e("Ftambah_pelanggan: ", "8" );
       this.params=params_;
        return true;
    }

    @Override void after_save_succeed() {
android.util.Log.e("after_save_succeed: ", " 1");
        if( !( parent instanceof Ftransaksi ) ) return;
        //Ftransaksi.form.refresh_agn();
        //merely additem to Ccode_agn and Cname_agn    //refresh_agn(barcode_init);
        ((android.widget.ArrayAdapter)Ftransaksi.form.Cname_agent.getAdapter()).add( Tname.getText().toString() );
        String code = jtable.getString( "id" );    //harusnya barcode
        Ftransaksi.form.Ccode_agent.items.add( new jcdb_item( jtable.getString( "id" ), code ) );    //i+1

        ((android.widget.ArrayAdapter)Ftransaksi.form.Cname_agent.getAdapter()).notifyDataSetChanged();    ((android.widget.ArrayAdapter)Ftransaksi.form.Ccode_agent.getAdapter()).notifyDataSetChanged();
//android.util.Log.e("ongetagent:", " 42 name_agent.getCount()" + Cname_agent.getAdapter().getCount() + " name_agent.items.getCount()" + Cname_agent.getAdapter().getCount()  + " name_agent.getAdapter().getCount()" + Cname_agent.getAdapter().getCount() );
        //Ftransaksi.form.Cname_agent.setListSelection( ((android.widget.Adapter)Ftransaksi.form.Cname_agent.getAdapter()).getCount()-1 );
        Ftransaksi.form.Cname_agent.setText( Tname.getText().toString() );
        Ftransaksi.form.Ccode_agent.setText( Tcode.getText().toString() );
android.util.Log.e("after_save_succeed: ", " 5");
        super.close();    //langsung keluar klo ada barcode_init heeh heee
android.util.Log.e("after_save_succeed: ", " 6");
    }

}