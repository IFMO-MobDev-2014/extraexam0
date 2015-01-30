package me.volhovm.android.extraexam

import android.content.ContentValues
import android.database.Cursor

import scala.util.Random

object Build {
  def fromCursor(cursor: Cursor): Build =
  new Build(
    cursor.getString(1),
    cursor.getInt(2),
    cursor.getInt(3),
    cursor.getInt(4),
    cursor.getInt(5) match {
      case 0 => SuccessBuild()
      case 1 => FailedBuild()
      case 2 => Running()
    })
  def newBuild(name: String): Build =
    new Build(name, 0, (System.currentTimeMillis() / 1000L).toInt, new Random().nextInt(30) + 10, Running())
}

class Build(val name: String,
            val version: Int,
            val startTimeSec: Int,
            val buildLengthSec: Int,
            val status: Status)  extends Ordered[Build]{
  def getValues: ContentValues = {
    import DatabaseHelper._
    val values = new ContentValues()
    values.put(BUILD_NAME, name)
    values.put(BUILD_VERSION, Int.box(version))
    values.put(BUILD_START_TIME, Int.box(startTimeSec.toInt))
    values.put(BUILD_LENGTH, Int.box(buildLengthSec.toInt))
    values.put(BUILD_STATUS, Int.box(status match {
      case SuccessBuild() => 0
      case FailedBuild() => 1
      case Running() => 2
    }))
    values
  }
  override def compare(that: Build): Int = startTimeSec compare that.startTimeSec
  def next =
    new Build(name, version + 1, (System.currentTimeMillis() / 1000L).toInt, new Random().nextInt(30) + 10, Running())
}

sealed trait Status
case class SuccessBuild() extends Status {
  override def toString = "Success"
}
case class FailedBuild() extends Status {
  override def toString = "Fail"
}
case class Running() extends Status {
  override def toString = "Running"
}