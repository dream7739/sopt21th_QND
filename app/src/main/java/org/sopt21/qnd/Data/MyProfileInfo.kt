package org.sopt21.qnd.Data

class MyProfileInfo {
    var status: String? = null
    var msg: String? = null
    var data = MyProfileResponseData()

    inner class MyProfileResponseData {
        var user_available_point: Int = 0
        var user_used_point: Int = 0
        var user_accumulate_point: Int = 0
        var user_img: String? = null
        var user_email: String? = null
    }
}
