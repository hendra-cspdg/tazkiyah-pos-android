/*
 * table view
 *
 */

package net.muhajirin.solusitoko;

//import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
//import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
//import android.view.LayoutInflater;
//import android.support.v4.app.DialogFragment;

import android.content.Context;
//import android.widget.ImageView;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;

import android.text.InputType;

import java.sql.Types;
import android.view.Gravity;

/*
import android.text.format.DateUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;

*/
import java.util.ArrayList;
import java.util.List;
/*






import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
*/


import android.support.v7.widget.RecyclerView;

public class JTable extends RecyclerView {    //why I named it JTable while it's nothing to di with oracle JTable. This is just one of my pulled hair when i need to porting my java codes to android from nearly scratch.
    TextView[] col;    //hanya alias aja biar ga susah diakses caller
    Table_adapter adapter;
    public JTable( db_connection db, final android.app.Activity activity ) {
        super(activity);


        //ini utk memastikan payload di adapter.notifyItemChanged( row_idx, new Integer(col_idx) ) berhasil memanggil bind() tapi tetap aja gagal, jadi saya panggil adapter.notifyItemChanged( row_idx ) aja di db_connection :p
        android.support.v7.widget.DefaultItemAnimator animator = new android.support.v7.widget.DefaultItemAnimator() {
            @Override public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        animator.setSupportsChangeAnimations(false);
        setItemAnimator(animator);    //setItemAnimator(null);
        db.jtable = this;
        adapter = new Table_adapter(db, activity);
        col = adapter.col;
        //adapter.notifyDataSetChanged();    //ditujukan utk mengisi data ke adapter tapi kayaknya lebih berat krn semua langsung difilled
        setAdapter( adapter );
        this.adapter=adapter;

/*
        addOnScrollListener( new RecyclerView.OnScrollListener() {
            @Override public void onScrolled( RecyclerView recyclerView, int dx, int dy ) {
                if( dy==0 || (ViewGroup)getFocusedChild()==null ) return;   //This callback will also be called if visible item range changes after a layout calculation. In that case, dx and dy will be 0.
                View cur_focus = ((ViewGroup)((ViewGroup)((ViewGroup)getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild();    //this.getActivity.getCurrentFocus();    //ato loop db_col_editor lalu cek visibilitynya?
                android.util.Log.e("onscroll:", "cur_focus!=null="+ (cur_focus!=null)  );
                if( cur_focus!=null && cur_focus.getParent() instanceof android.widget.ViewSwitcher ) {
                    final android.widget.ViewSwitcher col_switcher_this = ((android.widget.ViewSwitcher) cur_focus.getParent()) ;
                    android.util.Log.e("onscroll:", "col_switcher_this!=null="+ (col_switcher_this!=null)  );
                    if( col_switcher_this!=null && col_switcher_this.getDisplayedChild()==1 )
                        adapter.remove_editor( cur_focus, true );
                }
            }
            @Override public void onScrollStateChanged( RecyclerView recyclerView, int newState ) {}
        });
*/
    }
    public JTable( db_connection db, android.support.v7.app.AppCompatActivity activity ) {
         this( db, (android.app.Activity) activity );
    }
    public JTable( db_connection db, android.support.v4.app.FragmentActivity activity ) {
         this( db, (android.app.Activity) activity );
    }
    //state	the new scroll state, one of SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING or SCROLL_STATE_SETTLING
    @Override public void onScrollStateChanged( int state ) {
                if( state!=RecyclerView.SCROLL_STATE_DRAGGING || (ViewGroup)getFocusedChild()==null ) return;   //This callback will also be called if visible item range changes after a layout calculation. In that case, dx and dy will be 0.
                View cur_focus = ((ViewGroup)((ViewGroup)getFocusedChild()).getFocusedChild()).getFocusedChild();    //this.getActivity.getCurrentFocus();    //ato loop db_col_editor lalu cek visibilitynya?
                if( !( cur_focus instanceof ViewGroup ) ) return;    //sometimes i get no viewgroup when i longclick on banyak then double click with scrolling horizontal on diskon
                cur_focus = ((ViewGroup)cur_focus).getFocusedChild();    //this.getActivity.getCurrentFocus();    //ato loop db_col_editor lalu cek visibilitynya?
                android.util.Log.e("onscroll:", "cur_focus!=null="+ (cur_focus!=null)  );
                if( cur_focus!=null && cur_focus.getParent() instanceof android.widget.ViewSwitcher ) {
                    final android.widget.ViewSwitcher col_switcher_this = ((android.widget.ViewSwitcher) cur_focus.getParent()) ;
                    android.util.Log.e("onscroll:", "col_switcher_this!=null="+ (col_switcher_this!=null)  );
                    if( col_switcher_this!=null && col_switcher_this.getDisplayedChild()==1 )
                        ((Table_adapter) getAdapter()).remove_editor( cur_focus, true );
                }
    }

/* better on listener krn di sini dipanggil berulang2
    @Override public void onScrolled( int dx, int dy ) {
                if( dy==0 || (ViewGroup)getFocusedChild()==null ) return;   //This callback will also be called if visible item range changes after a layout calculation. In that case, dx and dy will be 0.
                View cur_focus = ((ViewGroup)((ViewGroup)((ViewGroup)getFocusedChild()).getFocusedChild()).getFocusedChild()).getFocusedChild();    //this.getActivity.getCurrentFocus();    //ato loop db_col_editor lalu cek visibilitynya?
                android.util.Log.e("onscroll:", "cur_focus!=null="+ (cur_focus!=null)  );
                if( cur_focus!=null && cur_focus.getParent() instanceof android.widget.ViewSwitcher ) {
                    final android.widget.ViewSwitcher col_switcher_this = ((android.widget.ViewSwitcher) cur_focus.getParent()) ;
                    android.util.Log.e("onscroll:", "col_switcher_this!=null="+ (col_switcher_this!=null)  );
                    if( col_switcher_this!=null && col_switcher_this.getDisplayedChild()==1 )
                        ((Table_adapter) getAdapter()).remove_editor( cur_focus, true );
                }
    }
*/


    public int getRowCount() {    //like swingx.JTable :p
        return adapter.getItemCount();
    }
    public int getSelectedRow() {
        return adapter.selected_row;
    }
    public void setRowSelectionInterval( int row_idx1, int row_idx2 ) {
        adapter.select_row(row_idx1);    //ga sah enable range selected krn aku ga pernah pake kayaknya
    }
    public db_connection getModel() {
        return adapter.db;
    }

}


