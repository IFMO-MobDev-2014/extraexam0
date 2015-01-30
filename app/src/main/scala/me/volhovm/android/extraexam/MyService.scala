package me.volhovm.android.extraexam

import android.app.IntentService
import android.content.Intent
import android.os.{Bundle, Handler, ResultReceiver}
import android.util.Log

import scala.util.Random

object MyService {
  val SERVICE_NAME = "MyService"
  val STATUS_RUNNING = 0
  val STATUS_FINISHED = 1
  val STATUS_ERROR = 2

  val SERVICE_MODE = "service_mode"
  val FIRST_MODE = 0
  val SECOND_MODE = 1
  val THIRD_MODE = 2

  val RECEIVER = "receiver"
}

class MyService extends IntentService("MyService") {
  override def onHandleIntent(intent: Intent): Unit = {
    import me.volhovm.android.extraexam.MyService._
    val receiver: ResultReceiver = intent.getParcelableExtra(RECEIVER)
    val mWrapper = new HelperWrapper(getContentResolver)
    var mCurrentBuilds: List[Build] = mWrapper.getLatestBuilds

    while (true) {
      Thread.sleep(1000)
      try {
        mCurrentBuilds = mWrapper.getLatestBuilds
        for (i <- mCurrentBuilds) {
          // if it's running it can fail
          if (i.status.isInstanceOf[Running]) {
            if (Random.nextInt(100) < 3) {
              mWrapper.replaceBuild(i.version, i.name, new Build(i.name, i.version, i.startTimeSec, i.buildLengthSec, FailedBuild()))
            }
          }
          // if it's ended, so it is
          else if (i.status.isInstanceOf[Running] && (i.startTimeSec + i.buildLengthSec) > (System.currentTimeMillis() / 1000L).toInt)
          {
            Log.d(this.toString, "Making it complete")
            mWrapper.replaceBuild(i.version, i.name, new Build(i.name, i.version, i.startTimeSec, i.buildLengthSec, SuccessBuild()))
          }
          // if it's ended, rerun it
          else if (!i.status.isInstanceOf[Running] && Random.nextInt(100) < 6)
            mWrapper.putBuild(i.next)

          receiver.send(0, null)
        }
      } catch {
        case a: Throwable =>
          Log.e("MyService", "Exception for mode #" + a.toString)
          val bundle: Bundle = new Bundle()
          bundle.putString(Intent.EXTRA_TEXT, a.toString)
          if (receiver != null) receiver.send(STATUS_ERROR, bundle)
      }
    }
  }
}

trait Receiver {
  def onReceiveResult(resCode: Int, resData: Bundle): Unit
}

class MyCustomReceiver(handler: Handler) extends ResultReceiver(handler) {
  private var mReceiver: Receiver = null
  def setReceiver(r: Receiver) = mReceiver = r
  override def onReceiveResult(resCode: Int, resData: Bundle) = if (mReceiver != null) mReceiver.onReceiveResult(resCode, resData)
}
