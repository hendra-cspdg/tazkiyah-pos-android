/*
 * main form ...
 *
 */


package net.muhajirin.solusitoko;


import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Gravity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.widget.RelativeLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.View.OnClickListener;
import android.graphics.Point;
import android.widget.EditText;
import android.view.Menu;
import android.view.SubMenu;
import android.view.MenuItem;

import java.util.*;

public class retail extends AppCompatActivity {
    static String file_konfigurasi = "konfigurasi.txt";
    static android.content.Context base_context;
    static android.content.Context get_my_app_context() {
        return base_context;
    }
    static AppCompatActivity base_activity;
    static AppCompatActivity get_my_app_activity() {
        return base_activity;
    }


    static int modal_result;    //for forms to get showmodal :p

    static java.text.DecimalFormat numeric_format;

    static Boolean scan_is_init = false, scan_cancelled = false;    //agar saat user back dari scan activity, tidak manggil back dari form utamanya :p
    static Intent scan_intent;
    public static void scan_init() {
        Log.e("before : ", " scan_init ");
        if( scan_is_init ) return;
        scan_is_init = true;
        scan_intent = new Intent( "com.google.zxing.client.android.SCAN" );
        scan_intent.setPackage( "net.muhajirin.solusitoko" );
        scan_intent.putExtra("com.google.zxing.client.android.SAVE_HISTORY", false);//this stops saving ur barcode in barcode scanner app's history
        //?scan_intent.putExtra( "com.google.zxing.client.android.SCAN.SCAN_FORMATS", "PRODUCT_MODE,CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF");
        //scan_intent.putExtra( "com.google.zxing.client.android.SCAN.SCAN_MODE", "PRODUCT_MODE" );    //"QR_CODE_MODE"    //SCAN_MODE as in com.google.zxing.client.android.intens.java
        scan_intent.putExtra( "com.google.zxing.client.android.SCAN.WIDTH" , (int) (screen_width * 30/100) );
        scan_intent.putExtra( "com.google.zxing.client.android.SCAN.HEIGHT", (int) (screen_height * 30/100) );
        scan_intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    }
    public static void scan( /*int view_id*/ ) {
        scan_init();
        Log.e("before : ", "scan");
        //scan_intent.putExtra( target, view_id );    //"QR_CODE_MODE"    //SCAN_MODE as in com.google.zxing.client.android.intens.java
        get_my_app_activity().startActivityForResult( scan_intent, 0 );
        Log.e("after : ", "scan");
    }
    static String add_ribuan( String str ) {
        if( !is_number(str) ) return str;
        str = str.replace(digit_separator,"").trim();
        int num = Integer.valueOf(str);
        if( num>0 && num < convert_number( retail.setting.get("Maximum Autocomplete Ribuan"), 0 ) ) str += "000";
        return str;
    };
    static View.OnFocusChangeListener add_ribuan_when_lost_focus = new View.OnFocusChangeListener() { @Override public void onFocusChange( View v, boolean hasFocus ) {
        Log.e("add_ribuan:", "hasFocus:"+ hasFocus );    //ingat: lost focus terjadi setelah onclick di cell lain!!!
        if( hasFocus ) return;
        Log.e("add_ribuan:", "2" );
        String s_old = ((TextView)v).getText().toString().replace(digit_separator,"").trim();
        Log.e("add_ribuan:", "3 s_old:"+ s_old );
        String s_new = add_ribuan(s_old);
        Log.e("add_ribuan:", "3 s_new:"+ s_new );
        if( s_old.length() != s_new.length() ) ((TextView)v).setText(s_new);
        Log.e("add_ribuan:", "last");
    }};

    static TextView barcode_target;
    static View.OnClickListener scan_listener = new View.OnClickListener() { @Override public void onClick(View v) {    //on doubleclick :p
        Log.e("listener", "scan");
        barcode_target = (TextView) v;
        //luweh, toh dia masi harus mengarahkan barcode ke tengah camera hp >> if( barcode_target.getText().toString().equals("") ) {
        if( v.getTag() instanceof Object && System.currentTimeMillis() - (long)v.getTag() < 1000 ) scan();
        Log.e("after", "scan");
        v.setTag(System.currentTimeMillis());
    }};
    static Boolean in_progress = false;
    static Boolean result_delayed = false;
    static int delayed_requestCode, delayed_resultCode;
    static Intent delayed_intent;
    @Override public void onActivityResult( final int requestCode, final int resultCode, final Intent intent ) {
        Log.e("requestCode", "requestCode=" + requestCode + "   resultCode=" + resultCode+ "   result_delayed=" + result_delayed) ;
        if( !result_delayed ) super.onActivityResult( requestCode, resultCode, intent );    //agar fragment2 lain seperti Flogin dapat terima activityresult juga
        if( requestCode == 0 ) {
            try {
                if( resultCode == RESULT_OK ){
                    Log.e("requestCode", "ok first" ) ;
                    final String ret0 = intent.getStringExtra("SCAN_RESULT").trim();
                    Log.e("requestCode", "1" ) ;
                    final String barcode_remove_chars_before = "08" ;    //barcode kartu perdana mengandung karakter2 yg ga dipakai sebelum nomor hp.
                    Log.e("requestCode", "2" ) ;
                    String ret_ = ret0;
                    Log.e("requestCode", "3" ) ;
                    if( barcode_remove_chars_before.length()>0 && ret_.length()>12 ) {
                        int pos = ret_.substring( 0, ret_.length()-10 ).lastIndexOf( barcode_remove_chars_before );
                        if( pos>0 ) ret_ = ret_.substring( pos );
                    }
                    Log.e("requestCode", "4" ) ;
                    final String ret = ret_ ;
                    Boolean continues = false, valid = false;    final TextView barcode_target = retail.barcode_target;    //krn nanti akan direset oleh form.tambah_brg(ret)
                    if( barcode_target.getTag() instanceof Object[] ) {
                        Log.e("requestCode", "5" ) ;
                        final Object[] tag = (Object[]) barcode_target.getTag();
                        pending_error = -1;
                        Log.e("requestCode", "6 " ) ;
                        if( tag[0] instanceof JCdb && ((JCdb)tag[0]).getOnItemClickListener()==retail.sync_brg ) {
                            Log.e("onActivityResult", "1 tag[0] instanceof JCdb");
                            int valid_index = ((JCdb)tag[0]).my_index_of(ret);
                            Log.e("onActivityResult", "2");
                            final Fedit form = Fedit.form;    //Fpenjualan.form;    // = fm.findFragmentByTag("Fpenjualan")
                            if( valid_index<0 ) {    //jika barang tidak ditemukan, langsung tampilkan form tambah barang
                                Log.e("onActivityResult", "3 valid_index=" + valid_index);
                                if( retail.hak_akses.indexOf("'Tambah Barang di Fitur Transaksi'") >= 0 && ( form!=null ) ) {
                                    Log.e("onActivityResult", "7");
                                    form.view.post(new Runnable() { @Override public void run() {    //this take my 2 days!!!!
                                        ((Ftransaksi)form).tambah_brg(ret);    //try { form.getClass().getMethod("tambah_brg", String.class ).invoke(form, ret); } catch ( Exception e1)   {  Log.e("onActivityResult", "Gagal invoke method tambah barang.\n. Pesan Kesalahan: " + e1 );  }    //Item Baru
                                    }});
                                    pending_error--;
                                }
                            } else {
                                final int valid_index_f = valid_index;
                                Log.e("onActivityResult", "66");
                                _sync_brg( (JCdb)tag[0], valid_index_f );
                                form.view.post(new Runnable() { @Override public void run() {
                                    Log.e("onActivityResult", "before setValueAt");
                                    ((db_connection)tag[1]).setValueAt( false, ret, (Integer) tag[2], barcode_target.getId() );    //pengganti remove_editor
                                }});
                                valid = false;    continues = true;
                            }
                        } else
                            valid = true;

                        if( pending_error == -1 && valid ) {
                            ((db_connection)tag[1]).setValueAt( ret, (Integer) tag[2], barcode_target.getId() );
                            barcode_target.requestFocus();    //otherwise, the focus back to last focused view (may my searchview)
                        }
                        pending_error++;
                    } else
                        barcode_target.setText( barcode_target.getText() + ret );

                    Log.e("onActivityResult", "contents: "+intent.getStringExtra("SCAN_RESULT")); // Handle successful scan

                } else if( resultCode == RESULT_CANCELED ) {
                    Log.e("xZing", " Cancelled dan entah kenapa zxing ngirim RESULT_CANCELED dulu sebelum RESULT_OK, sehingga show_soft_keyboard di bawah ini bikin hang!!!"); 
                    //take my 2 days!!! >>> if( ! ( barcode_target instanceof JCdb ) ) show_soft_keyboard( (View)barcode_target, (android.app.Activity) this );
                    scan_cancelled = true;
                }

            } catch (Exception e) {
                Log.e("RETAIL: ", "error: " + "RETAIL:" + e.getMessage() );
                show_error( "\nMaaf, \n" + e.getMessage(), "eee" );
            }
        }
    }

    public static void show_soft_keyboard( final View v, final android.app.Activity activity) {
        new android.os.Handler().postDelayed(new Runnable() { public void run() {
            v.requestFocus();    //agar bisa diketik manual oleh user    //agar soft keyboard muncul :p
            ( (android.view.inputmethod.InputMethodManager) activity.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) ).showSoftInput(v, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                    //or this >> getWindow().setSoftInputMode( android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE );
                    //di bawah ini mensimulasikan touch agar soft keyboard muncul, tapi justru manggil ontouchlistener gua :p
                    //barcode_target.dispatchTouchEvent( android.view.MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), android.view.MotionEvent.ACTION_DOWN , 0, 0, 0));    //klo gagal, coba SystemClock.uptimeMillis()
                    //barcode_target.dispatchTouchEvent( android.view.MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), android.view.MotionEvent.ACTION_UP , 0, 0, 0));
        }}, 200);
    }