    class Table_adapter extends RecyclerView.Adapter<Table_adapter.Row_holder> {
        db_connection db;
        android.util.SparseBooleanArray selectedItems;
        int selected_row = -13;
        Table_adapter( db_connection db, android.app.Activity my_activity ) {
android.util.Log.e("onConstructor:", "1" );
            this.db = db;    this.my_activity = my_activity;
            db.set_table_adapter(this);    //supaya notify2update bisa dikirim balik ke sini:p
            //items = modelData;
            //selectedItems = new android.util.SparseBooleanArray();

            col_count = db.col_width.length;    //metaData.getColumnCount();

            for( int i=0; i<col_count; i++ ) {    //creating db.col_editor
                //column = column_model.getColumn(i+shift);    //column_model diset oleh caller
                //int c = i+shift;
                if( db.col_width[i] == 0 ) {                                     //There is also the way about the TableCellRenderer. But you may ask about this, if this solution does won't work.
                    //column_model.removeColumn(column);    //ato ini? table.removeColumn(table.getColumnModel().getColumn(i)); // REMOVE   //ga sah pake cellEditor.shouldSelectCell :D    //ini masih traverse :( >>  //column.setMinWidth(0);    //column.setMaxWidth(0);
                    //shift--;
                } else {
                    //column.setPreferredWidth( Math.abs(db.col_width[i]) );  //incomparable types:( >> if( db.col_width[i] != null )

                    if( db.col_width[i] > 0 ) { //otherwise jika negatif, berarti non editable:p    //another alternatif (but not preferred by me:)) as TableFTFEditDemo.java >> table.setDefaultEditor(Integer.class, new IntegerEditor(0, 100));
                        //if( columnNames[i].indexOf("bayar_gaji")>=0 ) column.setCellEditor( new Button_CellEditor() );
                        //else
                        if( db.col_editor[i]==null ) {
                            db.col_editor[i] = new EditText(my_activity);    //column.setCellEditor(new filtered_text_CellEditor( get_column_pattern(i) ));
                            int inputType = db.col_type[i]==Types.INTEGER || db.col_type[i]==Types.SMALLINT ? InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL  ;
                            ((EditText)db.col_editor[i]).setInputType(inputType);
                        } //else if( err_msg.equals("")  )  column.setCellEditor(new combo_db_CellEditor( (JComboBox)db.col_editor[i] ));
//else db.col_editor[i] = JCdb.newInstance("SELECT id, name FROM supplier ORDER BY IF(id=1,-111,name)", (android.support.v4.app.FragmentActivity) my_activity );

                        if( db.editor_barcode_target>=0 ) {
                            db.col_editor[db.editor_barcode_target].setTag("barcode target");
                            db.editor_barcode_target = -1;    //reset aja, aneh sebenarnya direset di sini :p
                        }

                        db.col_editor[i].setOnFocusChangeListener(remove_editor_when_lost_focus);
                        //hanya detect hardware key!!! >> db.col_editor[i].setOnKeyListener(cancel_when_back_pressed);
                        db.col_editor[i].setId(i);         //str.setClickable(false);    //str.setEnabled(false);

                        db.col_width[i] = (int) ( db.col_width[i] * retail.scale_width ) ;     //untested, klo di oncreateview kok bikin masalah

                    }
                }
            }
            col_editor = db.col_editor;    //hanya alias aja

            //tetap aja null pointer exception >> creating col here rather than in onCreateViewHolder so we have col object immediately to get accessed by the caller, lagipula, seprtinya bisa terjadi pengulangan proses klo ditaroh di oncreateview
        }


/*
    static android.app.Activity my_activity;
    static android.app.Activity get_my_activity() {
        return my_activity;
    }
*/
        Context context;    android.app.Activity my_activity;
        android.widget.ViewSwitcher[] col_switcher;    TextView[] col;    View[] col_editor;
        int col_count;
        int height = (int) ( ( 50+4 ) * retail.scale_height );
        Row_holder row_holder;
        android.view.MenuItem Bhapus;

