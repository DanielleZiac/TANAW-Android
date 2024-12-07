package com.example.testtanaw.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.testtanaw.R;
import io.github.jan.supabase.postgrest.SimpleFilter;
import io.github.jan.supabase.storage.Storage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.launch;

public class CRUD extends Supabase {
    
    public static class SdgPhoto {
        @NonNull private final String userSdgId;
        @NonNull private final String userId;
        @NonNull private final String sdgNumber;
        @NonNull private final String url;
        @NonNull private final String caption;
        @NonNull private final String createdDate;
        @NonNull private final String institutionId;
        @NonNull private final String phototChall;
        @NonNull private final String institution;
        @NonNull private final String campus;
        @NonNull private final String institutionLogo;
        private final double lat;
        private final double lng;
        @NonNull private final String avatarUrl;

        public SdgPhoto(@NonNull String userSdgId, @NonNull String userId, 
                       @NonNull String sdgNumber, @NonNull String url,
                       @NonNull String caption, @NonNull String createdDate,
                       @NonNull String institutionId, @NonNull String phototChall,
                       @NonNull String institution, @NonNull String campus,
                       @NonNull String institutionLogo, double lat, double lng,
                       @NonNull String avatarUrl) {
            this.userSdgId = userSdgId;
            this.userId = userId;
            this.sdgNumber = sdgNumber;
            this.url = url;
            this.caption = caption;
            this.createdDate = createdDate;
            this.institutionId = institutionId;
            this.phototChall = phototChall;
            this.institution = institution;
            this.campus = campus;
            this.institutionLogo = institutionLogo;
            this.lat = lat;
            this.lng = lng;
            this.avatarUrl = avatarUrl;
        }

        // Getters
        @NonNull public String getUserSdgId() { return userSdgId; }
        @NonNull public String getUserId() { return userId; }
        @NonNull public String getSdgNumber() { return sdgNumber; }
        @NonNull public String getUrl() { return url; }
        @NonNull public String getCaption() { return caption; }
        @NonNull public String getCreatedDate() { return createdDate; }
        @NonNull public String getInstitutionId() { return institutionId; }
        @NonNull public String getPhototChall() { return phototChall; }
        @NonNull public String getInstitution() { return institution; }
        @NonNull public String getCampus() { return campus; }
        @NonNull public String getInstitutionLogo() { return institutionLogo; }
        public double getLat() { return lat; }
        public double getLng() { return lng; }
        @NonNull public String getAvatarUrl() { return avatarUrl; }
    }

    public static class UserLocation {
        @NonNull private final String userId;
        private final double userLatitude;
        private final double userLongitude;

        public UserLocation(@NonNull String userId, double userLatitude, double userLongitude) {
            this.userId = userId;
            this.userLatitude = userLatitude;
            this.userLongitude = userLongitude;
        }
    }

    public static class UserAvatarData {
        private final String avatarId;
        private final String userId;
        @NonNull private final String avatarUrl;
        @NonNull private final String bg;
        private final String eye;
        @NonNull private final String sex;
        @NonNull private final String shirtStyle;
        private final String smile;
        private final String eyewear;

        public UserAvatarData(String avatarId, String userId, @NonNull String avatarUrl,
                            @NonNull String bg, String eye, @NonNull String sex,
                            @NonNull String shirtStyle, String smile, String eyewear) {
            this.avatarId = avatarId;
            this.userId = userId;
            this.avatarUrl = avatarUrl;
            this.bg = bg;
            this.eye = eye;
            this.sex = sex;
            this.shirtStyle = shirtStyle;
            this.smile = smile;
            this.eyewear = eyewear;
        }

        // Getters
        public String getAvatarId() { return avatarId; }
        public String getUserId() { return userId; }
        @NonNull public String getAvatarUrl() { return avatarUrl; }
        @NonNull public String getBg() { return bg; }
        public String getEye() { return eye; }
        @NonNull public String getSex() { return sex; }
        @NonNull public String getShirtStyle() { return shirtStyle; }
        public String getSmile() { return smile; }
        public String getEyewear() { return eyewear; }
    }

