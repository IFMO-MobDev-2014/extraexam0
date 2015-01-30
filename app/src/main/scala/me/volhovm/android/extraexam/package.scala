package me.volhovm.android

import android.view.View
import android.widget.TextView

package object extraexam {
  def cast[A, B](x: A): B = x match {
    case a: B => a
    case _ => throw new ClassCastException
  }

  def setText(view: View, res: Int, text: String) =
    cast[View, TextView](view.findViewById(res)).setText(text)
}