        String getCellEditorValue( View editor ) {
            String ret="";
            if( editor instanceof JCdb     ) {
//android.util.Log.e("getCellEditorValue:", "((JCdb)editor).getSelectedItemPosition()=" + ((JCdb)editor).getSelectedItemPosition()  );
//((JCdb)editor).setText("");
//((JCdb)editor).showDropDown();
//((JCdb)editor).performFiltering( "", 0 );
}

            if( editor instanceof JCdb     ) {
                //return //((JCdb)editor).getText().toString();    //
android.util.Log.e("getCellEditorValue:", "1");
//pindah ke remove_editor >> ((JCdb)editor).clear_filter();
android.util.Log.e("getCellEditorValue:", "count=" + ((JCdb)editor).getAdapter().getCount()  );    //getSelectedItemPosition() di sini dlm keadaan masih terfilter()
ret = String.valueOf(((jcdb_item)((JCdb)editor).getAdapter().getItem( ((JCdb)editor).getSelectedItemPosition() ) ).toString());    //getSelectedItem()
android.util.Log.e("getCellEditorValue:", "return=" + String.valueOf(((jcdb_item)((JCdb)editor).getAdapter().getItem( ((JCdb)editor).getSelectedItemPosition() ) ).toString())  );

                return ret;


            } else if( editor instanceof EditText ) {
                ret = ((EditText)editor).getText().toString();
                if( db.getColumnName( editor.getId() ).toLowerCase().contains("harga") ) ret = retail.add_ribuan(ret);
                return ret;
            }
android.util.Log.e("getCellEditorValue:", "end" );
            return "";
        }
        void remove_editor( View v, Boolean post_update ) {
android.util.Log.e("onremove:", "1" );
            final android.widget.ViewSwitcher col_switcher_this = ((android.widget.ViewSwitcher) v.getParent()) ;    //col_switcher[v.getId()];
android.util.Log.e("onremove:", "col_switcher_this!=null="+ (col_switcher_this!=null)  );
            if( col_switcher_this!=null && col_switcher_this.getDisplayedChild()==1 ) { 
android.util.Log.e("onremove:", "2"  );
                TextView col_text = (TextView) col_switcher_this.getChildAt(0);
android.util.Log.e("onremove:", "3"  );
                View editor = (View) col_switcher_this.getChildAt(1);
android.util.Log.e("onremove:", "4"  );
                //no, listener doesn't: jika editor adalah jcdb dan punya listener sync_brg: let listener do setvalue rather then this remove_editor!!! so, set post_update=false >>   if( editor instanceof JCdb && ((android.widget.AdapterView)editor).getOnItemSelectedListener()==retail.sync_brg ) post_update = false;
                Boolean async = editor instanceof JCdb && ((android.widget.AutoCompleteTextView)editor).getOnItemClickListener()==retail.sync_brg  ? false : true ;
android.util.Log.e("onremove:", "5"  );
                //berat >> android.util.Log.e("removing :", "async=" + async + "   editor!!! db.setValueAt( " + getCellEditorValue(editor) + " ,  " + Integer.valueOf(editor.getTag().toString()) + " ,  " + editor.getId() );
android.util.Log.e("onremove:", "6"  );
                if( editor instanceof JCdb ) ((JCdb)editor).clear_filter();
                if( post_update ) db.setValueAt( async, getCellEditorValue(editor), Integer.valueOf(editor.getTag().toString()), editor.getId() );
                col_switcher_this.showPrevious();
                col_switcher_this.removeViewAt(1);
            }
        }
        void remove_editor( View v ) {
            remove_editor( v, true );
        }
        View.OnFocusChangeListener remove_editor_when_lost_focus = new View.OnFocusChangeListener() { @Override public void onFocusChange( View v, boolean hasFocus ) {
            android.util.Log.e("onfocus:", "hasFocus:"+ hasFocus );    //ingat: lost focus terjadi setelah onclick di cell lain!!!
            if( hasFocus ) return;
            remove_editor(v);
        }};

