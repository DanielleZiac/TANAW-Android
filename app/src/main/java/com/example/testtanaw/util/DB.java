package com.example.testtanaw.util;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.jan.supabase.auth.Auth;
import io.github.jan.supabase.auth.UserInfo;
import io.github.jan.supabase.auth.providers.builtin.Email;
import io.github.jan.supabase.postgrest.Columns;
import io.github.jan.supabase.postgrest.PostgrestDefaultClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DB extends Supabase {
    private static final String defaultAvatarUrl = "https://srxhcymqociarjinmkpp.supabase.co/storage/v1/object/public/avatars/default/d8598e52-34b0-4f96-b7a7-e0ff3df7b2cb";

    public static class Institution {
        @NonNull private final String institution;
        @NonNull private final String institutionId;
        @NonNull private final String emailExtension;

        public Institution(@NonNull String institution, @NonNull String institutionId,
                         @NonNull String emailExtension) {
            this.institution = institution;
            this.institutionId = institutionId;
            this.emailExtension = emailExtension;
        }

        @NonNull public String getInstitution() { return institution; }
        @NonNull public String getInstitutionId() { return institutionId; }
        @NonNull public String getEmailExtension() { return emailExtension; }
    }

    private static class UserDataLogin {
        @Nullable private final JsonElement srCode;
        @Nullable private final String email;
        @Nullable private final JsonElement firstName;
        @Nullable private final JsonElement lastName;
        @Nullable private final JsonElement institutionId;

        private UserDataLogin(@Nullable JsonElement srCode, @Nullable String email,
                            @Nullable JsonElement firstName, @Nullable JsonElement lastName,
                            @Nullable JsonElement institutionId) {
            this.srCode = srCode;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.institutionId = institutionId;
        }
    }

    public static class UserData {
        @NonNull private final String userId;
        @NonNull private final String email;
        @NonNull private final String srCode;
        @NonNull private final String firstName;
        @NonNull private final String lastName;
        @NonNull private final HashMap<String, String> institutions;
        @Nullable private final HashMap<String, String> departments;
        @Nullable private final HashMap<String, String> avatars;

        public UserData(@NonNull String userId, @NonNull String email, @NonNull String srCode,
                       @NonNull String firstName, @NonNull String lastName,
                       @NonNull HashMap<String, String> institutions,
                       @Nullable HashMap<String, String> departments,
                       @Nullable HashMap<String, String> avatars) {
            this.userId = userId;
            this.email = email;
            this.srCode = srCode;
            this.firstName = firstName;
            this.lastName = lastName;
            this.institutions = institutions;
            this.departments = departments;
            this.avatars = avatars;
        }

        @NonNull public String getUserId() { return userId; }
        @NonNull public String getEmail() { return email; }
        @NonNull public String getSrCode() { return srCode; }
        @NonNull public String getFirstName() { return firstName; }
        @NonNull public String getLastName() { return lastName; }
        @NonNull public HashMap<String, String> getInstitutions() { return institutions; }
        @Nullable public HashMap<String, String> getDepartments() { return departments; }
        @Nullable public HashMap<String, String> getAvatars() { return avatars; }
    }

    private static class DefaultAvatar {
        @NonNull private final String userId;
        @NonNull private final String avatarUrl;

        private DefaultAvatar(@NonNull String userId, @NonNull String avatarUrl) {
            this.userId = userId;
            this.avatarUrl = avatarUrl;
        }
    }

    private static class DefaultDepartment {
        @NonNull private final String departmentId;

        private DefaultDepartment(@NonNull String departmentId) {
            this.departmentId = departmentId;
        }
    }

    private static class UserInstitutionId {
        @NonNull private final String institutionId;

        private UserInstitutionId(@NonNull String institutionId) {
            this.institutionId = institutionId;
        }
    }

    private CompletableFuture<String> getDefaultDepartment(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PostgrestDefaultClient client = supabase.getFrom("users");
                UserInstitutionId institutionId = client
                    .select(Columns.list("institution_id"))
                    .eq("user_id", userId)
                    .decodeSingle(UserInstitutionId.class);

                DefaultDepartment defaultDepartment = supabase
                    .getFrom("dept_id")
                    .select(Columns.list("department_id"))
                    .eq("institution_id", institutionId.institutionId)
                    .decodeSingle(DefaultDepartment.class);

                return defaultDepartment.departmentId;
            } catch (Exception e) {
                Log.d("DB", "ERROR GET DEFAULT DEPT: " + e.getMessage());
                return null;
            }
        });
    }

    private CompletableFuture<String> getUserId() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UserInfo user = supabase.getAuth().getCurrentUserOrNull();

                if (user != null) {
                    PostgrestDefaultClient client = supabase.getFrom("users");
                    List<UserDataLogin> userData = client
                        .select()
                        .eq("user_id", user.getId())
                        .decodeList(UserDataLogin.class);

                    if (userData.isEmpty()) {
                        UserDataLogin addUser = new UserDataLogin(
                            user.getIdentities().get(0).getIdentityData().get("srCode"),
                            user.getEmail(),
                            user.getIdentities().get(0).getIdentityData().get("firstName"),
                            user.getIdentities().get(0).getIdentityData().get("lastName"),
                            user.getIdentities().get(0).getIdentityData().get("institutionId")
                        );

                        client.upsert(addUser).onConflict("user_id").execute();

                        DefaultAvatar addDefaultAvatar = new DefaultAvatar(
                            user.getId(),
                            defaultAvatarUrl
                        );

                        supabase.getFrom("avatars")
                            .upsert(addDefaultAvatar)
                            .onConflict("user_id")
                            .execute();

                        String cicsDeptId = getDefaultDepartment(user.getId()).get();

                        supabase.getFrom("users")
                            .update(new DefaultDepartment(cicsDeptId))
                            .eq("user_id", user.getId())
                            .execute();

                    } else {
                        Log.d("DB", "user_data: " + userData);
                    }

                    return user.getId();
                } else {
                    Log.d("DB", "no user found");
                    return null;
                }
            } catch (Exception e) {
                Log.d("DB", "error getUser " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<List<Institution>> getInstitutions(Context context, Spinner institutionSpinner) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Institution> results = supabase
                    .getFrom("institutions")
                    .select(Columns.list("institution", "email_extension", "institution_id"))
                    .decodeList(Institution.class);

                if (!results.isEmpty()) {
                    List<String> institutionNames = new ArrayList<>();
                    for (Institution inst : results) {
                        institutionNames.add(inst.getInstitution());
                    }

                    context.getMainExecutor().execute(() -> {
                        InstitutionAdapter institutionAdapter = new InstitutionAdapter(context, institutionNames);
                        institutionSpinner.setAdapter(institutionAdapter);
                        institutionSpinner.setSelection(0);
                    });
                } else {
                    context.getMainExecutor().execute(() ->
                        Toast.makeText(context, "No institutions available.", Toast.LENGTH_SHORT).show()
                    );
                    Log.d("DB", "no institutions available");
                }

                return results;
            } catch (Exception e) {
                context.getMainExecutor().execute(() ->
                    Toast.makeText(context, "Error fetching data: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
                );
                Log.d("DB", e.getMessage());
                return new ArrayList<>();
            }
        });
    }

    public CompletableFuture<String> login(String emailInp, String passwordInp, Context context) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Auth.SignInResult user = supabase.getAuth().signInWith(Email.INSTANCE)
                    .setEmail(emailInp)
                    .setPassword(passwordInp)
                    .execute();

                Log.d("DB", user.toString());
                return getUserId().get();
            } catch (Exception e) {
                context.getMainExecutor().execute(() ->
                    Toast.makeText(context, "Error signing up: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show()
                );
                Log.d("DB", "Error login: " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<UserInfo> signup(String firstName, String lastName, String srCode,
                                            String passwordInp, String emailExtension,
                                            String institutionId) {
        return CompletableFuture.supplyAsync(() -> {
            String emailInp = srCode + emailExtension;

            Log.d("DB", firstName + ", " + lastName + ", " + srCode + ", " + passwordInp + ", " +
                emailExtension + ", " + emailInp + ", " + institutionId);

            try {
                JsonObject data = new JsonObject();
                data.addProperty("srCode", srCode);
                data.addProperty("firstName", firstName);
                data.addProperty("lastName", lastName);
                data.addProperty("institutionId", institutionId);

                return supabase.getAuth().signUpWith(Email.INSTANCE)
                    .setEmail(emailInp)
                    .setPassword(passwordInp)
                    .setData(data)
                    .execute();
            } catch (Exception e) {
                Log.d("DB", "error signup " + e.getMessage());
                return null;
            }
        });
    }

    public CompletableFuture<UserData> getUserDataByUserId(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            String columns = """
                user_id,
                email,
                sr_code,
                first_name,
                last_name,
                institutions (
                    institution,
                    institution_logo,
                    campus
                ),
                departments (
                    department
                ),
                avatars (
                    avatar_url
                )
                """.trim();

            try {
                Log.d("DB", "hzxcxasd " + userId);
                UserData userData = supabase
                    .getFrom("users")
                    .select(Columns.raw(columns))
                    .eq("user_id", userId)
                    .decodeSingle(UserData.class);

                Log.d("DB", "user dataaa: " + userData);
                return userData;
            } catch (Exception e) {
                Log.d("DB", "erroreeee get user " + e.getMessage());
                return null;
            }
        });
    }
}
