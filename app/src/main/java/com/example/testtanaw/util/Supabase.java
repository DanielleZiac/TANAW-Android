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
        SupabaseClientBuilder builder = new SupabaseClientBuilder(
            BuildConfig.SUPABASE_URL,
            BuildConfig.SUPABASE_ANON_KEY
        );
        builder.install(Auth.INSTANCE);
        builder.install(Postgrest.INSTANCE);
        builder.install(Realtime.INSTANCE);
        builder.install(Storage.INSTANCE);
        
        this.supabase = builder.build();
    }
}
