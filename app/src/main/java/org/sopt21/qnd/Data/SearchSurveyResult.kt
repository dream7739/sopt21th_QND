package com.soyeon.qnd_search2.data

/**
 * Created by soyeon on 2018. 1. 7..
 */

class SearchSurveyResult {
    var status: String? = null
    var data :ArrayList<SearchSurveyItemResult> = ArrayList<SearchSurveyItemResult>()

    inner class SearchSurveyItemResult {
        var servay_type = ""
        var servay_id: Int = 0
        var servay_title: String? = ""
        var servay_goal: Int = 0
        var servay_anonymous: Int = 0
        var servay_start_age: Int = 0
        var servay_end_age: Int = 0
        var servay_tag1: String = ""
        var servay_tag2: String = ""
        var servay_tag3: String =""
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
