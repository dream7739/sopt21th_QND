package org.sopt21.qnd.Data

import java.io.File

class MakeSurveyData {
    var data = MakeSurveyRequestData()

    inner class MakeSurveyRequestData {
        var user_id: Int = 0
        var servay_servay_type: Int = 0
        var servay_title: String? = null
        var servay_valid_period: Int = 0
        var servay_goal: Int = 0
        var servay_anonymous: Int = 0
        var servay_start_age: Int = 0
        var servay_end_age: Int = 0
        var servay_tag1: String? = null
        var servay_tag2: String? = null
        var servay_tag3: String? = null
        var servay_explanation: String? = null
        var servay_gender: Int = 0
        var servay_marriage: Int = 0
        var servay_option_count: Int = 0
        var servay_tag_count: Int = 0

        var servay_q1: String? = null
        var servay_q2: String? = null
        var servay_q3: String? = null
        var servay_q4: String? = null
        var servay_q_count: Int = 0
        var servay_duple: Int = 0

        var servay_a_txt: String? = null
        var servay_a_img: File? = null
        var servay_b_txt: String? = null
        var servay_b_img: File? = null
    }
}
