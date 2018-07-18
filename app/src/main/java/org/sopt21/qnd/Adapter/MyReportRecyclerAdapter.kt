package org.sopt21.qnd.Adpater

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt21.qnd.Data.GetMySurveyListResult
import org.sopt21.qnd.R
import org.sopt21.qnd.ViewHolder.MyReportRecyclerViewHolder
import java.util.*

class MyReportRecyclerAdapter (private var mySurveyListDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>?, private val context: Context, private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<MyReportRecyclerViewHolder>() {

    fun setAdapter(mySurveyListDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>) {
        this.mySurveyListDatas = mySurveyListDatas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.myreport_recyclerview_item, parent, false)
        val viewHolder = MyReportRecyclerViewHolder(view)

        view.setOnClickListener(onClickListener)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyReportRecyclerViewHolder, position: Int) {
        holder.tv_report_title.text = mySurveyListDatas!![position].servay_title
        holder.tv_survey_write_time.text = mySurveyListDatas!![position].servay_write_time
        //TODO 신고횟수 바인딩시켜야돼! 서버에서 response 주는거 보고 수정해야돼!!
        //ArrayList 데이터를 바꿔야될 수도 있어!
    }

    override fun getItemCount(): Int {
        return if (mySurveyListDatas != null) mySurveyListDatas!!.size else 0
    }
}