    static Point getDisplaySize( android.view.Display display ) {    //dipakai di my dialog class, dll.
        Point point = new Point();
        if( android.os.Build.VERSION.SDK_INT >= 13 ) {
            display.getSize(point);
        } else { // Older device ... This method was deprecated in API level 13.
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    static android.content.res.AssetManager assetManager;
    static FragmentManager fm;
    static int pending_error = 0;    //dari onactivityresult bikin aplikasi stop responding :(
    static String pending_msg, pending_title;
    static void show_error( String msg, String title ) {
        if( pending_error==-1 ) {
            pending_msg = msg;
            pending_title = title;
            pending_error++;    //supaya tetap bernilai 1 setelah dinaikan nanti setelah eksekusi ke db oleh onactivityresult
            return;
        }
        if( pending_error>0 ) pending_error=0;
        alert.newInstance(msg, title, new String[]{"Oke"} ).show(fm, "alert");
    }
    static int show_confirm( String msg, String title ) {
        return alert.newInstance( msg, title, new String[]{"Ya", "Tidak", "Batal"} ).show_modal( fm, "alert" );
    }
    static int show_confirm2( String msg, String title ) {
        return alert.newInstance( msg, title, new String[]{"Ya", "Tidak"} ).show_modal( fm, "alert" );
    }

    static db_connection db;    //krn aplikasi ini bukan aplikasi multiform, maka hanya akan ada satu object db_connection ini.
    static LinearLayoutCompat p_wrap_wrap;

    static int screen_width, screen_height;
    static float scale_width, scale_height;
    static int backup_SCREEN_ORIENTATION;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) return;    // However, if we're being restored from a previous state, then we don't need to do anything and should return or else we could end up with overlapping fragments.

        ExceptionHandler.registerExceptionHandler();

        keepWiFiOn( this, true );
        Log.e("tag", "after wifi") ;

        numeric_format = (java.text.DecimalFormat) java.text.NumberFormat.getInstance(java.util.Locale.US);
        numeric_format.applyPattern("#,###,###,###");

        android.graphics.Point screen_size = retail.getDisplaySize( getWindow().getWindowManager().getDefaultDisplay() );
        if( screen_size.x < screen_size.y ) {    //it's currently in portrait mode
            screen_width = screen_size.x;    screen_height = screen_size.y;
        } else {
            screen_width = screen_size.y;    screen_height = screen_size.x;
        }

        scale_width = 1 + (float)(screen_width-320)/(320*2);   scale_height = 1 + (float)(screen_height-480)/(480*2);
        Log.e("scale_width=", ""+scale_width);

        assetManager = getAssets();
        fm = getSupportFragmentManager();

        base_context = this;    //getApplicationContext();
        base_activity = this;

        backup_SCREEN_ORIENTATION = this.getRequestedOrientation();

        String dst_dir = getFilesDir().getAbsolutePath() ;
        if( dst_dir.endsWith("/files") ) dst_dir = dst_dir.substring( 0, dst_dir.length() - "/files".length() );

        copy_asset_dir( file_konfigurasi, dst_dir ) ;
        file_konfigurasi = dst_dir + java.io.File.separator + file_konfigurasi;

        Locale.setDefault( new Locale( "in", "ID" ) );    //Locale.setDefault(Locale.ITALIAN);    //in_ID    //Locale.setDefault(Locale.ITALIAN);    //in_ID

        digit_separator=".";    //otherwise >> java.lang.NoSuchMethodError : java.text.DecimalFormatSymbols.getInstance()
        decimal_separator = ",";

        if( BuildConfig.VERSION_CODE > 9 ) android.os.StrictMode.setThreadPolicy( new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build() );    //allowing network access in the user interface thread

        db = new db_connection();
        //play_wav("sound/nicetry.wav");  //sfwarb.wav  yippee.wav  ak47.wav

        if( new Date().before( java.sql.Date.valueOf("2017-01-01") ) )  {
            show_error( "Mohon perbaiki dulu tanggal di device ini!\nTerima kasih.\nAplikasi ini akan ditutup.", "Tanggal Salah" );
            System.exit(0);    //f.dispose();
            return;
        }

        p_wrap_wrap = new LinearLayoutCompat(this);    p_wrap_wrap.setOrientation(LinearLayoutCompat.VERTICAL);    //JPanel p_wrap_wrap = new JPanel();    p_wrap_wrap.setLayout(null);
        setContentView( p_wrap_wrap, new LinearLayoutCompat.LayoutParams( LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT ) );    //f.getContentPane().add(p_wrap_wrap);

        //creating menu_panel
        LinearLayoutCompat menu_panel = new LinearLayoutCompat(this);
        menu_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);
        LayoutParams prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        p_wrap_wrap.addView( menu_panel, prms );

        RelativeLayout toolbar_panel = new RelativeLayout(this);
        //RelativeLayout toolbar_panel = (RelativeLayout) getLayoutInflater().inflate(R.layout.toolbar, p_wrap_wrap, false);
        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
        menu_panel.addView( toolbar_panel, prms );

        android.support.v7.widget.Toolbar toolbar = new android.support.v7.widget.Toolbar(this);
        toolbar.setTitle( app_name.trim() );
        //theme ga bisa diset dari program >> toolbar.setStyle( android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar );
        toolbar.setPopupTheme( android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Light );

        menuBar = toolbar.getMenu();

        RelativeLayout.LayoutParams prms1 = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
        prms1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        toolbar_panel.addView( toolbar, prms1 );

        try {
            java.io.File httpCacheDir = new java.io.File( getCacheDir(), "http" );
            long httpCacheSize = 10 * 1024 * 1024;    // 10 MiB
            android.net.http.HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch( java.io.IOException e ) {
            Log.e("retail", "HTTP response cache installation failed:" + e);
        }

        Flogin.newInstance( app_name + "- LOGIN" ).show(fm, "Flogin");    //show login form
    }

    static Menu menuBar;

    public static void after_login(Boolean logged_in) {    //called by Flogin
        if( nama.equals("") || !logged_in ) {    //gara2 user tekan keluar di form login....
            System.exit(0);    //f.dispose();
            return;
        }
        //by rafraf, grant permission    //since marshmallow
        if( android.os.Build.VERSION.SDK_INT >= 23 && android.support.v4.content.ContextCompat.checkSelfPermission( get_my_app_activity(), android.Manifest.permission.CAMERA ) != android.content.pm.PackageManager.PERMISSION_GRANTED ) {
            android.support.v4.app.ActivityCompat.requestPermissions( get_my_app_activity(), new String[]{ android.Manifest.permission.CAMERA }, 0 );
        }
        createMenuBar();
    }

    public static void createMenuBar() {
        int i=100;    //menu-id to referred by Bmenu

        LinearLayoutCompat.LayoutParams params_submenu = new LinearLayoutCompat.LayoutParams( LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT );
        //params_submenu.gravity = Gravity.CENTER;

        LinearLayoutCompat.LayoutParams params_Bmenu = new LinearLayoutCompat.LayoutParams( (int) (120 *retail.scale_width), (int) (120*retail.scale_width) );    //setPreferredSize(new Dimension(115,115));
        params_Bmenu.setMargins( 10, 10, 10, 0 );

        //JMenuBar menuBar;
        //menuBar = new JMenuBar();

        JMenu menu, submenu;
        JMenuItem menuItem;
        JP_menu jp_menu; JP_submenu jp_submenu;  //JBmenu Bmenu;  ActionListener act;

        LinearLayoutCompat p_wrap = new LinearLayoutCompat(get_my_app_context());    p_wrap.setOrientation(LinearLayoutCompat.VERTICAL);
        //p_wrap.setBounds( 0, 0, f.getWidth(), f.getHeight() );

        menu = new JMenu("Data Master");
        //menu.setMnemonic(KeyEvent.VK_D);
        //menu.getAccessibleContext().setAccessibleDescription("Ubah Data Master ...");
        jp_menu = new JP_menu("Data Master");    JBmenu Bmenu;


        submenu = new JMenu("Barang");
        //submenu.setMnemonic(KeyEvent.VK_B);
        submenu.setIcon(R.drawable.box);    //gift.png
        jp_submenu = new JP_submenu("Barang");
        //JButton Bmenu_default=null;
        if( hak_akses.indexOf("'Tambah Barang'") >= 0 ) {
            menuItem = new JMenuItem( "Tambah Barang Baru", R.drawable.symbol_add_1, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Ftambah_barang.newInstance(app_name + "- Tambah Barang").show(fm, "Ftambah_barang");  return true; }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_T);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
            Bmenu = new JBmenu( "tambah barang", R.drawable.symbol_add_3, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});    //f.setVisible(true);  fc=null;  }});    //fc=null >> to force garbage collector?!    //"<html><center>F1<br>tambah barang"
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});    //menuItem.addActionListener(act);
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }
        if( hak_akses.indexOf("'Edit Barang'") >= 0 ) {
            menuItem = new JMenuItem("Product", R.drawable.pencil, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Fedit_barang.newInstance(app_name + "- Product").show(fm, "Fedit_barang");  return true;   }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_E);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
            Bmenu = new JBmenu( "product", R.drawable.edit_1, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }

        if( jp_submenu.getChildCount()>0 ) {  menu.add(submenu);  jp_menu.addView(jp_submenu,params_submenu);  }    //if( submenu.getItemCount()>0 ) {  menu.add(submenu);  jp_menu.add(jp_submenu);  }

        submenu = new JMenu("Customer");
        //submenu.setMnemonic(KeyEvent.VK_P);
        submenu.setIcon(R.drawable.system_users);
        jp_submenu = new JP_submenu("Pelanggan");

        if( hak_akses.indexOf("'Tambah Pelanggan'") >= 0 ) {
            menuItem = new JMenuItem("Tambah Customer Baru", R.drawable.user_group_new24, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Ftambah_pelanggan.newInstance(app_name + "- Tambah Customer").show(fm, "Ftambah_pelanggan");  return true;   }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_T);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
            //menuItem.getAccessibleContext().setAccessibleDescription("Tambah Pelanggan Baru ...");
            Bmenu = new JBmenu( "tambah customer", R.drawable.user_group_new, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }
/*
        if( hak_akses.indexOf("'Edit Pelanggan'") >= 0 ) {
            menuItem = new JMenuItem("Edit Customer", R.drawable.user_group_properties24, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Fedit_pelanggan.newInstance(app_name + "- Edit Customer").show(fm, "Fedit_pelanggan");  return true;   }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_E);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));    //kehabisan tombol ntar:) >> menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
            Bmenu = new JBmenu( "edit customer", R.drawable.user_group_properties, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);

        }
*/
        if( jp_submenu.getChildCount()>0 ) {  menu.add(submenu);  jp_menu.addView(jp_submenu,params_submenu);  }    //if( submenu.getItemCount()>0 ) {  menu.add(submenu);  jp_menu.add(jp_submenu);  }
/* 
        submenu = new JMenu("Supplier");
        //submenu.setMnemonic(KeyEvent.VK_S);
        submenu.setIcon(R.drawable.meeting_chair);
        jp_submenu = new JP_submenu("Supplier");

        if( hak_akses.indexOf("'Tambah Supplier'") >= 0 ) {
            menuItem = new JMenuItem("Tambah Supplier Baru", R.drawable.resource_group_new24, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Ftambah_supplier.newInstance(app_name + "- Tambah Supplier").show(fm, "Ftambah_supplier");  return true;   }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_T);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));    //kehabisan tombol ntar:) >> menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
            //menuItem.getAccessibleContext().setAccessibleDescription("Tambah Pemasok Baru ...");
            Bmenu = new JBmenu( "tambah supplier", R.drawable.resource_group_new, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }
        if( hak_akses.indexOf("'Edit Supplier'") >= 0 ) {
            menuItem = new JMenuItem("Edit Supplier", R.drawable.resource_group24, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Fedit_supplier.newInstance(app_name + "- Edit Supplier").show(fm, "Fedit_supplier");  return true;   }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_E);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));    //kehabisan tombol ntar:) >> menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
            Bmenu = new JBmenu( "edit supplier", R.drawable.resource_group, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            submenu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }

        if( jp_submenu.getChildCount()>0 ) {  menu.add(submenu);  jp_menu.addView(jp_submenu,params_submenu);  }    //if( submenu.getItemCount()>0 ) {  menu.add(submenu);  jp_menu.add(jp_submenu);  }

*/


        if( jp_menu.getChildCount()>0 ) {  menu.add_to(menuBar);    /*menuBar.add(menu);*/  p_wrap.addView(jp_menu,params_submenu);  }    //if( menu.getItemCount()>0 ) {  menuBar.add(menu);  p_wrap.add(jp_menu);  }



        menu = new JMenu("Transaksi");
        //menu.setMnemonic(KeyEvent.VK_T);
        //menu.getAccessibleContext().setAccessibleDescription("Transaksi ...");
        jp_submenu = new JP_submenu("Transaksi");

        if( hak_akses.indexOf("'Penjualan'") >= 0 ) {
            menuItem = new JMenuItem( "Order", R.drawable.full_cart24, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Fpenjualan.newInstance(app_name + "- Order").show(fm, "Fpenjualan");  return true; }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_J);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));    //30143_72806_cart_ecommerce_shopping.png
            Bmenu = new JBmenu( "order", R.drawable.full_cart48, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});    //f.setVisible(true);  fc=null;  }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            menu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
            //Bmenu_default=(JButton)Bmenu;
        }

