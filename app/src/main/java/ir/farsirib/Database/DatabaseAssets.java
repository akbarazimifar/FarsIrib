package ir.farsirib.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("ALL")
public class DatabaseAssets extends SQLiteOpenHelper {

	public static final String DIR_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String DIR_DATABASE = DIR_SDCARD + "/farsirib.sqlite/";



	private static final int DATABASE_VERSION = 1;
	protected  String DB_PATH = null;

	protected static final String TABLE_BARNAME= "barname";
//	protected static final String TABLE_OPTION_BTN = "option_btn";

    protected static String DB_NAME = "farsirib.sqlite";
	private SQLiteDatabase myDataBase;

	private final Context myContext;

	protected static final String KEY_CATEGORY_ID = "category_id";
	protected static final String KEY_IMAGE_URL = "image_url";
	protected static final String KEY_TITLE = "title";
	protected static final String KEY_VIDEO_URL = "video_url";
	protected static final String KEY_DESCRIPTION = "description";


	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DatabaseAssets(Context context) {

		super(context, DB_NAME, null,DATABASE_VERSION );
		this.myContext = context;
		DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {


		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}
	}

	
	private boolean checkDataBase() {

		myContext.getApplicationContext();
		SQLiteDatabase checkDB = null;

		try {

			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY
							| SQLiteDatabase.NO_LOCALIZED_COLLATORS
							| SQLiteDatabase.CREATE_IF_NECESSARY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY
						| SQLiteDatabase.NO_LOCALIZED_COLLATORS
						| SQLiteDatabase.CREATE_IF_NECESSARY);

		createTable();
	}

	private void createTable() {

		myDataBase.execSQL("CREATE  TABLE  IF NOT EXISTS barname (" +
				"id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
				"category_id INTEGER , " +
				"image_url TEXT , " +
				"title TEXT , " +
				"video_url TEXT , " +
				"description TEXT " +
				")");
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			}

	// return cursor
	public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
		return myDataBase
				.query(TABLE_BARNAME, null, null, null, null, null, null);

	}
}
