package edlib.rumusmatematikalengkap.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import edlib.rumusmatematikalengkap.Adapter.ItemHolder;
import edlib.rumusmatematikalengkap.RvConfig.SpacingDecoration;
import edlib.rumusmatematikalengkap.Model.RcModel;
import edlib.rumusmatematikalengkap.R;

public class ListMateri extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcModel, ItemHolder> adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_materi);
        recyclerView = findViewById(R.id.all_materi);
        setupDatabase();
        setupToolbar();
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Semua Materi");
    }

    private void setupDatabase() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("kategori");
        reference.keepSynced(true);
        Query query = reference.orderByKey();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RcModel>()
                .setQuery(query, RcModel.class).build();

        adapter = new FirebaseRecyclerAdapter<RcModel, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, final int position, @NonNull final RcModel model) {
                holder.setTitle(model.getTitle());
                holder.setImg(getBaseContext(), model.getImg());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ContentSlider.class);
                        intent.putExtra("namePut", model.getName());
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("id", model.getIdCat());
                        startActivityForResult(intent, 1);
                    }
                });
            }

            @NonNull
            @Override
            public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_materi, parent, false);
                return new ItemHolder(view);
            }
        };

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new SpacingDecoration(10, SpacingDecoration.GRID));
        recyclerView.setAdapter(adapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                final int posisi = data.getIntExtra("id", 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, new RecyclerView.State(), posisi + 1);
                    }
                }, 200);

            }
        }
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