/*
        if( hak_akses.indexOf("'Retur Penjualan'") >= 0 ) {
            menuItem = new JMenuItem("Retur Penjualan", R.drawable.view_loan_asset24));
            menuItem.setMnemonic(KeyEvent.VK_R);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
            Bmenu = new JBmenu( "<html><center>F6 - Retur Penjualan", R.drawable.view_loan_asset, new ActionListener() { public void actionPerformed(ActionEvent e) {     Fretur_penjualan fc = new Fretur_penjualan(f);   f.setVisible(true);  fc=null;  }});
            menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            menu.add(menuItem);    jp_submenu.add(Bmenu);
        }
 
        if( hak_akses.indexOf("'Pembelian'") >= 0 ) {
            menuItem = new JMenuItem( "Pembelian", R.drawable.custom_icon_60c66883_1800_4b1a_8384_08263d221672, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {    Fpembelian.newInstance(app_name + "- Pembelian").show(fm, "Fpembelian");  return true; }}, i++ );    final int id = i-1;
            //menuItem.setMnemonic(KeyEvent.VK_B);
            //menuItem.setAccelerator(KeyStroke.getKeyStroke("F7"));               //1400707795_Cart_by_Artdesigner.lv.png    30147_72824_delivery_transportation_truck.png
            Bmenu = new JBmenu( "pembelian", R.drawable.a30145_72816_cash_checkout_money, new OnClickListener() { @Override public void onClick(View v) {    menuBar.performIdentifierAction(id, 0);    }});    //f.setVisible(true);  fc=null;  }});
            //menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            menu.add(menuItem);
            jp_submenu.addView(Bmenu,params_Bmenu);
        }

        if( hak_akses.indexOf("'Retur Pembelian'") >= 0 ) {
            menuItem = new JMenuItem("Retur Pembelian", R.drawable.view_loan24));
            menuItem.setMnemonic(KeyEvent.VK_T);
            menuItem.setAccelerator(KeyStroke.getKeyStroke("F8"));
            Bmenu = new JBmenu( "<html><center>F8 - Retur Pembelian", R.drawable.view_loan, new ActionListener() { public void actionPerformed(ActionEvent e) {     Fretur_pembelian fc = new Fretur_pembelian(f);   f.setVisible(true);  fc=null;  }});
            menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {  Bmenu.doClick();  }});
            menu.add(menuItem);    jp_submenu.add(Bmenu);
        }
*/

        //if( menu.getItemCount()>0 ) {  menuBar.add(menu);  jp_menu.add(jp_submenu);  }    //p_wrap.add(jp_submenu);
        if( jp_submenu.getChildCount()>0 ) {  menu.add_to(menuBar);    /*menuBar.add(menu);*/  p_wrap.addView(jp_submenu,params_submenu);  }    //if( menu.getItemCount()>0 ) {  menuBar.add(menu);  p_wrap.add(jp_submenu);  }



/*

        menu = new JMenu("Pengaturan");
        menu.setMnemonic(KeyEvent.VK_P);
        menuBar.add(menu);
  
        menuItem = new JMenuItem("Page Setup", R.drawable.zoom_fit_best));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));

        menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            pageFormat = pj.pageDialog(pageFormat);
        }});
        menu.add(menuItem);

        menuItem = new JMenuItem("Ganti Password", R.drawable.preferences_desktop_user_password));
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            Fganti_password fc = new Fganti_password(f);   f.setVisible(true);  fc=null;
        }});
        menu.add(menuItem);

        if( hak_akses.indexOf("'Setting Absensi'") >= 0 ) {
          menuItem = new JMenuItem("Absensi", R.drawable.user_group_properties24));
          menuItem.setMnemonic(KeyEvent.VK_A);
          menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
          menuItem.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
              Fedit_absensi_setting fc = new Fedit_absensi_setting(f);   f.setVisible(true);  fc=null;
          }});
          menu.add(menuItem);
        }

        submenu = new JMenu("User");
        submenu.setMnemonic(KeyEvent.VK_U);
        submenu.setIcon(R.drawable.user6));

        if( hak_akses.indexOf("'Tambah User'") >= 0 ) {
            menuItem = new JMenuItem("Tambah User Baru", R.drawable.user6_add));
            menuItem.setMnemonic(KeyEvent.VK_T);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
            submenu.add(menuItem);
            ActionListener tambah_user = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Ftambah_user fc = new Ftambah_user(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(tambah_user);
        }
        if( hak_akses.indexOf("'Edit User'") >= 0 ) {
            menuItem = new JMenuItem("Edit User", R.drawable.user6_edit));
            menuItem.setMnemonic(KeyEvent.VK_E);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
            submenu.add(menuItem);
            ActionListener edit_user = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Fedit_user fc = new Fedit_user(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(edit_user);
        }
        if( submenu.getItemCount()>0 ) menu.add(submenu);
 
        submenu = new JMenu("Grup User");
        submenu.setMnemonic(KeyEvent.VK_G);
        submenu.setIcon(R.drawable.user_grup7));

        if( hak_akses.indexOf("'Tambah Grup User'") >= 0 ) {
            menuItem = new JMenuItem("Tambah Grup Baru", R.drawable.user_grup7_add));
            menuItem.setMnemonic(KeyEvent.VK_T);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, ActionEvent.ALT_MASK));
            submenu.add(menuItem);
            ActionListener tambah_grup = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Ftambah_grup fc = new Ftambah_grup(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(tambah_grup);
        }
        if( hak_akses.indexOf("'Edit Grup User'") >= 0 ) {
            menuItem = new JMenuItem("Edit Grup", R.drawable.user_grup7_edit));
            menuItem.setMnemonic(KeyEvent.VK_E);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, ActionEvent.ALT_MASK));
            submenu.add(menuItem);
            ActionListener edit_grup = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Fedit_grup fc = new Fedit_grup(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(edit_grup);
        }
        if( submenu.getItemCount()>0 ) menu.add(submenu);
 

        submenu = new JMenu("Modul");
        submenu.setMnemonic(KeyEvent.VK_M);
        submenu.setIcon(R.drawable.folder_development));

        if( hak_akses.indexOf("'Tambah Modul'") >= 0 ) {
            menuItem = new JMenuItem("Tambah Modul Baru", R.drawable.folder_new));
            menuItem.setMnemonic(KeyEvent.VK_T);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
            submenu.add(menuItem);
            ActionListener tambah_modul = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Ftambah_modul fc = new Ftambah_modul(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(tambah_modul);
        }
        if( hak_akses.indexOf("'Edit Modul'") >= 0 ) {
            menuItem = new JMenuItem("Edit Modul", R.drawable.folder_txt));
            menuItem.setMnemonic(KeyEvent.VK_E);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
            submenu.add(menuItem);
            ActionListener edit_modul = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Fedit_modul fc = new Fedit_modul(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(edit_modul);
        }
        if( submenu.getItemCount()>0 ) menu.add(submenu);
 

        submenu = new JMenu("Hak Akses Modul");
        submenu.setMnemonic(KeyEvent.VK_H);
        submenu.setIcon(R.drawable.view_time_schedule_baselined));

        if( hak_akses.indexOf("'Tambah Hak Akses Modul'") >= 0 ) {
            menuItem = new JMenuItem("Tambah Hak Akses Modul", R.drawable.view_time_schedule_baselined_add));
            menuItem.setMnemonic(KeyEvent.VK_T);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
            submenu.add(menuItem);
            ActionListener tambah_grup_modul = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Ftambah_grup_modul fc = new Ftambah_grup_modul(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(tambah_grup_modul);
        }
        if( hak_akses.indexOf("'Edit Hak Akses Modul'") >= 0 ) {
            menuItem = new JMenuItem("Edit Hak Akses Modul", R.drawable.view_time_schedule_baselined_remove));
            menuItem.setMnemonic(KeyEvent.VK_E);
            menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
            submenu.add(menuItem);
            ActionListener edit_grup_modul = new ActionListener() { public void actionPerformed(ActionEvent e) {
                   Fedit_grup_modul fc = new Fedit_grup_modul(f);   f.setVisible(true);  fc=null;
            }};
            menuItem.addActionListener(edit_grup_modul);
        }
        if( submenu.getItemCount()>0 ) menu.add(submenu);




        menuBar.add(Box.createHorizontalGlue());
*/

        menu = new JMenu("Menu Utama");
        //menu.setMnemonic(KeyEvent.VK_U);
        //menu.getAccessibleContext().setAccessibleDescription("Menu Utama ...");

        menuItem = new JMenuItem( "Petunjuk", R.drawable.preferences_system_power_management32, new MenuItem.OnMenuItemClickListener() { @Override public boolean onMenuItemClick(MenuItem item) {
            show_error(  ""
                        +"\n.  tap dua kali pada kolom isian atau tekan tombol Perbesar Volume untuk menscan barcode barang"
                        +"\n.  setelah kamera aktif, tekan tombol Perbesar Volume jika ingin mengaktifkan flash/senter"
                        +"\n.  tap agak lama pada kolom isian untuk mengetik"
                        +"\n.  pada fitur penjualan, uang dibayar dapat diketik tanpa ribuan (dengan cara tap ke kolom lain)"
                        +"\n.  beberapa setting terkait koneksi ke database, dll dapat dilakukan melalui tombol setting"


                        +"\n\n\n"
                , "Tips penggunaan aplikasi ini ... "
            );

            return true; }}, i++ );    final int id = i-1;
        menu.add(menuItem);

        if( menu.menuItems.size()>0 ) {  menu.add_to(menuBar);    /*menuBar.add(menu);*/  }

        android.widget.HorizontalScrollView scroll_panel = new android.widget.HorizontalScrollView( get_my_app_context() );
        scroll_panel.addView( p_wrap, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
        p_wrap_wrap.addView(scroll_panel, new LinearLayoutCompat.LayoutParams( LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT ) );    //p_wrap_wrap.add(p_wrap,prms);
    }


    static void copy_asset_dir( String src, String dst_dir ) {
        src = src.startsWith(java.io.File.separator) ? src.substring(1) : src;
        java.io.InputStream in = null;    java.io.OutputStream out = null;
        try {
            String dst = dst_dir + java.io.File.separator + ( src.endsWith(".jpg") ? src.substring(0, src.length()-4) : src ) ;    //extension was added to avoid compression on APK file
            java.io.File file = new java.io.File( dst );
            if( file.exists() ) return;
            String[] srcs = assetManager.list(src);
            if( srcs.length>0 ) {    //<< tdk mo ngopy empty dir:p    //if( srcs!=null ) {    //new java.io.File( src ).isDirectory()
                if( !file.exists() && !src.startsWith("images") && !src.startsWith("sounds") && !src.startsWith("webkit") )
                    if( !file.mkdirs() ) Log.e("tag", "could not create dir " + dst ) ;    //create dirs first
                for( String src_ : srcs )
                    if( !src.startsWith("images") && !src.startsWith("sounds") && !src.startsWith("webkit") )
                        copy_asset_dir( ( src.equals("") ? "" : src+java.io.File.separator ) + src_ , dst_dir ) ;
            } else {    //it's a file
                in = assetManager.open(src);
                out = new java.io.FileOutputStream(file);    //ini ga boleh ada path ... jadi ga bisa dikontrol pathnya >> openFileOutput( dst, MODE_PRIVATE );
                byte[] buffer = new byte[1024];    //ini sama aja >> 512
                int read;
                while( (read = in.read(buffer)) != -1 )  out.write(buffer, 0, read);
                out.flush();
                //java.lang.NoSuchMethodError: java.io.File.setExecutable >> if( src.endsWith("mysqld-arm") ) file.setExecutable(true,false);    //false ini biar bisa diakses oleh bukan owner
                //show_error( "berhasil\n\n\n\n" , "Kesalahan" );
            }
        } catch( java.io.IOException ex ) {
            Log.e("tag", "I/O Exception pada src=" + src + "   "  + ex);
                    //show_error( "\nPenyimpanan file gambar gagal!\nPesan Kesalahan: " + ex + "\n\n\n\n" , "Kesalahan" );
        } finally {
            try{ if(in!=null) { in.close();in=null; } if(out!=null) { out.close();out=null; } } catch( Exception e2 ) {}
        }
    }

    @Override public void onBackPressed() {
            //just to make sure :)
            db.close(true);    db=null;
            //Ccode_brg=null;    Cname_brg=null;    harga_brg=null;    diskon_brg=null;    gambar_brg=null;
            //tbl_brg=null;
            //sync_brg=null;
            //onHover=null;
            //Brefresh=null;


        android.net.http.HttpResponseCache cache = android.net.http.HttpResponseCache.getInstalled();
        if( cache != null ) {
            if( cache.getHitCount()>0 ) android.util.Log.e("retail", "The cache is working" );
            cache.flush();    //to use next on this app start ...
        }


        //di bawah ini si katanya dah default ... tapi biar sure aja deh
        //this.finish();    //this.finishAffinity();
        System.exit(0);
finish();
    }

