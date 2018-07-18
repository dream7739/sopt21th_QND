package org.sopt21.qnd.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.sopt21.qnd.R

class MyReportRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv_report_title: TextView
    var tv_report_count: TextView
    var tv_survey_write_time: TextView

    init {
        tv_report_title = itemView.findViewById(R.id.tv_report_title) as TextView
        tv_report_count = itemView.findViewById(R.id.tv_report_count) as TextView
        tv_survey_write_time = itemView.findViewById(R.id.tv_survey_write_time) as TextView
    }
}
