package org.sopt21.qnd.Data

import java.util.*

class GetMySurveyListResult {
    var status: String? = null
    var data = ArrayList<GetResponseMySurveyList>()

    inner class GetResponseMySurveyList {
        var servay_id: Int = 0
        var servay_title: String? = null
        var servay_write_time: String? = null

    }
}