public static String now_from_db() {
    db.exec( "SELECT now()" );
    try { while( db.in_progress>0 ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
    String now="";
    if( !db.err_msg.equals("") )    {  show_error( "Pembacaan waktu dari db gagal!\n" + db.err_msg, "Pembacaan waktu dari db gagal" );    return now; }
    try {
        while( db.rs.next() ) {
            now = db.rs.getString(1);
            int dot_pos = now.lastIndexOf(".");    //ada .0 nya di belakang
            now = now.substring( 0, dot_pos );
        }
        db.rs.close();
    } catch (Exception e) {  show_error( "\nMaaf, waktu dari db gagal diinisiasi!\n\n\n. Pesan Kesalahan: " + e, "Waktu dari db gagal diinisiasi" ); }

    return now;
}


public static Boolean add_row( final JTable table) {
            new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
                add_row_sync(table);
                return null;
            }}.execute();
            return true;    //luweh!!!
}
public static Boolean add_row_sync(JTable table) {
 //try{
    db_connection model = ((db_connection)table.getModel());
    if( table.getRowCount()!=model.getRowCount() ) return false;    //if filtered
    int selected_v = table.getRowCount()-1;
    int selected_m = -1;
    if( selected_v>=0 ) {
        selected_m = selected_v;    //table.convertRowIndexToModel(selected_v);
        String left_val = model.getValueAt(selected_m, 0).toString();
//if(retail.input_buffer.length()==14) JOptionPane.showMessageDialog( null, debug + " << debug\n. ", "Gagal baca Variable", JOptionPane.ERROR_MESSAGE);
        if( left_val.equals("0") || left_val.equals("") ) return false;    //just inserted //if( table.isEditing() ) table.getCellEditor().stopCellEditing(); 
    }
//JOptionPane.showMessageDialog( null, "2 selected_m=" + selected_m, "Gagal baca Variable", JOptionPane.ERROR_MESSAGE);
    model.addRow( false, selected_m+1 );    //selected_v+1    //ga iso krn Fpembelian bikin instance object sendiri >> db.addRow();
//JOptionPane.showMessageDialog( null, "22222 selected_m=" + selected_m, "Gagal baca Variable", JOptionPane.ERROR_MESSAGE);
    int row_idx = selected_m+1;    //table.convertRowIndexToView(selected_m+1);
    table.setRowSelectionInterval(row_idx, row_idx);

/*
    int selected_col = table.getSelectedColumn();
    table.scrollRectToVisible( table.getCellRect( row_idx, selected_col, true ) );    //kasar:) >> new Rectangle(0, 1000+table.getRowHeight()*table.getRowCount(), 1, 1);
                    //table.getParent().getParent().getParent().getComponent(10).requestFocus();    //table.cancelEditing();     //table.getCellEditor().cancelEditing();
                    //percuma >> table.putClientProperty("terminateEditOnFocusLost", false); 
    if( selected_col<0 ) selected_col=0;    //hrsnya loop yg editable:) ...
    if( ! retail.convert_null(retail.setting.get("Prefer Barcode")).toLowerCase().equals("ya") ) table.editCellAt( row_idx, selected_col );    //unfully tested:p  //anyway, ini gagal juga klo add_row ini dipanggil via keydown?:p
                    //ini langsung bikin editCellAtnya gagal saat form dibuka ulang. .. Tp klo dia lostfocus (panggil JoptionPane) kok bs!! >> table.putClientProperty("terminateEditOnFocusLost", true); 
                    table.requestFocus();  //ini hanya utk mindahin focus dari component lain jika user tekan F11 ... but untested >> //agar edit terposting dulu... cappe deeh

                //} catch (Exception ex) {    JOptionPane.showMessageDialog( null, debug +" << debug from addroww n\n. Pesan Kesalahan: " + ex, "Gagal baca Variable", JOptionPane.ERROR_MESSAGE); }
*/
    return true;
};

    public static boolean empty( EditText[] no_empty, String[] label ) {
        //String err_msg = "";
        int i=0;
        Boolean ret = false;
        for( EditText comp : no_empty ) {
            if( label[i]==null || label[i].equals("") ) label[i] = ((android.support.design.widget.TextInputLayout)comp.getParent()).getHint().toString();    //((JLabel)comp.getParent().getComponent( comp.getParent().getComponentZOrder(comp) - 1 )).getText();
            if( comp.getText().toString().trim().equals("") ) {
                comp.setError( "Mohon isi  \"" + label[i].replace("*","").trim() + "\" !");    //err_msg += "\nMohon isi  \"" + label[i] + "\" !";
                comp.requestFocus();
                ret = true;
                break;
            }
            i++;
        }
        return ret;
    }
    public static boolean empty( EditText[] no_empty ) {    //the overload method
        return empty( no_empty, new String[no_empty.length] );
    }
    public static int round( long n, int prec ) {  //bulatkan n sampai per prec ... misalnya, jika ingin ngilangin dua digit terakhir, isi n dgn n*100, isi prec = 100
        byte sign = n>=0 ? (byte)1 : (byte)-1;
        return (int) ( ((long) n + sign*prec/2 ) /prec  );
    }
    public static boolean is_number(String str) {
        str = str.trim();
        if( str.equals("") ) return false;
        str = str.replace(retail.digit_separator,"");
        if( str.charAt(0)=='-' ) str = str.substring( 1, str.length() );    //allowing negative    //unfully checked :)
        for( char c : str.toCharArray() ) if(!Character.isDigit(c)) return false;
        return true;    //or this a little bit expensive:) >>> return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean contains( String search, String[] arr ) {
        search = search.toLowerCase();
        for( String s : arr )
            if( s.equals(search)) return true;
	return false;
    }

    protected static int icon_reset   = R.drawable.user_busy;
    protected static int icon_save    = R.drawable.save5;
    public static String app_name           = "Solusi Toko     ";
    protected static int user_id            = -13;
    protected static String nama            = "";
    protected static String hak_akses       = "";

    protected static Map<String, String> setting = new HashMap<String, String>() {{ //java does not support associative arrays :p
    }};

    public static String convert_null(String str) {
        return str==null ? "" : str ;
    }
    public static int convert_number(String str, int init) {
        str = str==null ? "" : str.trim().replace(digit_separator,"") ;
        return is_number(str) ? Integer.valueOf(str) : init;
    }

    protected static String digit_separator;
    protected static String decimal_separator;


    static String repeat( char chr, int len ) {
        char[] chrs = new char[len];
        Arrays.fill(chrs, chr);
        return new String(chrs);
    }
    static String space( int len ) {
        return repeat( ' ', len );
    }
    public static String lpad( String str, char chr, int len ) {
        if( str.length()>=len ) return str;
        return repeat( chr, len-str.length() ) + str;
    }

    public static synchronized String escape( String str ) { //to sql    //Synchronization does not allow invocation of this Synchronized method for the same object until the first thread is done with the object. 
        String output = "";
        if( str.length() > 0 ) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            output = str;
        }
        return output;
    }


    public static void log_error( String error ) {
    }

    public static String toTitleCase( String str_ ) {
        if( str_.length()<=3 ) return str_.toUpperCase();
        String str = str_.toLowerCase();
        StringBuilder sb = new StringBuilder();
        //try{
        final char delim = '_';  final char delim2 = ' ';
        char value;
        boolean capitalize = true;
        for( int i=0; i<str.length(); ++i ) {
            value = str.charAt(i);
            if( value == delim || value == delim2 ) {
                sb.append(' ');
                capitalize = true;
            } else if( capitalize ) {
                sb.append( Character.toUpperCase(value) );
                capitalize = false;
            } else
                sb.append(value);
        }
        //} catch( Exception ex ) { JOptionPane.showMessageDialog( null, "Pesan Kesalahan: " + ex + "\n\n\n\n" , "Kesalahan", JOptionPane.ERROR_MESSAGE ); }
        return sb.toString();
    }


    protected static JCdb Ccode_brg;    protected static JCdb Cname_brg;    protected static ArrayList<Integer> harga_brg = new ArrayList<Integer>();    protected static ArrayList<String> diskon_brg = new ArrayList<String>();    protected static ArrayList<String> gambar_brg = new ArrayList<String>();
    protected static int brg_id;
    protected static JTable tbl_brg;

    static void _sync_brg(final JCdb Csrc, final int position) {
        _sync_brg(Csrc, position, 0);
    }
    static void _sync_brg(final JCdb Csrc, final int position, final int filter_position) {
android.util.Log.e("onlistener:", "first Csrc=" + (Csrc==Ccode_brg ? "Ccode" : "Cname" ));
        if( brg_id != -13 ) return;  //supaya tidak listen setSelection yg dilakukan method ini.
        if( last_sql_brg.length()==0 ) return;
        in_progress=true ;

android.util.Log.e("onlistener:", "1 brg_id=" + brg_id  + "   position=" + position   );
            //JCdb Csrc = (JCdb)parent;    //e.getItemSelectable();
android.util.Log.e("onlistener:", "3 brg_id=" + brg_id  + "   position=" + position    );
            //if( Csrc.isShowing() && e.getStateChange()!=ItemEvent.SELECTED ) return;   //Csrc.isShowing() perlu utk mengakomodir removeeditor
            final JCdb Cdst   = Csrc==Cname_brg ? Ccode_brg : Cname_brg ;
            final int col_dst = Csrc==Cname_brg ? 0 : 1 ;
android.util.Log.e("onlistener:", "5 tbl_brg==null" + (tbl_brg==null) );
            //int col_src = (col_dst+1)%2;
            final db_connection db = (db_connection) tbl_brg.getModel();
            //JOptionPane.showMessageDialog( null, "3 tbl_brg.getSelectedRow()="+tbl_brg.getSelectedRow(), "Pembacaan Data Barang", JOptionPane.ERROR_MESSAGE);
            final int row = tbl_brg.getSelectedRow();
            //brg_id perlu diset sebelum thread baru agar hitung_sub_total bisa deteksi klo listener in progress!!
            //klo item sudah ada, pengelompokan harus terjadi di akhir (saat setvalue di bawah) biar bisa menghapus row terakhir >> brg_id = position;    //Csrc.getSelectedItemPosition();    //Csrc.isShowing() ? Csrc.getSelectedIndex() : Csrc.my_index_of(db.getValueAt(row, col_src).toString());
            brg_id = -12;    //Csrc.getSelectedItemPosition();    //Csrc.isShowing() ? Csrc.getSelectedIndex() : Csrc.my_index_of(db.getValueAt(row, col_src).toString());
            Csrc.setSelectedItemPosition(position);    //to used by remove editor (untested for others)
//            Csrc.clear_filter();
            //Csrc.post(new Runnable() { public void run() {
                //Csrc.setVisibility( View.INVISIBLE);
                //Csrc.dismissDropDown();
                //Csrc.setVisibility( View.VISIBLE);
            //}});

            Cdst.post(new Runnable() { public void run() {    //well, perlu untuk memastikan clear_filter() dah selesai di remove_editor()    //I can't remember why I did this >> new android.os.Handler().post(new Runnable() { public void run() {    //new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
                //percuma si >> Csrc.dismissDropDown();
                brg_id = ((jcdb_item)Csrc.getItemAtPosition(position)).get_id();    //position;    //Csrc.getSelectedItemPosition();    //Csrc.isShowing() ? Csrc.getSelectedIndex() : Csrc.my_index_of(db.getValueAt(row, col_src).toString());
android.util.Log.e("onlistener:", "inside handler  brg_id=" + brg_id );
android.util.Log.e("onlistener:", "inside handler  db.getValueAt(row, col_dst).toString()=" + db.getValueAt(row, col_dst).toString() );
android.util.Log.e("onlistener:", "inside handler  Cdst.my_index_of( )=" + Cdst.my_filtered_index_of( db.getValueAt(row, col_dst).toString() ) );
            if( brg_id != Cdst.my_filtered_index_of( db.getValueAt(row, col_dst).toString() ) ) {
android.util.Log.e("onlistener:", "555 brg_id=" + brg_id  + " Cdst.getCount()" + Cdst.getCount() );
android.util.Log.e("onlistener:", "Cdst.getItemAt(brg_id).toString()=" + Cdst.getItemAt(brg_id).toString() );
            //Csrc.clear_filter();
                //Csrc.setSelectedItemPosition(filter_position);
                //int bid = brg_id;
                //brg_id = -12;    //supaya harga, total, dkk tidak terset dua kali oleh Fpenjualan.setValue
                db.setValueAt( false, ( brg_id==-1 ? "" : Cdst.getItemAt(brg_id).toString() ), row, col_dst );    //brg_id==-1 bisa terjadi jika baru mulai ngedit tapi user mencet Esc
                //db.setValueAt( ( brg_id==-1 ? "" : gambar_brg.get(brg_id)                ), row, 6       );
android.util.Log.e("onlistener:", "after set dst value"  );

                in_progress=false ;
            }
            brg_id = -13;
android.util.Log.e("onlistener last:", "   brg_id=" + brg_id + "  Selectedpos=" + Cdst.getSelectedItemPosition()   );
            }});    //    return null;    }}.execute();

    }
    static android.widget.AdapterView.OnItemClickListener sync_brg = new android.widget.AdapterView.OnItemClickListener() {    //android.widget.AdapterView.OnItemSelectedListener sync_brg = new android.widget.AdapterView.OnItemSelectedListener() {
        @Override public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {    //public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
android.util.Log.e("onlistener:", "aa1");
            JCdb Csrc = parent.getAdapter()==Ccode_brg.getAdapter() ? Ccode_brg : Cname_brg;
            int filter_position = position;
            //Csrc.setSelectedItemPosition(position);    //to used by remove editor
//biarlah selalu tetap dalam kondisi terfilter!!! >> position = ((jcdb_item)parent.getItemAtPosition(position)).get_id();     //Csrc.getAdapter().getPosition( ((EditText)Csrc).getText().toString() );    //.getfilter().getItem(position)    //krn position di atas adalah position setelah terfilter :p

android.util.Log.e("onlistener:", "aa2");
            _sync_brg( Csrc, position, filter_position );
android.util.Log.e("onlistener:", "aa3");
        }
        //public void onNothingSelected(android.widget.AdapterView<?> parent) {}
    };	


    public static int get_diskon_check( int banyak, int qty_idx, String[] diskon_arr ) {
        if( banyak<=0 || qty_idx<=0 ) return 0;
        int qty    = Integer.valueOf(diskon_arr[qty_idx]);
        int qty_multiplier = qty<=0 ? 0 : (int) Math.floor( banyak/qty );
        int amount = Integer.valueOf(diskon_arr[qty_idx+1]);
        if( banyak>=qty && qty>0 && amount>0 ) banyak -= qty * qty_multiplier;
        else                                    amount = 0;
        return amount*qty_multiplier + get_diskon_check( banyak, qty_idx-2, diskon_arr ) ;
    }
    public static int get_diskon( String diskon_form, int harga, int banyak ) {
        if( diskon_form.equals("0,0,0,0,0,0,0,0,0") ) return 0;
        String[] diskon_arr = diskon_form.split(",");
        int diskon_by_qty     = diskon_arr.length<=1 ? 0 : get_diskon_check( banyak, 7, diskon_arr ) ;
        int diskon_by_percent = round( (long) (harga*banyak - diskon_by_qty) * Integer.valueOf(diskon_arr[0]), 100 );
        return diskon_by_qty + diskon_by_percent ;
    }

    protected static String last_sql_brg = "";
    public static void get_brg(JTable table) {
        get_brg(table, "SELECT id, "
                      + (convert_null(setting.get("Tampilkan Harga Jual di Item Pembelian")).toLowerCase().equals("ya") ? "CAST( CONCAT(name, ' [', CAST(FORMAT(harga_jual,0) AS char), ']' ) AS char ) AS" : "")
                      + " name, code, harga_beli, diskon_beli AS diskon, gambar FROM barang ORDER BY name" );    //mending by name, krn klo BY code dia pastinya udah hafal (ato ada guideline dari kode barcode)
    }
    public static void get_brg(final JTable table, final String sql) {

        if( table!=null ) tbl_brg = table;    //utk dipakai di itemlistener
        brg_id = -1;   //supaya itemlistener ga jalan di saat pertama ini
        if( sql.equals(last_sql_brg) ) {    //kadang2 pake harga_beli, kadang2 pakai harga_jual:p    //if( Ccode_brg != null ) {
            //jika table.getClientProperty("terminateEditOnFocusLost"), solusinya adalah focuslistener di constructor cell editor atau di bawah ini:
            //Ccode_brg  = new JCdb(Ccode_brg.getModel());    Cname_brg  = new JCdb(Ccode_brg.getModel());    //hrs di sini .. klo gak, dia ga ngerender as look&feel (nimbus) :p
            //percuma >> JCdb temp = Ccode_brg;    Ccode_brg=null;    Ccode_brg=temp;   temp = Cname_brg;    Cname_brg=null;    Cname_brg=temp;   Ccode_brg.setVisible(false);    Ccode_brg.setVisible(true);    //Ccode_brg.setFocusable(false);    //Ccode_brg.setFocusable(true);    Cname_brg.setVisible(false);    Cname_brg.setVisible(true);    //Cname_brg.setFocusable(false);    //Cname_brg.setFocusable(true);  Cname_brg.removeItemListener(sync_brg);  Ccode_brg.removeItemListener(sync_brg);
            Ccode_brg.setSelection(0);    Cname_brg.setSelection(0);    //supaya ItemListener aktif saat item pertama dipilih....            //if( Ccode_brg.getItemCount() > 0 ) return;
            brg_id = -13;
android.util.Log.e("ongetbrg00:", "   brg_id=" + brg_id + "  Selectedpos=" + Ccode_brg.getSelectedItemPosition()   );
            //percuma >> Ccode_brg.firePopupMenuCanceled();   Ccode_brg.firePopupMenuWillBecomeInvisible();    Ccode_brg.firePopupMenuWillBecomeVisible();
            //percuma >> Cname_brg.addItemListener(sync_brg);  Ccode_brg.addItemListener(sync_brg);
            return;
        }
            if( Ccode_brg==null ) {    Ccode_brg  = JCdb.newInstance(false, "", (AppCompatActivity)get_my_app_context());    Cname_brg  = JCdb.newInstance(false, "", (AppCompatActivity)get_my_app_context());    /*just_created=true;*/    }  //hrs di sini .. klo gak, dia ga ngerender as look&feel (nimbus) :p
            if( harga_brg==null ) {
                harga_brg = new ArrayList<Integer>();    diskon_brg = new ArrayList<String>();    gambar_brg = new ArrayList<String>();
            }

            //fill Ccode_brg dan Cname_brg with http://api.muhajirin.net/v1/product/index
            new DownloadJSON(){
                @Override protected void onPostExecute( String result ) {
                    super.onPostExecute(result);
android.util.Log.e("get_brg json: ", "1");
                    if( result.startsWith( "Error:" ) ) {
                        return;
                    }

android.util.Log.e("get_brg json: ", "2");

                    try {
                        org.json.JSONObject data = new org.json.JSONObject(result);
                        if( !data.has( "products" ) ) {
                            retail.show_error( "\n" + data.getString("message") + "\n\n\n\n", "Koneksi Gagal" );
                            return;
                        }
                        reset_brg();
                        Cname_brg.items.add( new jcdb_item( 0, "" ) );    //( -1, "" )    //as long as I remember, I never use Ccode_brg.get_id() and Cname_brg.get_id(), then I can use it to save originate position while filtering:p
                        Ccode_brg.items.add( new jcdb_item( 0, "" ) );
                        harga_brg.add(0);
                        diskon_brg.add("");
                        gambar_brg.add("");
                        org.json.JSONArray jArray = new org.json.JSONArray( data.getString( "products" ) );
                        for( int i=0; i<jArray.length(); i++ ) {
                            org.json.JSONObject product = jArray.getJSONObject(i);
                            Cname_brg.items.add( new jcdb_item( product.getInt("id"), product.getString( "label" ) ) );    //i+1
                            Ccode_brg.items.add( new jcdb_item( product.getInt("id"), product.getString( "barcode" ) ) );    //i+1
                            harga_brg.add( product.getInt( "price" ) );
                            diskon_brg.add("0");
                            gambar_brg.add("");
                        }

                        ((android.widget.ArrayAdapter)Cname_brg.getAdapter()).notifyDataSetChanged();    ((android.widget.ArrayAdapter)Ccode_brg.getAdapter()).notifyDataSetChanged();
android.util.Log.e("ongetbrg:", " 42 Code_brg.getCount()" + Ccode_brg.getAdapter().getCount() + " Code_brg.getAdapter().getCount()" + Ccode_brg.getAdapter().getCount()  + " Code_brg.getAdapter().getCount()" + Ccode_brg.getAdapter().getCount() );
android.util.Log.e("ongetbrg:", " 42 name_brg.getCount()" + Cname_brg.getAdapter().getCount() + " name_brg.items.getCount()" + Cname_brg.getAdapter().getCount()  + " name_brg.getAdapter().getCount()" + Cname_brg.getAdapter().getCount() );
                        Ccode_brg.setSelection(0);    Cname_brg.setSelection(0);    //supaya ItemListener aktif saat item pertama dipilih....
                        brg_id = -13;
android.util.Log.e("ongetbrg:", "   brg_id=" + brg_id + "  Selectedpos=" + Ccode_brg.getSelectedItemPosition()   + "    Ccode_brg.getCount()" + Ccode_brg.getAdapter().getCount() );
                        Cname_brg.setOnItemClickListener(sync_brg);/*ribet amat idup lo >> new AutoCompleteTextViewClickListener(Cname_brg,sync_brg) */    Ccode_brg.setOnItemClickListener(sync_brg);
android.util.Log.e("ongetbrg:", "   after setlistener "   + "    Ccode_brg.getCount()" + Ccode_brg.getCount()  );
                        last_sql_brg = sql;

                        if( Ftransaksi.form!=null ) Ftransaksi.form.after_get_brg();
android.util.Log.e("ongetbrg:", "   Ftransaksi.form!=null" + (Ftransaksi.form!=null)   + "    Code_brg.getCount()" + Ccode_brg.getCount() );
                    } catch( Exception e ) {    //org.json.JSONException 
                        db.err_msg += "\nMaaf, Data \"Barang\" gagal diinisialisasi!\n\n\n(" + e.toString() + ")";
                        show_error( db.err_msg, "Pembacaan Data Barang" );
                        android.util.Log.e("get_brg error: ", "e.toString()="+ e.toString() );
                    }

                }
            }.execute(
                       db.cfg.get( "url_product_index" )    //url to call
                     , db.cfg.get( "client_token" )    //auth header to send
                     , "access_token=" + db.cfg.get( "access_token" )    //post params
                     );    //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // we target SDK > API 11


/*

{
    "status": 200,
    "products": [
        {
            "id": 1,
            "label": "Nasi Goreng Seafood",
            "description": "Nasi Goreng Seafood",
            "price": "18000.00",
            "position": 0,
            "attributes": [
                {
                    "id": 2,
                    "price": "0.00",
                    "label": "Tidak Pedas"
                },
                {
                    "id": 3,
                    "price": "0.00",
                    "label": "Sedang"
                },
                {
                    "id": 4,
                    "price": "0.00",
                    "label": "Pedas"
                }
            ]
        },
        {
            "id": 11,
            "label": "Susu Coklat",
            "description": "Susu Coklat",
            "price": "8000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 10,
            "label": "Cappucino",
            "description": "Cappucino",
            "price": "11000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 9,
            "label": "Lemon Tea",
            "description": "Lemon Tea",
            "price": "7000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 8,
            "label": "Es Jeruk",
            "description": "Es Jeruk",
            "price": "8000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 7,
            "label": "Teh Tarik",
            "description": "Teh Tarik",
            "price": "9000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 6,
            "label": "Pastel",
            "description": "Pastel",
            "price": "4500.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 5,
            "label": "Risoles",
            "description": "Risoles",
            "price": "5000.00",
            "position": 0,
            "attributes": []
        },
        {
            "id": 4,
            "label": "Roti Bakar",
            "description": "Roti Bakar",
            "price": "8000.00",
            "position": 0,
            "attributes": [
                {
                    "id": 21,
                    "price": "0.00",
                    "label": "Reguler, Keju"
                },
                {
                    "id": 22,
                    "price": "0.00",
                    "label": "Reguler, Coklat"
                },
                {
                    "id": 23,
                    "price": "0.00",
                    "label": "Reguler, Strawberry"
                },
                {
                    "id": 24,
                    "price": "0.00",
                    "label": "Reguler, Kacang"
                },
                {
                    "id": 25,
                    "price": "2000.00",
                    "label": "Extra, Keju"
                },
                {
                    "id": 26,
                    "price": "2000.00",
                    "label": "Extra, Coklat"
                },
                {
                    "id": 27,
                    "price": "2000.00",
                    "label": "Extra, Strawberry"
                },
                {
                    "id": 28,
                    "price": "2000.00",
                    "label": "Extra, Kacang"
                }
            ]
        },
        {
            "id": 3,
            "label": "Roti Maryam",
            "description": "Roti Maryam",
            "price": "5500.00",
            "position": 0,
            "attributes": [
                {
                    "id": 7,
                    "price": "0.00",
                    "label": "Reguler, Daging Ayam"
                },
                {
                    "id": 8,
                    "price": "1000.00",
                    "label": "Reguler, Daging Sapi"
                },
                {
                    "id": 9,
                    "price": "1000.00",
                    "label": "Reguler, Keju"
                },
                {
                    "id": 10,
                    "price": "0.00",
                    "label": "Reguler, Sayur"
                },
                {
                    "id": 11,
                    "price": "2000.00",
                    "label": "Extra, Daging Ayam"
                },
                {
                    "id": 12,
                    "price": "3000.00",
                    "label": "Extra, Daging Sapi"
                },
                {
                    "id": 13,
                    "price": "3000.00",
                    "label": "Extra, Keju"
                },
                {
                    "id": 14,
                    "price": "2000.00",
                    "label": "Extra, Sayur"
                }
            ]
        },
        {
            "id": 2,
            "label": "Kentang Goreng",
            "description": "Kentang Goreng",
            "price": "8000.00",
            "position": 0,
            "attributes": [
                {
                    "id": 5,
                    "price": "0.00",
                    "label": "Reguler"
                },
                {
                    "id": 6,
                    "price": "2000.00",
                    "label": "Extra"
                }
            ]
        },
        {
            "id": 12,
            "label": "Jus Buah",
            "description": "Jus Buah",
            "price": "10000.00",
            "position": 0,
            "attributes": [
                {
                    "id": 15,
                    "price": "1000.00",
                    "label": "Alpukat"
                },
                {
                    "id": 16,
                    "price": "0.00",
                    "label": "Jambu"
                },
                {
                    "id": 17,
                    "price": "0.00",
                    "label": "Mangga"
                },
                {
                    "id": 18,
                    "price": "0.00",
                    "label": "Buah Naga"
                },
                {
                    "id": 19,
                    "price": "0.00",
                    "label": "Campuran"
                },
                {
                    "id": 20,
                    "price": "0.00",
                    "label": "Apel"
                }
            ]
        }
    ]
}


*/



    }

    public static void reset_brg() {
        //untested:p >> if( last_sql_brg.isEmpty() ) return;
        //untested: Ccode_brg=null;    Cname_brg=null;    //biarin:p >> tidak bijaksana
        //ini malah bikin masalah krn sepertinya dia ga langsung ilang >>
        last_sql_brg="";    //biar get_brg create new Ccode_brg ....
        if( Ccode_brg!=null ) {
            Cname_brg.setOnItemClickListener(null);    Ccode_brg.setOnItemClickListener(null);
            //Ccode_brg.my_removeAllItems();    Cname_brg.my_removeAllItems();    //
            //Ccode_brg.clear_filter();    Cname_brg.clear_filter();
            Ccode_brg.items.clear();    Cname_brg.items.clear();
        }
        harga_brg.clear();    diskon_brg.clear();    gambar_brg.clear();    //harga_brg=null;    diskon_brg=null;    gambar_brg=null;
    }


    //https://www.codota.com/android/scenarios/52fcbc8eda0a0bd4740ef489/android.net.wifi.WifiManager.WifiLock?tag=dragonfly
    static android.net.wifi.WifiManager.WifiLock wifiLock;
    public static void keepWiFiOn( android.content.Context context, boolean on ) {
        if( wifiLock == null ) {
            android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);
            if( wm != null ) {
                wifiLock = wm.createWifiLock( android.net.wifi.WifiManager.WIFI_MODE_FULL, "TAG" );
                wifiLock.setReferenceCounted(true);
            }
        } 
        if( wifiLock != null ) { // May be null if wm is null
            if( on ) {
                wifiLock.acquire();
                Log.e( "TAG", "Acquired WiFi lock");
            } else if( wifiLock.isHeld() ) {
                wifiLock.release();
                Log.e( "TAG", "Released WiFi lock");
            }
        } 
    }

}