        @Override public Row_holder onCreateViewHolder( ViewGroup parent, int viewType ) {    // Create new views (invoked by the layout manager)
            android.util.Log.e("onCreateViewHolder:", "" );
            context = parent.getContext() ;


/*
<ViewSwitcher
        android:id="@+id/viewSwitcher1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:inAnimation="@android:anim/slide_in_left" >

        <LinearLayout
        android:id="@+id/view1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" >
        <TextView
            android:id="@+id/text"
            android:text="This is simplezdscsdc text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">
            </TextView>

        </LinearLayout>


    <LinearLayout
        android:id="@+id/view2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" >
        <TextView
            android:id="@+id/text"
            android:text="This issdsdsds simplezdscsdc text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">
            </TextView>

        </LinearLayout>
    </ViewSwitcher>

*/



            RelativeLayout row_view = new RelativeLayout(context);        //View row_view = LayoutInflater.from(context).inflate(R.layout.item_demo_01 /*android.R.layout.simple_list_item_1*/, parent, false);
            //tetap aja ga bisa, yg clickable cuman textview2 >> row_view.setClickable(true);    row_view.setFocusable(true);    row_view.setFocusableInTouchMode(true);    //utk lebih efisien mentrigger select_row()

            LinearLayoutCompat panel = new LinearLayoutCompat(context);
            panel.setOrientation(LinearLayoutCompat.HORIZONTAL);


            col_switcher = new android.widget.ViewSwitcher[col_count];     col = new TextView[col_count];

            //TableColumn column;
            //int shift=0;
            for( int i=0; i<col_count; i++ ) {
                //column = column_model.getColumn(i+shift);    //column_model diset oleh caller
                //int c = i+shift;
                if( db.col_width[i] == 0 ) {                                     //There is also the way about the TableCellRenderer. But you may ask about this, if this solution does won't work.
                    //column_model.removeColumn(column);    //ato ini? table.removeColumn(table.getColumnModel().getColumn(i)); // REMOVE   //ga sah pake cellEditor.shouldSelectCell :D    //ini masih traverse :( >>  //column.setMinWidth(0);    //column.setMaxWidth(0);
                    //shift--;
                } else {
                    //column.setPreferredWidth( Math.abs(db.col_width[i]) );  //incomparable types:( >> if( db.col_width[i] != null )

                    //creating TextView and (if it's editable) it's switcher parent
                    col[i] = new TextView(context);    //my_activity
                                                  /* {
                                                  @Override public boolean onTouchEvent(android.view.MotionEvent event){       
                                                     android.util.Log.e("row:", ""+ vh.getAdapterPosition() );
                                                     if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                                                         //if (!isEnabled() && mListner != null ) { //if (!isClickable() && mListner != null) {
                                                         android.util.Log.e("not:", "!isenabled");
                                                         if( ((android.app.Activity)context).getCurrentFocus()!=null ) {
                                                             android.util.Log.e("last:", "focus");
                                                             ((android.app.Activity)context).getCurrentFocus().setClickable(false);    //setEnabled(false);
                                                         }
                                                         setClickable(true);    //setEnabled(true);
                                                         new android.os.Handler().postDelayed( new Runnable() { public void run() {
                                                                setClickable(false);    //behave like double click :p
                                                         }}, 1000);
                                                         //            mListner.onClickWhenEnabled();

                                                         return true;
                                                     }
                                                     return super.onTouchEvent(event);
                                                 }};    */

                    col[i].setId(i);    //mengidentifikasi cell pada kolom yg mana saat dionlongclicklistener
                    //?col[i].setText(db.getValueAt(i,?));
                    col[i].setClickable(true);    col[i].setFocusable(true);    col[i].setFocusableInTouchMode(true);    //utk mentrigger col_editor lostfocus agar col_editor tersebut terremove

                    col[i].setEllipsize( android.text.TextUtils.TruncateAt.END );

                    int align = Gravity.LEFT;
                    //if( db.col_editor[i]!=null && db.col_editor[i]==retail.Cname_brg ) align = Gravity.LEFT;    //column.setCellRenderer( retail.right_align_renderer );    //&& i==1   //(kayaknya beresiko klo dibatalin return Stringnya) name barang kayaknya bagus rata kanan deh...
                    //else
                    //if( db.col_editor[i]==null )    //this take my 3 hours :( ... the error does not raise ... just form not showing apparently :p
                        //if( columnNames[i].indexOf("banyak")>=0 )                                           column.setCellRenderer( retail.number_center_renderer );
                        //else if( columnNames[i].indexOf("bayar_gaji")>=0 ) column.setCellRenderer( new Button_CellEditor() );    //ga bisa retail.button_CellEditor
                        //else
                        if( db.col_type[i]==Types.INTEGER || db.col_type[i]==Types.SMALLINT )   align = Gravity.RIGHT;    //column.setCellRenderer( columnNames[i].indexOf("_id")==columnNames[i].length()-3 ? retail.number_renderer_no_digit_separator : retail.number_renderer );
//if( db.col_type[i]==Types.INTEGER || db.col_type[i]==Types.SMALLINT )   android.util.Log.e("INTEger:", "iiii" );
                        //else if( columnNames[i].indexOf("diskon")>=0 || columnNames[i].indexOf("disc")>=0 ) column.setCellRenderer( retail.percent_renderer );
                        //else if( db.col_type[i]==Types.TIME )                                                                          column.setCellRenderer( retail.time_renderer );


                    //ini malah bikin dia cuman satu baris :) col[i].setInputType( InputType.TYPE_TEXT_FLAG_MULTI_LINE );


                    col[i].setPadding(5,5,5,5);

                    final int height    = retail.is_number( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) && Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) > 16 ? Integer.valueOf( db.cfg.get("tinggi_baris_di_tabel_transaksi") ) : 60;
                    //aeng >> final int font_size = retail.is_number( db.cfg.get("font_size_di_tabel_transaksi") ) ? Integer.valueOf( db.cfg.get("font_size_di_tabel_transaksi") ) : 16 ;

                    col[i].setMinHeight(height);    //col[i].setMinWidth(db.col_width[i]);    //MaxLines(2);
                    //kok ini dijalankan di tiap row?? >> db.col_width[i] = (int) ( db.col_width[i] * retail.scale_width ) ;
                    int width = Math.abs( db.col_width[i] );
                    if( width>=10000 ) {    //9990?
                        ///width = LayoutParams.FILL_PARENT;    //dari caller, tentukan satu kolom aja yg widthnya flexible misal kolom nama, dengan cara ngeset widthnya=10000
                        ///col[i].setMinWidth(120);
                        width = 120;
                    }
                    col[i].setWidth(width);
                    LayoutParams prms = new LayoutParams( width, height );
                    col[i].setGravity( Gravity.CENTER_VERTICAL | align );    //prms.gravity = Gravity.CENTER_VERTICAL | align ;
                    //prms.setMargins( 10, 5, 10, 0 );
                    if( db.col_width[i] > 0 ) { //otherwise jika negatif, berarti non editable:p    //another alternatif (but not preferred by me:)) as TableFTFEditDemo.java >> table.setDefaultEditor(Integer.class, new IntegerEditor(0, 100));
                        //di class Row_holder aja >> col[i].setOnLongClickListener(show_editor);
                        //if( columnNames[i].indexOf("bayar_gaji")>=0 ) column.setCellEditor( new Button_CellEditor() );
                        //else if( db.col_editor[i]==null ) column.setCellEditor(new filtered_text_CellEditor( get_column_pattern(i) ));
                        //else if( err_msg.equals("")  ) column.setCellEditor(new combo_db_CellEditor( (JComboBox)db.col_editor[i] ));

                        col_switcher[i] = new android.widget.ViewSwitcher(context);
                        //bikin masalah ni ...ninggalin jejak gitu di layar (walo sudah pake asynctask sekalipun) >> user.setInAnimation( android.view.animation.AnimationUtils.loadAnimation(context, android.R.anim.fade_in) );    user.setOutAnimation( android.view.animation.AnimationUtils.loadAnimation(context, android.R.anim.fade_out) );

                        col_switcher[i].addView( col[i], -1, prms );

                        prms = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
                        /*prms.setMargins( 10, 5, 10, 0 );*/    panel.addView( col_switcher[i], prms );

                    } else {
                        panel.addView( col[i], prms );

                    }
                }
            }


//android.util.Log.e("screen_width: ", "screen_width=" + retail.screen_width  + "   screen_height=" + retail.screen_height );

