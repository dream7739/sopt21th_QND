package org.sopt21.qnd.Data

class EmailAuthResult {
    var status: String? = null
    var data: AuthResponseData? = null
    var msg: String? = null

    inner class AuthResponseData {
        var authorization_code: String? = null
    }
}
