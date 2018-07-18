package org.sopt21.qnd.Data

class RegisterUserAddData {
    var data = RegisterRequestData()
    var user_type: Int = 0
    var user_email: String? = null
    var user_age: Int = 0
    var user_marriage: Int = 0
    var user_job: String? = null
    var user_city: String? = null
    var user_pwd: String? = null
    var img: String? = null

    inner class RegisterRequestData {
        var user_type: Int = 0
        var user_email: String? = null
        var user_age: Int = 0
        var user_marriage: Int = 0
        var user_job: String? = null
        var user_city: String? = null
        var user_pwd: String? = null
    }
}
