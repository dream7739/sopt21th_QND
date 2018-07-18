package org.sopt21.qnd.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import org.sopt21.qnd.R

class FragmentFilter : Fragment() {
    internal lateinit var filterBack: ImageButton
    internal lateinit var rg1: RadioGroup
    internal lateinit var rg2: RadioGroup
    internal lateinit var radioAll: RadioButton
    internal lateinit var radioFinish: RadioButton
    internal lateinit var radioUnfinish: RadioButton
    //  Bundle bundle;
    internal var ing = 2
    internal var order = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, parentView: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_servey_filter, parentView, false)

        filterBack = view.findViewById(R.id.filter_back) as ImageButton
        rg1 = view.findViewById(R.id.radio_group) as RadioGroup
        rg2 = view.findViewById(R.id.radio_group2) as RadioGroup

        radioAll = view.findViewById(R.id.radio_all) as RadioButton
        radioFinish = view.findViewById(R.id.radio_finish) as RadioButton
        radioUnfinish = view.findViewById(R.id.radio_unfinish) as RadioButton

        rg1.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.radio_all -> {
                    Toast.makeText(activity, "전체", Toast.LENGTH_SHORT).show()
                    ing = 2
                }

                R.id.radio_finish -> {
                    Toast.makeText(activity, "완료", Toast.LENGTH_SHORT).show()
                    //   bundle.putInt("filter1", 0);
                    ing = 1 //설문진행
                }

                R.id.radio_unfinish -> {
                    Toast.makeText(activity, "미완료", Toast.LENGTH_SHORT).show()
                    //bundle.putInt("filter1", 1);
                    ing = 0 //설문 종료
                }
            }//  bundle.putInt("filter1", 2);
        }


        rg2.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.radio_recent -> {
                    Toast.makeText(activity, "최신순", Toast.LENGTH_SHORT).show()
                    order = 0 //최신순
                }
                R.id.radio_popular -> {
                    Toast.makeText(activity, "인기순", Toast.LENGTH_SHORT).show()
                    order = 1 //인기순
                }
            }// bundle.putInt("filter1", 0);
        }


        filterBack.setOnClickListener {
            val searchFragment = FragmentSearch()
            getFragmentManager().beginTransaction().replace(R.id.make_container,searchFragment, "filter").commit();
            val bundle = Bundle()
            bundle.putInt("ing", ing)
            bundle.putInt("order", order)
            searchFragment.arguments = bundle
        }
        return view
    }

}