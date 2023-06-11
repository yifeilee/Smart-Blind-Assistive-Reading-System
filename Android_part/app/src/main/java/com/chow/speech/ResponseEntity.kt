package com.chow.speech

class ASRresponse {
    var result_type: String? = null
    var best_result: String? = null
    var origin_result: OriginResultBean? = null
    var error = 0
    var results_recognition: List<String>? = null

    class OriginResultBean {
        var asr_align_begin = 0
        var asr_align_end = 0
        var corpus_no: Long = 0
        var err_no = 0
        var raf = 0
        var result: ResultBean? = null
        var sn: String? = null

        class ResultBean {
            var word: List<String>? = null
        }
    }
}