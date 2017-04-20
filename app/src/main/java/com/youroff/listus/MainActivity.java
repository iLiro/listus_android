package com.youroff.listus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.youroff.listus.adapters.LListAdapter;
import com.youroff.listus.models.LList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LListAdapter.ItemClickCallback {

    private RecyclerView recView;
    private LListAdapter listAdapter;
    private List<LList> lists = new ArrayList<LList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This initializes SugarORM, since this moment within this activity it's bound to it
        SugarContext.init(this);

        // Initializing RecyclerView: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
        recView = (RecyclerView)findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new LListAdapter(lists, this);
        listAdapter.setItemClickCallback(this);
        recView.setAdapter(listAdapter);

        // Creating ItemTouchHelper (responsible for drag and swipe)
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    // This defines callback for ItemTouchHelper
    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {

                    // On Move we call local method moveItem with source and target positions
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                                          RecyclerView.ViewHolder target) {
                        moveItem(source.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    // On swiped we call deleteItem with original position of swiped element
                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder source, int swipeDir) {
                        deleteItem(source.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    // On move we remove list from collection and insert it back to collection in desired position
    // listAdapter.notifyItemMoved used to update RecyclerView
    private void moveItem(int source, int target) {
        LList list = lists.get(source);
        lists.remove(source);
        lists.add(target, list);
        resort();
        listAdapter.notifyItemMoved(source, target);
    }

    // Deleting Item with updating RecyclerView with listAdapter.notifyItemRemoved
    private void deleteItem(int source) {
        LList list = lists.get(source);
        lists.remove(source);
        SugarRecord.delete(list);
        resort();
        listAdapter.notifyItemRemoved(source);
    }

    // Here we simply synchronize position in collection with database 'pos' field, saving it if changed
    private void resort() {
        int ctr = 0;
        for (LList l : lists) {
            if (l.getPos() != ctr) {
                l.setPos(ctr);
                SugarRecord.save(l);
            }
            ctr++;
        }
    }

    // This callback runs when we return to an activity, also it runs when it first run
    // this is why we actually load LLists from database here
    @Override
    public void onResume() {
        super.onResume();
        lists.clear();
        lists.addAll(SugarRecord.find(LList.class, null, null, null, "POS", null));
        listAdapter.notifyDataSetChanged();
    }

    // This callback is used to inflate Toolbar menu (see res/menu/lists.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists, menu);
        return true;
    }

    // This is handler for toolbar events, we case-match by button ids
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                LList list = new LList("Untitled");
                list.setPos(lists.size());
                SugarRecord.save(list);
                Intent i = new Intent(this, UseActivity.class);
                i.putExtra("listId", list.getId());
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // Callback when List name is clicked. Here we create an intent and run the transition to UseActivity
    @Override
    public void onItemClick(int source) {
        LList list = lists.get(source);
        Intent i = new Intent(this, UseActivity.class);
        i.putExtra("listId", list.getId());
        startActivity(i);
    }
}
