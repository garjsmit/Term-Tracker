package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.R;

import java.util.List;

@SuppressLint("ResourceType")
public class TermListActivity extends AppCompatActivity {

    ListView listView;
    WGU_DB db;
    int termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        db = WGU_DB.getInstance(getApplicationContext());
        listView = findViewById(R.id.listView);

        //gets the item clicked and sends that data to the next activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TermDetailActivity.class);
                List<Term> termList = db.termDao().getAllTerms();
                termID = termList.get(position).getTermID();
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });
        populateTermList();

    }

    //populates the listView of terms - comments can be deleted, they're for some debugging.
    private void populateTermList(){
        List<Term> allTerms = db.termDao().getAllTerms();
        //System.out.println("Number of Rows in Terms Table: " + allTerms.size());

        String[] terms = new String[allTerms.size()];
        if(!allTerms.isEmpty()){
            for(int i = 0; i < allTerms.size(); i++){
                terms[i] = allTerms.get(i).getTermName();
                //System.out.println("Term in position | " + i + " | with termName: " + terms[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, terms);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void editTermDetailActivity(View view) {
        Intent intent = new Intent(TermListActivity.this, EditTermActivity.class);
        intent.putExtra("termID", -1);
        startActivity(intent);
    }
}