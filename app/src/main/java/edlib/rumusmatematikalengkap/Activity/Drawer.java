package edlib.rumusmatematikalengkap.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import edlib.rumusmatematikalengkap.Adapter.ItemHolder;
import edlib.rumusmatematikalengkap.RvConfig.CheckNetwork;
import edlib.rumusmatematikalengkap.RvConfig.SpacingDecoration;
import edlib.rumusmatematikalengkap.Model.RcModel;
import edlib.rumusmatematikalengkap.R;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseRecyclerAdapter<RcModel, ItemHolder> adapter;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        bindView();
        setSupportActionBar(toolbar);
        setupDrawer();
        setupDatabase();
        setupHeader();
        checkNetwork();
    }

    private void checkNetwork() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Sedang offline, mungkin beberapa fitur tidak berfungsi", Snackbar.LENGTH_INDEFINITE);
        if (!CheckNetwork.isInternetAvailable(this)) {
            snackbar.show();
        } else {
            snackbar.dismiss();
        }
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.menu_dashboard);
    }

    private void setupDatabase() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("kategori");
        reference.keepSynced(true);
        Query query = reference.orderByKey().limitToFirst(6);
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_dashboard, parent, false);
                return new ItemHolder(view);
            }
        };

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new SpacingDecoration(16, SpacingDecoration.HORIZONTAL));
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            startActivity(new Intent(getApplicationContext(), About.class));
        }
        if (id == R.id.nav_web) {
            openUrl();
        }
        if (id == R.id.nav_share) {
            shareApp();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Rumus Matematika Lengkap");
        String sAux = "\nMari pelajari rumus-rumus matematika dengan androidmu, unduh di sini\n\n";
        sAux = sAux + "https://appmath-uhamka.github.io/\n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "pilih media"));
    }

    private void openUrl() {
        String url = "https://appmath-uhamka.github.io/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void listMateri(View view) {
        startActivity(new Intent(this, ListMateri.class));
    }

    private void setupHeader() {
        View header = navigationView.getHeaderView(0);

        final ImageView imgHeader = header.findViewById(R.id.img_header);
        final ImageView logoHeader = header.findViewById(R.id.logo_header);
        final TextView titleHeader = header.findViewById(R.id.title_header);
        final TextView subtitleHeader = header.findViewById(R.id.subtitle);
        TextView versi = findViewById(R.id.versi);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("about");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgUrl = dataSnapshot.child("imgheader").getValue(String.class);
                String logoUrl = dataSnapshot.child("logoheader").getValue(String.class);
                String title = dataSnapshot.child("titleheader").getValue(String.class);
                String subtitle = dataSnapshot.child("subtitle").getValue(String.class);

                Glide.with(getApplicationContext()).load(imgUrl).into(imgHeader);
                Glide.with(getApplicationContext()).load(logoUrl).into(logoHeader);

                titleHeader.setText(title);
                subtitleHeader.setText(subtitle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versi.setText(versionName);

    }
}
