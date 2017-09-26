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
import android.view.LayoutInflater;
import android.support.v4.app.DialogFragment;
//import android.view.WindowManager;
//import android.view.Window;
import android.view.Gravity;
import android.content.Context;
//import android.widget.ImageView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;

	
import java.sql.Types;


//import android.widget.Spinner;
import android.view.KeyEvent;
import java.util.*;
//import java.awt.*;
//import java.awt.event.*;


//di bawah ini baru test doang
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;








import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;



//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Fedit extends DialogFragment implements android.support.v7.widget.Toolbar.OnMenuItemClickListener {
    //must be public to let retail.click_button() ...
    //public AppCompatButton Bhapus;
    protected db_connection db;
    protected static String modul;    protected String sql;    //protected View default_focused_comp;    protected int default_combo_index=0;

    //public JTable table;    //public JMenuItem menu_tambah = null;
    int last_selected_row = -13;
    protected static RelativeLayout panel;

    static ViewGroup view;    //static LayoutParams prms;
    static String barcode_init = "";
    static Fedit form;
    public Fedit() {}
    public static void newInstance(String title, Fedit form /*, Object parent_*/ ) {    //ViewGroup or another DialogFragment? :p
        //parent=parent_;
        form.setStyle( DialogFragment.STYLE_NORMAL, R.style.AppTheme );    //flogin    //dia dimasukin ke oncreate() klo menurut ini >> file:///E:/Android/sdk/docs/reference/android/app/DialogFragment.html
        modul = title;
        //super(parent, retail.app_name + " - " + title + "   ::   Petugas: " + retail.nama, Dialog.ModalityType.APPLICATION_MODAL);
        form.setCancelable(false);    //defaultnya true
        //?!alert.setCanceledOnTouchOutside(false);    //<- to cancel outside touch
        Fedit.form=form;
        //return form;
    }



/* ini method2 pelengkap dari RecyclerViewDemoActivity

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeTransform;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.ImageButton;

import java.util.Date;
import java.util.List;

import static android.view.GestureDetector.SimpleOnGestureListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_cardview_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_remove) {
            removeItemFromList();
        }
        return true;
    }

    private void addItemToList() {
        DemoModel model = new DemoModel();
        model.label = "New Item " + itemCount;
        itemCount++;
        model.dateTime = new Date();
        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).
                findFirstVisibleItemPosition();
        // needed to be able to show the animation
        // otherwise the view would be inserted before the first
        // visible item; that is outside of the viewable area
        position++;
        RecyclerViewDemoApp.addItemToList(model, position);
        adapter.addData(model, position);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            // fab click
            addItemToList();
        } else if (view.getId() == R.id.container_list_item) {
            // item click
            int idx = recyclerView.getChildPosition(view);
            if (actionMode != null) {
                myToggleSelection(idx);
                return;
            }
            DemoModel data = adapter.getItem(idx);
            View innerContainer = view.findViewById(R.id.container_inner_item);
            innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER + "_" + data.id);
            Intent startIntent = new Intent(this, CardViewDemoActivity.class);
            startIntent.putExtra(Constants.KEY_ID, data.id);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, innerContainer, Constants.NAME_INNER_CONTAINER);
            this.startActivity(startIntent, options.toBundle());
        }
    }

    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
        fab.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_delete:
                List<Integer> selectedItemPositions = adapter.getSelectedItems();
                int currPos;
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    currPos = selectedItemPositions.get(i);
                    RecyclerViewDemoApp.removeItemFromList(currPos);
                    adapter.removeData(currPos);
                }
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        adapter.clearSelections();
        fab.setVisibility(View.VISIBLE);
    }

    private class RecyclerViewDemoOnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            actionMode = startActionMode(RecyclerViewDemoActivity.this);
            int idx = recyclerView.getChildPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }
*/



