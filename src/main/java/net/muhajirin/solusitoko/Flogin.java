/*
 * for client auth and user auth
 *
 */

package net.muhajirin.solusitoko;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
import android.view.ViewGroup;
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
import android.text.InputType;

public class Flogin extends DialogFragment {
    EditText Tuser;
    EditText Ppassword;
    AppCompatButton Blogin;
    static Flogin form;
    java.util.Map<String, String> cfg;
    Boolean client_token_is_exist;
    public Flogin() {}
    public static Flogin newInstance(String title) {
        form = new Flogin();
        form.setStyle( DialogFragment.STYLE_NORMAL, R.style.flogin );
        form.setCancelable(true);
        return form;
    }

    @Override public void onCancel( android.content.DialogInterface dialog ) {
        System.exit(0);    //close app
    };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayoutCompat  lview = new LinearLayoutCompat(this.getActivity());
        ViewGroup view = (ViewGroup) lview;
        LinearLayoutCompat  panel = new LinearLayoutCompat(this.getActivity());
        panel.setOrientation(LinearLayoutCompat.VERTICAL);

        cfg = retail.db.cfg;    //hanya alias aja biar ga kepanjangan
        client_token_is_exist = cfg.get("client_token").trim().length()>0;    //jika belum punya client_token di file konfigurasi.txt, tampilkan interface utk client auth

