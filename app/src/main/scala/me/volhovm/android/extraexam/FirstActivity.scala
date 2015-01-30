package me.volhovm.android.extraexam

import java.text.SimpleDateFormat

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.{Intent, AsyncTaskLoader, Context, Loader}
import android.os.{Handler, Bundle}
import android.util.Log
import android.view._
import android.widget.{BaseAdapter, ListView, Toast}

class FirstActivity extends Activity with LoaderCallbacks[List[Build]] with Receiver {
  private var mDatabaseHelper: DatabaseHelper = null
  private var mListView: ListView = null
  private var mListAdapter: MyYobaBaseAdapter = null
  private var mCurrentBuilds: List[Build] = Nil

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    mDatabaseHelper = new DatabaseHelper(this)
    mListView = new ListView(this)
    mListAdapter = new MyYobaBaseAdapter(this)
    mListView.setAdapter(mListAdapter)
    setContentView(mListView)
    getLoaderManager.initLoader(0, null, this).forceLoad()
    val intent = new Intent(this, classOf[MyService])
    val mReciever: MyCustomReceiver = new MyCustomReceiver(new Handler())
    mReciever.setReceiver(cast(this))
    intent.putExtra(MyService.RECEIVER, mReciever)
    startService(intent)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
    case R.id.action_add_build => {
      mDatabaseHelper.mWrapper.putBuild(Build.newBuild("RandB#" + scala.util.Random.nextInt(1125).toString))
      Toast.makeText(this, "Added new build", Toast.LENGTH_SHORT).show()
      getLoaderManager.restartLoader(0, null, this).forceLoad()
      true
    }
    case R.id.action_refresh =>
      Toast.makeText(this, "TEST", Toast.LENGTH_LONG).show()
      true
  }

  class MyYobaBaseAdapter(context: Context) extends BaseAdapter {
    override def getCount: Int = mCurrentBuilds.length
    override def getItemId(p1: Int): Long = p1
    override def getView(p1: Int, p2: View, p3: ViewGroup): View =
    {
      val inflater: LayoutInflater = cast(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
      val view: View = cast(inflater.inflate(R.layout.build_status, p3, false))
      val bld = mCurrentBuilds(p1)
      setText(view, R.id.build_id, bld.version.toString)
      setText(view, R.id.build_name, bld.name.toString)
      setText(view, R.id.build_start_time, new SimpleDateFormat("hh:mm:ss").format(new java.util.Date(bld.startTimeSec.toLong * 1000L)))
      setText(view, R.id.time_status, bld.status match {
        case Running() => "Estimated (s):"
        case _ => "Finished:"
      })
      setText(view, R.id.build_end_time, bld.status match {
        case Running() =>
          ((bld.startTimeSec.toLong + bld.buildLengthSec.toLong) - (System.currentTimeMillis() / 1000).toInt).toString
        case _ => new SimpleDateFormat("hh:mm:ss")
          .format(new java.util.Date((bld.startTimeSec.toLong + bld.buildLengthSec.toLong) * 1000))
      })
      setText(view, R.id.build_status, bld.status.toString)
      view
    }
    override def getItem(p1: Int): AnyRef = mCurrentBuilds(p1)
  }

  override def onCreateLoader(p1: Int, p2: Bundle): Loader[List[Build]] =
  new AsyncTaskLoader[List[Build]](this) {
    override def loadInBackground(): List[Build] = mDatabaseHelper.mWrapper.getLatestBuilds
  }
  override def onLoaderReset(p1: Loader[List[Build]]): Unit = ()
  override def onLoadFinished(p1: Loader[List[Build]], p2: List[Build]): Unit = {
    Log.d(this.toString, "Refreshing list: got n=" + p2.length)
    mCurrentBuilds = p2
    mListAdapter.notifyDataSetChanged()
  }
  override def onReceiveResult(resCode: Int, resData: Bundle): Unit = {
    getLoaderManager.restartLoader(0, null, this).forceLoad()
    mListAdapter.notifyDataSetChanged()
  }
}