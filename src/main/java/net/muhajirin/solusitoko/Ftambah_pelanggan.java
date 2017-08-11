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
import java.net.URLEncoder;

public class Ftambah_pelanggan extends Ftambah {
    EditText Tname, Tcode, Tkontak, Tno_hp, Tno_telp, Tkota, Talamat, Tdiskon ;
    JCdb Cgender;
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


/*
"username",
							"value": "Coba",
"phone_number",
							"value": "0890909090909",
"gender",
							"value": "male",
							
"address",
							"value": "Jl Simatupang 34",
							
*/


        Tname = new EditText(form.getActivity());    Tname.setHint( "Username" );    comps.add(Tname);
        Tname.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tname.setMaxLines(50);    //Tname = new JTextField(50);    //ini tipu aja ... sebenarnya mo ngirim variable maxLength ke super class    //setMaxLines(int)
/*
        Tcode = new EditText(form.getActivity());    Tcode.setHint( "Kode Customer" );    comps.add(Tcode);
        Tcode.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tcode.setMaxLines(20);
        Tcode.setOnClickListener(retail.scan_listener);    //Tcode.setOnTouchListener(retail.scan_listener);    //Tcode.setOnFocusChangeListener(retail.scan_listener);    //retail.barcode_view = (TextView) Tcode;

        Tkontak = new EditText(form.getActivity());    Tkontak.setHint( "Kontak" );    comps.add(Tkontak);
        Tkontak.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tkontak.setMaxLines(4);
*/

        Tno_hp = new EditText(form.getActivity());    Tno_hp.setHint( "Phone Number" );    comps.add(Tno_hp);
        Tno_hp.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Tno_hp.setMaxLines(30);

        floating_label Lgender = new floating_label(form.getActivity());
        Lgender.setText("Gender");    comps.add(Lgender);
        Cgender = JCdb.newInstance(false,"", form.getActivity(), 1);    comps.add(Cgender);

        Cgender.items.add( new jcdb_item( 1, "male" ) );
        Cgender.items.add( new jcdb_item( 2, "female" ) );
        ((android.widget.ArrayAdapter)Cgender.getAdapter()).notifyDataSetChanged();

        Talamat = new EditText(form.getActivity());    Talamat.setHint( "address" );    comps.add(Talamat);
        Talamat.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );    Talamat.setMaxLines(255);

debug+="2";
android.util.Log.e("Ftambah_pelanggan: ", "2" );

debug+="5";
android.util.Log.e("Ftambah_pelanggan: ", "5" );
        mandatory.add(Tname);     //mandatory.add(Tcode);    //blm bisa >> mandatory.add(Cgender);
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
        table = "customer";
        url   = "url_" +table+ "_create";
        //validate first
        if( Cgender.getText().toString().trim().length()==0 ) Cgender.setText("male");    //sementara aja, harusnya sih server tentukan apa ini mandatory ato tidak, dan defaultnya apa
        if( Cgender.my_index_of( Cgender.getText().toString() ) < 0 ) {
            retail.show_error( "\nMohon perbaiki isian \"Gender\"" + "\n\n\n\n", "Isian salah" );
            return false;
        }
        try { params = "&username="+URLEncoder.encode( Tname.getText().toString().trim(), "UTF-8" ) + "&phone_number="+URLEncoder.encode( Tno_hp.getText().toString().trim(), "UTF-8" ) + "&gender="+URLEncoder.encode( Cgender.getText().toString().trim(), "UTF-8" ) + "&address="+URLEncoder.encode( Talamat.getText().toString().trim(), "UTF-8" );    } catch ( java.io.UnsupportedEncodingException ex) { android.util.Log.e("oncharset: ", "error: " + ex.toString() );}
 android.util.Log.e("Ftambah_pelanggan: ", "8" );
        return true;
    }

    @Override void after_save_succeed( int id ) {
android.util.Log.e("after_save_succeed: ", " 0");
        if( !( parent instanceof Ftransaksi ) ) return;
android.util.Log.e("after_save_succeed: ", " 1");
        super.close();    //langsung keluar klo ada barcode_init heeh heee
android.util.Log.e("after_save_succeed: ", " 2");
        //Ftransaksi.form.refresh_agn();
        //merely additem to Ccode_agn and Cname_agn    //refresh_agn(barcode_init);
        Ftransaksi.form.Cname_agent.items.add( new jcdb_item( id, Tname.getText().toString() ) );    //i+1    //        ((android.widget.ArrayAdapter)Ftransaksi.form.Cname_agent.getAdapter()).add( Tname.getText().toString() );
android.util.Log.e("after_save_succeed: ", " 3");
        String code = String.valueOf(id);    //harusnya barcode
android.util.Log.e("after_save_succeed: ", " 4");
        Ftransaksi.form.Ccode_agent.items.add( new jcdb_item( id, code ) );    //i+1
android.util.Log.e("after_save_succeed: ", " 5");

        ((android.widget.ArrayAdapter)Ftransaksi.form.Cname_agent.getAdapter()).notifyDataSetChanged();
android.util.Log.e("after_save_succeed: ", " 5");
    ((android.widget.ArrayAdapter)Ftransaksi.form.Ccode_agent.getAdapter()).notifyDataSetChanged();
android.util.Log.e("after_save_succeed: ", " 6");
//android.util.Log.e("ongetagent:", " 42 name_agent.getCount()" + Cname_agent.getAdapter().getCount() + " name_agent.items.getCount()" + Cname_agent.getAdapter().getCount()  + " name_agent.getAdapter().getCount()" + Cname_agent.getAdapter().getCount() );
        //Ftransaksi.form.Cname_agent.setListSelection( ((android.widget.Adapter)Ftransaksi.form.Cname_agent.getAdapter()).getCount()-1 );
        Ftransaksi.form.Cname_agent.setText( Tname.getText().toString() );
android.util.Log.e("after_save_succeed: ", " 7");
        Ftransaksi.form.Ccode_agent.setText( String.valueOf(id) );
android.util.Log.e("after_save_succeed: ", " 8");
    }

}