        Tuser = new EditText(this.getActivity());
        Tuser.setHint( client_token_is_exist ? "Email": "Client Name" );
        Tuser.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL );
        Tuser.setText( client_token_is_exist ? cfg.get("user_login_default") : "client_a", TextView.BufferType.EDITABLE );
        TextInputLayout TL = new TextInputLayout(this.getActivity());
        TL.addView( Tuser, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
        LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        prms.setMargins( 10, 5, 10, 0 );    panel.addView( TL, prms );

        Ppassword = new EditText(this.getActivity());
        Ppassword.setHint( client_token_is_exist ? "Password" : "Api Key" );
        Ppassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        try{ Ppassword.setText( client_token_is_exist ? retail.db.decrypt( cfg.get("password_login_default"), retail.db.encryptionKey ) : "abc123" , TextView.BufferType.EDITABLE ); } catch (Exception e) {};    //Ppassword.setText( (CharSequence)"admin", TextView.BufferType.EDITABLE );
        TL = new TextInputLayout(this.getActivity());
        TL.addView( Ppassword, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
        panel.addView( TL, prms );

        final AppCompatButton Bdatabase    = new AppCompatButton(this.getActivity());
        Bdatabase.setText( "Setting" );
        Bdatabase.setHint( "Silahkan klik untuk mengubah setting!" );
        prms = new LayoutParams( (int) (100 *retail.scale_width ), (int) (80 *retail.scale_height ) );
        prms.setMargins( 10, 7, 10, 0 );
        prms.gravity = Gravity.RIGHT;    panel.addView( Bdatabase, prms );

        Blogin    = new AppCompatButton(this.getActivity());
        Blogin.setText( "Login" );
        Blogin.setBackgroundColor(0x5500ff00);
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        prms.setMargins( 10, 25, 10, 0 );    panel.addView( Blogin, prms );

        ImageView img = new ImageView(this.getActivity());
        img.setImageResource( R.drawable.a7_48x48 );
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        prms.setMargins(0, 20, 0, 0);
        prms.gravity = Gravity.CENTER_HORIZONTAL;    panel.addView( img, prms );

        if(      cfg.get("user_login_default").equals("") ) Tuser.requestFocus();
        else if( cfg.get("password_login_default").equals("") ) Ppassword.requestFocus();
        else Blogin.requestFocus();     //agar softkey does not showup :p

        //the events ....

        Blogin.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
            if( retail.empty( new EditText[]{ Tuser, Ppassword } )) return;
            Blogin.setEnabled(false);
            String post_data_ = "";
            //try { post_data_ = java.net.URLEncoder.encode( client_token_is_exist?"email":"client_name", "UTF-8" ) + "=" +java.net.URLEncoder.encode( Tuser.getText().toString(), "UTF-8" ) + "&" + java.net.URLEncoder.encode( client_token_is_exist?"password":"api_key", "UTF-8" ) + "=" + java.net.URLEncoder.encode( Ppassword.getText().toString() , "UTF-8" );
            //  } catch ( java.io.UnsupportedEncodingException ex) { android.util.Log.e("oncharset: ", "error: " + ex.toString() );}
            post_data_ = ( client_token_is_exist?"email":"client_name" ) + "=" +Tuser.getText().toString()+ "&" + ( client_token_is_exist?"password":"api_key" ) + "=" + Ppassword.getText().toString();
            //try { post_data_ = ( client_token_is_exist?"email":"client_name" ) + "=" +Tuser.getText().toString()+ "&" + ( client_token_is_exist?"password":"api_key" ) + "=" + retail.db.encrypt( Ppassword.getText().toString().toCharArray(), retail.db.encryptionKey );  } catch (Exception ex) { retail.show_error( "\n error encrypt" + ex.toString() + "\n\n\n\n", "JSONException" ); }
            final String post_data = post_data_ ;
            new DownloadJSON(){
                @Override protected void onPostExecute( String result ) {
                    super.onPostExecute(result);
                    if( result.startsWith( "Error:" ) ) {
                        Blogin.setEnabled(true);
                        return;
                    }
                    String token="";
                    try {
                        org.json.JSONObject json_data = new org.json.JSONObject(result);
                        if( json_data.has( client_token_is_exist?"access_token":"client_token" ) ) {
                            token = json_data.getString( client_token_is_exist?"access_token":"client_token" );    //{"client_label":"Client A","client_token":"bv8bmzhhPBYZTOppVqREIzT-GgZi8pAU"}
                            retail.nama = json_data.getString( client_token_is_exist?"user_name":"client_label");
                        } else {  //others like 'Bad Request'
                            retail.show_error( "\n" + json_data.getString("message") + "\n\n\n\n", "Koneksi Gagal" );
                            Blogin.setEnabled(true);
                            return;
                        }
                    } catch( org.json.JSONException e ) {
                        android.util.Log.e("flogin error: ", "e.toString()="+ e.toString() );
                        //retail.show_error( "\n" + e.toString() + "\n\n\n\n", "JSONException" );
                    }
                    if( token.length()>0 ) cfg.put( client_token_is_exist ? "access_token" : "client_token", token );

                    if( client_token_is_exist ) {
                        retail.hak_akses = "'Tambah Barang', 'Edit Barang', 'Tambah Pelanggan', 'Penjualan', 'Laporan Penjualan', 'Default Tombol Simpan di Dialog Kembali', 'Otomatis Print/Simpan di Dialog Kembali'" ;    //may got from the db
                        retail.setting.put( "Maximum Autocomplete Ribuan", "300" );
                        retail.setting.put( "Buka Faktur Baru Setelah Simpan", "ya" );
                        retail.setting.put( "Aktifkan Print Ulang Transaksi", "ya" );
                        retail.setting.put( "Aktifkan Edit Rupiah Potongan", "ya" );
                        retail.setting.put( "Prosentase PPN", "10" );

                        //retail.play_wav("sound/plunger.wav");  //yippee.wav   Door Unlock-SoundBible.com-1558114225.wav //
                        setShowsDialog( false );    //setVisible(false);    //supaya ga saru:)
                        retail.after_login(true);
                        dismiss();    //dispose();
                    } else {    //save client_token to konfigurasi.txt to use later on app next start
                        java.io.File file = new java.io.File( cfg.get("file_konfigurasi") );
                        StringBuilder file_content = new StringBuilder();
                        String line = null;
                        try {
                            String newline = "\r\n"; //"\\n";  //kok unix version semua :p >> java.lang.Character newline = java.lang.Character.LINE_SEPARATOR;    //System.lineSeparator()
                            java.io.BufferedReader br = new java.io.BufferedReader( new java.io.FileReader(file) );    //read all file konfigurasi.txt lines
                            while ((line = br.readLine()) != null) {
                                line = line.trim();    //.replaceAll("[ \x0B\f\r]","")
                                if( line.toLowerCase().startsWith("client token") ) line = "Client Token    : " + token ;    //modify only Client Token line
                                file_content.append( line + newline );
                            }
                            br.close();
                            java.io.FileWriter writer = new java.io.FileWriter( cfg.get("file_konfigurasi") ); // burn file_content to file konfigurasi.txt
                            writer.write( file_content.toString() );    writer.flush();    writer.close();
                            android.widget.Toast.makeText( form.getActivity(), "Client Authentication Succeed!", android.widget.Toast.LENGTH_LONG).show();

                            //just change the UI :D for user auth
                            client_token_is_exist = true;
                            ((android.support.design.widget.TextInputLayout)Tuser.getParent()).setHint("Email");
                            ((android.support.design.widget.TextInputLayout)Ppassword.getParent()).setHint("Password");
                            Tuser.setText( cfg.get("user_login_default"), TextView.BufferType.EDITABLE );
                            Ppassword.setText( "tazkiyah499", TextView.BufferType.EDITABLE );
                            Blogin.setEnabled(true);
//Blogin.performClick();

                        } catch (Exception e) {
                            retail.show_error( "\n" + "Modifikasi File '" +cfg.get("file_konfigurasi")+ "' Gagal!\n" + e.getMessage() + "\n\n\n\n", "Gagal Simpan Client Token" );
                        }
                    }
                }
            }.execute(
                       cfg.get( client_token_is_exist ? "url_user_login" : "url_client_login" )    //url to call
                     //, ( client_token_is_exist?cfg.get("client_token"):"" )    //auth header to send
                     , post_data    //post params
                     );    //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // we target SDK > API 11

        }});

        Bdatabase.setOnClickListener(new View.OnClickListener() {    @Override public void onClick(View v) {
                //open Fconfig to save db config
                Fconfig.newInstance(retail.app_name + "- Konfigurasi").show(retail.fm, "Fconfig");
        }});


//Blogin.performClick();

        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        prms.gravity = Gravity.CENTER_VERTICAL;
        view.addView( panel, prms );
        return (View) view;    //return inflater.inflate(R.layout.flogin, container, false);    //return inflater.inflate(R.layout.dialog, container);
    }



}
