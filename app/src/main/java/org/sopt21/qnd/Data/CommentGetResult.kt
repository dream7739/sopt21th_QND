package org.sopt21.qnd.Data

class CommentGetResult {
    var status:String?=""
    var msg:String?=""
    var data: ArrayList<CommentItemGetResult> = ArrayList<CommentItemGetResult>()

    inner class CommentItemGetResult{
        var comment_content:String=""
        var user_email:String=""
        var user_img:String=""
    }

}