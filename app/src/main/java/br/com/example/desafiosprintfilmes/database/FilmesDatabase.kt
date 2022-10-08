package br.com.example.desafiosprintfilmes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.example.desafiosprintfilmes.model.Filme

@Database(
    entities = arrayOf(Filme::class),
    version = 3,
    exportSchema = false
)
abstract class FilmesDatabase : RoomDatabase() {
    abstract fun filmeFavoritoDao(): FilmeFavoritoDao
    companion object {
        @Volatile
        private var instance: FilmesDatabase? = null

        fun pegaDatabase(context: Context): FilmesDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = buildDatabase(context)
                }
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): FilmesDatabase {
            val MIGRATION_1_2 = object: Migration(1,2){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("drop table FilmesFavoritos")
                }
            }

            return Room.databaseBuilder(
                context.applicationContext,
                FilmesDatabase::class.java,
                "filmes_db"
            ).addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration()
                .build()
        }
    }

}