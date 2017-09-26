/*
 */

package net.muhajirin.solusitoko;


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;


public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	//private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	//private HashMap<String, List<String>> _listDataChild;
	//private int next_level;
        //private android.graphics.drawable.Drawable groupIndicator;
        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.myDrawable));

        public org.json.JSONArray items;    //ArrayList<jcdb_item> items;
	public ExpandableListAdapter( Context context, /*android.graphics.drawable.Drawable groupIndicator, */org.json.JSONArray items ) {    //public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, int next_level ) {
		this._context = context;
		this.items=items;
android.util.Log.e("items: ", " " + items );

		//this.groupIndicator=groupIndicator;
	}
	public ExpandableListAdapter( Context context/*, android.graphics.drawable.Drawable groupIndicator */) {    //public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, int next_level ) {
		this( context, /*groupIndicator, */null );
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
            Object ret = "";
            try {
                org.json.JSONObject product = items.getJSONObject(groupPosition);
                org.json.JSONArray attributes = new org.json.JSONArray( product.getString( "attributes" ) );
                ret = attributes.length()>childPosititon ? attributes.getJSONObject(childPosititon) : "" ;    //ret = attributes.length()>childPosititon ? attributes.getJSONObject(childPosititon) : product.getString( "label" ) ;
            } catch( org.json.JSONException e ) {
                        retail.show_error( "Gagal memuat product adapter di getChild() !\nPesan Kesalahan:" + e.toString(), "Gagal memuat product adapter!" );
                        android.util.Log.e( "Gagal memuat product adapter di getChild() !\nPesan Kesalahan:" , e.toString() );
            }
            return ret;    //return this._listDataChild.get( String.valueOf( this._listDataHeader.get(groupPosition)) ).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
//?
		return childPosition;
	}

    @Override public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final android.content.Context context = parent.getContext() ;    //this._context ;
        try {
            final org.json.JSONObject product_child = (org.json.JSONObject) getChild( groupPosition, childPosition );
            org.json.JSONArray attributes = new org.json.JSONArray( product_child.getString( "attributes" ) );
//android.util.Log.e("getChildView: attributes=", "" + attributes );
            if( attributes.length()>0 ) {
                final android.widget.ExpandableListView next_level_view = new android.widget.ExpandableListView(context);
                final ExpandableListAdapter next_level_adapter = new ExpandableListAdapter( context, /*groupIndicator, */ new org.json.JSONArray().put( product_child ) );
//	        if (convertView == null) {    //later check if covertview containing expandablelist ... then only setadapter ...


                    //convertView = (View) next_level_view;


                    LinearLayoutCompat panel = new LinearLayoutCompat(context);
                    panel.setOrientation(LinearLayoutCompat.VERTICAL);

                    LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, 35*attributes.length() + 38 );
                    prms.setMargins( 30, 5, 5, 5 );    panel.addView( next_level_view, prms );


        LinearLayoutCompat  lview = new LinearLayoutCompat(context);
        ViewGroup view = (ViewGroup) lview;

        prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        //prms.gravity = Gravity.CENTER_VERTICAL;
        view.addView( panel, prms );



                    convertView = (View) view;


//			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = infalInflater.inflate(R.layout.list_group, null);





                    //new android.os.Handler().post(new Runnable() { public void run() {
                        next_level_view.setAdapter(next_level_adapter);
                        next_level_view.expandGroup(0);
                        //next_level_view.setFocusable(true);
                        //next_level_view.setOnItemClickListener(retail.sync_brg);

        next_level_view.setOnGroupClickListener(new android.widget.ExpandableListView.OnGroupClickListener() { @Override public boolean onGroupClick( android.widget.ExpandableListView parent, View view, int groupPosition, long id ) {
            view = ((ViewGroup)view).getChildAt(0);    //to get the textview object
android.util.Log.e("onlistener:", "view.getTag()==null" + (view.getTag()==null) );
            if( view.getTag()==null ) return false;    //abaikan saat klik product dengan attribut
android.util.Log.e("onlistener:", "aa1");
            int key = Integer.valueOf(view.getTag().toString()) ;
            int position = retail.Cname_brg.my_index_of_key(key);    //later: need to optimize
            int filter_position = position;

android.util.Log.e("onlistener:", "aa2");
            retail._sync_brg( retail.Cname_brg, position, filter_position );
android.util.Log.e("onlistener:", "aa3");

            android.widget.Toast.makeText(context, "next_level_view. grup clicked", android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }});


        next_level_view.setOnChildClickListener(new android.widget.ExpandableListView.OnChildClickListener() { @Override public boolean onChildClick( android.widget.ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
            view = ((ViewGroup)view).getChildAt(0);    //to get the textview object
android.util.Log.e("onlistener:", "view.getTag()==null" + (view.getTag()==null) );
            if( view.getTag()==null ) return false;    //abaikan saat klik product dengan attribut
android.util.Log.e("onlistener:", "aa1");
            int key = Integer.valueOf(view.getTag().toString()) ;
            int position = retail.Cname_brg.my_index_of_key(key);    //later: need to optimize
            int filter_position = position;

android.util.Log.e("onlistener:", "aa2 key=" + key + "  position =" + position  );
            retail._sync_brg( retail.Cname_brg, position, filter_position );
android.util.Log.e("onlistener:", "aa3");

            android.widget.Toast.makeText(context, "next_level_view.child clicked", android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }});







                    //}});

//}




                /* if u want to collapse the others
                next_level_view.setOnGroupExpandListener( new android.widget.ExpandableListView.OnGroupExpandListener() {
 
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        for (int i = 0; i < getGroupCount(); i++) {
                            if (i != groupPosition) {
                                next_level_view.collapseGroup(i);
                            }
                        }
                    }
                });

                */

            } else {
                //TextView txtListChild = new TextView(context);    //  (android.support.v7.app.AppCompatActivity) this._context  

//                if (convertView == null) {
/*
                    LinearLayoutCompat panel = new LinearLayoutCompat(context);
                    panel.setOrientation(LinearLayoutCompat.VERTICAL);

                    LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
                    prms.setMargins( 30, 10, 10, 10 );    panel.addView( txtListChild, prms );

                    prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
                    //prms.gravity = Gravity.CENTER_VERTICAL;
                    convertView = (View) panel;
*/
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
//                }


		final String label = product_child.getString( "label" );
                    //new android.os.Handler().post(new Runnable() { public void run() {
		final TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
                    //new android.os.Handler().post(new Runnable() { public void run() {
		txtListChild.setText( label );
		txtListChild.setTag( product_child.getString( "id" ) );    //txtListChild.setTag( getFlatListPosition( getFlatListPosition (groupPosition, childPosition) ) );

                    //}});
            }


        } catch( org.json.JSONException e ) {
                        retail.show_error( "Gagal memuat product adapter di getChildView() !\nPesan Kesalahan:" + e.toString(), "Gagal memuat product adapter!" );
                        android.util.Log.e( "Gagal memuat product adapter di getChildView() !\nPesan Kesalahan:" , e.toString() );
        }





        return convertView;
    }

	@Override
	public int getChildrenCount(int groupPosition) {
            int ret = 0;
            try {
                org.json.JSONObject product = items.getJSONObject(groupPosition);
                org.json.JSONArray attributes = new org.json.JSONArray( product.getString( "attributes" ) );
                ret = attributes.length();
            } catch( org.json.JSONException e ) {
                        retail.show_error( "Gagal memuat product adapter di getChildrenCount() !\nPesan Kesalahan:" + e.toString(), "Gagal memuat product adapter!" );
                        android.util.Log.e( "Gagal memuat product adapter di getChildrenCount() !\nPesan Kesalahan:" , e.toString() );
            }
            return ret;    //return this._listDataChild.get( String.valueOf( this._listDataHeader.get(groupPosition) ) ).size();
	}

	@Override public Object getGroup(int groupPosition) {
            Object ret = "";
            try {
                org.json.JSONObject product = items.getJSONObject(groupPosition);
                org.json.JSONArray attributes = new org.json.JSONArray( product.getString( "attributes" ) );
                ret = product.getString( "label" ) + (attributes.length()>0?"...":"") ;
            } catch( org.json.JSONException e ) {
                        retail.show_error( "Gagal memuat product adapter di getGroup() !\nPesan Kesalahan:" + e.toString(), "Gagal memuat product adapter!" );
                        android.util.Log.e( "Gagal memuat product adapter di getGroup() !\nPesan Kesalahan:" , e.toString() );
            }
            return ret;    //this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
            return items.length();    //this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
            long ret = 0;
            try {
                org.json.JSONObject product = items.getJSONObject(groupPosition);
                ret = product.getInt( "id" );
            } catch( org.json.JSONException e ) {
                        retail.show_error( "Gagal memuat product adapter di getGroupId() !\nPesan Kesalahan:" + e.toString(), "Gagal memuat product adapter!" );
                        android.util.Log.e( "Gagal memuat product adapter di getGroupId() !\nPesan Kesalahan:" , e.toString() );
            }
            return ret;    //return groupPosition;
	}

    @Override public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

                //android.support.v7.app.AppCompatActivity context = (android.support.v7.app.AppCompatActivity)  this._context ;    //parent.getContext() ;
                android.content.Context context = parent.getContext() ;    //this._context ;




