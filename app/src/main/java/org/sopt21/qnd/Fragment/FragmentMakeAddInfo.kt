package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.sopt21.qnd.AdapterSpinner
import org.sopt21.qnd.Dialog.DialogMake
import org.sopt21.qnd.R
import java.util.*

class FragmentMakeAddInfo : Fragment() {
    internal var personnel: Int = 0
    internal var validity: Int = 0
    internal var realType: Int = 0
    internal var noname_check = 0
    internal var age_check = 0
    internal var gender_check = 0
    internal var gender_choice = 0
    internal var age_start = 0
    internal var age_end = 0
    internal var option_count = 0
    internal var tag_count = 0
    internal var q_count = 4

    internal lateinit var edit_personnel: EditText
    internal lateinit var edit_validity: EditText
    internal lateinit var noname: TextView

    internal var type: String? = null
    internal var title: String? = null
    internal var subtitle: String? = null
    internal var tag1: String? = null
    internal var tag2: String? = null
    internal var tag3: String? = null
    internal var multiselect: String? = null
    internal var answer1: String? = null
    internal var answer2: String? = null
    internal var answer3: String? = null
    internal var answer4: String? = null

    internal lateinit var ageStartSpinner: Spinner
    internal lateinit var ageEndSpinner: Spinner
    internal lateinit var genderSpinner: Spinner
    internal lateinit var adapterSpinner: AdapterSpinner
    internal lateinit var fragment_make_add_info: FragmentMakeAddInfo
    internal lateinit var noname_btn: ImageButton
    internal lateinit var age_btn: ImageButton
    internal lateinit var gender_btn: ImageButton
    internal lateinit var btn: Button
    internal lateinit var back_btn: RelativeLayout