/*
    public static final List<DemoModel> addItemToList(DemoModel model, int position) {
        demoData.add(position, model);
        demoMap.put(model.id, model);
        return new ArrayList<DemoModel>(demoData);
    }

    public static final List<DemoModel> removeItemFromList(int position) {
        demoData.remove(position);
        demoMap.remove(demoData.get(position).id);
        return new ArrayList<DemoModel>(demoData);
    }

    public static DemoModel findById(int id) {
        return demoMap.get(id);
    }
*/




    //RecyclerView recyclerView;
    //int itemCount;
    //GestureDetectorCompat gestureDetector;
    //ActionMode actionMode;
    //ImageButton fab;
    //Context mContext;

/*
    @Override public void onCreateOptionsMenu( android.view.Menu menu, android.view.MenuInflater inflater ) {
        super.onCreateOptionsMenu(menu, inflater);
        //getActivity().getMenuInflater()
        inflater.inflate(R.menu.edit, menu);
        final android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) android.support.v4.view.MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String search) {
                ((Table_adapter)table.getAdapter()).filter(search);
                return true;
            }
            @Override public boolean onQueryTextChange(String search) {
                ((Table_adapter)table.getAdapter()).filter(search);
                return true;
            }
        });
    }
*/

    /* @Override public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    */

    void hapus_final() {
    }
    void hapus( /*View caller */) {
            if( table==null ) return;
            final int selected_row = table.getSelectedRow();    //((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if( selected_row<0 ) return;

            final int count = 1;    //selectedItems.length()    //((Table_adapter)table.getAdapter()).selected_row;    //((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if( count<1 ) return;

            String name="";    int c=0;    Boolean has_value=false;
            if( count==1 )
                for( int i=0; i<db.col_width.length; i++ ) {
                    if( db.col_width[i]==0 ) continue;
                    if( name.length()==0 && db.getColumnName(i).startsWith("Nama") ) name = db.getStringAt( selected_row, i );
                    if( c==0 ) c=1;
                    if( !has_value ) {
                        String val = db.getStringAt( selected_row, i ).toString();
                        if( !val.equals("0") && val.length()>0 ) has_value = true;
                    }
                }
            if( !has_value ) return;
            final Boolean final_has_value = has_value;
            if( name.length()==0 && c>0 ) name = db.getStringAt( selected_row, c );    //c adalah kolom kedua yg visible

            final String confirm = count==1 ? "\nData baris ke-" + (selected_row+1) + " (" +name+ ")" : "\n" + count + " baris data" ;
            //no confirmation >> new android.os.AsyncTask<Void, Void, Integer> () {
            //no confirmation >>         @Override protected Integer doInBackground( Void... v ) {
            //no confirmation >>             return retail.show_confirm2( confirm + " ingin dihapus?\n\n\n\n", "Konfirmasi" );
            //no confirmation >>         } @Override protected void onPostExecute( Integer resp ) {
            //no confirmation >>             if( resp!=0 ) return;
                        //if( table.isEditing() ) table.getCellEditor().cancelCellEditing();    //jika tidak di sini, post editing terjadi setelah didelete ... lah?! aneh :)
                        for( int i=0; i<count; i++ ) {
                            final int row = selected_row;    //table.getSelectedRow();
                            String val = db.getStringAt( row, 0 ).toString();
                            //db.removeRow(false,row);
                            new android.os.AsyncTask<Void, Void, Void> () {
                                @Override protected Void doInBackground( Void... v ) {
                                    db.removeRow_sync(row);
                                    return null;
                                }
                                @Override protected void onPostExecute(Void v) {
                                    ((Table_adapter)table.getAdapter()).notifyItemRemoved(row);
                                    ((Table_adapter)table.getAdapter()).selected_row = -1;
                                    new android.os.AsyncTask<Void, Void, Void> () {
                                        @Override protected Void doInBackground( Void... v ) {
                                            if( final_has_value ) retail.add_row_sync(table);    //!val.equals("0") && val.length()>0    //mungkin user ingin menghapus row ini yg sudah terlanjur diisi tapi kolom idnya masih 0    //if( table.getRowCount()==0 )
                                            return null;
                                        }
                                        @Override protected void onPostExecute(Void v) {
                                            if( row<table.getRowCount() ) {
                                                //last_selected_row++;    //ngumpanin ListSelectionListener() ... agar tetap load_gambar
                                                table.setRowSelectionInterval(row, row);
                                            } else if( table.getRowCount()>0 ) {
                                                table.setRowSelectionInterval(row-1, row-1);   //ini harus sesudah diremove:p  else if( table.getRowCount()>0 ) table.setRowSelectionInterval(row+1, row+1);
                                               //caller.setEnabled(false);    caller.getIcon().setAlpha(130);
                                            }
                                            hapus_final();
                                        }
                                    }.execute();
                                }
                            }.execute();
                        }    //end of for
            //no confirmation >>         }
            //no confirmation >> }.execute();
    }


            @Override public boolean onMenuItemClick( android.view.MenuItem item ) {
                switch( item.getItemId() ) {
                    case R.id.action_remove: hapus(/*convertClassException krn ini hanya interface (View)item */);    return true;
                    default: return false;
                }
            }


    JTable table;    android.support.v7.widget.RecyclerView.AdapterDataObserver data_listener;
    LinearLayoutCompat footer_panel;
    Boolean enable_filter = true, summary=false;    //blm dipake >> enable_edit=false;
    android.view.Menu menu;    android.support.v7.widget.Toolbar toolbar;
    android.view.MenuItem Bhapus;
    LinearLayoutCompat menu_panel;    LayoutParams prms;
    @Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {    //Use onCreateView when the entire view of the dialog is going to be defined via custom XML.
android.util.Log.e("edit: ", "111 form==null " + (form==null) );
        form.getDialog().setTitle(modul);
        form.getDialog().getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //setHasOptionsMenu(true);

        LinearLayoutCompat lview = new LinearLayoutCompat(form.getActivity());    //getApplicationContext()
        view = (ViewGroup) lview;

        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
        //prms.gravity = Gravity.CENTER_VERTICAL;
        panel = new RelativeLayout(form.getActivity());    //panel = new LinearLayoutCompat(form.getActivity());
        panel.setFocusableInTouchMode(true);    //untested: to not focus to last component then scrolled it to bottom :p
        //panel.setOrientation(LinearLayoutCompat.VERTICAL);
        view.addView( panel, prms );


android.util.Log.e("edit: ", "2" );
        //creating menu_panel
        menu_panel = new LinearLayoutCompat(form.getActivity());
        //android.widget.RelativeLayout menu_panel = new android.widget.RelativeLayout(form.getActivity());
        menu_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);
        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        panel.addView( menu_panel, prms );

        RelativeLayout toolbar_panel = (RelativeLayout) inflater.inflate(R.layout.toolbar, container, false);
        menu_panel.addView( toolbar_panel, prms );

