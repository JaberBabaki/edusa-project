package ir.edusa.parents.Activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import fontdroid.TextView;
import ir.edusa.parents.Helpers.IntentHelper;
import ir.edusa.parents.R;

/**
 * Created by pouya on 5/11/17.
 */


public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean isRunning = false;
    private ViewGroup lytFirstMenuButton;
    public static String         FONT1_NAME   = "fonts/b_yekan.ttf";
    public static Typeface font1;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //Call Supper Method
        super.setContentView(layoutResID);

        //Initialize Toolbar
        initializeToolbar();
    }


    //Hide Status Bar
    public void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //Initialize Toolbar
    public void initializeToolbar() {
        //Initialize Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Check Toolbar
        if (toolbar == null) {
            return;
        }

        //Set Support ActionBar
        setSupportActionBar(toolbar);

        //Initialize Toolbar Component
        ViewGroup lytRightButton = (ViewGroup) findViewById(R.id.lytRightButton);
        ImageView imgRightButton = (ImageView) findViewById(R.id.imgRightButton);
        ImageView imgFirstMenuButton = (ImageView) findViewById(R.id.imgFirstMenuButton);
        lytFirstMenuButton = (ViewGroup) findViewById(R.id.lytFirstMenuButton);
        ImageView imgSecondMenuButton = (ImageView) findViewById(R.id.imgSecondMenuButton);
        ViewGroup lytSecondMenuButton = (ViewGroup) findViewById(R.id.lytSecondMenuButton);
        ImageView imgFakeButton = (ImageView) findViewById(R.id.imgFakeButton);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);

        //Check Page Title
        if (getPageTitle() != null) {
            //Set Page Title
            txtTitle.setText(getPageTitle());
        }

        //OnClickListener
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.lytRightButton:
                        //Finish With Animation
//                        IntentHelper.finishActivityWithAnimation(BaseActivity.this);
                        finishWithAnimation();
                        break;
                    case R.id.lytSecondMenuButton:
                        //On Second Menu Clicked
                        onSecondMenuClicked();
                        break;
                }
            }
        };

        //Set OnClickListener
        lytRightButton.setOnClickListener(onClickListener);
        lytFirstMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFirstMenuClicked();
            }
        });
        lytSecondMenuButton.setOnClickListener(onClickListener);


        //Check First menu
        if (getFirstMenuImageResource() == -1) {
            imgFirstMenuButton.setVisibility(View.INVISIBLE);
            lytFirstMenuButton.setVisibility(View.INVISIBLE);
        } else {
            imgFirstMenuButton.setVisibility(View.VISIBLE);
            lytFirstMenuButton.setVisibility(View.VISIBLE);
            if (getFirstMenuImageResource() != 0) {
                imgFirstMenuButton.setImageResource(getFirstMenuImageResource());
            }
        }

        //Check Second menu
        if (getSecondMenuImageResource() == 0) {
            lytSecondMenuButton.setVisibility(View.GONE);
            imgSecondMenuButton.setVisibility(View.GONE);
            imgFakeButton.setVisibility(View.GONE);
        } else {
            if (imgFirstMenuButton.getVisibility() == View.INVISIBLE) {
                imgFirstMenuButton.setVisibility(View.GONE);
                lytFirstMenuButton.setVisibility(View.GONE);
            }

            imgSecondMenuButton.setVisibility(View.VISIBLE);
            lytSecondMenuButton.setVisibility(View.VISIBLE);
            imgFakeButton.setVisibility(View.VISIBLE);
            imgSecondMenuButton.setImageResource(getSecondMenuImageResource());
        }
    }


    //Get Toolbar
    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract String getPageTitle();

    //Get First Menu Image Resource
    protected int getFirstMenuImageResource() {
        return 0;
    }

    //On First Menu Clicked
    protected void onFirstMenuClicked() {
    }

    //Get Second Menu Image Resource
    protected int getSecondMenuImageResource() {
        return 0;
    }

    //On Second Menu Clicked
    protected void onSecondMenuClicked() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Finish With Animation
            finishWithAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Finish With Animation
    public void finishWithAnimation() {
        //Hide Keyboard
        hideKeyboard();

        //Finish With Animation
        IntentHelper.finishActivityWithAnimation(BaseActivity.this);
    }

    //Hide Keyboard
    public void hideKeyboard() {
        if (BaseActivity.this.getCurrentFocus() != null) {

            InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendScreenViewEvent();
        font1 = Typeface.createFromAsset(getAssets(), FONT1_NAME);
        isRunning = true;

    }

    public void sendScreenViewEvent() {
//        AnalyticsHelper.with().setScreenName(AnalyticsHelper.getSimpleClassName(getClass()));
    }

    public void setPageTitle(String title) {
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        if (txtTitle != null) {
            txtTitle.setText(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public boolean isFinishing() {
        return super.isFinishing() || !isRunning;
    }

}
