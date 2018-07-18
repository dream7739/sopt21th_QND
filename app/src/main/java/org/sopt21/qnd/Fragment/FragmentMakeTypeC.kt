package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.sopt21.qnd.R

class FragmentMakeTypeC : Fragment() {
    internal lateinit var fragment_make_add_info: FragmentMakeAddInfo
    internal lateinit var create_btn: Button

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_make_type_c, container, false)

        fragment_make_add_info = FragmentMakeAddInfo()

        create_btn = view.findViewById(R.id.frag3_btn_create) as Button

        create_btn.setOnClickListener {
            val parentView = view.parent.parent as View
            val edit_title = parentView.findViewById(R.id.make_edit_title) as EditText
            val edit_subtitle = parentView.findViewById(R.id.make_edit_sub) as EditText
            val edit_tag1 = parentView.findViewById(R.id.make_edit_tag1) as EditText
            val edit_tag2 = parentView.findViewById(R.id.make_edit_tag2) as EditText
            val edit_tag3 = parentView.findViewById(R.id.make_edit_tag3) as EditText

            val title = edit_title.text.toString()
            val subtitle = edit_subtitle.text.toString()
            val tag1 = edit_tag1.text.toString()
            val tag2 = edit_tag2.text.toString()
            val tag3 = edit_tag3.text.toString()

            if (title.length == 0) {
                Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val args = Bundle()
                args.putString("type", "C")
                args.putString("title", title)
                if (subtitle.length == 0) {
                    args.putString("subtitle", "")
                } else {
                    args.putString("subtitle", subtitle)
                }
                if (tag1.length == 0) {
                    args.putString("tag1", "")
                } else {
                    args.putString("tag1", tag1)
                }
                if (tag2.length == 0) {
                    args.putString("tag2", "")
                } else {
                    args.putString("tag2", tag2)
                }
                if (tag3.length == 0) {
                    args.putString("tag3", "")
                } else {
                    args.putString("tag3", tag3)
                }
                fragment_make_add_info.arguments = args

                val fm = activity.supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.add(R.id.make_container, fragment_make_add_info)
                transaction.commit()
            }
        }

        return view
    }
}
