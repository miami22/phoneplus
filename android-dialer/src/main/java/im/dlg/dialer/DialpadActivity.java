package im.dlg.dialer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Boris Maslakov.
 */
public class DialpadActivity extends AppCompatActivity implements DialpadFragment.Callback {
    public final static String EXTRA_RESULT_FORMATTED = "EXTRA_RESULT_FORMATTED";
    public final static String EXTRA_RESULT_RAW = "EXTRA_RESULT_RAW";
    public final static String EXTRA_REGION_CODE = "EXTRA_REGION_CODE";
    public final static String EXTRA_FORMAT_AS_YOU_TYPE = "EXTRA_FORMAT_AS_YOU_TYPE";
    public final static String EXTRA_ENABLE_STAR = "EXTRA_ENABLE_STAR";
    public final static String EXTRA_ENABLE_POUND = "EXTRA_ENABLE_POUND";
    public final static String EXTRA_ENABLE_PLUS = "EXTRA_ENABLE_PLUS";
    public final static String EXTRA_CURSOR_VISIBLE = "EXTRA_CURSOR_VISIBLE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Fragment fragment = new DialpadFragment();
            Bundle args = new Bundle();
            args.putString(DialpadFragment.EXTRA_REGION_CODE,
                    getIntent().getStringExtra(EXTRA_REGION_CODE));
             Bundle extra=getIntent().getExtras();
             if (extra!=null){
                 if (getIntent().getExtras().containsKey(EXTRA_FORMAT_AS_YOU_TYPE)) {
                     args.putBoolean(DialpadFragment.EXTRA_FORMAT_AS_YOU_TYPE,
                             getIntent().getExtras().getBoolean(EXTRA_FORMAT_AS_YOU_TYPE, true));
                 }
                 if (getIntent().getExtras().containsKey(EXTRA_ENABLE_STAR)) {
                     args.putBoolean(DialpadFragment.EXTRA_ENABLE_STAR,
                             getIntent().getExtras().getBoolean(EXTRA_ENABLE_STAR, true));
                 }
                 if (getIntent().getExtras().containsKey(EXTRA_ENABLE_PLUS)) {
                     args.putBoolean(DialpadFragment.EXTRA_ENABLE_POUND,
                             getIntent().getExtras().getBoolean(EXTRA_ENABLE_POUND, true));
                 }
                 if (getIntent().getExtras().containsKey(EXTRA_ENABLE_PLUS)) {
                     args.putBoolean(DialpadFragment.EXTRA_ENABLE_PLUS,
                             getIntent().getExtras().getBoolean(EXTRA_ENABLE_PLUS, true));
                 }
                 if (getIntent().getExtras().containsKey(EXTRA_CURSOR_VISIBLE)) {
                     args.putBoolean(DialpadFragment.EXTRA_CURSOR_VISIBLE,
                             getIntent().getExtras().getBoolean(EXTRA_CURSOR_VISIBLE, false));
                 }
             }


            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit();
        }
    }

    @Override
    public void ok(String formatted, String raw) {
        Intent result = new Intent();
        result.putExtra(EXTRA_RESULT_FORMATTED, formatted);
        result.putExtra(EXTRA_RESULT_RAW, raw);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