class JMenu {
    String title;
    int icon=-1;
    ArrayList<JMenuItem> menuItems;
    public JMenu(String title) {
Log.e("constructor JMenu: ", "title=" + title);
        this.title=title;
    }
    public void setIcon( int icon ) {
        this.icon=icon;
    }
    public void add( JMenuItem menuItem ) {
Log.e("add( JMenuItem menuItem ):", "title=" + menuItem.title );
        if( menuItems==null ) menuItems = new ArrayList<JMenuItem>();
        menuItems.add( menuItem );
    }
    public void add( JMenu menu ) {
Log.e("add( JMenu menu ): ", "1 menu.title=" + menu.title);
        JMenuItem menuItem = new JMenuItem( menu.title );
Log.e("add( JMenu menu ): ", "2");
        add(menuItem);
Log.e("add( JMenu menu ): ", "3");
        menuItem.add(menu);
Log.e("add( JMenu menu ): ", "4");
    }

    public void add_to( Menu menuBar ) {
//        add_to( menuBar, this ) {
//    }
//    public void add_to( Menu menuBar, JMenu menu ) {
try{

                   //String title = menu.title;    //( menuBar instanceof SubMenu ) ? menuBar.title: this.title;
        Log.e("JMenu  add_to( Menu menuBar ): ", "this.title=" + this.title);
        SubMenu subMenu = menuBar.addSubMenu(this.title);
        for( JMenuItem menuItem : this.menuItems ) {
            Log.e("JMenu  add_to( Menu menuBar ): ", "inside loop >> menuItem.title=" + menuItem.title);
//?            item = menuItem.add_to( (Menu)subMenu);
            if( menuItem.menus!=null )
                for( JMenu menu : menuItem.menus ) {
                    if( menu.icon!=-1 ) subMenu.setIcon(menu.icon);
                    menu.add_to( subMenu );
                }
            else
                menuItem.add_to( (Menu)subMenu );
        }

} catch (Exception e) {
Log.e("error: ", "JMenu  add_to( Menu menuBar ): " + e.getMessage() );
e.printStackTrace();
}

    }

}

