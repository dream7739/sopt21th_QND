package org.sopt21.qnd.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.sopt21.qnd.R

/**
 * Created by dream on 2018-01-03.
 */
class CommentViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
    var comName : TextView = itemView!!.findViewById(R.id.comment_name) as TextView
    var comTv:TextView=itemView!!.findViewById(R.id.comment_text) as TextView
}