    public static class LeaderboardSchool {
        @NonNull private final String institutionId;
        @NonNull private final String institution;
        @NonNull private final String campus;
        @NonNull private final String departmentLogo;
        @NonNull private final String department;
        @NonNull private final String count;

        public LeaderboardSchool(@NonNull String institutionId, @NonNull String institution,
                               @NonNull String campus, @NonNull String departmentLogo,
                               @NonNull String department, @NonNull String count) {
            this.institutionId = institutionId;
            this.institution = institution;
            this.campus = campus;
            this.departmentLogo = departmentLogo;
            this.department = department;
            this.count = count;
        }

        // Getters
        @NonNull public String getInstitutionId() { return institutionId; }
        @NonNull public String getInstitution() { return institution; }
        @NonNull public String getCampus() { return campus; }
        @NonNull public String getDepartmentLogo() { return departmentLogo; }
        @NonNull public String getDepartment() { return department; }
        @NonNull public String getCount() { return count; }
    }

    public List<SdgPhoto> getSdgPhoto(Integer sdg, String date, String institutionId) {
        Map<String, Object> qryParams = new HashMap<>();

        if (sdg != null) {
            qryParams.put("sdg_number", "sdg" + sdg);
        }

        if (institutionId != null) {
            qryParams.put("institution_id", institutionId);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
        String currentDate = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();

        if (date != null) {
            switch (date) {
                case "today":
                    qryParams.put("created_date", currentDate);
                    break;
                case "yesterday":
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    qryParams.put("created_date", sdf.format(calendar.getTime()));
                    break;
                case "last week":
                    calendar.add(Calendar.DAY_OF_YEAR, -7);
                    qryParams.put("gte", sdf.format(calendar.getTime()));
                    qryParams.put("lte", currentDate);
                    break;
                case "last month":
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    String firstDayOfMonth = sdf.format(calendar.getTime());
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    qryParams.put("gte", sdf.format(calendar.getTime()));
                    qryParams.put("lt", firstDayOfMonth);
                    break;
                default:
                    Log.d("CRUD", "Invalid filter date");
            }
        }

        try {
            return supabase.getFrom("get_photo_and_avatar")
                    .select()
                    .filter(qryParams)
                    .executeAndGetList(SdgPhoto.class);
        } catch (Exception e) {
            Log.d("CRUD", "Error getSdgPhoto: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveUserLastLocation(String userId, double latitude, double longitude) {
        CoroutineScope scope = new CoroutineScope(Dispatchers.getMain());
        scope.launch(() -> {
            try {
                UserLocation location = new UserLocation(userId, latitude, longitude);
                supabase.getFrom("user_location")
                        .upsert(location)
                        .onConflict("user_id")
                        .execute();
            } catch (Exception e) {
                Log.d("CRUD", "Error saveUserLastLocation: " + e.getMessage());
            }
        });
    }

    public List<SdgPhoto> getPhotoByUserId(String userId) {
        try {
            List<SdgPhoto> photos = supabase.getFrom("get_photo_and_avatar")
                    .select()
                    .eq("user_id", userId)
                    .executeAndGetList(SdgPhoto.class);
            Log.d("CRUD", "Photos: " + photos);
            return photos;
        } catch (Exception e) {
            Log.d("CRUD", e.getMessage());
            return null;
        }
    }

    public List<SdgPhoto> getPhotoByInstitutionId(String institutionId) {
        try {
            List<SdgPhoto> photos = supabase.getFrom("get_photo_and_avatar")
                    .select()
                    .eq("institution_id", institutionId)
                    .executeAndGetList(SdgPhoto.class);
            Log.d("CRUD", "Photos: " + photos);
            return new ArrayList<>();
        } catch (Exception e) {
            Log.d("CRUD", "ERROR GET PHOTO BY INSTITUTION ID: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public UserAvatarData getUserAvatarData(String userId) {
        try {
            return supabase.getFrom("avatars")
                    .select()
                    .eq("user_id", userId)
                    .executeAndGetSingle(UserAvatarData.class);
        } catch (Exception e) {
            Log.d("CRUD", e.getMessage());
            return null;
        }
    }

    private byte[] combineImg(Resources resources, String eyewear, String shirtStyle, 
                            String sex, String bg) {
        Bitmap bgBitmap;
        Bitmap sexBitmap;
        Bitmap shirtStyleBitmap;
        Bitmap eyewearBitmap = null;
        Bitmap eyeBitmap = BitmapFactory.decodeResource(resources, R.drawable.eyes_opened);
        Bitmap smileBitmap = BitmapFactory.decodeResource(resources, R.drawable.mouth_closed);

        if (eyewear != null) {
            eyewearBitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses);
        }

        switch (shirtStyle) {
            case "shirt":
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.shirt);
                break;
            case "polo":
                shirtStyleBitmap = BitmapFactory.decodeResource(resources, R.drawable.polo);
                break;
            default:
                throw new IllegalArgumentException("Invalid shirt style");
        }

        switch (sex) {
            case "boy":
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.boy);
                break;
            case "girl":
                sexBitmap = BitmapFactory.decodeResource(resources, R.drawable.girl);
                break;
            default:
                throw new IllegalArgumentException("Invalid sex");
        }

        switch (bg) {
            case "cics":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cics);
                break;
            case "coe":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_coe);
                break;
            case "cet":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cet);
                break;
            case "cafad":
                bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_cafad);
                break;
            default:
                throw new IllegalArgumentException("Invalid background");
        }

        Bitmap overlayedBitmap = Bitmap.createBitmap(bgBitmap.getWidth(), 
                bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlayedBitmap);

        canvas.drawBitmap(bgBitmap, 0f, 0f, null);
        canvas.drawBitmap(sexBitmap, 0f, 0f, null);
        canvas.drawBitmap(shirtStyleBitmap, 0f, 0f, null);
        if (eyewearBitmap != null) {
            canvas.drawBitmap(eyewearBitmap, 0f, 0f, null);
        }
        canvas.drawBitmap(eyeBitmap, 0f, 0f, null);
        canvas.drawBitmap(smileBitmap, 0f, 0f, null);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        overlayedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private String uploadAvatar(byte[] byteArray, String userId) {
        String path = userId + "/" + UUID.randomUUID() + ".png";
        try {
            Storage bucket = supabase.getStorage().from("avatars");
            Log.d("CRUD", "path: " + path);

            if (byteArray != null) {
                bucket.upload(path, byteArray);
                return supabase.getStorage().from("avatars").getPublicUrl(path);
            }
            return null;
        } catch (Exception e) {
            Log.d("CRUD", "ERROR UPLOAD AVATAR: " + e.getMessage());
            return null;
        }
    }

    private void updateAvatar(String userId, String publicUrl, String eyewear,
                            String shirtStyle, String sex, String bg) {
        try {
            UserAvatarData avatar = new UserAvatarData(
                    null,
                    userId,
                    publicUrl,
                    bg,
                    "eyes_opened",
                    sex,
                    shirtStyle,
                    "mouth_closed",
                    eyewear
            );

            supabase.getFrom("avatars")
                    .upsert(avatar)
                    .onConflict("user_id")
                    .execute();
        } catch (Exception e) {
            Log.d("CRUD", "ERROR UPDATE AVATAR: " + e.getMessage());
        }
    }

    public String saveAvatar(Resources resources, String userId, String eyewear,
                           String shirtStyle, String sex, String bg) {
        byte[] imageData = combineImg(resources, eyewear, shirtStyle, sex, bg);
        if (imageData != null) {
            String publicUrl = uploadAvatar(imageData, userId);
            Log.d("CRUD", "PUBLIC URL: " + publicUrl);

            if (publicUrl != null) {
                updateAvatar(userId, publicUrl, eyewear, shirtStyle, sex, bg);
                Log.d("CRUD", "Avatar updated successfully");
                return publicUrl;
            }
        }
        return null;
    }

    public List<LeaderboardSchool> getLeaderboardsSchools(String institutionId) {
        try {
            return supabase.getFrom("leaderboards_schools")
                    .select()
                    .eq("institution_id", institutionId)
                    .executeAndGetList(LeaderboardSchool.class);
        } catch (Exception e) {
            Log.d("CRUD", "Error getting leaderboards: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
