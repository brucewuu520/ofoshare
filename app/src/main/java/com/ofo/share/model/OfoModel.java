package com.ofo.share.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;


@Entity(indexes = {
        @Index(value = "number, date DESC", unique = true)
})
public class OfoModel {

    @Id
    private Long id;

    @NotNull
    private String number;

    @NotNull
    private String password;

    private Date date;

    private Boolean hasUpload;

    @Generated(hash = 436228909)
    public OfoModel(Long id, @NotNull String number, @NotNull String password,
                    Date date, Boolean hasUpload) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.date = date;
        this.hasUpload = hasUpload;
    }

    @Generated(hash = 1859116033)
    public OfoModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getHasUpload() {
        return this.hasUpload;
    }

    public void setHasUpload(Boolean hasUpload) {
        this.hasUpload = hasUpload;
    }
}
