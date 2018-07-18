package org.sopt21.qnd.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import org.sopt21.qnd.R

class MySurveyRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tv_survey_name: TextView
    var rl_survey: RelativeLayout
    var tv_survey_write_time: TextView

    init {
        rl_survey = itemView.findViewById(R.id.rl_mysurvey) as RelativeLayout
        tv_survey_name = itemView.findViewById(R.id.survey_name) as TextView
        tv_survey_write_time = itemView.findViewById(R.id.tv_survey_write_time) as TextView
    }
}
