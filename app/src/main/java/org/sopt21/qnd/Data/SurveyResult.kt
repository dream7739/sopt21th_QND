package org.sopt21.qnd.Data

class SurveyResult {
    var status: String? = null
    var data: ArrayList<SurveyItemResult> = ArrayList<SurveyItemResult>()

    inner class SurveyItemResult {
        var servay_id: Int = 0
        var servay_title: String = ""
        var servay_type: Int = 0
        var servay_goal: Int = 0
        var servay_anonymous: Int = 0
        var servay_start_age: Int = 0
        var servay_end_age: Int = 0
        var servay_tag1: String = ""
        var servay_tag2: String = ""
        var servay_tag3: String = ""
        var servay_like_count: Int = 0
        var servay_gender: Int = 0
        var servay_marriage: Int = 0
        var servay_done: Int = 0
        var servay_get: Int = 0
        var servay_selected: Int = 0
        var servay_liked: Int = 0
        var servay_man: Int = 0
        var servay_title_img: String = ""
    }
}