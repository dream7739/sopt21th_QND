package org.sopt21.qnd.Data

class CheckAuthData {
    var data = CheckAuthRequestData()

    inner class CheckAuthRequestData {
        var authorization_code: String? = null
        var authorization_code_accept: String? = null
    }
}