android.util.Log.e("edit: ", "3" );
        toolbar = (android.support.v7.widget.Toolbar) toolbar_panel.findViewById(R.id.toolbar);    //new android.support.v7.widget.Toolbar(form.getActivity());
        //theme ga bisa diset dari program >> toolbar.setStyle( android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Dark_ActionBar );
//        toolbar.setPopupTheme( android.support.v7.appcompat.R.style.ThemeOverlay_AppCompat_Light );
//        ((android.support.v7.app.AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


/*
        toolbar.setTitle("Post");
        //Set naviagtion icon to back button drawable
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// handle back button naviagtion
                dismiss();
            }
        });
*/

android.util.Log.e("edit: ", "4" );


android.util.Log.e("edit: ", "7" );



        db = retail.db; //hanya alias aja:p agar ga kepanjangan
android.util.Log.e("edit: ", "8" );
        if( sql.length()>0 ) {
android.util.Log.e( "edit: ", "81" );
            db.enable_filter = enable_filter;
android.util.Log.e( "editbrg: ", "82" );
            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
                    android.util.Log.e("edit: ", "71" );
                    build_sql();    //di Fedit_brg, Cagent yg berisi data supplier harus ready dulu utk mengisi kolom supplier!!!
                    return null;
                }
                @Override protected void onPostExecute(Void v) {



            new android.os.AsyncTask<Void, Void, Void> () {
                @Override protected Void doInBackground( Void... v ) {
android.util.Log.e("edit: ", "72" );
                    db.exec( sql, null, false, true );
android.util.Log.e("edit: ", "88" );
                    return null;
                }
                @Override protected void onPostExecute(Void v) {
android.util.Log.e("edit: ", "9" );
                    if( !db.err_msg.equals("") ) { error("Pembacaan Data Gagal");    close();    return;  }    //luweh >>  return (View) view;
android.util.Log.e("edit: ", "10" );
                    table = new JTable( db, form.getActivity() );        //View view = inflater.inflate(R.layout.fragment_recycler, container, false);
                    create_next();
                }

            }.execute();



                }

            }.execute();
        } else {
android.util.Log.e("edit: ", "71" );
                    build_sql();
                    create_next();
        }

        return (View) view;    
    }

    android.support.v7.widget.SearchView searchView;    String delayed_search;    android.os.Handler handler = new android.os.Handler();

    void create_next() {

String debug="";
try{
        //final int row_height = 32;

debug+="1";

debug+="5"; android.util.Log.e("edit: ", debug /*+ db.is_editable*/ );


        toolbar.inflateMenu(R.menu.edit);
        menu = toolbar.getMenu();
        Bhapus = menu.findItem(R.id.action_remove);
        Bhapus.setEnabled(false);
        Bhapus.setVisible( db.is_editable );
        if( db.is_editable ) ((Table_adapter)table.getAdapter()).Bhapus = Bhapus;    //agar Bhapus bisa terenable saat select_row() :p

        toolbar.setOnMenuItemClickListener(this);
        /*
        toolbar.setOnMenuItemClickListener( new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick( android.view.MenuItem item ) {
                switch( item.getItemId() ) {
                    case R.id.action_remove: hapus();    break;
                    default: return true;
                }

                return true;
            }
        });
        */

android.util.Log.e("edit: ", "5" );
        //final android.support.v7.widget.SearchView searchView;
        final android.view.MenuItem searchItem = menu.findItem(R.id.action_search);    //menu.findItem(R.id.action_search);
        if( enable_filter ) {
            searchView = (android.support.v7.widget.SearchView) android.support.v4.view.MenuItemCompat.getActionView(searchItem);
            searchView.setQueryHint("Cari");
            handler = new android.os.Handler();
            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override public boolean onQueryTextSubmit(String search) {
                    handler.removeCallbacksAndMessages(null);
                    ((Table_adapter)table.getAdapter()).filter(search);
                    return true;
                }
                @Override public boolean onQueryTextChange(String search) {
                    delayed_search = search;    //delayed search >> thanks to http://stackoverflow.com/users/2336734/killianke
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new Runnable() { @Override public void run() {
                            ((Table_adapter)table.getAdapter()).filter(delayed_search);
                    }}, 900);
                    return true;
                    //return onQueryTextSubmit(search);
                }
            });
            if( barcode_init.length()>0 ) searchView.setQuery(barcode_init,true);
            searchView.findViewById(R.id.search_src_text).setOnClickListener(retail.scan_listener);
            searchView.setIconified(false);    //Expand the SearchView
        } else
            searchItem.setVisible(false);


