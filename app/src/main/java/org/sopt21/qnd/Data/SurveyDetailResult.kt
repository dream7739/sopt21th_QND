package org.sopt21.qnd.Data

class SurveyDetailResult {
    var data = surveyDetailItemResult()

    inner class surveyDetailItemResult {
        //TYPE A
        var servay_id: Int = 0
        var servay_type: Int = 0
        var servay_title: String = ""
        var servay_explanation: String = ""
        var servay_participation_count: Int = 0 //총 참여자 수
        var is_like: Int = 0
        var servay_q_count: Int = 0
        var servay_q1: String = ""
        var servay_q2: String = ""
        var servay_q3: String = ""
        var servay_q4: String = ""
        var servay_choice1_selection_count: Int = 0
        var servay_choice2_selection_count: Int = 0
        var servay_choice3_selection_count: Int = 0
        var servay_choice4_selection_count: Int = 0


        //TYPE B&C
        var servay_a_txt: String = ""
        var servay_a_img: String = ""
        var servay_b_txt: String = ""
        var servay_b_img: String = ""


    }
}