package com.loguito.clase6.views.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MarvelCharacter::class), version = 1, exportSchema = false)
public abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao() : CharacterDAO

    companion object {
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        fun getDatabase(context: Context) : CharacterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CharacterDatabase::class.java,
                    "character_database",
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}