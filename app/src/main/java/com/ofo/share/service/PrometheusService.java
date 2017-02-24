package com.ofo.share.service;


import android.app.IntentService;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ofo.share.AppContext;
import com.ofo.share.api.OfoRetrofit;
import com.ofo.share.model.OfoModel;
import com.ofo.share.model.OfoModelDao;
import com.ofo.share.model.Result;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class PrometheusService extends IntentService {

    public PrometheusService() {
        super("PrometheusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String number = intent.getStringExtra("number");
        String password = intent.getStringExtra("password");
        try {
            Response<Result> response =  OfoRetrofit.get().getShareApi()
                    .save(number, password)
                    .execute();
            if (response.isSuccessful() && response.body().code == 0) {
                OfoModel ofoModel = new OfoModel(null, number, password, new Date(), true);
                AppContext.getInstance().getDaoSession().getOfoModelDao().insert(ofoModel);
            } else {
                OfoModel ofoModel = new OfoModel(null, number, password, new Date(), false);
                AppContext.getInstance().getDaoSession().getOfoModelDao().insert(ofoModel);
            }

            List<OfoModel> ofoModelList = AppContext.getInstance().getDaoSession().getOfoModelDao()
                    .queryBuilder().where(OfoModelDao.Properties.HasUpload.eq(false)).list();
            if (null != ofoModelList && ofoModelList.size() > 0) {
                JsonArray jsonArray = new JsonArray();
                for (OfoModel ofoModel : ofoModelList) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("code", ofoModel.getNumber());
                    jsonObject.addProperty("password", ofoModel.getPassword());
                    jsonArray.add(jsonObject);
                }

                Response<Result> response1 = OfoRetrofit.get().getShareApi().saveAll(jsonArray).execute();
                if (response1.isSuccessful() && response1.body().code == 0) {
                    for (OfoModel ofoModel : ofoModelList) {
                        ofoModel.setHasUpload(true);
                    }
                    AppContext.getInstance().getDaoSession().getOfoModelDao().updateInTx(ofoModelList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            OfoModel ofoModel = new OfoModel(null, number, password, new Date(), false);
            AppContext.getInstance().getDaoSession().getOfoModelDao().insert(ofoModel);
        }
    }
}
