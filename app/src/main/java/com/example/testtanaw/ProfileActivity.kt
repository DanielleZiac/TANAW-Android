package com.example.testtanaw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import com.example.testtanaw.models.UserParcelable;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private UserParcelable userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            WindowInsetsCompat.Type.systemBars();
            v.setPadding(
                insets.getSystemWindowInsetLeft(),
                insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(),
                insets.getSystemWindowInsetBottom()
            );
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            userData = getIntent().getParcelableExtra("userData", UserParcelable.class);
        } else {
            userData = getIntent().getParcelableExtra("userData");
        }

        if (userData != null) {
            Log.d("xxxxxx", userData.toString());
            setupToolbar();
            setupViews();
        }

        // Set up the OnClickListener for the "Edit Avatar" button
        Button editAvatarButton = findViewById(R.id.editAvatar);
        editAvatarButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AvatarActivity.class);
            intent.putExtra("userData", userData);
            startActivity(intent);
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the toolbar title to be centered
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title
        }
        TextView titleTextView = toolbar.findViewById(R.id.toolbar_title);

        // Enable the back button in the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViews() {
        ShapeableImageView avatarImageView = findViewById(R.id.roundedImageView);
        TextView srCodeTextView = findViewById(R.id.srCode);
        TextView nameTextView = findViewById(R.id.name);
        TextView institutionTextView = findViewById(R.id.institution);
        TextView collegeTextView = findViewById(R.id.college);
        TextView currentEmailTextView = findViewById(R.id.currentEmail);

        Picasso.get()
               .load(userData.getAvatarUrl())
               .fit()
               .centerCrop()
               .placeholder(R.drawable.loading)
               .error(R.drawable.baseline_error_outline_24)
               .into(avatarImageView);

        srCodeTextView.setText(userData.getSrCode());
        nameTextView.setText(userData.getFirstName() + " " + userData.getLastName());
        institutionTextView.setText(userData.getInstitution());
        collegeTextView.setText(userData.getDepartment());
        currentEmailTextView.setText(userData.getEmail());
    }

    // Handle back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