android.util.Log.e("edit: ", "6" );




        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //layoutManager.scrollToPosition(0);
        table.setLayoutManager( new LinearLayoutManager( form.getActivity() ) );    //table.setLayoutManager( new RecyclerView.LayoutManager() );

        table.setHasFixedSize(true);        // allows for optimizations if all items are of the same size.    // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView

        //ga usah pake pemisah baris :p >> table.addItemDecoration( new DividerItemDecoration(form.getActivity(), DividerItemDecoration.VERTICAL_LIST) );
        //table.setItemAnimator(new DefaultItemAnimator());        // this is the default; this call is actually only necessary with custom ItemAnimators

        //table.setVerticalScrollBarEnabled(true);
        table.setScrollBarStyle( View.SCROLLBARS_INSIDE_OVERLAY );    //ini mlh ga muncul samasekali >> OUTSIDE_INSET
        table.setScrollbarFadingEnabled(true);    //android:scrollbarFadeDuration="0"    //Added in API level 11  table.setVerticalScrollbarPosition (View.SCROLLBAR_POSITION_LEFT)

        android.widget.HorizontalScrollView scroll_panel = new android.widget.HorizontalScrollView( form.getActivity() );    //supaya ga keganggu saat soft keyboard muncul
        //scroll_panel.setOrientation(LinearLayoutCompat.VERTICAL);
        scroll_panel.setFillViewport(true);
        scroll_panel.setScrollbarFadingEnabled(true);
        RelativeLayout.LayoutParams prms2 = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        menu_panel.setId(1000);
        prms2.addRule(RelativeLayout.BELOW, menu_panel.getId());
        if( footer_panel!=null ) {
            footer_panel.setId(1001);
            prms2.addRule(RelativeLayout.ABOVE, footer_panel.getId());
        }
        //prms.gravity = Gravity.CENTER_VERTICAL;
        panel.addView( scroll_panel, prms2 );


        LinearLayoutCompat sub_panel = new LinearLayoutCompat(form.getActivity());
        sub_panel.setOrientation(LinearLayoutCompat.VERTICAL);
        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        scroll_panel.addView( sub_panel, prms );

        //creating column header
        LinearLayoutCompat header_panel = new LinearLayoutCompat(form.getActivity());
        header_panel.setOrientation(LinearLayoutCompat.HORIZONTAL);
        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        sub_panel.addView( header_panel, prms );

        int height = (int) ( 50 * retail.scale_height ), border_with=1;
        for( int i=0; i<db.col_width.length; i++ ) {
            if( db.col_width[i] == 0 ) continue;
            TextView header = new TextView(form.getActivity());
            header.setClickable(true);    header.setFocusable(true);    header.setFocusableInTouchMode(true);    //utk mentrigger col_editor lostfocus agar col_editor tersebut terremove
            header.setText( db.getColumnName(i) );    header.setFocusable(true);    header.setFocusableInTouchMode(true);    //utk mentrigger col_editor lostfocus agar col_editor tersebut terremove
            header.setEllipsize( android.text.TextUtils.TruncateAt.END );

            int align = Gravity.LEFT;
            if( db.col_type[i]==Types.INTEGER || db.col_type[i]==Types.SMALLINT )   align = Gravity.RIGHT;    //column.setCellRenderer( columnNames[i].indexOf("_id")==columnNames[i].length()-3 ? retail.number_renderer_no_digit_separator : retail.number_renderer );
            header.setPadding(5,5,5,5);
            int width = (int) Math.abs( db.col_width[i] /* * retail.scale_width */ ) - border_with;
            if( width>=9990 ) {
                //width = LayoutParams.FILL_PARENT;    //dari caller, tentukan satu kolom aja yg widthnya flexible misal kolom nama, dengan cara ngeset widthnya=10000
                //header.setMinWidth(120);
                width = 120;
                header.setWidth(width);
            }
            header.setMinHeight(height);    //header.setWidth( Math.abs(db.col_width[i]) );    //paksa aja agar colom data juga sama lebarnya >> header.setMinWidth( Math.abs(db.col_width[i]) );    //MaxLines(2);
            header.setBackgroundResource( android.R.color.background_dark );

            prms = new LayoutParams( width, height );
            header.setGravity( Gravity.CENTER_VERTICAL | align);    //prms.gravity = Gravity.CENTER_VERTICAL | align ;
            //prms.setMargins( 10, 5, 10, 0 );
            header_panel.addView( header, prms );

            View border = new View(form.getActivity());
            border.setBackgroundResource( android.R.color.transparent /*transparent #00000000  black*/ );
            prms = new LayoutParams( border_with, height );
            header_panel.addView( border, prms );
            //blm dipake >> if( db.col_width[i]>0 ) enable_edit=true;
        }
        //header_panel.setBackgroundResource( android.R.color.background_dark );

        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        sub_panel.addView( table, prms );

        if( footer_panel!=null ) android.util.Log.e("onfooter:", "awal" );
        if( footer_panel!=null ) {
            RelativeLayout.LayoutParams prms1 = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, 50+7 );
            prms1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            panel.addView( footer_panel, prms1 );    //add footer_panel sent by subclass
        }

        if( summary ) {
            refresh_summary();
            data_listener = new android.support.v7.widget.RecyclerView.AdapterDataObserver() {
                public void onChanged() { refresh_summary(); }
                public void onItemRangeChanged( int positionStart, int itemCount ) { onChanged(); }
                public void onItemRangeChanged( int positionStart, int itemCount, Object payload ) { onChanged(); }
                public void onItemRangeInserted (int positionStart, int itemCount ) { onChanged(); }
                public void onItemRangeMoved (int fromPosition, int toPosition, int itemCount ) { onChanged(); }
                public void onItemRangeRemoved (int positionStart, int itemCount ) { onChanged(); }
            };
            ((Table_adapter)table.getAdapter()).registerAdapterDataObserver(data_listener);
        }


