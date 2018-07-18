package org.sopt21.qnd.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import org.sopt21.qnd.R

/**
 * Created by soyeon on 2018. 1. 1..
 */
class SurveyViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView){
    var surImg : ImageView  =  itemView!!.findViewById(R.id.sur_img) as ImageView

    var surTitle : TextView =  itemView!!.findViewById(R.id.sur_title) as TextView
    var sursubTitle : TextView  =  itemView!!.findViewById(R.id.sur_subTitle) as TextView

    var sur_text1:TextView=itemView!!.findViewById(R.id.tag1_text) as TextView
    var sur_text2:TextView=itemView!!.findViewById(R.id.tag2_text) as TextView
    var sur_text3:TextView=itemView!!.findViewById(R.id.tag3_text) as TextView

    var surPinset:ImageView=itemView!!.findViewById(R.id.sur_pinset) as ImageView

    var surLikeImage:ImageView=itemView!!.findViewById(R.id.sur_like_image) as ImageView
    var surLikeCount : TextView = itemView!!.findViewById(R.id.sur_like_count) as TextView

    var surVote1 : TextView  = itemView!!.findViewById(R.id.vote_num1) as TextView //현재까지 참여 인원
    var surVote2: TextView=itemView!!.findViewById(R.id.vote_num2) as TextView  //제한 인원


    var surCoin : TextView  =  itemView!!.findViewById(R.id.sur_coin_plus) as TextView

}