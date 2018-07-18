package org.sopt21.qnd.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt21.qnd.Data.CommentGetResult
import org.sopt21.qnd.R
import org.sopt21.qnd.ViewHolder.CommentViewHolder

class CommentAdapter(var commentList: ArrayList<CommentGetResult.CommentItemGetResult>?) : RecyclerView.Adapter<CommentViewHolder>(){
    override fun onBindViewHolder(holder: CommentViewHolder?, position: Int) {

        var email:String=commentList!!.get(position).user_email
        var split=email.split("@")

        var preEmail:String=split[0].substring(0,4)
        var temp : String = ""
        var tempLength : Int = split[0].length - 4
        for (i in 1..tempLength) {
            temp = temp + "*"
        }
        var anonymousEmail = preEmail + temp + "@" + split[1]


        holder!!.comName.text = anonymousEmail
        holder.comTv.text = commentList!!.get(position).comment_content
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentViewHolder {
        var mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.comment_item, parent, false )
        return CommentViewHolder(mainView)

    }

    override fun getItemCount(): Int = commentList!!.size
}