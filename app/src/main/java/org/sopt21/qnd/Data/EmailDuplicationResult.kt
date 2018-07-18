package org.sopt21.qnd.Data

class EmailDuplicationResult {
    var status: String? = null
    var msg: String? = null
    var data: EmailDupleResponse? = null

    inner class EmailDupleResponse{
        var response_code: Int = 0
    }
}
