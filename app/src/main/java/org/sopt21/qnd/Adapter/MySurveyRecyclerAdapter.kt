package org.sopt21.qnd.Adpater

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt21.qnd.Data.GetMySurveyListResult
import org.sopt21.qnd.R
import org.sopt21.qnd.ViewHolder.MySurveyRecyclerViewHolder
import java.util.*

class MySurveyRecyclerAdapter (private var mySurveyListDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>?, private val context: Context, private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<MySurveyRecyclerViewHolder>() {

    fun setAdapter(mySurveyListDatas: ArrayList<GetMySurveyListResult.GetResponseMySurveyList>) {
        this.mySurveyListDatas = mySurveyListDatas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySurveyRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mysurvey_recycerview_item, parent, false)
        val viewHolder = MySurveyRecyclerViewHolder(view)

        view.setOnClickListener(onClickListener)

        return viewHolder
    }

    override fun onBindViewHolder(holder: MySurveyRecyclerViewHolder, position: Int) {
        holder.tv_survey_name.text = mySurveyListDatas!![position].servay_title
        holder.tv_survey_write_time.text = mySurveyListDatas!![position].servay_write_time
    }

    override fun getItemCount(): Int {
        return if (mySurveyListDatas != null) mySurveyListDatas!!.size else 0
    }
}