            //panel_root.addView( panel, new LayoutParams( 200, LayoutParams.MATCH_PARENT ) );
            //row_view.addView( panel_root, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT ) );
            row_view.addView( panel, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ) );
            //retail.set_listener_to_hide_soft_keboard( row_view, (android.app.Activity)context );
            row_holder = new Row_holder(row_view);
            return row_holder;
        }

        public void bind( final Row_holder row_holder, final int row_idx, final int col_idx ) {
            //if( col_idx!=-3 ) android.util.Log.e("bind!!: ", " col_idx=" + col_idx /* + " db.getStringAt( row_idx, col_idx ) " + db.getStringAt( row_idx, col_idx ) */  );
            if( col_idx==-3 )                    set_row_background( row_holder, row_idx );
            else if( db.col_width[col_idx]!=0 )  row_holder.col[col_idx].setText( db.getStringAt( row_idx, col_idx ) );    //di sini bisa rendering kayaknya, kayak ginilah kira2 >> String newStr = DateUtils.formatDateTime( viewHolder.label.getContext(), model.dateTime.getTime(), DateUtils.FORMAT_ABBREV_ALL);
        }
        @Override public void onBindViewHolder( Row_holder row_holder, int row_idx ) {    // Replace the contents of a view (invoked by the layout manager)
//android.util.Log.e("bind:", "row");
            set_row_background( row_holder, row_idx );
            //android.util.Log.e("onBindViewHolder:", "db.getStringAt(row_idx, pos):"+db.getStringAt( row_idx, 3 ) + "  getLayoutPosition()=" + row_holder.getLayoutPosition() + "   getAdapterPosition()=" + row_holder.getAdapterPosition() );

            //int availableWidth = context.getResources().getDisplayMetrics().widthPixels;    //itemView.getContext()
            /*if (position % 4 == 0) {    //ini contoh orientation horizontal tapi katanya bisa scroll vertical juga
                int height = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.card_staggered_height);
                itemView.setMinimumHeight(height);
              } else {
                itemView.setMinimumHeight(0);
              }
            */


            //int height = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.card_staggered_height);

            //if( row_idx % 2 == 1 ) row_holder.itemView.setBackgroundResource( android.R.color.background_dark );
            //else row_holder.itemView.setBackgroundResource( android.R.color.black );
            for( int i=0; i<db.col_width.length; i++ ) bind( row_holder, row_idx, i );
//android.util.Log.e("bind:", "row last");
        }
        @Override public void onBindViewHolder( final Row_holder row_holder, final int row_idx, final List<Object> col_idx ) {    //update hanya satu cell ajah!!
            //new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
//android.util.Log.e("bind first:", ""+row_idx );
            if( col_idx.isEmpty() ) onBindViewHolder( row_holder, row_idx );
            else for( int i=0; i<col_idx.size(); i++ ) bind( row_holder, row_idx, (int) col_idx.get(i) );    //Integer.valueOf( col_idx.get(0).toString() )
//android.util.Log.e("bind lasttt:", ""+row_idx );
            //}},200);
        }

        public void select_row( int row_idx ) {
            if( row_idx==selected_row ) return;
//android.util.Log.e("select_row :", ""+row_idx );
            if( row_idx>=0 && Bhapus!=null && Bhapus.isVisible() ) Bhapus.setEnabled(true);
            final int old_selected_row = selected_row;
            final int new_selected_row = row_idx;
            selected_row = new_selected_row;
            new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
                notifyItemChanged( new_selected_row, new Integer(-3) );    notifyItemChanged( old_selected_row, new Integer(-3) );    //to highlight
            }},500);
        }
        /*
        public void toggleSelection(int pos) {
            if( selectedItems.get( pos, false ) ) selectedItems.delete( pos );
            else                                  selectedItems.put( pos, true );
            notifyItemChanged(pos);
        }
        */

        public void set_row_background( Row_holder row_holder, int row_idx ) {    // Replace the contents of a view (invoked by the layout manager)
            int background = android.graphics.Color.TRANSPARENT;
            if( row_idx==selected_row  ) background = 0xff000033;    //663399
            else if( row_idx % 2 == 1  ) background = android.R.color.background_dark;
            row_holder.itemView.setBackgroundColor( background );    //row_holder.itemView.setBackgroundResource( ( row_idx==selected_row ? android.R.color.black : android.R.color.background_dark ) );        //if( BuildConfig.VERSION_CODE >= 11 ) row_holder.itemView.setActivated( selectedItems.get( row_idx, false ) );
        }

        public void filter( String search ) {
            db.rows.clear();
            search = search.trim();
            if( search.length()==0 ){
                //db.rows.addAll( db.rows_backup );
                for( ArrayList<Object> row : db.rows_backup )
                    if( row.size()>0 ) db.rows.add(row);    //ada yg sudah kena db.removeRow() :p
            } else{
                String[] filtered_column = new String[] { "kode", "nama", "tanggal", "harga","item" };
                search = search.toLowerCase();
                for( ArrayList<Object> row : db.rows_backup ) {
                    for( int i=0; i<col_count; i++ )
                        if( retail.contains( db.getColumnName(i), filtered_column ) ) {
                            if( row.get(i).toString().toLowerCase().contains(search) ) db.rows.add(row);
                            continue;
                        }
                }
            }
            notifyDataSetChanged();
        }

        @Override public int getItemCount() {
            return db.rows.size();
        }
        public void clearSelections() {
            selectedItems.clear();
            notifyDataSetChanged();
        }

        public int getSelectedItemCount() {
            return selectedItems.size();
        }

        public List<Integer> getSelectedItems() {
            List<Integer> items = new ArrayList<Integer>(selectedItems.size());
            for (int i = 0; i < selectedItems.size(); i++) items.add(selectedItems.keyAt(i));
            return items;
        }

        /* ini klo caller (Fedit) mo dapat event onselectedrow
        public interface RecyclerViewClickListener {
            public void recyclerViewListClicked(View v, int position);
        }

        //lalu panggil dari caller ...
        @Override public void recyclerViewListClicked(View v, int position){... ...}

        //lalu set up adapter and pass clicked listener this
        myAdapter = new MyRecyclerViewAdapter(context, this);
        */
        public final /*static*/ class Row_holder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
            TextView[] col;
            /*@Override public void onClick(View view) {
                select_row( getAdapterPosition() );    //to set selected row for highliting and enable deleting
            }*/
            //private static RecyclerViewClickListener itemListener;
            /*
            int selected_row = -13;
            @Override public void onClick(View view) {
               //last_selected_row = selected_row ;
               selected_row = getLayoutPosition();    //getAdapterPosition()    //itemListener.recyclerViewListClicked(v, this.getPosition());
                if( selectedItems.get(getAdapterPosition(), false) ) {
                    selectedItems.delete(getAdapterPosition());
                    //ni kudu styling <selector> di xml pula >>
                    view.setSelected(false);
                    //view.setBackgroundColor(Color.parseColor("#f8f8f8"));
                } else {
                    selectedItems.put(getAdapterPosition(), true);
                    view.setSelected(true);
                }
            }
            */

            /*
            I like your tutorial but I encountered 2 problems when I implemented it in my app. First, as I scrolled the list up and down, with more items than one screen, the background was randomly changed on items I did not selected. The second problem is that in addition to changing the background, I also changed the content of the row and therefore had to call notifyDataSetChanged(). This call reset the background back to non selected.
            How can I have both effects together?

            so, klo ini gagal, pake method2 bawaan recyclerdemo di Fedit-ku
            */

            //int last_selected_row = -13, selected_row = -13;
            View.OnLongClickListener show_editor = new View.OnLongClickListener() { @Override public boolean onLongClick(View v) {
                select_row( getAdapterPosition() );
                //android.util.Log.e("row:", ""+ vh.getAdapterPosition() );
                //v.setEnabled(true);

                /*
                ViewGroup parent = (ViewGroup) v.getParent();
                final int index = parent.indexOfChild(v);
                //removeView(currentView);
                //removeView(newView);
                parent.removeView(v);
                col_editor1.setText( ((TextView)v).getText(), TextView.BufferType.EDITABLE );
                parent.addView(col_editor1, index, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
                */

                final android.widget.ViewSwitcher parent = (android.widget.ViewSwitcher) v.getParent();
                final int c = v.getId()/* - 1000*/;
                //ga iso manggil parent class object:p >> Row_holder row_holder = (Row_holder) parent.getParent().getParent().getParent();
                if( parent.getChildCount()==1 ) {
                    remove_editor( col_editor[c] );
                    parent.addView( col_editor[c], -1, new LayoutParams( db.col_width[c], height ) );
                }
                //last_selected_row = selected_row ;
                //selected_row = row_holder.getLayoutPosition();    //getAdapterPosition()    //itemListener.recyclerViewListClicked(v, this.getPosition());
android.util.Log.e("onlongclick:", "c="+ c + "  parent.getChildCount()=" + parent.getChildCount()  + "  getLayoutPosition()=" + getLayoutPosition() + "   getAdapterPosition()=" + getAdapterPosition() );
                col_editor[c].setTag( getAdapterPosition() );    //selected_row    //getLayoutPosition()    //to use in remove_editor to apply change to db

android.util.Log.e("onlongclick:", "before show_next" );
                if( col_editor[c] instanceof JCdb ) retail.brg_id = -1;    //agar listener tak aktif >> somehow, showNext() yg pertamakali utk JCdb ini mengaktifkan listener:p
                parent.showNext();
android.util.Log.e("onlongclick:", "after show_next" );

                final String cur_value = ((TextView)v).getText().toString();
android.util.Log.e("onlongclick:", "44444" );
                if( col_editor[c] instanceof JCdb ) {
                    //retail.brg_id = -1;    //agar listener tak aktif
                        final JCdb editor = (JCdb)col_editor[c];
android.util.Log.e("onlongclick: ", "Jcdb before  Selectedpos=" + ((JCdb)col_editor[c]).getSelectedItemPosition()   + "  cur_value" + cur_value + "   count=" + editor.getAdapter().getCount()  );
                            //untested >> ga usah aja >> editor.my_setSelectedItem(cur_value);
                            editor.setText(cur_value);
android.util.Log.e("onlongclick: ", "Jcdb after  Selectedpos=" + ((JCdb)col_editor[c]).getSelectedItemPosition() );
//                            if( !editor.mode_dropdown )
                            editor.requestFocusFromTouch();
                } else if( col_editor[c] instanceof EditText ) {
                    ((EditText)col_editor[c]).setText( cur_value, TextView.BufferType.EDITABLE );
                    ((EditText)col_editor[c]).selectAll();
                ///* pindah ke thread baru di bawah aja krn entah kenapa, dia tetap mentrigger listener sync_brg
                }

                //*/

/*
                if( col_editor[c] instanceof JCdb ) retail.brg_id = -1;    //agar listener tak aktif >> somehow, showNext() yg pertamakali utk JCdb ini mengaktifkan listener:p
                parent.showNext();

*/
                    new android.os.Handler().postDelayed(new Runnable() { @Override public void run() {
                if( col_editor[c] instanceof JCdb ) {
                    //new android.os.Handler().post(new Runnable() { @Override public void run() {
                        final JCdb editor = (JCdb)col_editor[c];
/*
                            editor.my_setSelectedItem(cur_value);
                            //editor.items.notifyDataSetChanged();
                        new android.os.Handler().postDelayed(new Runnable() { public void run() {
                            editor.requestFocusFromTouch();
*/
android.util.Log.e("onlongclick: ", "before performClick" );
if( !editor.mode_dropdown ) android.util.Log.e("onlongclick: ", "editor.mode_dropdown " + editor.mode_dropdown );
//                            if( !editor.mode_dropdown ) editor.performClick();    //supaya list item langsung muncul
android.util.Log.e("onlongclick: ", "after performClick" );
                            retail.brg_id = -13;    //agar listener tak aktif
//                        }}, 200);
                    //}});
                } else if( col_editor[c] instanceof EditText ) {
                    //((EditText)col_editor[c]).selectAll();
                    retail.show_soft_keyboard( (View)col_editor[c], my_activity );    //unsolved: ini terjadi sebelum focus lost dari editor terakhir, akibatnya: ada dua editor yg on focus di satu baris (misal: kolom banyak dan harga) :p
                }

                    }}, 300);
                return true;
            }};

            View.OnClickListener scan_listener_on_double_click = new View.OnClickListener() { @Override public void onClick(View v) {    //on doubleclick :p
                retail.barcode_target = (TextView) v;
                //luweh, toh dia masi harus mengarahkan barcode ke tengah camera hp >> if( barcode_target.getText().toString().equals("") )
android.util.Log.e("ondoubbleclick:", "aa v.getTag() ==null=" + (v.getTag() ==null) );

android.util.Log.e("ondoubbleclick:", "aa v.getTag() instanceof Object[]=" + (v.getTag() instanceof Object[]) );
//if( v.getTag() instanceof Object[] ) android.util.Log.e("ondoubbleclick:", "(long) ((Object[])v.getTag())[0]=" + ((long) ((Object[])v.getTag())[0]) );
                if( v.getTag() instanceof Object[] && !(v.getTag() instanceof View) /* << dipake oleh retail.onActivityResult saat continues scan*/  && System.currentTimeMillis() - (long) ((Object[])v.getTag())[0] < 1000 )  retail.scan();
                else select_row( getAdapterPosition() );    //to set selected row for highliting and enable deleting
android.util.Log.e("ondoubbleclick:", "bb" );
                Object[] tag = new Object[] { System.currentTimeMillis(), db, getAdapterPosition() };
android.util.Log.e("ondoubbleclick:", "cc" );
                v.setTag( tag );
            }};
            View.OnClickListener select_row = new View.OnClickListener() { @Override public void onClick(View v) {    //on doubleclick :p
                select_row( getAdapterPosition() );
            }};

            public Row_holder( View row_view ) {
                super(row_view);
                col = new TextView[db.col_width.length];    int shift=0;
                for( int i=0; i<db.col_width.length; i++ ) {
                    if( db.col_width[i]==0 ) {  shift++;    continue;  }
                    ViewGroup parent = (ViewGroup) ((ViewGroup) row_view).getChildAt(0);
                    View v = parent.getChildAt(i-shift);
                    if( db.col_width[i]>0 ) {
                        col[i] = (TextView) ((ViewGroup) v).getChildAt(0);    //it is inside my ViewSwitcher
                        col[i].setOnLongClickListener(show_editor);
                        if( col_editor[i].getTag()!=null && col_editor[i].getTag().toString().equals("barcode target") ) col[i].setOnClickListener(scan_listener_on_double_click);
                        else col[i].setOnClickListener(select_row);
                    } else {
                        col[i] = (TextView) v;
                        col[i].setOnClickListener(select_row);
                    }
                }

                //row_view.setBackgroundResource( R.drawable.statelist_item_background );    //row_view.setSelector( android.R.color.holo_blue_dark );
                //gak clickable :p  >> row_view.setOnClickListener(this);




                /* ini layak dicoba utk memberikan kesan inline editing:p
                android:focusableInTouchMode was added in API Level 1
                This makes the EditText not editable with the keyboard. They keyboard will not display by default. The user can select, copy and cut data from the EditText still. The users IS able to paste data to the EditText still.
                */
                //di constructor ViewHolder aja as http://stackoverflow.com/questions/26076965/android-recyclerview-addition-removal-of-items >> public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

                /*
                <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                    android:id="@+id/container_list_item"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:background="@drawable/statelist_item_background">

                    <include layout="@layout/common_item_layout" />
                <?xml version="1.0" encoding="utf-8"?>

                <LinearLayout xmlns:tools="http://schemas.android.com/tools"
                    xmlns:android="http://schemas.android.com/apk/res/android"
                    android:id="@+id/container_inner_item"
                    android:layout_width="200dp"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:layout_margin="8dp"
                    android:layout_centerVertical="true"
                    tools:showIn="@layout/item_demo_01">

                    <TextView
                        android:id="@+id/txt_label_item"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textAppearance="?android:attr/textAppearanceLarge"
                        tools:text="Item Number One" />

                    <TextView
                        android:id="@+id/txt_date_time"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textAppearance="?android:attr/textAppearanceMedium"
                        tools:text="Item Number One" />
                </LinearLayout>


                </RelativeLayout>

                */
            }
        }
    }//end of class Table_adapter
