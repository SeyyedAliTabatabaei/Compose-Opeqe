package ir.composeopeqe.data.dbimport androidx.room.Databaseimport androidx.room.RoomDatabaseimport androidx.room.TypeConvertersimport ir.composeopeqe.data.*@Database(entities = [Result::class] , version = 2 )@TypeConverters(    NameConventer::class ,    LocationConventer::class ,    CoordinatesConventer::class ,    TimezoneConventer::class ,    LoginConventer::class ,    DobConventer::class ,    RegisteredConventer::class ,    IdConventer::class ,    PictureConventer::class ,    InfoConventer::class)abstract class DatabaseApp : RoomDatabase(){    abstract fun getDao() : DaoApp}