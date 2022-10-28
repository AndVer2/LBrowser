package com.lpi.lbrowser;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.asksira.webviewsuite.WebViewSuite;

public class MainActivity extends AppCompatActivity {

	EditText search;
	String website;
	WebViewSuite body;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode
				& Configuration.UI_MODE_NIGHT_MASK;
		switch (nightModeFlags) {
		case Configuration.UI_MODE_NIGHT_YES:
			setTheme(R.style.Theme_MyApplicationDark);
			break;

		case Configuration.UI_MODE_NIGHT_NO:
			setTheme(R.style.Theme_MyApplication);
			break;

		case Configuration.UI_MODE_NIGHT_UNDEFINED:
			setTheme(R.style.Theme_MyApplicationDark);
			break;
		}

		setContentView(R.layout.activity_main);

		search = findViewById(R.id.search);
		body = findViewById(R.id.body);

		body.setBackgroundColor(Color.TRANSPARENT);
		setSearch("https://google.com");
		body.startLoading(website);
		search.setText(website);

		search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String a = search.getText().toString();
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					if (a.trim().isEmpty()) {
						setSearch("https://google.com");
						//Toaster(website + " empty");
					} else {
						if ((a.startsWith("https://") || a.startsWith("http://") || a.contains("."))
								&& !a.contains(" ")) {
							setSearch(a);
							//Toaster(website + " website");
						} else {
							setSearch("https://www.google.com/search?q=" + a);
							//Toaster(website + " keyword");
						}
					}
					body.startLoading(website);
					body.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
						@Override
						public void onPageStarted(WebView view, String url, Bitmap favicon) {
							//Do your own stuffs. These will be executed after default onPageStarted().
							setSearch(view.getUrl());
							setUrl(url);
							Toaster(url);
							dimkey();
						}

						@Override
						public void onPageFinished(WebView view, String url) {
							//Do your own stuffs. These will be executed after default onPageFinished().
							setUrl(url);							
						}

						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							return false;
						}
					});

					return true;
				}
				return false;
			}
		});

		body.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				//Do your own stuffs. These will be executed after default onPageStarted().
				setSearch(url);
				setUrl(url);
				Toaster(url);
				dimkey();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				//Do your own stuffs. These will be executed after default onPageFinished().
				setSearch(url);
				setUrl(url);				
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void setSearch(String searched) {
		this.website = searched;
	}

	public void setUrl(String url) {
		search.setText(url);
	}

	public void Toaster(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public void dimkey() {
		//dimiss the keyboard after click go/enter
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
	}

	@Override
	public void onBackPressed() {
		if (!body.goBackIfPossible())
			super.onBackPressed();
	}

}