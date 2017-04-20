package com.youroff.listus;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.youroff.listus.adapters.LItemAdapter;
import com.youroff.listus.databinding.ActivityUseBinding;
import com.youroff.listus.dialogs.LItemEditDialogFragment;
import com.youroff.listus.dialogs.LListEditDialogFragment;
import com.youroff.listus.models.LItem;
import com.youroff.listus.models.LList;

import java.util.ArrayList;
import java.util.List;

public class UseActivity extends AppCompatActivity implements LItemAdapter.ItemClickCallback {

    public LList list;
    private RecyclerView recView;
    private LItemAdapter listAdapter;
    public List<LItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(this);

        // Here we retrieve data passed with intent
        Intent i = getIntent();

        // Hello SDK developers, burn in hell!!! What if I don't have a value at "listId"???
        // Exception? Option type finally?
        list = SugarRecord.findById(LList.class, i.getLongExtra("listId", 0));

        // Creating binding: this will inflate a template with list available in template
        // (see res/layout/activity_use.xml)
        ActivityUseBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_use);
        binding.setList(list);

        // Setting up a toolbar with support of "back" button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initializing RecyclerView
        recView = (RecyclerView)findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new LItemAdapter(list.getItems(), this);
        listAdapter.setItemClickCallback(this);
        recView.setAdapter(listAdapter);

        // Initializing ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    // ItemTouchHelper callback, pretty much the same we saw in MainActivity
    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    // We delegate adding LItem to LList, method return position at which item was inserted,
    // so we could notify RecyclerView
    public void addItem(LItem item) {
        listAdapter.notifyItemInserted(list.addItem(item));
    }

    // Moving item, delegate to LList
    private void moveItem(int source, int target) {
        list.moveItem(source, target);
        listAdapter.notifyItemMoved(source, target);
    }

    // Same sh...
    private void deleteItem(int source) {
        list.deleteItem(source);
        listAdapter.notifyItemRemoved(source);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    // Custom "back" action handler, normally it returns to previous activity
    // Here we just start MainActivity every time
    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editListDialogue();
                return true;
            case R.id.action_add:
                editItemDialogue(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Creating edit list DialogFragment
    public void editListDialogue() {
        DialogFragment newFragment = new LListEditDialogFragment();
        newFragment.show(getSupportFragmentManager(), "EDIT_LIST");
    }

    // Creating edit/create item DialogFragment, if pos is null, we assume that it's a Create dialog
    public void editItemDialogue(Integer pos) {
        DialogFragment newFragment = LItemEditDialogFragment.newInstance(pos);
        newFragment.show(getSupportFragmentManager(), "EDIT_ITEM");
    }

    // Launching Edit Item dialog with current position
    @Override
    public void onItemClick(int p) {
        editItemDialogue(p);
    }

    // Creating new List of unbought items, and calling UseActivity with it
    public void newList(View view) {
        LList newList = new LList(list.getName() + " (cont)");
        SugarRecord.save(newList);
        LItem newItem;
        int ctr = 0;
        for (LItem i : list.getItems()) {
            if (!i.getBought()) {
                newItem = new LItem(newList, i.getName(), i.getPrice());
                newItem.setQty(i.getQty());
                newItem.setPriority(ctr);
                SugarRecord.save(newItem);
                ctr++;
            }
        };
        Intent i = new Intent(this, UseActivity.class);
        i.putExtra("listId", newList.getId());
        startActivity(i);
    }
}