/*<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="8dp" 
    android:background="#000000">


    <TextView
        android:id="@+id/lblListHeader"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:paddingLeft="?android:attr/expandableListPreferredItemPaddingLeft"
        android:textSize="17dp"
        android:textColor="#f9f93d" />

</LinearLayout>
*/

	//TextView lblListHeader = new TextView(context);
        if (convertView == null) {

/*
		lblListHeader.setTextColor( android.graphics.Color.parseColor("#f9f93d") );    
		lblListHeader.setTypeface(null, Typeface.BOLD);

            LinearLayoutCompat panel = new LinearLayoutCompat(context);
            panel.setOrientation(LinearLayoutCompat.VERTICAL);

android.util.Log.e("getGroupView: ", " 5" );

            LayoutParams prms = new LayoutParams( LayoutParams.MATCH_PARENT, 50 );
            prms.setMargins( 13, 10, 0, 10 );    panel.addView( lblListHeader, prms );

            //prms = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
            //prms.gravity = Gravity.CENTER_VERTICAL;
            convertView = (View) panel;

*/
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
        }


/*
if ( getChildrenCount( groupPosition ) == 0 ) {
       android.R.attr.groupIndicator.setVisibility( View.INVISIBLE );
    } 
else {
       android.R.attr.groupIndicator.setVisibility( View.VISIBLE );
       //groupIndicator.setImageResource( isExpanded ? R.drawable.group_expanded : R.drawable.group_closed );
    }

*/


		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);

		String headerTitle = (String) getGroup(groupPosition);
		lblListHeader.setText(headerTitle);
		if( !headerTitle.endsWith("...") ) lblListHeader.setTag( getGroupId(groupPosition) );    //lblListHeader.setTag( getFlatListPosition( getFlatListPosition (groupPosition, childPosition) ) );

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


/*@Override
protected void bindGroupView(View view, Context paramContext, Cursor cursor, boolean paramBoolean){
    if ( getChildrenCount( groupPosition ) == 0 ) {
       indicator.setVisibility( View.INVISIBLE );
    } else {
       indicator.setVisibility( View.VISIBLE );
       indicator.setImageResource( isExpanded ? R.drawable.list_group_expanded : R.drawable.list_group_closed );
    }
}
*/


}

