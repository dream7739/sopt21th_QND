package org.sopt21.qnd.Data

class CheckAuthResult {
    var status: String? = null
    var data: CheckAuthResponseData? = null

    inner class CheckAuthResponseData {
        var check: Int = 0
    }
}
