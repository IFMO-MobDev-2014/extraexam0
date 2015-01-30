package me.volhovm.android.extraexam

import android.app.Activity
import android.app.LoaderManager.LoaderCallbacks
import android.content.Loader
import android.os.Bundle
import android.view.{Menu, MenuItem}
import android.widget.Toast

class ThirdActivity extends Activity with LoaderCallbacks[Void] {
  private var mDatabaseHelper: DatabaseHelper = null

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    mDatabaseHelper = new DatabaseHelper(this)
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.menu_main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
    case R.id.action_settings => ???
    case R.id.action_refresh =>
      Toast.makeText(this, "TEST", Toast.LENGTH_LONG).show()
      super.onOptionsItemSelected(item)
  }

  override def onCreateLoader(p1: Int, p2: Bundle): Loader[Void] = ???
  override def onLoaderReset(p1: Loader[Void]): Unit = ???
  override def onLoadFinished(p1: Loader[Void], p2: Void): Unit = ???
}