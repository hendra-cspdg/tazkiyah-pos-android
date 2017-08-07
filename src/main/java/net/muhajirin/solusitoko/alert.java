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

//import android.support.v7.app.Activity;
import android.os.Bundle;
//import android.widget.TextView;
//import android.view.View;
//import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

class alert extends DialogFragment {
    public alert() {
        // Empty constructor required for DialogFragment
    }
    private static alert frag;
    public int modal_result=-1, modal_result_cancel=-1;    //public Integer modal_result=-1;    //public final java.util.concurrent.Semaphore dialogSemaphore = new java.util.concurrent.Semaphore( 0, true );    //java.util.concurrent.atomic.AtomicBoolean    //public java.lang.Thread click_thread;
    public static alert newInstance( String msg, String title, String[] btn_txt ) {
        frag = new alert();
    	Bundle args = new Bundle();
    	args.putString("title", title);
    	args.putString("msg", msg);
    	args.putString("btn_txt0",btn_txt[0]);
    	if( btn_txt.length>1 ) args.putString("btn_txt1",btn_txt[1]);
    	if( btn_txt.length>2 ) args.putString("btn_txt2",btn_txt[2]);
    	frag.setArguments(args);
        frag.setCancelable(false);
        my_activity = retail.get_my_app_activity();    //ini null >> this.getActivity();
    	return frag;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {    //Use onCreateDialog when you just need to construct and configure a standard Dialog class (such as AlertDialog) to display.
        //EditText input = new EditText(getActivity()) 
        //my_activity = (android.app.Activity) retail.app_context;    //this.getActivity();
        AlertDialog.Builder alert_builder = new AlertDialog.Builder( get_my_activity() )    //, R.style.AppAlertTheme >> stylenya kok ga mo berubah?! lagian bikin no such method error //getApplicationContext()    //retail.get_my_app_context()
            .setTitle( getArguments().getString("title") )
            .setMessage( getArguments().getString("msg") )
            .setCancelable(false)

            //.setIcon(R.drawable.icon)
            //.setView( input )
            .setPositiveButton( getArguments().getString("btn_txt0"),  new DialogInterface.OnClickListener() { @Override public void onClick( DialogInterface dialog, int which ) {
                modal_result = 0 ;    //get_modal_result();    //new Integer( modal_result_ );    //notify();    //dialogSemaphore.release();   //click_thread.start();
                dialog.cancel();
            }});
        modal_result_cancel = 0;
        if( getArguments().getString("btn_txt1") != null ) {
            alert_builder.setNeutralButton( getArguments().getString("btn_txt1"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
                modal_result = 1;
                dialog.cancel();
            }});
            modal_result_cancel = 1;
        }
        if( getArguments().getString("btn_txt2") != null ) {
            alert_builder.setNegativeButton( getArguments().getString("btn_txt2"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
                modal_result = 2;    modal_result_cancel = 2;
                dialog.cancel();
            }});
            modal_result_cancel = 2;
        }

        Dialog dialog = alert_builder.create() ; 
        dialog.setOnKeyListener( new android.content.DialogInterface.OnKeyListener() {    //@Override public void onCancel( android.content.DialogInterface dialog ) { }    //doesn't exist >> @Override public void onBackPressed() {}
            @Override public boolean onKey( android.content.DialogInterface dialog, int keyCode, KeyEvent event ) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && dialog != null && frag.getDialog().isShowing() ) {
                    modal_result = modal_result_cancel;
                    dialog.cancel();
                    return true;
                }
                return false;
            }
        });

        return dialog;    //untested: request a window without the title >> return alert_builder.create().getWindow().requestFeature(Window.FEATURE_NO_TITLE)


/*
 AlertDialog.Builder builder = new AlertDialog.Builder(this);
 builder.setMessage("Are you sure you want to exit?")
  .setCancelable(false)
   .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
   public void onClick(DialogInterface dialog, int id) {
        MyActivity.this.finish();
   }
 })
 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
   public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
   }
});
AlertDialog alert = builder.create();

*/


    }


    static android.app.Activity my_activity;
    static android.app.Activity get_my_activity() {
        return my_activity;
    }

    public /*synchronized(modal_result)*/ int show_modal( final android.support.v4.app.FragmentManager fm, final String tag/*, final android.app.Activity act */) {
        modal_result = -13;
        get_my_activity().runOnUiThread(new Runnable() { @Override public void run() {
                show( fm, tag );
        }});
        try {
            while( modal_result<0 ) java.lang.Thread.sleep(100);
        } catch( InterruptedException e ) {}
        return modal_result;

            /* all of this, take my 3 days :p ... damn android !!!
            click_thread = new java.lang.Thread( new Runnable() { @Override public void run() {
                //frag.modal_result = modal_result_ ;    // get_modal_result();    //new Integer( modal_result_ );
                synchronized( click_thread ) {
                //Toast.makeText( getBaseContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();
                click_thread.notify();    //E/AndroidRuntime(12280): java.lang.IllegalMonitorStateException: object not locked by thread before notify()
                }

            }}); 

            dialogSemaphore.acquire();
            */
            /*
            synchronized( click_thread ) {
                //while (q.peek() == null)
                //frag.show( fm, tag );
                //notify();
                //modal_result.wait();
            //}
            //}}).start();
            //join();
            //synchronized( modal_result ) {
                click_thread.wait();    //frag.wait();
            }
            */

    }

}
