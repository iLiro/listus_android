package com.youroff.listus.models;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.youroff.listus.BR;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Table
public class LList extends BaseObservable {
    private Long id;
    private String name;
    private Float budget;
    private Integer pos;

    @Ignore
    private List<LItem> items;

    @Ignore
    private Float taken;

    @Ignore
    private Float left;

    public LList() {}
    public LList(String name){
        this.name = name;
        this.budget = new Float(0);
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
    public Float getBudget() { return budget; }

    public void setBudget(Float budget) {
        this.budget = budget;
        notifyPropertyChanged(BR.budget);
    }

    @Bindable
    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
        notifyPropertyChanged(BR.pos);
    }

    public List<LItem> getItems() {
        if (items == null) {
            String[] args = {getId().toString()};
            items = SugarRecord.find(LItem.class, "llist = ?", args, null, "priority", null);
            refreshItems();
        }
        return items;
    }

    public LItem getItem(int pos) {
        return items.get(pos);
    }

    public int addItem(LItem item) {
        int pos = items.size();
        item.setPriority(pos);
        SugarRecord.save(item);
        items.add(item);
        refreshItems();
        return pos;
    }

    public void moveItem(int source, int target) {
        LItem item = items.get(source);
        items.remove(source);
        items.add(target, item);
        refreshItems();
    }

    public void deleteItem(int source) {
        LItem item = items.get(source);
        items.remove(source);
        SugarRecord.delete(item);
        refreshItems();
    }

    public void refreshItems() {
        int ctr = 0;
        Float funds = budget;
        setTaken(0F);
        setLeft(0F);
        for (LItem i : items) {
            if (i.getTotal() <= funds) {
                i.setBought(true);
                setTaken(taken + i.getTotal());
                funds -= i.getTotal();
            } else {
                setLeft(left + i.getTotal());
                i.setBought(false);
            }
            if (i.getPriority() != ctr) {
                i.setPriority(ctr);
                SugarRecord.save(i);
            }
            ctr++;
        }
    }

    public Long getId() {
        return id;
    }

    @Bindable
    public Float getTaken() {
        return taken;
    }

    public void setTaken(Float taken) {
        this.taken = taken;
        notifyPropertyChanged(BR.taken);
    }

    @Bindable
    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
        notifyPropertyChanged(BR.left);
    }

    public List<String> getTitles() {
        List<String> names = new ArrayList<String>();
        items.forEach((i) -> {
            names.add(i.getName().toLowerCase().trim());
        });
        return names;
    }
}
