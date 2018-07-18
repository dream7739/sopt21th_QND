package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import org.sopt21.qnd.R

class FragmentHelp : Fragment(), View.OnClickListener {
    internal lateinit var btn_back: RelativeLayout
    internal lateinit var mFragment: Fragment

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_help, container, false)

        mFragment = fragmentManager.findFragmentById(R.id.fragment)
        btn_back = view.findViewById(R.id.btn_back) as RelativeLayout
        btn_back.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        val fm = activity.supportFragmentManager

        when (v.id) {
            R.id.btn_back -> {
                fm.beginTransaction().remove(this).commit()
                fm.popBackStack()
            }
        }
    }
}
