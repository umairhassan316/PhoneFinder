package com.findphone.whistleclapfinder.clapsClasess.clapsdatabase;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.AudioModelWhisleDatabase;
import com.findphone.whistleclapfinder.whistleClasess.whistledatabase.WhistleAudioModelDao;

@Database(entities = {AudioModelClapsDatabase.class, AudioModelWhisleDatabase.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {
    public abstract AudioModelDao audioModelDao();
    public abstract WhistleAudioModelDao whistleAudioModelDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "my_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Perform any necessary migration logic here
            // You might need to create the new table, add columns, etc.
            // Example: database.execSQL("CREATE TABLE IF NOT EXISTS BatterySaving (...)");
        }
    };
}