/*
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" >

    <Button
        android:id="@+id/button1"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:text="Button"
        android:background="#99cc00" />

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_toRightOf="@id/button1"
        android:text="Button" 
        android:background="#0077cc"/>

</RelativeLayout>
*/

/* later, syaratnya, acitivity / fragment ini memuat:
        extends Activity implements RecyclerView.OnItemTouchListener, View.OnClickListener, ActionMode.Callback 


        // onClickDetection is done in this Activity's onItemTouchListener
        // with the help of a GestureDetector;
        // Tip by Ian Lake on G+ in a comment to this post:
        // https://plus.google.com/+LucasRocha/posts/37U8GWtYxDE
        table.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        // fab
        View fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(this);

*/

        final LinearLayoutManager lm = (LinearLayoutManager) table.getLayoutManager();
        form.getDialog().setOnKeyListener( new android.content.DialogInterface.OnKeyListener() {    //@Override public void onCancel( android.content.DialogInterface dialog ) { }    //doesn't exist >> @Override public void onBackPressed() {}
            @Override public boolean onKey( android.content.DialogInterface dialog, int keyCode, KeyEvent event ) {
                //if( keyCode == KeyEvent.KEYCODE_BACK ) android.util.Log.e("onbackpressed:", "awal event.getAction() == KeyEvent.ACTION_UP:" + (event.getAction() == KeyEvent.ACTION_UP) + " !event.isCanceled():" + !event.isCanceled() + "  form != null:" + (form != null) + "  form.getDialog().isShowing():" + (form != null&&form.getDialog().isShowing()) );
                if( retail.scan_cancelled ) {
android.util.Log.e("onbackpressed:", "scan_cancelled" );
                    retail.scan_cancelled = false;
                    return true;
                } else if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && ((android.app.Dialog)dialog).isShowing() ) {    //} else if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && form != null && form.getDialog().isShowing() ) {
android.util.Log.e("onbackpressed:", "1111" );                    //jika table isediting ... cancel update
                    View cur_focus = ((android.app.Dialog)dialog).getCurrentFocus();    //form.getDialog().getCurrentFocus();    //getActivity()
                    android.util.Log.e("onbackpressed:", "cur_focus!=null="+ (cur_focus!=null) + "   cur_focus.getParent()!=null="+ (cur_focus.getParent()!=null)  );
                    if( cur_focus!=null && cur_focus.getParent()!=null )
                        if( cur_focus.getParent() instanceof android.widget.ViewSwitcher ) {
                            final android.widget.ViewSwitcher col_switcher_this = ((android.widget.ViewSwitcher) cur_focus.getParent()) ;
                            android.util.Log.e("onbackpressed:", "col_switcher_this!=null="+ (col_switcher_this!=null)  );
                            if( col_switcher_this!=null && col_switcher_this.getDisplayedChild()==1 ) {
                                ((Table_adapter)table.getAdapter()).remove_editor( cur_focus, false );
                               return true;
                            }
                        }
                    close();
                } else if( ( keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_CAMERA ) && ((android.app.Dialog)dialog).isShowing() && db.is_editable ) {    //} else if( ( keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_CAMERA ) && form != null && form.getDialog().isShowing() && db.is_editable ) {
                    int cc=0;
android.util.Log.e("key:", "detected ");
                    for( int i=table.getRowCount()-1; i>=0; i-- )
                        for( int c=0; c<db.col_width.length; i++ ) {
                            if( db.col_width[c]<=0 ) continue;
                            if( cc==1 ) return true;
                            cc=1;
                            String val = db.getStringAt( i, c ).toString();
                            if( !val.equals("0") && val.length()>0 ) return true;
                            final int row=i, col=c;
                            //Only the original thread that created a view hierarchy can touch its views. >> new android.os.AsyncTask<Void, Void, Void> () {   @Override protected Void doInBackground( Void... v ) {
android.util.Log.e("row > lm.findLastCompletelyVisibleItemPosition():", " " + row +" > "+ lm.findLastCompletelyVisibleItemPosition() );
                            if( lm.findLastCompletelyVisibleItemPosition() < row ) lm.scrollToPosition(row);

                                      //luweh, toh dia masi harus mengarahkan barcode ke tengah camera hp >> if( barcode_target.getText().toString().equals("") )
android.util.Log.e("camera:", "1");
                            ((Table_adapter)table.getAdapter()).select_row(row);
android.util.Log.e("camera:", "2");
android.util.Log.e("camera:", "21 ini sering null:" + ( lm.findViewByPosition(row) ==null) );
                            //retail.barcode_target = (TextView) ((ViewGroup) ((ViewGroup) ((ViewGroup) lm.findViewByPosition(row)).getChildAt(0)).getChildAt(col)).getChildAt(0);
                            retail.barcode_target = ((Table_adapter)table.getAdapter()).col[col];    //col[col] di sini sebetulnya ga akan diapai2n oleh retail.scan :p
                            Object[] tag = new Object[] { db.col_editor[col], db, row };
                            retail.barcode_target.setTag( tag );
android.util.Log.e("camera:", "3");
                            retail.scan();
android.util.Log.e("camera:", "4");
                             //}}, 200);    //}}.execute();



                            return true;
                        }
                } else if( ( keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_POWER ) && ((android.app.Dialog)dialog).isShowing() ) {    //} else if( ( keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_POWER ) && form != null && form.getDialog().isShowing() ) {
                    android.util.Log.e("key logcat:", "detected ");
                    StringBuilder log=new StringBuilder();
                    try{
                      Process pr = new ProcessBuilder().command("logcat", /*"\"*:E\"",*/ "-d", /*"-c",*/ "*:e").redirectErrorStream(true).start();
                      //Process pr = Runtime.getRuntime().exec("logcat \"*:E\" -d");
                      //pr.waitFor();
                      java.io.BufferedReader bufferedReader = new java.io.BufferedReader( new java.io.InputStreamReader(pr.getInputStream()));
                      String line = "";
                      while ((line = bufferedReader.readLine()) != null) log.append(line+"\n");
                      if( pr.waitFor()==0 ) pr.destroy();
                      bufferedReader.close();
                      retail.show_error( log.toString(), "logcat" );
                    } catch( Exception e1 ) {
                        retail.show_error( "Exception pada logcat: "  + e1.getMessage(), "error" );
                    }

                    return true;
                }


                return false;
            }
        });


} catch (Exception e) { retail.show_error( e + " debug=" +debug, "Edit" ); }

    }

/*
    @Override public void onResume() {
android.util.Log.e("Fedit", "onresume" ) ;
        super.onResume();
        form.view.postDelayed(new Runnable() { @Override public void run() {    retail.hideSoftKeyboard( form.getActivity() );    }},500);    //already give me a lot of problem
    }
*/

    void refresh_summary() {
    }

    Boolean build_sql() {
        return false;
    }
    void after_save_succeed() {
    }
    void close() {
        if( summary ) ((Table_adapter)table.getAdapter()).unregisterAdapterDataObserver(data_listener);
        //try { db.rs.close(); } catch (Exception e) {}
        //unfully tester: never close the retail.db to get permanent connection
        //anyway, subclasses may create their own db (like Fedit_kas) :p, so ...
        if( db!=retail.db ) db.close();
        //retail.barcode_target = null;
        dismiss();
android.util.Log.e("after : ", "dismiss" );
        form=null;
    }
    void error(String title) {
        android.util.Log.e( title, db.err_msg );    //mboh, malah ga bisa tampil >> retail.show_error( db.err_msg, title );    //JOptionPane.showMessageDialog( this, db.err_msg, title, JOptionPane.ERROR_MESSAGE);
        close();
    }

}