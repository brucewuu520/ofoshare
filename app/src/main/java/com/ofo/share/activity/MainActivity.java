package com.ofo.share.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ofo.share.AppContext;
import com.ofo.share.R;
import com.ofo.share.api.OfoRetrofit;
import com.ofo.share.dialog.AddNumberDialog;
import com.ofo.share.model.OfoModel;
import com.ofo.share.model.OfoModelDao;
import com.ofo.share.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etNumber;
    private TextView tvPassword;
    private AppCompatButton btnFindPassword;

    private ProgressDialog mProgressDialog;
    private Call<Result> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.etNumber = (EditText) findViewById(R.id.et_number);
        this.tvPassword = (TextView) findViewById(R.id.tv_password);
        this.btnFindPassword = (AppCompatButton) findViewById(R.id.btn_find_password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNumber();
            }
        });
        btnFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNumber.length() == 0) {
                    Toast.makeText(MainActivity.this, "input no~", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryPassword();
            }
        });
        etNumber.requestFocus();
    }

    private void queryPassword() {
        String number = etNumber.getText().toString();
        List<OfoModel> ofoModels = AppContext.getInstance().getDaoSession().getOfoModelDao().queryBuilder().where(OfoModelDao.Properties.Number.eq(number)).list();
        if (null != ofoModels && ofoModels.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (OfoModel ofoModel : ofoModels) {
                builder.append(ofoModel.getPassword()).append("、");
            }
            tvPassword.setText(builder.subSequence(0, builder.length() - 1));
            return;
        }
        showProgressDialog();
        request = OfoRetrofit.get().getShareApi().query(number);
        request.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                dismissProgressDialog();
                if (response.isSuccessful()) {
                    Result result = response.body();
                    if (null != result && 0 == result.code) {
                        tvPassword.setText(result.password);
                    } else {
                        Toast.makeText(MainActivity.this, "empty~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "empty~", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(MainActivity.this, "empty~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNumber() {
        new AddNumberDialog(this).show();
    }

    private void showProgressDialog() {
        if (null == mProgressDialog)
            mProgressDialog = new ProgressDialog(this);
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void dismissProgressDialog() {
        if (null == mProgressDialog)
            return;
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            addNumber();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (null != request && !request.isCanceled()) {
            request.cancel();
            request = null;
        }
        super.onDestroy();
    }
}