class JMenuItem {
    String title;
    int icon = -1;
    ArrayList<JMenu> menus;
    MenuItem.OnMenuItemClickListener listener;
    int id=-1;

    public JMenuItem( String title ) {
Log.e("constructor JMenuItem: ", "title=" + title);
        this.title=title;
    }
    public JMenuItem( String title, int icon ) {
        this(title);
        this.icon=icon;
    }
    public JMenuItem( String title, int icon, MenuItem.OnMenuItemClickListener listener, int id ) {
        this(title);
        this.icon=icon;
        this.listener=listener;
        this.id=id;
    }

    public void add( JMenu menu ) {
        if( menus==null ) menus = new ArrayList<JMenu>();
Log.e("add menus: ", "title=" + title);
        menus.add( menu );
    }
    public void add_to( Menu menuBar ) {
try{
Log.e("JMenuItem add_to: ", "title=" + title);
        MenuItem item = ( id==-1 ) ? menuBar.add( title ) : menuBar.add( Menu.NONE, id, 0, title );
        if( icon!=-1 ) item.setIcon(icon);
        if( listener!=null ) item.setOnMenuItemClickListener(listener);

} catch (Exception e) {
Log.e("error: ", "JMenuItem,  add_to( Menu menuBar ): " + e.getMessage() );
e.printStackTrace();
}

    }
}









