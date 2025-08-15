package com.vitoravelar.pokedex.service.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity

@Database(entities = [PokemonDetailEntity::class], version = 1)
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
                    .build().also { INSTANCE = it }
            }
        }
    }
}

