package edlib.rumusmatematikalengkap.Activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edlib.rumusmatematikalengkap.R;

public class About extends AppCompatActivity {

    private TextView versi, prog, kontr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindView();
        versioning();
        getDatabase();
    }

    private void getDatabase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("about");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String progm = dataSnapshot.child("programmer").getValue(String.class);
                String kontri = dataSnapshot.child("kontributor").getValue(String.class);

                if (progm != null) {
                    progm = progm.replaceAll("[~]","\n");
                }
                if (kontri != null) {
                    kontri = kontri.replaceAll("[~]","\n");
                }

                prog.setText(progm);
                kontr.setText(kontri);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void versioning() {
        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versi.setText("Versi "+versionName);
    }

    private void bindView() {
        versi = findViewById(R.id.versi_about);
        prog = findViewById(R.id.programmer);
        kontr = findViewById(R.id.kontributor);
    }
}
