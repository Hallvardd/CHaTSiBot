/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.taphan.core1.languageProcessing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taphan.core1.languageProcessing.model.TokenInfo;


public class MainActivity extends AppCompatActivity implements ApiFragment.Callback {

    private static final String FRAGMENT_API = "api";

    private static final int LOADER_ACCESS_TOKEN = 1;


    // TODO: startAnalyze() will need to be added to our onClickListeners !


    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the view pager
        final FragmentManager fm = getSupportFragmentManager();


        // Prepare the API
        if (getApiFragment() == null) {
            fm.beginTransaction().add(new ApiFragment(), FRAGMENT_API).commit();
        }
        prepareApi();
    }


    private ApiFragment getApiFragment() {
        return (ApiFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_API);
    }

    private void prepareApi() {
        // Initiate token refresh
        getSupportLoaderManager().initLoader(LOADER_ACCESS_TOKEN, null,
                new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        return new AccessTokenLoader(MainActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String token) {
                        getApiFragment().setAccessToken(token);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                    }
                });
    }

    private void startAnalyze() {
        // Call the API  TODO: THIS IS WHERE THE MAGIC HAPPENS, PUT OUR INPUT HERE!!
        final String text = mInput.getText().toString();
        getApiFragment().analyzeSyntax(text);
    }



    @Override
    public void onSyntaxReady(TokenInfo[] tokens) {

        String words = "";
        for(TokenInfo t : tokens){
            if(t.partOfSpeech.equals("NOUN")){
                words += t.lemma + "-";
            }
        }
        if(words.length() > 1){
            words = words.substring(0, words.length() - 1);
        }
        Log.d("STUFF", words );
    }
}
