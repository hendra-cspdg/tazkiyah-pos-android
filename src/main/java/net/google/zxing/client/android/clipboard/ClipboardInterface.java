/*
 * Copyright (C) 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.clipboard;

import android.content.ClipData;
/* hanya di android versi baru >> import android.content.ClipboardManager; */
import android.content.Context;
import android.util.Log;

public final class ClipboardInterface {
  
  private static final String TAG = ClipboardInterface.class.getSimpleName();

  private ClipboardInterface() {
  }

  private static Boolean new_version = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB ;

  public static CharSequence getText(Context context) {
    ClipData clip = null;
    if( new_version ) {
        //Log.e("kok", " new version=" + android.os.Build.VERSION.SDK_INT );
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);    //getManager_new(context);
        clip = clipboard.getPrimaryClip();
    } else {
        android.text.ClipboardManager clipboard = getManager_old(context);
        clip = ClipData.newPlainText( null, clipboard.getText() );    //new ClipData( new android.content.ClipDescription("a", new String[] {"MIMETYPE_TEXT_PLAIN"} ), new android.content.ClipData.Item( clipboard.getText() ) );
    }
    return hasText(context) ? clip.getItemAt(0).coerceToText(context) : null;
  }

  public static void setText(CharSequence text, Context context) {
    if (text != null) {
      try {
        if( new_version ) ((android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText(null, text));    //getManager_new(context);
        else              getManager_old(context).setText(text);
      } catch (NullPointerException | IllegalStateException e) {
        // Have seen this in the wild, bizarrely
        Log.w(TAG, "Clipboard bug", e);
      }
    }
  }

  public static boolean hasText(Context context) {
    ClipData clip = null;
    if( new_version ) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);    //getManager_new(context);
        clip = clipboard.getPrimaryClip();
    } else {
        android.text.ClipboardManager clipboard = getManager_old(context);
        clip = ClipData.newPlainText( null, clipboard.getText() );    //new ClipData( new android.content.ClipDescription("a", new String[] {"MIMETYPE_TEXT_PLAIN"} ), new android.content.ClipData.Item( clipboard.getText() ) );
    }
    return clip != null && clip.getItemCount() > 0;
  }
  
/*
  private static ClipboardManager getManager(Context context) {
    return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
  }
*/
  
/*
  private static android.content.ClipboardManager getManager_new(Context context) {
    return (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
  }
*/

  private static android.text.ClipboardManager getManager_old(Context context) {
    return (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
  }

}
