package me.volhovm.android.extraexam

import android.content.{ContentResolver, Context}
import android.database.Cursor
import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.provider.BaseColumns
import android.util.Log

object DatabaseHelper extends BaseColumns {
  private val DATABASE_VERSION: Int = 1
  val DATABASE_NAME = "androidscratch.db"

  val FIRST_TABLE_NAME = "builds"

  val BUILD_NAME = "build_name"
  val BUILD_VERSION = "build_version"
  val BUILD_STATUS = "build_status"
  val BUILD_START_TIME = "build_start_time"
  val BUILD_LENGTH = "build_length"

  val CREATE_FIRST_TABLE = "create table " +
    FIRST_TABLE_NAME + " (" +
    BaseColumns._ID + " integer primary key autoincrement" +
    ", " + BUILD_NAME + " text not null" +
    ", " + BUILD_VERSION + " integer" +
    ", " + BUILD_START_TIME + " integer" +
    ", " + BUILD_LENGTH + " integer" +
    ", " + BUILD_STATUS + " integer" +
    ");"
}

class DatabaseHelper(context: Context) extends SQLiteOpenHelper(
  context,
  DatabaseHelper.DATABASE_NAME,
  null,
  DatabaseHelper.DATABASE_VERSION) with BaseColumns {

  val mWrapper = new HelperWrapper(context.getContentResolver)
  import me.volhovm.android.extraexam.DatabaseHelper._

  override def onCreate(db: SQLiteDatabase): Unit = {
    db.execSQL(CREATE_FIRST_TABLE)
  }

  override def onUpgrade(p1: SQLiteDatabase, p2: Int, p3: Int): Unit = throw new UnsupportedOperationException("CANNOT UPGRADE DB")
}

class HelperWrapper(mContentResolver: ContentResolver) {
  def putBuild(build: Build) = mContentResolver.insert(MyContentProvider.MAIN_CONTENT_URI, build.getValues)

  def getLatestBuilds: List[Build] = {
    val allBuilds = getAllBuilds
    Log.d(this.toString, "Got all builds, number:" + allBuilds.length.toString)
    allBuilds.groupBy(_.name).values.toList
      //      .map((x: List[Build]) => x.sorted)
      .map(_.last).reverse
  }
  
  def replaceBuild(version: Int, name: String, newBuild: Build) =
    mContentResolver.update(MyContentProvider.MAIN_CONTENT_URI, newBuild.getValues, DatabaseHelper.BUILD_VERSION + "=" + version +
      " AND " + DatabaseHelper.BUILD_NAME + "='"+ name +"'",null)

  
  
  def getAllBuilds: List[Build] = moveAndCompose(
    mContentResolver.query(MyContentProvider.MAIN_CONTENT_URI, null, null, null, null),
    Build.fromCursor
  )

  private def moveAndCompose[A](cursor: Cursor, foo: (Cursor) => A): List[A] = {
    cursor.moveToFirst()
    compose(cursor, foo)
  }

  private def compose[A](cursor: Cursor, foo: (Cursor) => A): List[A] =
    if (cursor.isAfterLast) {cursor.close(); Nil}
    else foo(cursor) :: compose({
      cursor.moveToNext()
      cursor
    }, foo)
}