package org.sopt21.qnd.Adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import org.sopt21.qnd.Data.SurveyResult
import org.sopt21.qnd.R
import org.sopt21.qnd.ViewHolder.SurveyViewHolder

/**
 * Created by soyeon on 2018. 1. 1..
 */
class SurveyAdapter(var dataList : ArrayList<SurveyResult.SurveyItemResult>?) : RecyclerView.Adapter<SurveyViewHolder>() {

    private var onItemClick: View.OnClickListener? = null //item클릭 시 event

    val TAG : String = "LOG::SurveyAdapter"


    override fun onBindViewHolder(holder: SurveyViewHolder?, position: Int) { //viewholder 커스텀
        var gender: String = ""
        var start_age: String = ""
        var end_age: String = ""

        var tag1:String= dataList!!.get(position).servay_tag1
        var tag2:String=dataList!!.get(position).servay_tag2
        var tag3:String=dataList!!.get(position).servay_tag3

        var current = dataList!!.get(position)


        //여자 남자
        if (dataList!!.get(position).servay_gender == 1) {
            gender = " 여자 "
        } else if (dataList!!.get(position).servay_gender == -1) {
            gender = " 남자 "
        } else if (dataList!!.get(position).servay_gender == 0) {
            gender = ""
        }


        //나이
        if (current.servay_start_age == 0) {
            start_age = ""
        } else {
            start_age = " " + current.servay_start_age.toString() + "세~"
        }

        if (current.servay_end_age == 0) {
            end_age = ""
        } else {
            end_age = "" + current.servay_end_age.toString() + "세 "
        }


        //나이&성별 조건 없을 시
        if (start_age == "" && end_age == "" && gender == "") {
            holder!!.sursubTitle.text = "for All"
        } else {
            holder!!.sursubTitle.text = "for " +
                    start_age +
                    end_age +
                    gender
        }

        //내가 설문을 선택했었으면
        if (dataList!!.get(position).servay_selected == 1) {
            holder!!.surPinset.setBackgroundResource(R.drawable.pinimage2) //핀셋 말고 회색 이미지
        }



        holder!!.sur_text1.text = tag1
        holder!!.sur_text2.text = tag2
        holder!!.sur_text3.text = tag3


        //TODO 해시태그 고쳐야함
        holder!!.sur_text1.visibility=View.VISIBLE
        holder!!.sur_text2.visibility=View.VISIBLE
        holder!!.sur_text3.visibility=View.VISIBLE


        if (tag1 == "") {
            holder!!.sur_text1.visibility = View.GONE

        }
        if (tag2 == "") {
            holder!!.sur_text2.visibility = View.GONE

        }
        if (tag3 == "") {
            holder!!.sur_text3.visibility = View.GONE
        }



        if (dataList!![position].servay_liked == 1) {
            holder!!.surLikeImage.setBackgroundResource(R.drawable.heart_fill)
        } else {
            holder!!.surLikeImage.setBackgroundResource(R.drawable.heart)

        }


        //TODO "~~.PNG"는 사진이 뜨는데 나머지는 사진이 안뜸



        holder!!.surImg.setBackgroundResource(R.drawable.pinimage_caracter)
        holder!!.surTitle.text = dataList!!.get(position).servay_title
        holder!!.surLikeCount.text = dataList!!.get(position).servay_like_count.toString() //좋아요 수
        holder!!.surVote1.text = dataList!!.get(position).servay_man.toString()
        holder!!.surVote2.text = dataList!!.get(position).servay_goal.toString()
        holder!!.surCoin.text = dataList!!.get(position).servay_get.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SurveyViewHolder {
        val mainView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.survey_board_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return SurveyViewHolder(mainView)
    }

    override fun getItemCount(): Int = dataList!!.size

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }
}