    internal lateinit var dialogMake: DialogMake

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_make_add_info, container, false)

        dialogMake = DialogMake()

        btn = view.findViewById(R.id.frag_btn_complete) as Button
        back_btn = view.findViewById(R.id.make_btn_back) as RelativeLayout
        noname_btn = view.findViewById(R.id.frag_btn_noname) as ImageButton
        age_btn = view.findViewById(R.id.frag_btn_age) as ImageButton
        gender_btn = view.findViewById(R.id.frag_btn_gender) as ImageButton

        edit_personnel = view.findViewById(R.id.frag_edit_personnel) as EditText
        edit_validity = view.findViewById(R.id.frag_edit_validity) as EditText
        noname = view.findViewById(R.id.text_noname) as TextView

        ageStartSpinner = view.findViewById(R.id.frag_spin_age1) as Spinner
        ageEndSpinner = view.findViewById(R.id.frag_spin_age2) as Spinner
        genderSpinner = view.findViewById(R.id.frag_spin_gender) as Spinner

        fragment_make_add_info = FragmentMakeAddInfo()

        val age1 = ArrayList<String>()
        age1.add("10대")
        age1.add("20대")
        age1.add("30대")
        age1.add("40대")
        age1.add("50대")
        age1.add("60대")
        age1.add("70대")

        val age2 = ArrayList<String>()

        val gender = ArrayList<String>()
        gender.add("남자")
        gender.add("여자")

        adapterSpinner = AdapterSpinner(activity, age1)
        ageStartSpinner.adapter = adapterSpinner

        adapterSpinner = AdapterSpinner(activity, gender)
        genderSpinner.adapter = adapterSpinner

        if (arguments != null) {
            type = arguments.getString("type")
            title = arguments.getString("title")
            subtitle = arguments.getString("subtitle")
            tag1 = arguments.getString("tag1")
            tag2 = arguments.getString("tag2")
            tag3 = arguments.getString("tag3")

            if ("A" == arguments.getString("type")) { // A/B
                answer1 = arguments.getString("answer1")
                answer2 = arguments.getString("answer2")
                realType = 1
            } else if ("B" == arguments.getString("type")) { // 객관식
                multiselect = arguments.getString("multiselect")
                answer1 = arguments.getString("answer1")
                answer2 = arguments.getString("answer2")
                answer3 = arguments.getString("answer3")
                answer4 = arguments.getString("answer4")
                realType = 0
            } else { // 찬반
                realType = 2
            }
        }

        noname_btn.setOnClickListener {
            if (noname_check == 0) {
                noname_btn.setImageResource(R.drawable.make_checkbox_on)
                noname.visibility = View.VISIBLE
                noname_check = 1
            } else {
                noname_btn.setImageResource(R.drawable.make_checkbox_off)
                noname.visibility = View.INVISIBLE
                noname_check = 0
            }
        }

        age_btn.setOnClickListener {
            if (age_check == 0) {
                age_btn.setImageResource(R.drawable.make_checkbox_on)
                age_check = 1
                option_count++
            } else {
                age_btn.setImageResource(R.drawable.make_checkbox_off)
                age_check = 0
                option_count--
            }
        }

        gender_btn.setOnClickListener {
            if (gender_check == 0) {
                gender_btn.setImageResource(R.drawable.make_checkbox_on)
                gender_check = 1
                option_count++
            } else {
                gender_btn.setImageResource(R.drawable.make_checkbox_off)
                gender_check = 0
                option_count--
            }
        }

        ageStartSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    age_start = 10
                    age2.clear()
                    age2.add("10대")
                    age2.add("20대")
                    age2.add("30대")
                    age2.add("40대")
                    age2.add("50대")
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 1) {
                    age_start = 20
                    age2.clear()
                    age2.add("20대")
                    age2.add("30대")
                    age2.add("40대")
                    age2.add("50대")
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 2) {
                    age_start = 30
                    age2.clear()
                    age2.add("30대")
                    age2.add("40대")
                    age2.add("50대")
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 3) {
                    age_start = 40
                    age2.clear()
                    age2.add("40대")
                    age2.add("50대")
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 4) {
                    age_start = 50
                    age2.clear()
                    age2.add("50대")
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 5) {
                    age_start = 60
                    age2.clear()
                    age2.add("60대")
                    age2.add("70대")
                } else if (position == 6) {
                    age_start = 70
                    age2.clear()
                    age2.add("70대")
                }
                adapterSpinner = AdapterSpinner(activity, age2)
                ageEndSpinner.adapter = adapterSpinner
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        ageEndSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    when (age2[0]) {
                        "10대" -> age_end = 19
                        "20대" -> age_end = 29
                        "30대" -> age_end = 39
                        "40대" -> age_end = 49
                        "50대" -> age_end = 59
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 1) {
                    when (age2[1]) {
                        "20대" -> age_end = 29
                        "30대" -> age_end = 39
                        "40대" -> age_end = 49
                        "50대" -> age_end = 59
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 2) {
                    when (age2[2]) {
                        "30대" -> age_end = 39
                        "40대" -> age_end = 49
                        "50대" -> age_end = 59
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 3) {
                    when (age2[3]) {
                        "40대" -> age_end = 49
                        "50대" -> age_end = 59
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 4) {
                    when (age2[4]) {
                        "50대" -> age_end = 59
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 5) {
                    when (age2[5]) {
                        "60대" -> age_end = 69
                        "70대" -> age_end = 79
                    }
                } else if (position == 6) {
                    when (age2[6]) {
                        "70대" -> age_end = 79
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position == 0) {
                    gender_choice = -1
                } else if (position == 1) {
                    gender_choice = 1
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btn.setOnClickListener {
            Log.d("AAA", "age_start: " + age_start)
            Log.d("AAA", "age_end: " + age_end)
            Log.d("AAA", "age_check: " + age_check)
            if (edit_personnel.text.toString().isEmpty()) {
                Toast.makeText(context, "인원수를 입력하세요.", Toast.LENGTH_SHORT).show()
                edit_personnel.requestFocus()
            } else {
                personnel = Integer.parseInt(edit_personnel.text.toString())
                if (personnel > 200) {
                    Toast.makeText(context, "200명을 초과할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    edit_personnel.requestFocus()
                } else if (personnel == 0) {
                    Toast.makeText(context, "0명 이상의 인원을 작성해주세요.", Toast.LENGTH_SHORT).show()
                    edit_personnel.requestFocus()
                } else {
                    if (edit_validity.text.toString().isEmpty()) {
                        Toast.makeText(context, "유효기간을 입력하세요.", Toast.LENGTH_SHORT).show()
                        edit_validity.requestFocus()
                    } else {
                        validity = Integer.parseInt(edit_validity.text.toString())
                        val args = Bundle()
                        args.putInt("servay_servay_type", realType)
                        args.putString("servay_title", title)
                        args.putInt("servay_valid_period", validity)
                        args.putInt("servay_goal", personnel)
                        args.putInt("servay_anonymous", noname_check)
                        if (age_check == 1) {
                            args.putInt("servay_start_age", age_start)
                            args.putInt("servay_end_age", age_end)
                        } else {
                            args.putInt("servay_start_age", 0)
                            args.putInt("servay_end_age", 0)
                        }
                        if (tag1!!.length != 0) {
                            args.putString("servay_tag1", "#" + tag1!!)
                            tag_count++
                        } else {
                            args.putString("servay_tag1", tag1)
                        }
                        if (tag2!!.length != 0) {
                            args.putString("servay_tag2", "#" + tag2!!)
                            tag_count++
                        } else {
                            args.putString("servay_tag2", tag2)
                        }
                        if (tag3!!.length != 0) {
                            args.putString("servay_tag3", "#" + tag3!!)
                            tag_count++
                        } else {
                            args.putString("servay_tag3", tag3)
                        }
                        args.putString("servay_explanation", subtitle)
                        if (gender_check == 1) {
                            args.putInt("servay_gender", gender_choice)
                        } else {
                            args.putInt("servay_gender", 0)
                        }
                        args.putInt("servay_marriage", 0)

                        args.putInt("servay_option_count", option_count)
                        args.putInt("servay_tag_count", tag_count)

                        if (realType == 0) { // 객관식일 때
                            args.putString("servay_q1", answer1)
                            args.putString("servay_q2", answer2)

                            if ("" == answer3) {
                                answer3 = null
                                q_count--
                            }
                            if ("" == answer4) {
                                answer4 = null
                                q_count--
                            }

                            args.putString("servay_q3", answer3)
                            args.putString("servay_q4", answer4)

                            args.putInt("servay_q_count", q_count)
                            args.putInt("servay_duple", Integer.parseInt(multiselect))
                        } else if (realType == 1) { // A/B일 때
                            args.putString("servay_a_txt", answer1)
                            args.putString("servay_b_txt", answer2)


                        }

                        dialogMake.arguments = args
                        val fm = childFragmentManager
                        dialogMake.show(fm, "makeComplete")
                    }
                }
            }
        }

        back_btn.setOnClickListener {
            val fm = activity.supportFragmentManager
            val transaction = fm.beginTransaction()
            transaction.remove(this)
            transaction.commit()
        }

        return view
    }
}