class JP_menu extends JP_submenu {
    public JP_menu(String title) {
        super(title);
//setBounds(0, 0, 350,600);

//((JComponent)this).setAlignmentY(TOP_ALIGNMENT);

    }
//    public Dimension getPreferredSize() {
//        return new Dimension(350,600);
//    }
}
class JP_submenu extends LinearLayoutCompat {    //JPanel
    public JP_submenu() {
        this("");
    }
    public JP_submenu(String title) {
        super( retail.get_my_app_context() );    //klo mau flowlayout, bikin custom layout!!! >> super( new FlowLayout(FlowLayout.CENTER, 13, 13) );
        setOrientation(LinearLayoutCompat.HORIZONTAL);
//        setOpaque(false);
////        if( !title.equals("") ) 
//setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), title ) );
    }
}

class JBmenu extends AppCompatButton {    //android.widget.ImageButton    //JButton
    public JBmenu(String title, /*String */ int icon_path, OnClickListener onClickListener ) {    //ActionListener actionListener
        super( retail.get_my_app_context() );
        //setBackgroundColor(0x5500ff00);
        /* cannot find symbol class style location: package android.text
        android.text.Spannable label = new android.text.SpannableString(title);
        label.setSpan( android.text.style.ImageSpan( retail.base_context, R.drawable.icon, android.text.style.ImageSpan.ALIGN_BOTTOM), 0, 1, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        setText(label);
        */
        setText(title);
        //setBackgroundDrawable()
        //setCompoundDrawablesWithIntrinsicBounds ( null, android.graphics.drawable.Drawable.createFromPath( android.net.Uri.parse( "android.resource://com.solusiprogram.solusitoko/drawable/" + icon_path ).getPath()  ), null,null ) ;
        setCompoundDrawablesWithIntrinsicBounds( 0, icon_path, 0, 0 ) ;
        //android.content.res.Resources res = getResources();
        //icon_path = icon_path.replace("/",".");
        //createFromPath (String pathName)
        //setImageDrawable( res.getDrawable( res.getIdentifier("com.solusiprogram.solusitoko:drawable/" + icon_path, null, null) ) );    //setImageResource( res.getIdentifier("com.solusiprogram.solusitoko:drawable/" + icon_path, null, null) );    //setImageResource( getResources().getDrawable( R.drawable.myfirstimage )  );    //setImageDrawable( res.getDrawable( res.getIdentifier("com.solusiprogram.solusitoko:drawable/" + icon_path, null, null) ) );    //setIcon(new ImageIcon(icon_path));
        //setIconTextGap( 7 );
        setOnClickListener(onClickListener);
        //addFocusListener(retail.onHover);
        //setVerticalTextPosition(SwingConstants.BOTTOM);  
        //setHorizontalTextPosition(SwingConstants.CENTER);
        ////setAlignmentX( SwingConstants.WEST );
    }
}

class jcdb_item {
    private int id;
    private String name;
    public jcdb_item(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int get_id() { return id;   }
    //public void set_id( int id ) { this.id = id; }    //to save position in filtered result!!
    //public String get_name() { return name; }
    public String toString() { return name; }    //to override combo.toString() katanya:)
}



class JCdb extends android.widget.AutoCompleteTextView {    //MultiAutoCompleteTextView    //android.widget.Spinner { //fill the combobox with the value from db...
    static JCdb jcdb;
    public static JCdb newInstance( Boolean async, String table, final AppCompatActivity act ) {
android.util.Log.e("jcdb: ", "1");
        //if( android.os.Build.VERSION.SDK_INT < 11 )
        jcdb = new JCdb(async, table, act, 1);
//        after_create(jcdb);
        return jcdb;
        //else                                 return new JCdb(async, table, act, true);
    }
    public static JCdb newInstance( String table, final AppCompatActivity act ) {
        //if( android.os.Build.VERSION.SDK_INT < 11 )
        jcdb = new JCdb(true, table, act, 1);
//        after_create(jcdb);
        return jcdb;
        //else                                 return new JCdb(true, table, act, true);
    }
    public static JCdb newInstance( Boolean async, String table, final AppCompatActivity act, int threshold ) {
android.util.Log.e("jcdb: ", "1");
        //if( android.os.Build.VERSION.SDK_INT < 11 )
        jcdb = new JCdb(async, table, act, threshold);
//        after_create(jcdb);
        return jcdb;
        //else                                 return new JCdb(async, table, act, true);
    }
    public static JCdb newInstance( String table, final AppCompatActivity act, int threshold ) {
        //if( android.os.Build.VERSION.SDK_INT < 11 )
        jcdb = new JCdb(true, table, act, threshold );
//        after_create(jcdb);
        return jcdb;
        //else                                 return new JCdb(true, table, act, true);
    }



    public static JCdb newInstance( Boolean async, String table, final android.support.v4.app.FragmentActivity act ) {
        return newInstance( async, table, (AppCompatActivity) act );
    }
    public static JCdb newInstance( String table, final android.support.v4.app.FragmentActivity act ) {
        return newInstance( table, (AppCompatActivity) act );
    }
    public static JCdb newInstance( Boolean async, String table, final android.support.v4.app.FragmentActivity act, int threshold ) {
        return newInstance( async, table, (AppCompatActivity) act, threshold );
    }
    public static JCdb newInstance( String table, final android.support.v4.app.FragmentActivity act, int threshold ) {
        return newInstance( table, (AppCompatActivity) act, threshold );
    }

    public void after_create() {
android.util.Log.e("after_create: ", "1" );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
android.util.Log.e("after_create: ", "2" );
        setAdapter(adapter);
android.util.Log.e("after_create: ", "3" );
        setOnItemClickListener( new android.widget.AdapterView.OnItemClickListener() {    //perlu utk lakukan setSelectedItemPosition
            @Override public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
android.util.Log.e("jcdb setonitemclicklistener: ", "position before: " + position);
                position = items.indexOf( parent.getItemAtPosition(position) );    //((android.widget.ArrayAdapter)getAdapter()).getPosition( ((jcdb_item)parent.getItemAtPosition(position)) );
android.util.Log.e("jcdb setonitemclicklistener: ", "position after: " + position);
                setSelectedItemPosition(position);
            }
            //public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
android.util.Log.e("after_create: ", "4" );
    }


    Boolean mode_dropdown = false;
    public JCdb( Boolean async, String table, final AppCompatActivity act, int threshold ) {
        super( (android.content.Context) act );
android.util.Log.e("jcdb: ", "BuildConfig.VERSION_CODE " + BuildConfig.VERSION_CODE + "    Build.VERSION.SDK_INT " + android.os.Build.VERSION.SDK_INT   );
        mode_dropdown = android.os.Build.VERSION.SDK_INT >= 11 ;
android.util.Log.e("jcdb: ", "editor.mode_dropdown " + mode_dropdown );
        create( async, table, act, threshold );
    }
/*
    public JCdb( Boolean async, String table, final AppCompatActivity act, Boolean stupid ) {
        super( (android.content.Context) act, android.widget.Spinner.MODE_DROPDOWN );
android.util.Log.e("jcdb: ", "BuildConfig.VERSION_CODE " + BuildConfig.VERSION_CODE + "    Build.VERSION.SDK_INT " + android.os.Build.VERSION.SDK_INT   );
        mode_dropdown = stupid;
        create( async, table, act );
    }
*/

    public void clear_filter() {
//setText("");
//showDropDown();
        //performFiltering("", 0);
        ((android.widget.ArrayAdapter)getAdapter()).getFilter().filter(null);
        ((android.widget.ArrayAdapter)getAdapter()).notifyDataSetChanged();
    }


    android.widget.ArrayAdapter adapter;
    ArrayList<jcdb_item> items;
    public void create( final Boolean async, String table, final AppCompatActivity act, int threshold ) {
try{
        //setTokenizer( new android.widget.MultiAutoCompleteTextView.CommaTokenizer() );
android.util.Log.e("jcdb create: ", "1 threshold=" + threshold);
        setThreshold( threshold );    //default=1: will start working from 1st character

android.util.Log.e("jcdb create: ", "1a");
        items = new ArrayList<jcdb_item>();
android.util.Log.e("jcdb create: ", "1b");
        adapter = new android.widget.ArrayAdapter<jcdb_item>( act, android.R.layout.simple_spinner_item, items ) {    //ArrayList<jcdb_item> items = new ArrayList<jcdb_item>();
            @Override public android.widget.Filter getFilter() {
                return contains_filter;
            }
            ArrayList<jcdb_item> filtered;
            android.widget.Filter contains_filter = new android.widget.Filter() {
                @Override protected android.widget.Filter.FilterResults performFiltering( CharSequence constraint ) {
                    android.widget.Filter.FilterResults filterResults = new android.widget.Filter.FilterResults();
                    if( constraint!=null && constraint.toString().trim().length()>0 ) {
android.util.Log.e("jcdb: ", " constraint!=null   filtered==null=" + (filtered==null));
                        if( filtered==null ) filtered = new ArrayList<jcdb_item>() ;
                        else filtered.clear();
                        String search = constraint.toString().trim();
                        //aku mo modif retail.is_number() utk mengexclude "." dan ".." tapi takutnya beresiko mempengaruhi callers yg lain :D
android.util.Log.e("jcdb: ", " search=" + search);
                        Boolean is_number = search.indexOf(retail.digit_separator)!=0 && search.indexOf(retail.digit_separator+retail.digit_separator)<0 && retail.is_number(search);
android.util.Log.e("jcdb: ", " is_number=" + is_number  +  "   getCount()" + getCount()  );
                        jcdb_item combo_item=null;    String combo_string="";
                        for( int i=0; i<getCount(); i++ ) {
//ni kayaknya bikin lambat >> android.util.Log.e("jcdb: ", " i=" + i );
                            combo_item = (jcdb_item)getItemAtPosition(i);
                            combo_string = combo_item.toString();
//android.util.Log.e("jcdb: ", " combo_string=" + combo_string );
                            if( !is_number ) {
                                search   = search.toUpperCase();
                                combo_string = combo_string.toUpperCase();
                            }
                            if( combo_string.contains(search) ) filtered.add( combo_item );
                            //if( combo_string.contains(search) ) android.util.Log.e("jcdb: ", combo_string + "  match!! >  '" +   search +"'");
                        }
                        filterResults.values = filtered;
                        filterResults.count  = filtered.size();

android.util.Log.e("jcdb: ", "  filterResults.values.size()'" +   ((ArrayList)filterResults.values).size() +  "  filtered.size()'" +   filtered.size()  );

                    } else {
                        filterResults.values = items;
                        filterResults.count  = items.size();
                    }
                    return filterResults;
                }
                @Override protected void publishResults( CharSequence constraint, final android.widget.Filter.FilterResults results ) {
android.util.Log.e("jcdb: ", "  results != null && results.count > 0'" +   (results != null && results.count> 0) +"'   results.count=" + results.count );

                    //ini sepertinya malah bikin error >> postDelayed(new Runnable() { @Override public void run() {

//                    post(new Runnable() { @Override public void run() {    //new android.os.Handler().
//hm, tetap aja error walo yg di bawah dah kudisable!!!
                    if( results != null && results.count > 0 ) notifyDataSetChanged();
                    else                                       notifyDataSetInvalidated();

//                    }});    //}},500);

                }
            };

        };


android.util.Log.e("jcdb create: ", "1c");

        if( !table.equals("") ) {

android.util.Log.e("jcdb create: ", "!table.equals('')" );

            /*setClickable(true);*/    setFocusable(true);    setFocusableInTouchMode(true);    //utk mentrigger col_editor lostfocus agar col_editor tersebut terremove

            String sql;
            if( table.indexOf(" ")>0 ) {
                   sql = table;    table = "";
            } else
                   sql = "SELECT id, name FROM " + table + " ORDER BY id" ;

            final String final_sql = sql,  final_table = table;
/*
            if( async )
                new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
                    create_sync( final_table, final_sql );
                    return null;
                }}.execute();
            else
*/
                    create_sync( final_table, final_sql );

        } else {

            android.util.Log.e("jcdb create: ", "last -2");

            after_create();

            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //jcdb.setAdapter(adapter);
        }

android.util.Log.e("jcdb create: ", "last -1");
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //jcdb.setAdapter(adapter);
android.util.Log.e("jcdb create: ", "last");

/*
        jcdb.setOnItemClickListener( new android.widget.AdapterView.OnItemClickListener() {    //perlu utk lakukan setSelectedItemPosition
            @Override public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
android.util.Log.e("jcdb setonitemclicklistener: ", "position before: " + position);
                position = items.indexOf( parent.getItemAtPosition(position) );    //((android.widget.ArrayAdapter)getAdapter()).getPosition( ((jcdb_item)parent.getItemAtPosition(position)) );
android.util.Log.e("jcdb setonitemclicklistener: ", "position after: " + position);
                setSelectedItemPosition(position);
            }
            //public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

*/
android.util.Log.e("jcdb create: ", "lastttttttt");

            } catch (Exception e) {
android.util.Log.e("jcdb create: ", "error: " + e);

retail.show_error("jcdb create: error: " + e, "errrr");

            }

    }

    public void create_sync( final String table, final String sql ) {

android.util.Log.e("jcdb create: ", "333");
            final db_connection db = retail.db; //hanya alias aja:p agar ga kepanjangan

            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
                    db.exec(sql);
                    //try { while( db.in_progress ) java.lang.Thread.sleep(100); } catch (InterruptedException e1) {}
                    return null;
                }
                @Override protected void onPostExecute( Void v ) {



android.util.Log.e("jcdb create: ", "4");
            if( !db.err_msg.equals("") ) return;
android.util.Log.e("jcdb create: ", "5");

/*
                @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return initView(position, convertView);
        }

        private View initView(int position, View convertView) {
            if(convertView == null) convertView = View.inflate( act,android.R.layout.simple_list_item_2,null);
            TextView tvText1 = new TextView( act, null, com.solusiprogram.solusitoko.R.style.floating_label );    //if( Build.VERSION.SDK_INT<23 ) {    Lagent.setTextAppearance( this.getActivity(), android.R.style.InputLabel );    } else {    Lagent.setTextAppearance( android.R.style.InputLabel );    }
            tvText1.setText( getItem(position).toString() );
            return convertView;
        }
};
*/


//String debug="";
            try {
//debug+="1 sql=" + sql;
                while( db.rs.next() ) items.add( new jcdb_item( db.rs.getInt(1), db.rs.getString(2) )  );
//debug+="3";
                db.rs.close();
//debug+="4";
                //items.add( new jcdb_item( 7, "testtt" )  );

        after_create();

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        jcdb.setAdapter(adapter);

            } catch (Exception e) { //numpang2 pake db.err_msg :D
                db.err_msg += "From jcdb create" + "\nMaaf, Data \""+ retail.toTitleCase(table) +"\" gagal diinisiasi!\n\n\n(" + e + ")";
            }
            //////if( getWidth()>70 ) {    //add padding/inset :p

                }
            }.execute();


    }


