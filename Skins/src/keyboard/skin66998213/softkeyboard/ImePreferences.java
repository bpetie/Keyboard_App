/*
 * Copyright (C) 2011 The Android Open Source Project
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.inputmethod.InputMethodManager;

import keyboard.skin66998213.R;

/**
 * Displays the IME preferences inside the input method setting.
 */
public class ImePreferences extends PreferenceActivity {
  @Override
  public Intent getIntent() {
    final Intent modIntent = new Intent(super.getIntent());
    modIntent.putExtra(EXTRA_SHOW_FRAGMENT, Settings.class.getName());
    modIntent.putExtra(EXTRA_NO_HEADERS, true);
    return modIntent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // We overwrite the title of the activity, as the default one is
    // "Voice Search".
    setTitle(R.string.settings_name);
  }

  public static class Settings extends
      keyboard.skin66998213.inputmethodcommon.InputMethodSettingsFragment
  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.ime_preferences);

      setInputMethodSettingsCategoryTitle(R.string.language_selection_title);
      setSubtypeEnablerTitle(R.string.select_language);

      // Load the preferences from an XML resource
      // addPreferencesFromResource(R.xml.ime_preferences);
    }
  }
}
