package org.sopt21.qnd.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.sopt21.qnd.R

/**
 * Created by soyeon on 2018. 1. 8..
 */
class SearchSurveyViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
    var searchImg : ImageView =  itemView!!.findViewById(R.id.search_img) as ImageView

    var searchTitle : TextView =  itemView!!.findViewById(R.id.search_title) as TextView
    var searchSubTitle : TextView =  itemView!!.findViewById(R.id.search_subTitle) as TextView

    var search_text1: TextView =itemView!!.findViewById(R.id.search_tag1_text) as TextView
    var search_text2: TextView =itemView!!.findViewById(R.id.search_tag2_text) as TextView
    var search_text3: TextView =itemView!!.findViewById(R.id.search_tag3_text) as TextView

    var searchPinset: ImageView =itemView!!.findViewById(R.id.search_pinset) as ImageView
    var searchLikeImage: ImageView =itemView!!.findViewById(R.id.search_like_image) as ImageView
    var searchLikeCount : TextView = itemView!!.findViewById(R.id.search_like_count) as TextView

    var searchVote1 : TextView = itemView!!.findViewById(R.id.search_vote_num1) as TextView //현재까지 참여 인원
    var searchVote2: TextView =itemView!!.findViewById(R.id.search_vote_num2) as TextView  //제한 인원

    var searchCoin : TextView =  itemView!!.findViewById(R.id.search_coin_plus) as TextView
    var searchCoinImage : ImageView = itemView!!.findViewById(R.id.search_coin) as ImageView
}