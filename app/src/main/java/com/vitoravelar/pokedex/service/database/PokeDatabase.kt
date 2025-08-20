package com.vitoravelar.pokedex.service.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vitoravelar.pokedex.feature.model.DetailFavoritesEntity
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity

@Database(entities = [PokemonDetailEntity::class, DetailFavoritesEntity::class], version = 2)
abstract class PokeDatabase : RoomDatabase() {

    abstract fun pokeDao(): PokeDao

    companion object {
        @Volatile
        private var INSTANCE: PokeDatabase? = null
        private const val DATABASE_NAME = "pokedb"

        fun getDatabase(context: Context): PokeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PokeDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `detail_favorites` (
                        `pokemon_id` INTEGER PRIMARY KEY, 
                        `hp` INTEGER NOT NULL, 
                        `attack` INTEGER NOT NULL, 
                        `defense` INTEGER NOT NULL, 
                        `special_attack` INTEGER NOT NULL, 
                        `special_defense` INTEGER NOT NULL, 
                        `speed` INTEGER NOT NULL, 
                        `skills` TEXT NOT NULL
                    )
                """)
            }
        }
    }
}