    public jcdb_item getItemAtPosition(int i) {
        return (jcdb_item) getAdapter().getItem(i);
    }
    //?! >> int selected_idx;
    public int getSelectedItemPosition() {
        return getListSelection();    //return selected_idx;
    }
    public void setSelectedItemPosition(int i) {
        setListSelection(i);    //selected_idx = i;
    }
    public jcdb_item getSelectedItem() {
        return (jcdb_item) getAdapter().getItem(getListSelection());
    }
    public int getCount() {
        if( getAdapter()==null ) return 0;
        return getAdapter().getCount();
    }
    public int getItemCount() {
        return getCount();
    }


    public void my_selectWithKeyChar(char chr) {    //if( !getItemAtPosition(i).toString().equals("") ) perlu, otherwise ... dia (silently) error pas item empty
        for( int i=0; i<getCount(); i++ )
            if( !getItemAtPosition(i).toString().equals("") ) if( Character.toUpperCase(chr) == Character.toUpperCase( getItemAtPosition(i).toString().charAt(0) )) {
                setSelection(i);   break;
            }
    }
    public Object[] my_item_of(String search) {
        Object[] ret = new Object[] {-1, null};
        search=search.trim();
//is_android... kutambahin dummy item "" krn dia tak bisa disetSelection(-1), tetap ngeset ke 0 saat showdropdownlist >>        if( search.equals("") ) return ret;
        //aku mo modif retail.is_number() utk mengexclude "." dan ".." tapi takutnya beresiko mempengaruhi callers yg lain :D
        Boolean is_number = search.indexOf(retail.digit_separator)!=0 && search.indexOf(retail.digit_separator+retail.digit_separator)<0 && retail.is_number(search);
        jcdb_item combo_item=null;    String combo_string="";
        for( int i=getItemCount()-1; i>=0; i-- ) {    //jika nama barang boleh duplicate, baca yg terakhir aja >> for( int i=0; i<getItemCount(); i++ ) {
            //biar faster, ganti jadi yg di bawah ?! >> if( !getItemAtPosition(i).toString().equals("") ) if( search.toUpperCase().equals( getItemAtPosition(i).toString().toUpperCase() ) )
            //combo_item = getItemAtPosition(i).toString();
            combo_item = (jcdb_item)getItemAtPosition(i);
            combo_string = combo_item.toString();
            //bialin ... if( combo_item.equals("") ) continue;
            if( !is_number ) {
                search=search.toUpperCase();
                combo_string = combo_string.toUpperCase();
            }
            if( search.equals(combo_string) ) {
                ret = new Object[] {i, combo_item};
                break;
            }
        }
        return ret;
    }
    public int my_filtered_index_of(String search) {    //utk JCdb terfilter di Ftransaksi yg menyimpan index asli di get_id()
        return ((jcdb_item)my_item_of(search)[1]).get_id();
    }
    public int my_index_of(String search) {
        return Integer.valueOf( my_item_of(search)[0].toString() );
    }
    public int my_key_of(String search) {
        jcdb_item item = (jcdb_item)my_item_of(search)[1];
        return ( item==null ? -1 : item.get_id() );
    }

    public Object[] my_item_of_key(int search) {
        Object[] ret = new Object[] {-1, null};
        jcdb_item combo_item=null;
        for( int i=0; i<getCount(); i++ ) {
            combo_item = (jcdb_item)getItemAtPosition(i);
            if( search==combo_item.get_id() ) {
                ret = new Object[] {i, combo_item};
                break;
            }
        }
        return ret;
    }
    public int my_index_of_key(int search) {
        return Integer.valueOf( my_item_of_key(search)[0].toString() );
    }
    public jcdb_item my_jcdb_item_of_key(int search) {
        return (jcdb_item) my_item_of_key(search)[1];
    }
    public String my_string_of_key(int search) {
        return my_item_of_key(search)[1].toString();
    }
    public void my_setSelectedItem(String search) {
        int index = my_index_of( search );
        //biar ajalah ngeset ke -1 >> if( index>=0 )
        android.util.Log.e("on my_setSelectedItem index:", index + "  search:" + search + "   brg_id=" + retail.brg_id + " getSelectedItemPosition()=" + getSelectedItemPosition() );
        setSelection(index);    //klo tanpa if( index>=0 ) , dia ngeset ke ??
    }
// is_android ...
    public Object getItemAt(int search) {
        return search<0 ? null : getItemAtPosition(search);    //my_item_of_key(search)[1];
    }
}

/*
class AutoCompleteTextViewClickListener implements OnItemClickListener {    //agar Ccode_brg dan Cname_brg bisa dideteksi di listener sync_brg
       android.widget.AutoCompleteTextView mAutoComplete;
       android.widget.AdapterView.OnItemClickListener mOriginalListener;
       public AutoCompleteTextViewClickListener(android.widget.AutoCompleteTextView acTextView, android.widget.AdapterView.OnItemClickListener originalListener) {
           mAutoComplete = acTextView;
           mOriginalListener = originalListener;
       }
       public void onItemClick(AdapterView<?> adView, View view, int position, long id) {
           mOriginalListener.onItemClick(adView, mAutoComplete, position, id);
       }       
}
*/


class floating_label extends TextView {    //styled programmatically    //jika masih gagal, coba ganti R.style.floating_label dengan R.attr.InputLabelStyle
    public floating_label( android.content.Context context ) {
        super( context, null, net.muhajirin.solusitoko.R.style.floating_label);    //super( context, null, 0, net.muhajirin.solusitoko.R.style.floating_label);  Added in API level 21 >> TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)    }
    }
    public floating_label( android.content.Context context, android.util.AttributeSet attrs ) {
        super( context, attrs, net.muhajirin.solusitoko.R.style.floating_label );
    }
    public floating_label( android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );
    }
}


    class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, final Throwable e) {
            try {
/*kok malah muncul terus ni
                new Thread() {
                    @Override public void run() {
                        android.os.Looper.prepare();
                        retail.show_error( "Terjadi Error: " + e.getMessage() + "\n\nMohon Hubungi Rafik!\n\n\n\n", "Error!" );
                        android.os.Looper.loop();
                    }
                }.start();
                try {
                    Thread.sleep(10000); // Let the Toast display before app will get shutdown
                } catch( InterruptedException ex ) {}
                //System.exit(2);    //katanya ga bs kerja tanpa ini
*/
            } catch( Throwable x ) {}    //don't let the exception get thrown out, will cause infinite looping!
        }
        public static void registerExceptionHandler() {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
            System.setProperty("sun.awt.exception.handler", ExceptionHandler.class.getName());
        }
    }

