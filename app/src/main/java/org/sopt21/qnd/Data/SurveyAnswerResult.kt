package org.sopt21.qnd.Data

class SurveyAnswerResult {
    var status: String? = null
    var data = SurveyAnswerItemResult()
    var msg: String? = null

    inner class SurveyAnswerItemResult {
        var servay_id: Int = 0
        var servay_option_id: Int = 0
        var servay_type: Int = 0
        var servay_title: String = ""
        var servay_explanation: String = ""
        var servay_option_count: Int = 0
        var servay_q_count: Int = 0 //A타입 일 때 응답지의 수
        var servay_goal: Int = 0
        var servay_like_count: Int = 0
        var servay_alert_count: Int = 0
        var selection_start_select_time: String = ""
        var servay_duple: Int = 0 //복수 선택 가능 여부
        var servay_q1: String = ""
        var servay_q2: String = ""
        var servay_q3: String = ""
        var servay_q4: String = ""
        var is_like: Int = 0 //user가 좋아요 했는지 안했는지
        //servay_servay_type=1일때 (B타입 관련 데이터) , C는 그냥 기본 데이터에 받아올 수 있음
        var servay_ab_id: Int = 0
        var servay_a_img: String = ""
        var servay_b_img: String = ""
        var servay_a_txt: String = ""
        var servay_b_txt: String = ""
    }
}