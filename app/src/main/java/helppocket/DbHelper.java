package helppocket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 08.03.2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public final static String ID = "_id";
    public final static String NAME = "name";
    public final static String MODEL = "model";
    public final static String TEAM = "team";
    public final static String TIME = "time";


    public final static String TABLE = "sport_table";
    public final static String createTableSportInfo = "CREATE TABLE " + TABLE + " (" + ID + " integer primary key , "
            + NAME + " text not null, " + MODEL + " text not null, " + TEAM + " text not null);";

    public final static String ALTER_TABLE_ADD_TIME_INFO = "ALTER TABLE " + TABLE + " ADD COLUMN " + TIME + " text";

    public DbHelper(Context context, int version) {
        super(context, "db_sport_type", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSportInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2)
        {
            db.execSQL(ALTER_TABLE_ADD_TIME_INFO);
        }
    }
}
