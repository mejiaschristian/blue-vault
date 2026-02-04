package com.example.blue_vault;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class nav_pass_change extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_pass_change);

        setupNavigation();

        Button backBtn = findViewById(R.id.backBtn);
        Button applyBtn = findViewById(R.id.confirmPassBtn);
        EditText confirmPass = findViewById(R.id.confirmPasswordInput);
        EditText oldPass = findViewById(R.id.oldPasswordInput);
        EditText newPass = findViewById(R.id.newPasswordInput);

        if (backBtn != null) {
            backBtn.setOnClickListener(v -> onBackPressed());
        }

        applyBtn.setOnClickListener(v -> {
            String oldPassString = oldPass.getText().toString().trim();
            String newPassString = newPass.getText().toString().trim();
            String confirmPassString = confirmPass.getText().toString().trim();

            if (oldPassString.isEmpty() || newPassString.isEmpty() || confirmPassString.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (oldPassString.equals(newPassString)) {
                Toast.makeText(this, "New password cannot be the same as the old password", Toast.LENGTH_SHORT).show();
            } else if (!newPassString.equals(confirmPassString)) {
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            } else if (newPassString.length() < 8) {
                Toast.makeText(this, "New password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            } else {
                applyPasswordChange(oldPass, newPass, confirmPass);
            }
        });
    }

    private void applyPasswordChange(EditText oldPass, EditText newPass, EditText confirmPass) {
        String oldP = oldPass.getText().toString().trim();
        String newP = newPass.getText().toString().trim();

        // Ensure this path matches exactly where you saved the file in htdocs
        String ip = DataRepository.getInstance().getIpAddress();
        String URL = "http://"+ip+"/bluevault/ChangePass.php";

        SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);

        // IMPORTANT: Check main_login.java to see if you used "id" or "IDnumber" when saving!
        String userId = sp.getString("id", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "Session error: User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    String res = response.trim();
                    if (res.equals("success")) {
                        Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                        logout();
                    } else if (res.equals("incorrect_old")) {
                        Toast.makeText(this, "The current password you entered is incorrect", Toast.LENGTH_LONG).show();
                    } else if (res.equals("user_not_found")) {
                        Toast.makeText(this, "User ID " + userId + " not found in database", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Server: " + res, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Connection Error. Check XAMPP and 10.0.2.2 IP.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId); // This goes to $_POST['user_id']
                params.put("old_password", oldP); // This goes to $_POST['old_password']
                params.put("new_password", newP); // This goes to $_POST['new_password']
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}