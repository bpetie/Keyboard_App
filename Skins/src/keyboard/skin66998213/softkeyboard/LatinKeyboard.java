/*
 * Copyright (C) 2008-2009 The Android Open Source Project
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

package keyboard.skin66998213.softkeyboard;

import keyboard.skin66998213.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class LatinKeyboard extends Keyboard {

  private Key mEnterKey;
  private Key mSpaceKey;
  private Key qKey;
  private Context mContext;

  public LatinKeyboard(Context context, int xmlLayoutResId) {
    super(context, xmlLayoutResId);
    mContext = context;
  }

  public LatinKeyboard(Context context, int layoutTemplateResId,
      CharSequence characters, int columns, int horizontalPadding) {
    super(context, layoutTemplateResId, characters, columns, horizontalPadding);
  }

  @Override
  protected Key createKeyFromXml(Resources res, Row parent, int x, int y,
      XmlResourceParser parser) {
    Key key = new LatinKey(res, parent, x, y, parser);
    if (key.codes[0] == 10) {
      mEnterKey = key;
      // key.icon = mContext.getResources().getDrawable(R.drawable.avatar);
    }
    else if (key.codes[0] == ' ') {
      mSpaceKey = key;
    }
    else if (key.codes[0] == 113) {
      qKey = key;
      System.out.println("found key 113 " + key.label);
    }

    return key;
  }

  /**
   * This looks at the ime options given by the current editor, to set the
   * appropriate label on the keyboard's enter key (if it has one).
   */
  void setImeOptions(Resources res, int options) {
    if (mEnterKey == null) {
      return;
    }

    switch (options
        & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
    case EditorInfo.IME_ACTION_GO:
      mEnterKey.iconPreview = null;
      mEnterKey.icon = null;
      mEnterKey.label = res.getText(R.string.label_go_key);
      break;
    case EditorInfo.IME_ACTION_NEXT:
      mEnterKey.iconPreview = null;
      mEnterKey.icon = null;
      mEnterKey.label = res.getText(R.string.label_next_key);
      break;
    case EditorInfo.IME_ACTION_SEARCH:
      mEnterKey.icon = res.getDrawable(R.drawable.sym_keyboard_search);
      mEnterKey.label = null;
      break;
    case EditorInfo.IME_ACTION_SEND:
      mEnterKey.iconPreview = null;
      mEnterKey.icon = null;
      mEnterKey.label = res.getText(R.string.label_send_key);
      break;
    default:
      mEnterKey.icon = res.getDrawable(R.drawable.sym_keyboard_return);
      mEnterKey.label = null;
      break;
    }
  }

  void setSpaceIcon(final Drawable icon) {
    if (mSpaceKey != null) {

      // mSpaceKey.icon =
      // mContext.getResources().getDrawable(R.drawable.avatar);
      // kKey.icon = mContext.getResources().getDrawable(R.drawable.avatar);
      // System.out.println("here" + kKey.toString());

      // Drawable icon2 =
      // mContext.getResources().getDrawable(R.drawable.sym_keyboard_return);
      // icon2.setAlpha(0);
      // mSpaceKey.icon = icon;
    }
  }

  public void setColor(int color) {
    //EditText ntext = null;
    //ntext.getText().replace(0, 0, "q");
    //ntext.setTextColor(color);
    
    //String str = "<FONT COLOR=\"000000\">&</FONT>";
    
    //qKey.label = Html.fromHtml(str);

  }

  static class LatinKey extends Keyboard.Key {

    public LatinKey(Resources res, Keyboard.Row parent, int x, int y,
        XmlResourceParser parser) {
      super(res, parent, x, y, parser);
    }

    /**
     * Overriding this method so that we can reduce the target area for the key
     * that closes the keyboard.
     */
    @Override
    public boolean isInside(int x, int y) {
      return super.isInside(x, codes[0] == KEYCODE_CANCEL ? y - 10 : y);
    }
  }

}
