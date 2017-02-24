package com.ofo.share.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.ofo.share.AppContext;
import com.ofo.share.R;
import com.ofo.share.model.OfoModelDao;
import com.ofo.share.service.PrometheusService;


public class AddNumberDialog extends AppCompatDialog {

    @SuppressWarnings("ConstantConditions")
    public AddNumberDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.dialog_add);
        final AppCompatEditText etNumber = (AppCompatEditText) findViewById(R.id.et_number);
        final AppCompatEditText etPassword = (AppCompatEditText) findViewById(R.id.et_password);
        findViewById(R.id.btn_add_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNumber.length() == 0) {
                    Toast.makeText(context, "input no~", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.length() == 0) {
                    Toast.makeText(context, "input password~", Toast.LENGTH_SHORT).show();
                    return;
                }
                String number = etNumber.getText().toString();
                String password = etPassword.getText().toString();

                etNumber.setText("");
                etPassword.setText("");
                if (AppContext.getInstance().getDaoSession().getOfoModelDao().queryBuilder().where(OfoModelDao.Properties.Number.eq(number), OfoModelDao.Properties.Password.eq(password)).buildCount().count() > 0) {
                    Toast.makeText(AppContext.getInstance(), "has exist~", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AppContext.getInstance(), "success~", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, PrometheusService.class);
                intent.putExtra("number", number);
                intent.putExtra("password", password);
                context.startService(intent);
            }
        });
    }
}
