package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.sopt21.qnd.AdapterSpinner
import org.sopt21.qnd.R
import java.util.*

class FragmentMake : Fragment() {
    internal lateinit var typeSpinner: Spinner
    internal lateinit var adapterSpinner: AdapterSpinner

    internal lateinit var edit_title: EditText
    internal lateinit var edit_sub_title: EditText
    internal lateinit var edit_tag1: EditText
    internal lateinit var edit_tag2: EditText
    internal lateinit var edit_tag3: EditText
    internal var title: String? = null
    internal lateinit var sub_title: String
    internal lateinit var tag1: String
    internal lateinit var tag2: String
    internal lateinit var tag3: String
    internal lateinit var tag_cancle_btn1: ImageButton
    internal lateinit var tag_cancle_btn2: ImageButton
    internal lateinit var tag_cancle_btn3: ImageButton
    internal lateinit var layout_tag1: LinearLayout
    internal lateinit var layout_tag2: LinearLayout
    internal lateinit var layout_tag3: LinearLayout

    internal lateinit var fragment_make_empty: FragmentMakeEmpty
    internal lateinit var fragment_make_type_a: FragmentMakeTypeA
    internal lateinit var fragment_make_type_b: FragmentMakeTypeB
    internal lateinit var fragment_make_type_c: FragmentMakeTypeC

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_make, container, false)

        fragment_make_empty = childFragmentManager.findFragmentById(R.id.make_frag) as FragmentMakeEmpty
        fragment_make_type_a = FragmentMakeTypeA()
        fragment_make_type_b = FragmentMakeTypeB()
        fragment_make_type_c = FragmentMakeTypeC()

        edit_title = view.findViewById(R.id.make_edit_title) as EditText
        edit_sub_title = view.findViewById(R.id.make_edit_sub) as EditText
        edit_tag1 = view.findViewById(R.id.make_edit_tag1) as EditText
        edit_tag2 = view.findViewById(R.id.make_edit_tag2) as EditText
        edit_tag3 = view.findViewById(R.id.make_edit_tag3) as EditText

        tag_cancle_btn1 = view.findViewById(R.id.make_btn_tag_cancel1) as ImageButton
        tag_cancle_btn2 = view.findViewById(R.id.make_btn_tag_cancel2) as ImageButton
        tag_cancle_btn3 = view.findViewById(R.id.make_btn_tag_cancel3) as ImageButton

        layout_tag1 = view.findViewById(R.id.make_layout_tagbox1) as LinearLayout
        layout_tag2 = view.findViewById(R.id.make_layout_tagbox2) as LinearLayout
        layout_tag3 = view.findViewById(R.id.make_layout_tagbox3) as LinearLayout

        typeSpinner = view.findViewById(R.id.make_spin_type) as Spinner

        val type = ArrayList<String>()
        type.add("선택")
        type.add("A/B")
        type.add("객관식")
        type.add("찬/반")

        edit_title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                title = edit_title.text.toString()


                if (title!!.length == 15) {
                    Toast.makeText(activity, "제목은 15자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_sub_title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                sub_title = edit_sub_title.text.toString()


                if (sub_title.length == 20) {
                    Toast.makeText(activity, "부연설명은 20자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_tag1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tag1 = edit_tag1.text.toString()

                if (tag1.length != 0) {
                    layout_tag1.setBackgroundResource(R.drawable.make_tagbox1)
                    tag_cancle_btn1.visibility = View.VISIBLE
                    if (tag1.length == 5) {
                        Toast.makeText(activity, "해시태그는 5자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                tag1 = edit_tag1.text.toString()

                if (tag1.length == 0) {
                    layout_tag1.setBackgroundResource(R.drawable.make_tagbox2)
                    tag_cancle_btn1.visibility = View.GONE
                }
            }
        })

        edit_tag2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tag2 = edit_tag2.text.toString()

                if (tag2.length != 0) {
                    layout_tag2.setBackgroundResource(R.drawable.make_tagbox1)
                    tag_cancle_btn2.visibility = View.VISIBLE
                    if (tag2.length == 5) {
                        Toast.makeText(activity, "해시태그는 5자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                tag2 = edit_tag2.text.toString()

                if (tag2.length == 0) {
                    layout_tag2.setBackgroundResource(R.drawable.make_tagbox2)
                    tag_cancle_btn2.visibility = View.GONE
                }
            }
        })

        edit_tag3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tag3 = edit_tag3.text.toString()

                if (tag3.length != 0) {
                    layout_tag3.setBackgroundResource(R.drawable.make_tagbox1)
                    tag_cancle_btn3.visibility = View.VISIBLE
                    if (tag3.length == 5) {
                        Toast.makeText(activity, "해시태그는 5자까지 입력이 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                tag3 = edit_tag3.text.toString()

                if (tag3.length == 0) {
                    layout_tag3.setBackgroundResource(R.drawable.make_tagbox2)
                    tag_cancle_btn3.visibility = View.GONE
                }
            }
        })

        tag_cancle_btn1.setOnClickListener {
            edit_tag1.setText("")
            layout_tag1.setBackgroundResource(R.drawable.make_tagbox2)
            tag_cancle_btn1.visibility = View.GONE
        }

        tag_cancle_btn2.setOnClickListener {
            edit_tag2.setText("")
            layout_tag2.setBackgroundResource(R.drawable.make_tagbox2)
            tag_cancle_btn2.visibility = View.GONE
        }

        tag_cancle_btn3.setOnClickListener {
            edit_tag3.setText("")
            layout_tag3.setBackgroundResource(R.drawable.make_tagbox2)
            tag_cancle_btn3.visibility = View.GONE
        }

        adapterSpinner = AdapterSpinner(activity, type)
        typeSpinner.adapter = adapterSpinner

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    childFragmentManager.beginTransaction().remove(fragment_make_type_a).show(fragment_make_empty).commit()
                    childFragmentManager.beginTransaction().remove(fragment_make_type_b).show(fragment_make_empty).commit()
                    childFragmentManager.beginTransaction().remove(fragment_make_type_c).show(fragment_make_empty).commit()
                } else if (position == 1) {
                    childFragmentManager.beginTransaction().replace(R.id.make_sub_container, fragment_make_type_a).commit()
                } else if (position == 2) {
                    childFragmentManager.beginTransaction().replace(R.id.make_sub_container, fragment_make_type_b).commit()
                } else if (position == 3) {
                    childFragmentManager.beginTransaction().replace(R.id.make_sub_container, fragment_make_type_c).commit()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        return view
    }
}
