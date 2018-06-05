package edlib.rumusmatematikalengkap.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edlib.rumusmatematikalengkap.Adapter.ItemHolder;
import edlib.rumusmatematikalengkap.Model.RcModel;
import edlib.rumusmatematikalengkap.R;

public class ContentSlider extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcModel, ItemHolder> adapter;


    private RecyclerView recyclerView;
    private String nameCat, title;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_slider);
        recyclerView = findViewById(R.id.recycler_slider);
        passingIntent();
        setupDatabase();
        setupToolbar();
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    private void passingIntent() {
        nameCat = getIntent().getStringExtra("namePut");
        id = getIntent().getIntExtra("id", 0);
        title = getIntent().getStringExtra("title");
    }

    private void setupDatabase() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("content/"+nameCat);
        reference.keepSynced(true);
        Query query = reference.orderByKey();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RcModel>()
                .setQuery(query, RcModel.class).build();

        adapter = new FirebaseRecyclerAdapter<RcModel, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull RcModel model) {
                holder.setBody(model.getBody());
            }

            @NonNull
            @Override
            public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false);
                return new ItemHolder(view);
            }
        };

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
