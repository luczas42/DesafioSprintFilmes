package br.com.example.desafiosprintfilmes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.example.desafiosprintfilmes.model.Filme
import br.com.example.desafiosprintfilmes.model.FilmesFavoritos

@Database(
    entities = arrayOf(FilmesFavoritos::class, Filme::class),
    version = 2,
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
            val MIGRATION_1_2 = object : Migration(1,2){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("DROP TABLE IF EXISTS `FilmesFavoritos`")
                    database.execSQL("CREATE TABLE IF NOT EXISTS `FilmesFavoritos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `filme_id` INTEGER NOT NULL)")
                }
            }
            return Room.databaseBuilder(
                context.applicationContext,
                FilmesDatabase::class.java,
                "filmes_db"
            ).addMigrations(MIGRATION_1_2)
                .build()
        }
    }

}