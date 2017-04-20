package com.youroff.listus.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.youroff.listus.BR;

@Table
public class LItem extends BaseObservable {
    private Long id;
    private String name;
    private Integer qty;
    private Integer priority;
    private Float price;
    private LList llist;

    @Ignore
    private Boolean bought;

    public LItem() {}
    public LItem(LList llist, String name, Float price) {
        this.bought = false;
        this.llist = llist;
        this.name = name;
        this.price = price;
        this.qty = 1;
        this.priority = 1;
    }


    public Long getId() {
        return id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
        notifyPropertyChanged(BR.qty);
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
        notifyPropertyChanged(BR.priority);
    }

    @Bindable
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
        notifyPropertyChanged(BR.total);
    }

    @Bindable
    public Float getTotal() {
        return qty * price;
    }

    @Bindable
    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
        notifyPropertyChanged(BR.bought);
    }
}
