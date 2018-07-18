package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.sopt21.qnd.R

class FragmentMakeTypeB : Fragment() {
    internal var check: Boolean? = false

    internal lateinit var create_btn: Button
    internal lateinit var add_btn: Button
    internal lateinit var del_btn1: ImageButton
    internal lateinit var del_btn2: ImageButton
    internal lateinit var check_btn: ImageButton
    internal lateinit var edit_answer1: EditText
    internal lateinit var edit_answer2: EditText
    internal lateinit var edit_answer3: EditText
    internal lateinit var edit_answer4: EditText
    internal lateinit var layout_answer3: LinearLayout
    internal lateinit var layout_answer4: LinearLayout

    internal lateinit var fragment_make_add_info: FragmentMakeAddInfo

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_make_type_b, container, false)

        fragment_make_add_info = FragmentMakeAddInfo()

        create_btn = view.findViewById(R.id.frag2_btn_create) as Button
        add_btn = view.findViewById(R.id.frag2_btn_add) as Button
        del_btn1 = view.findViewById(R.id.frag2_btn_del1) as ImageButton
        del_btn2 = view.findViewById(R.id.frag2_btn_del2) as ImageButton
        check_btn = view.findViewById(R.id.frag2_btn_check) as ImageButton

        edit_answer1 = view.findViewById(R.id.frag2_edit_answer1) as EditText
        edit_answer2 = view.findViewById(R.id.frag2_edit_answer2) as EditText
        edit_answer3 = view.findViewById(R.id.frag2_edit_answer3) as EditText
        edit_answer4 = view.findViewById(R.id.frag2_edit_answer4) as EditText

        layout_answer3 = view.findViewById(R.id.frag2_layout_answer3) as LinearLayout
        layout_answer4 = view.findViewById(R.id.frag2_layout_answer4) as LinearLayout

        add_btn.setOnClickListener {
            if (layout_answer3.visibility == View.GONE) {
                layout_answer3.visibility = View.VISIBLE
            } else if (layout_answer3.visibility == View.VISIBLE && layout_answer4.visibility == View.GONE) {
                layout_answer4.visibility = View.VISIBLE
            }
        }

        del_btn1.setOnClickListener {
            if (layout_answer4.visibility == View.VISIBLE) {
                layout_answer4.visibility = View.GONE
                edit_answer3.setText("")
            } else {
                layout_answer3.visibility = View.GONE
            }
        }

        del_btn2.setOnClickListener { layout_answer4.visibility = View.GONE }

        check_btn.setOnClickListener {
            if (!check!!) {
                check_btn.setImageResource(R.drawable.make_checkbox_on)
                check = true
            } else {
                check_btn.setImageResource(R.drawable.make_checkbox_off)
                check = false
            }
        }

        edit_answer1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val answer1 = edit_answer1.text.toString()

                if (answer1.length == 8) {
                    Toast.makeText(activity, "객관식 항목은 8자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_answer2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val answer2 = edit_answer2.text.toString()

                if (answer2.length == 8) {
                    Toast.makeText(activity, "객관식 항목은 8자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_answer3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val answer3 = edit_answer3.text.toString()

                if (answer3.length == 8) {
                    Toast.makeText(activity, "객관식 항목은 8자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_answer4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val answer4 = edit_answer4.text.toString()

                if (answer4.length == 8) {
                    Toast.makeText(activity, "객관식 항목은 8자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        create_btn.setOnClickListener(View.OnClickListener {
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
            val answer1 = edit_answer1.text.toString()
            val answer2 = edit_answer2.text.toString()
            val answer3 = edit_answer3.text.toString()
            val answer4 = edit_answer4.text.toString()

            if (title.length == 0) {
                Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (answer1.length == 0 || answer2.length == 0) {
                    Toast.makeText(context, "객관식 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    if (answer1.length == 0) {
                        edit_answer1.requestFocus()
                    } else if (answer2.length == 0) {
                        edit_answer2.requestFocus()
                    }
                } else {
                    if (layout_answer3.visibility == View.VISIBLE && answer3.length == 0) {
                        Toast.makeText(context, "객관식 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        edit_answer3.requestFocus()
//                        return@OnClickListener
                    }
                    if (layout_answer4.visibility == View.VISIBLE && answer4.length == 0) {
                        Toast.makeText(context, "객관식 항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        edit_answer4.requestFocus()
//                        return@OnClickListener
                    }

                    val args = Bundle()
                    args.putString("type", "B")
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

                    if (check!!) {
                        args.putString("multiselect", "1")
                    } else {
                        args.putString("multiselect", "0")
                    }

                    args.putString("answer1", answer1)
                    args.putString("answer2", answer2)
                    if (answer3.length == 0) {
                        args.putString("answer3", "")
                    } else {
                        args.putString("answer3", answer3)
                    }
                    if (answer4.length == 0) {
                        args.putString("answer4", "")
                    } else {
                        args.putString("answer4", answer4)
                    }

                    fragment_make_add_info.arguments = args

                    val fm = activity.supportFragmentManager
                    val transaction = fm.beginTransaction()
                    transaction.add(R.id.make_container, fragment_make_add_info)
                    transaction.commit()
                }
            }
        })

        return view
    }
}
