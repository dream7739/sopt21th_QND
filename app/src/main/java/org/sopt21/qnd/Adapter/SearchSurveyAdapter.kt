package org.sopt21.qnd.Adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soyeon.qnd_search2.data.SearchSurveyResult
import org.sopt21.qnd.R
import org.sopt21.qnd.ViewHolder.SearchSurveyViewHolder

class SearchSurveyAdapter(var dataList : ArrayList<SearchSurveyResult.SearchSurveyItemResult>?) : RecyclerView.Adapter<SearchSurveyViewHolder>() {

    private var onItemClick: View.OnClickListener? = null //item클릭 시 event

    val TAG: String = "LOG::SurveyAdapter"

    override fun onBindViewHolder(holder: SearchSurveyViewHolder?, position: Int) { //viewholder 커스텀
        var gender: String = ""
        var start_age: String = ""
        var end_age: String = ""

        var tag1:String= dataList!!.get(position).servay_tag1
        var tag2:String=dataList!!.get(position).servay_tag2
        var tag3:String=dataList!!.get(position).servay_tag3

        var current = dataList!!.get(position)

        var alpha: Drawable

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
            holder!!.searchSubTitle.text = "for All"
        } else {
            holder!!.searchSubTitle.text = "for " +
                    start_age +
                    end_age +
                    gender
        }

        //내가 설문을 선택했었으면
        if (dataList!!.get(position).servay_selected == 1) {
            holder!!.searchPinset.setBackgroundResource(R.drawable.pinimage2) //핀셋 말고 회색 이미지
        }



        holder!!.search_text1.text = tag1
        holder!!.search_text2.text = tag2
        holder!!.search_text3.text = tag3


        //TODO 해시태그 고쳐야함
        holder!!.search_text1.visibility=View.VISIBLE
        holder!!.search_text2.visibility=View.VISIBLE
        holder!!.search_text3.visibility=View.VISIBLE


        if (tag1 == "") {
            holder!!.search_text1.visibility = View.GONE

        }
        if (tag2 == "") {
            holder!!.search_text2.visibility = View.GONE

        }
        if (tag3 == "") {
            holder!!.search_text3.visibility = View.GONE
        }



        if (dataList!![position].servay_liked == 1) {
            holder!!.searchLikeImage.setBackgroundResource(R.drawable.heart_fill)
        } else {
            holder!!.searchLikeImage.setBackgroundResource(R.drawable.heart)

        }

        if (dataList!![position].servay_liked == 1) {
            holder!!.searchLikeImage.setImageResource(R.drawable.heart_fill)
        } else {
            holder!!.searchLikeImage.setImageResource(R.drawable.heart)

        }

        //selected == 0 설문 미참여
        //selected == 1이면 설문참여

        if(dataList!!.get(position).servay_selected == 1){
            holder!!.searchPinset.setImageResource(R.drawable.pinimage2)
        }
        else{
            holder!!.searchPinset.setImageResource(R.drawable.pinimage1)
        }

        //done == 0 //설문 진행
        //done == 1 설문종료

        var color = "#B9B9B9"
        if(dataList!!.get(position).servay_done == 0){
            holder!!.searchTitle.setTextColor(Color.parseColor(color))
            holder!!.searchSubTitle.setTextColor(Color.parseColor(color))
            holder!!.search_text1.setTextColor(Color.parseColor(color))
            holder!!.search_text2.setTextColor(Color.parseColor(color))
            holder!!.search_text3.setTextColor(Color.parseColor(color))
            holder!!.searchLikeCount.setTextColor(Color.parseColor(color))
            holder!!.searchVote1.setTextColor(Color.parseColor(color))
            holder!!.searchVote2.setTextColor(Color.parseColor(color))
            holder!!.searchCoin.setTextColor(Color.parseColor(color))
            holder!!.searchLikeImage.setImageResource(R.drawable.heart_fill_black)
            holder!!.searchCoinImage.setImageResource(R.drawable.coin_black)
        }

        holder!!.searchImg.setBackgroundResource(R.drawable.pinimage_caracter)
        holder!!.searchTitle.text = dataList!!.get(position).servay_title
        holder!!.searchLikeCount.text = dataList!!.get(position).servay_like_count.toString() //좋아요 수
        holder!!.searchVote1.text = dataList!!.get(position).servay_man.toString()
        holder!!.searchVote2.text = dataList!!.get(position).servay_goal.toString()
        holder!!.searchCoin.text = dataList!!.get(position).servay_get.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchSurveyViewHolder {
        val mainView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.search_board_item, parent, false)
        mainView.setOnClickListener(onItemClick)
        return SearchSurveyViewHolder(mainView)
    }


    override fun getItemCount(): Int = dataList!!.size

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }
}