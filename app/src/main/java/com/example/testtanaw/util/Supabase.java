package com.example.testtanaw.util;

import com.example.testtanaw.BuildConfig;
import io.github.jan.supabase.SupabaseClient;
import io.github.jan.supabase.createSupabaseClient;
import io.github.jan.supabase.auth.Auth;
import io.github.jan.supabase.postgrest.Postgrest;
import io.github.jan.supabase.realtime.Realtime;
import io.github.jan.supabase.storage.Storage;

public class Supabase {
    protected final SupabaseClient supabase;

    public Supabase() {
        this.supabase = createSupabaseClient(
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_ANON_KEY,
            config -> {
                config.install(Auth.INSTANCE);
                config.install(Postgrest.INSTANCE);
                config.install(Realtime.INSTANCE);
                config.install(Storage.INSTANCE);
                return null;
            }
        );
    }
}
