package ir.farsirib.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ir.farsirib.R;

public class AccountActivity extends Main2Activity {

    TextView account_tv;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout content_frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.account, content_frame);

        toolbar.setTitle("حساب کاربری شما");

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {

                TextView tv = (TextView) view;
                tv.setTypeface(myfont);
                tv.setTextSize(18);
            }
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable arrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.closeDrawer(Gravity.RIGHT);

                onBackPressed();

            }
        });

        if (settings.getInt("user_id",0)!=0)
        {
            navigationView = findViewById(R.id.nav_view);
            headerView = navigationView.getHeaderView(0);
            account_tv = headerView.findViewById(R.id.account_txt);
            account_tv.setText(settings.getString("mobile_num",""));

            TextView mobile_num = findViewById(R.id.mobile_number);
            TextView city_name = findViewById(R.id.city_name);

            mobile_num.setText(settings.getString("mobile_num",""));
            city_name.setText(settings.getString("city",""));

            drawer.closeDrawer(Gravity.RIGHT);

        }
        else
        {
            error_message_dialog("شبکه فارس!" , "شما وارد حساب کاربری خود نشده اید!");

            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            drawer.closeDrawer(Gravity.RIGHT);
        }

        Button exit_btn = findViewById(R.id.exit_bt);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(AccountActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("خروج از حساب کاربری")
                        .setContentText("آیا از خروج خود مطمٔنید؟")
                        .setCancelText("انصراف")
                        .setConfirmText("خروج")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                SharedPreferences.Editor editor = settings.edit();
                                editor.remove("user_id");
                                editor.commit();

                                navigationView = findViewById(R.id.nav_view);
                                headerView = navigationView.getHeaderView(0);
                                account_tv = headerView.findViewById(R.id.account_txt);
                                account_tv.setText("شما وارد حساب کاربری نشده اید");

                                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                                AccountActivity.this.finish();
                                startActivity(i);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("خروج از حساب کاربری")
                    .setContentText("آیا از خروج خود مطمٔنید؟")
                    .setCancelText("انصراف")
                    .setConfirmText("خروج")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.remove("user_id");
                            editor.commit();

//                            sDialog
//                                    .setTitleText("خروج از حساب کاربری!")
//                                    .setContentText("شما با موفقیت خارح شدید!")
//                                    .setConfirmText("خب")
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            navigationView = findViewById(R.id.nav_view);
                            headerView = navigationView.getHeaderView(0);
                            account_tv = headerView.findViewById(R.id.account_txt);
                            account_tv.setText("شما وارد حساب کاربری نشده اید");

                            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                            AccountActivity.this.finish();
                            startActivity(i);
                        }
                    })
                    .show();



        }

        return super.onOptionsItemSelected(item);
    }

}
