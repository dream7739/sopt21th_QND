package org.sopt21.qnd.Fragment

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.soyeon.qnd_search2.data.SearchSurveyData
import com.soyeon.qnd_search2.data.SearchSurveyResult
import de.hdodenhof.circleimageview.CircleImageView
import org.sopt21.qnd.Adapter.CommentAdapter
import org.sopt21.qnd.Adapter.SearchSurveyAdapter
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.Data.*
import org.sopt21.qnd.Network.NetworkService
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by soyeon on 2018. 1. 8..
 */
class FragmentSearch : Fragment(), View.OnClickListener {
    internal lateinit var surveyEt: EditText
    internal lateinit var surveyFilter: ImageButton
    internal lateinit var surveyCancel: ImageButton
    internal var ing = 2//default값 지정
    internal var order = 0


    private var networkService: NetworkService? = null
    internal lateinit var searchAdapter: SearchSurveyAdapter
    internal lateinit var searchList:ArrayList<SearchSurveyResult.SearchSurveyItemResult>//창에 띄울 내용들을 담음
    private lateinit var answerList: SurveyAnswerResult.SurveyAnswerItemResult
    lateinit var detailList: SurveyDetailResult.surveyDetailItemResult
    private var commentList: ArrayList<CommentGetResult.CommentItemGetResult>? = null //댓글 내용을 담음
    var commentAdapter: CommentAdapter? = null

    internal lateinit var requestManager :RequestManager

    lateinit var search_main_list:RecyclerView

    var loginInfo: SharedPreferences? = null
    var user_id: String  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, containter: ViewGroup?, savedInstanceState: Bundle?): View? {
        val searchview = inflater!!.inflate(R.layout.search_board_recycler, containter, false)

        networkService = ApplicationController.instance!!.networkService
        requestManager = Glide.with(activity)

        loginInfo = activity.getSharedPreferences("loginSetting", 0)
        user_id = loginInfo!!.getString("user_id","")


        search_main_list = searchview.findViewById(R.id.search_list) as RecyclerView
        search_main_list.layoutManager=LinearLayoutManager(activity)

        if (arguments != null) {
            ing = arguments.getInt("ing")
            order = arguments.getInt("order")
        }

        surveyEt = searchview.findViewById(R.id.survey_text) as EditText
        surveyFilter = searchview.findViewById(R.id.survey_filter) as ImageButton
        surveyCancel = searchview.findViewById(R.id.survey_edit_cancel) as ImageButton

        surveyEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) =
                    Unit

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) =
                    if (surveyEt.length() != 0)
                        surveyCancel.visibility = View.VISIBLE
                    else
                        surveyCancel.visibility = View.GONE
            override fun afterTextChanged(editable: Editable) = Unit
        }
        )
        surveyEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val str = surveyEt.text.toString()
                 //   Toast.makeText(activity, "검색", Toast.LENGTH_SHORT).show()
                    searchSurveyNetwork(str)
                }

                else -> {
                   // Toast.makeText(activity, "기본", Toast.LENGTH_SHORT).show()
                    return@OnEditorActionListener false
                }
            }
            true
        })
        surveyFilter.setOnClickListener {
            val filterFragment = FragmentFilter()
            getFragmentManager().beginTransaction().replace(R.id.make_container,filterFragment, "filter").commit();

        }

        surveyCancel.setOnClickListener { surveyEt.setText("") }

        searchSurveyNetwork("");//모든 리스트 돌려줌

        return searchview
    }

    fun searchSurveyNetwork(word: String) {
        val networkService = ApplicationController.instance!!.networkService
        val searchSurveyData = SearchSurveyData()

        searchSurveyData.ing = ing
        searchSurveyData.order = order
        searchSurveyData.search = word

        val searchSurvey = networkService!!.searchSurvey(user_id, searchSurveyData)

        searchSurvey.enqueue(object : Callback<SearchSurveyResult>
        {
            override fun onResponse(call: Call<SearchSurveyResult>, response: Response<SearchSurveyResult>) =
                    if (response.isSuccessful) {
                        searchList = response.body().data
                        searchAdapter = SearchSurveyAdapter(response.body().data)
                        searchAdapter!!.setOnItemClickListener(this@FragmentSearch)
                        if (searchList.size != 0) {
                            search_main_list!!.adapter = searchAdapter
                        }
                        else{
                            Toast.makeText(context, "일치하는 검색어가 없습니다", Toast.LENGTH_SHORT).show()

                        }
                    }
                else{}

            override fun onFailure(call: Call<SearchSurveyResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원활하지 않습니다", Toast.LENGTH_SHORT)
            }
        })
    }

    override fun onClick(v: View?) {
        val idx: Int = search_main_list.getChildAdapterPosition(v)

        var user_id = user_id //토큰 값
        var servay_id = searchList!!.get(idx).servay_id
        var surveyCoin = searchList!!.get(idx).servay_get


        var answerCheck = searchList!!.get(idx).servay_selected

        Toast.makeText(activity,"클릭",Toast.LENGTH_SHORT).show()

        //참여안하면 0
        if (answerCheck == 0)
        {//응답 안했으면 설문 참여 창으로
                surveyAnswer(user_id, servay_id, surveyCoin)
        }
        //참여했으면 1
        else
        {
                surveyDetailResult(user_id, servay_id)  //응답했으면 설문 결과 창으로
        }
    }

    //서버와 좋아요 통신--> 완료
    fun surveyLike(token: String, servay_id: Int) {
        var likeResponse = networkService!!.surveyLike(user_id, servay_id)
        likeResponse.enqueue(object : Callback<SurveyLikeResult> {
            override fun onResponse(call: Call<SurveyLikeResult>?, response: Response<SurveyLikeResult>?) {
                if (response!!.isSuccessful) {
                    searchSurveyNetwork("")
                }

            }

            override fun onFailure(call: Call<SurveyLikeResult>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("네트워크가 불안정합니다.!")
            }

        })
    }


    fun goDeclar(servay_id: Int) {

        var surveyDeclarData = SurveyDeclarData() //신고 내용&토큰 값&servay_id 필요
        val declareView = LayoutInflater.from(context).inflate(R.layout.survey_declar, null) //신고
        var declar_dialog: AlertDialog? = null //신고 dialog

        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder
        var declar_close = declareView.findViewById(R.id.declar_close) as ImageButton //신고 창 닫기
        var declar_text = declareView.findViewById(R.id.declar_text) as EditText //신고 내용
        var declar_finish = declareView.findViewById(R.id.declar_finish) as Button //신고 완료-> 통신(접수된건지 안된건지 판단 후 dismiss())


        declar_dialog = builder.create()
        declar_dialog!!.setView(declareView)
        declar_dialog!!.show() //신고 다이얼로그 띄우기


        declar_finish.setOnClickListener {
            //여기서 통신을 건다(내용 쓰고 신고 완료 누르면!)


            if (declar_text.text.toString().isEmpty()) {
                Toast.makeText(context, "신고 내용을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                surveyDeclarData.alert_alert_content = declar_text.text.toString()
                surveyDeclarData.servay_id = servay_id

                val declareResponse = networkService!!.surveyDeclare(user_id, surveyDeclarData)
                declareResponse.enqueue(object : Callback<SurveyDeclarResult> {
                    override fun onResponse(call: Call<SurveyDeclarResult>, response: Response<SurveyDeclarResult>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "신고가 접수되었습니다", Toast.LENGTH_SHORT).show()
                            declar_dialog!!.dismiss() //신고 접수하고 창 꺼주기
                        } else {
                            Toast.makeText(context, "이미 신고된 게시물입니다.", Toast.LENGTH_SHORT).show()
                            declar_dialog!!.dismiss() //신고 접수하고 창 꺼주기
                        }
                    }

                    override fun onFailure(call: Call<SurveyDeclarResult>, t: Throwable) {
                        Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }


        declar_close.setOnClickListener {
            declar_dialog!!.dismiss()
        }

    }

    //댓글 작성 통신-->완료
    fun writeComment(token: String, commentWriteData: CommentWriteData) {
        val writeCommentResponse = networkService!!.writeComment(user_id, commentWriteData)
        writeCommentResponse.enqueue(object : Callback<CommentWriteResult> {
            override fun onResponse(call: Call<CommentWriteResult>, response: Response<CommentWriteResult>) {
                Log.v("servay_id", commentWriteData.servay_id.toString())
                Log.v("content", commentWriteData.content)

                if (response.isSuccessful) {

                    if (response.body().status == "Success") {
                        getComment(commentWriteData.servay_id) //업뎃된 내용 반영해야함
                    }

                }
            }

            override fun onFailure(call: Call<CommentWriteResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })

    }


    //댓글 불러오기 통신-->완료
    fun getComment(servay_id: Int) {

        val commentResponse = networkService!!.getComment(servay_id)

        commentResponse.enqueue(object : Callback<CommentGetResult> {
            override fun onResponse(call: Call<CommentGetResult>?, response: Response<CommentGetResult>?) {
                if (response!!.isSuccessful) {
                    if (response!!.body().status.equals("Success")) {
                        if (response.body().msg.equals("comment empty")) {
                            Toast.makeText(context, "첫 번째 댓글을 작성해보세요", Toast.LENGTH_SHORT).show()
                        }
                        val commentView = LayoutInflater.from(context).inflate(R.layout.comment_load, null) //댓글
                        var comment_list: RecyclerView = commentView.findViewById(R.id.comment_list) as RecyclerView //리사이클러뷰 xml과 연결
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder
                        var comment_dialog: AlertDialog? = null //댓글 dialog
                        var comment_close = commentView.findViewById(R.id.comment_close) as ImageButton
                        var comment_write = commentView.findViewById(R.id.comment_write) as EditText
                        var comment_finish = commentView.findViewById(R.id.comment_finish) as Button


                        comment_dialog = builder.create()
                        comment_dialog!!.setView(commentView)
                        comment_dialog.show()

                        commentList = response!!.body().data
                        commentAdapter = CommentAdapter(commentList)
                        comment_list.layoutManager = LinearLayoutManager(activity)
                        comment_list.adapter = commentAdapter


                        //코멘트 close 선택 시
                        comment_close.setOnClickListener {
                            comment_dialog!!.dismiss()
                        }

                        comment_finish.setOnClickListener {
                            var commentWriteData = CommentWriteData()

                            if (comment_write.text.isEmpty()) {
                                Toast.makeText(context, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                            } else {

                                commentWriteData.content = comment_write.text.toString()
                                commentWriteData.servay_id = servay_id
                                writeComment(user_id, commentWriteData) //댓글쓰면 다시 getComment해서 다이얼로그가 뜸
                                comment_dialog!!.dismiss()}
                        }


                    }
                } else {
                    Log.v(TAG, "fail")

                }

            }

            override fun onFailure(call: Call<CommentGetResult>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("네트워크가 불안정합니다.")
            }
        })

    }

    //설문 참여 완료 통신
    fun surveyFinish(token: String, surveyFinishData: SurveyFinishData) { //코인 정보 받아와서 안씀 삭제해도 되는거..?
        val finishResponse = networkService!!.surveyFinish(user_id, surveyFinishData)

        finishResponse.enqueue(object : Callback<SurveyFinishResult> {
            override fun onResponse(call: Call<SurveyFinishResult>, response: Response<SurveyFinishResult>) {
                if (response.isSuccessful) {

                    Toast.makeText(context, "통신 성공", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "통신 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SurveyFinishResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //설문 결과 보기
    fun surveyDetailResult(token: String, servay_id: Int) {

        val detailResponse = networkService!!.surveyDetailRdesult(user_id, servay_id)

        detailResponse.enqueue(object : Callback<SurveyDetailResult> {
            override fun onResponse(call: Call<SurveyDetailResult>?, response: Response<SurveyDetailResult>?) {
                if (response!!.isSuccessful) {
                    detailList = response.body().data

                    var percentList: ArrayList<Int>? = ArrayList<Int>()

                    //타입 0일 때
                    if (detailList.servay_type == 0) {

                        val resultView1 = LayoutInflater.from(context).inflate(R.layout.survey_result_a, null) //설문 결과 type A 다이얼로그
                        var result_dialog1: AlertDialog? = null //결과 dialog type A
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder


                        var percent1: Int = 0 //퍼센테이지 비교 하기 위함
                        var percent2: Int = 0
                        var percent3: Int = 0
                        var percent4: Int = 0

                        var r1_good = resultView1.findViewById(R.id.r1_btn_good) as ImageButton
                        var r1_comment = resultView1.findViewById(R.id.r1_btn_declar) as ImageButton
                        var r1_close = resultView1.findViewById(R.id.r1_btn_close) as ImageButton
                        var r1_title = resultView1.findViewById(R.id.r1_text_title) as TextView
                        var r1_image_result1 = resultView1.findViewById(R.id.r1_image_result1) as ImageView
                        var r1_image_result2 = resultView1.findViewById(R.id.r1_image_result2) as ImageView
                        var r1_image_result3 = resultView1.findViewById(R.id.r1_image_result3) as ImageView
                        var r1_image_result4 = resultView1.findViewById(R.id.r1_image_result4) as ImageView
                        var r1_text_name1 = resultView1.findViewById(R.id.r1_text_name1) as TextView
                        var r1_text_name2 = resultView1.findViewById(R.id.r1_text_name2) as TextView
                        var r1_text_name3 = resultView1.findViewById(R.id.r1_text_name3) as TextView
                        var r1_text_name4 = resultView1.findViewById(R.id.r1_text_name4) as TextView
                        var r1_text_percent1 = resultView1.findViewById(R.id.r1_text_percent1) as TextView
                        var r1_text_percent2 = resultView1.findViewById(R.id.r1_text_percent2) as TextView
                        var r1_text_percent3 = resultView1.findViewById(R.id.r1_text_percent3) as TextView
                        var r1_text_percent4 = resultView1.findViewById(R.id.r1_text_percent4) as TextView
                        var r1_detail_btn = resultView1.findViewById(R.id.r1_detail_btn) as Button

                        var check: Boolean //좋아요 체크

                        r1_title.text = detailList.servay_title //타이틀 제목


                        //옵션 수에 따라 설문 수 다르게 띄우기
                        if (detailList.servay_q_count == 2) {

                            var opcount1: Double = detailList.servay_choice1_selection_count.toDouble()
                            var opcount2: Double = detailList.servay_choice2_selection_count.toDouble()
                            var participate: Double = detailList.servay_participation_count.toDouble()

                            r1_image_result3.visibility = View.INVISIBLE
                            r1_image_result4.visibility = View.INVISIBLE

                            r1_text_name1.text = "1.  " + detailList.servay_q1
                            r1_text_name2.text = "2.  " + detailList.servay_q2
                            r1_text_name3.visibility = View.INVISIBLE
                            r1_text_name4.visibility = View.INVISIBLE


                            if (detailList.servay_participation_count == 0) {
                                percent1 = 0
                                percent2 = 0
                            } else {
                                if (opcount1.toInt() == -1) {
                                    percent1 = 0
                                } else {
                                    percent1 = (opcount1 / participate * 100).toInt()
                                }
                                if (opcount2.toInt() == -1) {
                                    percent2 = 0
                                } else {
                                    percent2 = (opcount2 / participate * 100).toInt()
                                }

                            }

                            r1_text_percent1.text = percent1.toString() + "%"
                            r1_text_percent2.text = percent2.toString() + "%"
                            r1_text_percent3.visibility = View.INVISIBLE
                            r1_text_percent4.visibility = View.INVISIBLE


                            percentList!!.add(percent1)
                            percentList!!.add(percent2)
                            var maxCount = Collections.max(percentList)

                            if (maxCount == percent1) {
                                r1_image_result1.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent1.setTypeface(null, Typeface.BOLD)
                                r1_text_name1.setTextColor(Color.WHITE)
                                r1_text_name1.setTypeface(null, Typeface.BOLD)
                                r1_text_percent1.setTextColor(Color.WHITE)
                            }
                            if (maxCount == percent2) {
                                r1_image_result2.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent2.setTypeface(null, Typeface.BOLD)
                                r1_text_name2.setTextColor(Color.WHITE)
                                r1_text_name2.setTypeface(null, Typeface.BOLD)
                                r1_text_percent2.setTextColor(Color.WHITE)
                            }

                        }

                        if (detailList.servay_q_count == 3) {

                            r1_image_result4.visibility = View.INVISIBLE

                            r1_text_name1.text = "1.  " + detailList.servay_q1
                            r1_text_name2.text = "2.  " + detailList.servay_q2
                            r1_text_name3.text = "3.  " + detailList.servay_q3
                            r1_text_name4.visibility = View.INVISIBLE


                            var opcount1: Double = detailList.servay_choice1_selection_count.toDouble()
                            var opcount2: Double = detailList.servay_choice2_selection_count.toDouble()
                            var opcount3: Double = detailList.servay_choice3_selection_count.toDouble()
                            var participate: Double = detailList.servay_participation_count.toDouble()

                            if (detailList.servay_participation_count== 0) {
                                percent1 = 0
                                percent2 = 0
                                percent3 = 0
                            } else {
                                if (opcount1.toInt() == -1) {
                                    percent1 = 0
                                } else {
                                    percent1 = (opcount1 / participate * 100).toInt()
                                }
                                if (opcount2.toInt() == -1) {
                                    percent2 = 0
                                } else {
                                    percent2 = (opcount2 / participate * 100).toInt()
                                }
                                if (opcount3.toInt() == -1) {
                                    percent3 = 0
                                } else {
                                    percent3 = (opcount3 / participate * 100).toInt()
                                }
                            }

                            r1_text_percent1.text = percent1.toString() + "%"
                            r1_text_percent2.text = percent2.toString() + "%"
                            r1_text_percent3.text = percent3.toString() + "%"
                            r1_text_percent4.visibility = View.INVISIBLE


                            percentList!!.add(percent1)
                            percentList!!.add(percent2)
                            percentList!!.add(percent3)
                            var maxCount = Collections.max(percentList)

                            if (maxCount == percent1) {
                                r1_image_result1.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent1.setTypeface(null, Typeface.BOLD)
                                r1_text_name1.setTypeface(null, Typeface.BOLD)

                                r1_text_name1.setTextColor(Color.WHITE)
                                r1_text_percent1.setTextColor(Color.WHITE)

                            }
                            if (maxCount == percent2) {
                                r1_image_result2.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent2.setTypeface(null, Typeface.BOLD)
                                r1_text_name2.setTextColor(Color.WHITE)
                                r1_text_percent2.setTextColor(Color.WHITE)
                                r1_text_name2.setTypeface(null, Typeface.BOLD)

                            }

                            if (maxCount == percent3) {
                                r1_image_result3.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent3.setTypeface(null, Typeface.BOLD)
                                r1_text_name3.setTextColor(Color.WHITE)
                                r1_text_name3.setTypeface(null, Typeface.BOLD)
                                r1_text_percent3.setTextColor(Color.WHITE)
                            }


                        }


                        if (detailList.servay_q_count == 4) {
                            r1_text_name1.text = "1.  " + detailList.servay_q1
                            r1_text_name2.text = "2.  " + detailList.servay_q2
                            r1_text_name3.text = "3.  " + detailList.servay_q3
                            r1_text_name4.text = "4.  " + detailList.servay_q4


                            var opcount1: Double = detailList.servay_choice1_selection_count.toDouble()
                            var opcount2: Double = detailList.servay_choice2_selection_count.toDouble()
                            var opcount3: Double = detailList.servay_choice3_selection_count.toDouble()
                            var opcount4: Double = detailList.servay_choice4_selection_count.toDouble()
                            var participate: Double = detailList.servay_participation_count.toDouble()

                            if (detailList.servay_participation_count == 0) {
                                percent1 = 0
                                percent2 = 0
                                percent3 = 0
                                percent4 = 0
                            } else {
                                if (opcount1.toInt() == -1) {
                                    percent1 = 0
                                } else {
                                    percent1 = (opcount1 / participate * 100).toInt()
                                }
                                if (opcount2.toInt() == -1) {
                                    percent2 = 0
                                } else {
                                    percent2 = (opcount2 / participate * 100).toInt()
                                }
                                if (opcount3.toInt() == -1) {
                                    percent3 = 0
                                } else {
                                    percent3 = (opcount3 / participate * 100).toInt()
                                }
                                if (opcount4.toInt() == -1) {
                                    percent4 = 0
                                } else {
                                    percent4 = (opcount4 / participate * 100).toInt()
                                }
                            }

                            r1_text_percent1.text = percent1.toString() + "%"
                            r1_text_percent2.text = percent2.toString() + "%"
                            r1_text_percent3.text = percent3.toString() + "%"
                            r1_text_percent4.text = percent4.toString() + "%"


                            percentList!!.add(percent1)
                            percentList!!.add(percent2)
                            percentList!!.add(percent3)
                            percentList!!.add(percent4)
                            var maxCount = Collections.max(percentList)

                            Log.v("percentList", percentList.toString())

                            if (maxCount == percent1) {
                                r1_image_result1.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent1.setTypeface(null, Typeface.BOLD)
                                r1_text_name1.setTextColor(Color.WHITE)
                                r1_text_percent1.setTextColor(Color.WHITE)
                                r1_text_name1.setTypeface(null, Typeface.BOLD)

                            }
                            if (maxCount == percent2) {
                                r1_image_result2.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent2.setTypeface(null, Typeface.BOLD)
                                r1_text_name2.setTextColor(Color.WHITE)
                                r1_text_percent2.setTextColor(Color.WHITE)
                                r1_text_name2.setTypeface(null, Typeface.BOLD)
                            }

                            if (maxCount == percent3) {
                                r1_image_result3.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent3.setTypeface(null, Typeface.BOLD)
                                r1_text_name3.setTextColor(Color.WHITE)
                                r1_text_percent3.setTextColor(Color.WHITE)
                                r1_text_name3.setTypeface(null, Typeface.BOLD)

                            }

                            if (maxCount == percent4) {
                                r1_image_result4.setBackgroundResource(R.drawable.survey_atype_box3)
                                r1_text_percent4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                                r1_text_percent4.setTypeface(null, Typeface.BOLD)
                                r1_text_name4.setTextColor(Color.WHITE)
                                r1_text_percent4.setTextColor(Color.WHITE)
                                r1_text_name4.setTypeface(null, Typeface.BOLD)

                            }


                        }

                        result_dialog1 = builder.create()
                        result_dialog1!!.setView(resultView1)
                        result_dialog1!!.show()


                        r1_detail_btn.setOnClickListener {
                            Toast.makeText(context, "서비스 준비중입니다", Toast.LENGTH_SHORT).show()
                        }

                        //닫기
                        r1_close.setOnClickListener {
                            result_dialog1!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //초기 좋아요 0/1
                        if (detailList.is_like == 0) { //좋아요 한적X
                            r1_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            r1_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }


                        r1_good.setOnClickListener {
                            if (!check!!) {
                                r1_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                r1_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }

                        //댓글 보기
                        r1_comment.setOnClickListener {
                            getComment(servay_id)

                        }


                    }


                    //B타입
                    if (detailList.servay_type == 1) {
                        var lpWindow = WindowManager.LayoutParams()
                        val bTypeResultView = LayoutInflater.from(context).inflate(R.layout.survey_result_b, null)
                        var resultBtypeDialog: AlertDialog? = null
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)

                        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT
                        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT

                        var percent1: Int = 0
                        var percent2: Int = 0

                        var r2_image_option1 = bTypeResultView.findViewById(R.id.r2_image_option1) as CircleImageView
                        var r2_image_option2 = bTypeResultView.findViewById(R.id.r2_image_option2) as CircleImageView
                        var r2_btn_close = bTypeResultView.findViewById(R.id.r2_btn_close) as ImageButton
                        var r2_text_main = bTypeResultView.findViewById(R.id.r2_text_main) as TextView
                        var r2_text_ok = bTypeResultView.findViewById(R.id.r2_text_ok) as TextView
                        var r2_text_no = bTypeResultView.findViewById(R.id.r2_text_no) as TextView
                        var r2_text_result1 = bTypeResultView.findViewById(R.id.r2_text_result1) as TextView
                        var r2_text_result2 = bTypeResultView.findViewById(R.id.r2_text_result2) as TextView
                        var r2_btn_good = bTypeResultView.findViewById(R.id.r2_btn_good) as ImageButton
                        var r2_btn_comment = bTypeResultView.findViewById(R.id.r2_btn_comment) as ImageButton
                        var r2_btn_option1_check = bTypeResultView.findViewById(R.id.r2_btn_option1_check) as CircleImageView
                        var r2_btn_option2_check = bTypeResultView.findViewById(R.id.r2_btn_option2_check) as CircleImageView
                        var r2_detail_btn = bTypeResultView.findViewById(R.id.r2_detail_btn) as Button


                        var check: Boolean // 좋아요 버튼 체크

                        //퍼센트 계산
                        percent1 = (detailList.servay_choice1_selection_count / detailList.servay_participation_count) * 100
                        percent2 = (detailList.servay_choice2_selection_count / detailList.servay_participation_count) * 100

                        r2_text_main.text = "Q. " + detailList.servay_title
                        r2_text_ok.text = detailList.servay_a_txt
                        r2_text_no.text = detailList.servay_b_txt
                        r2_text_result1.text = percent1.toString() + "%"
                        r2_text_result2.text = percent2.toString() + "%"

                        requestManager = Glide.with(activity)
                        requestManager!!.load(detailList.servay_a_img).into(r2_image_option1)
                        requestManager!!.load(detailList.servay_b_img).into(r2_image_option2)

                        val scale = resources.displayMetrics.density
                        if(percent1 > percent2){
                            r2_image_option1.layoutParams.height = (100 * scale).toInt()
                            r2_image_option1.layoutParams.width = (100 * scale).toInt()
                            r2_text_ok.textSize = 20f
                            r2_text_ok.setTextColor(Color.parseColor("#000000"))
                            r2_text_result1.textSize = 22f
                            r2_text_result1.setTextColor(Color.parseColor("#8BBDFF"))
                            r2_btn_option1_check.visibility = View.VISIBLE
                        }else if(percent1 < percent2){
                            r2_image_option2.layoutParams.height = (100 * scale).toInt()
                            r2_image_option2.layoutParams.width = (100 * scale).toInt()
                            r2_text_no.textSize = 20f
                            r2_text_no.setTextColor(Color.parseColor("#000000"))
                            r2_text_result2.textSize = 22f
                            r2_text_result2.setTextColor(Color.parseColor("#8BBDFF"))
                            r2_btn_option2_check.visibility = View.VISIBLE
                        }

                        resultBtypeDialog = builder.create()
                        resultBtypeDialog!!.setView(bTypeResultView)
                        resultBtypeDialog!!.show()

                        r2_detail_btn.setOnClickListener {
                            Toast.makeText(context, "서비스 준비중입니다", Toast.LENGTH_SHORT).show()
                        }

                        //닫기
                        r2_btn_close.setOnClickListener {
                            resultBtypeDialog!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //초기 좋아요
                        if (detailList.is_like == 0) { //좋아요 한적X
                            r2_btn_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            r2_btn_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }

                        r2_btn_good.setOnClickListener {
                            if (!check!!) {
                                r2_btn_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                r2_btn_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }

                        //댓글
                        r2_btn_comment.setOnClickListener {
                            getComment(servay_id)
                        }

                    }


                    //C타입
                    if (detailList.servay_type == 2) {
                        var lpWindow = WindowManager.LayoutParams()
                        val cTypeResultView = LayoutInflater.from(context).inflate(R.layout.survey_result_c, null)
                        var resultCtypeDialog: AlertDialog? = null
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity)

                        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT
                        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT

                        var percent1: Int = 0
                        var percent2: Int = 0

                        var r3_image_option1 = cTypeResultView.findViewById(R.id.r3_image_option1) as CircleImageView
                        var r3_image_option2 = cTypeResultView.findViewById(R.id.r3_image_option2) as CircleImageView
                        var r3_btn_close = cTypeResultView.findViewById(R.id.r3_btn_close) as ImageButton
                        var r3_text_main = cTypeResultView.findViewById(R.id.r3_text_main) as TextView
                        var r3_text_ok = cTypeResultView.findViewById(R.id.r3_text_ok) as TextView
                        var r3_text_no = cTypeResultView.findViewById(R.id.r3_text_no) as TextView
                        var r3_text_result1 = cTypeResultView.findViewById(R.id.r3_text_result1) as TextView
                        var r3_text_result2 = cTypeResultView.findViewById(R.id.r3_text_result2) as TextView
                        var r3_btn_good = cTypeResultView.findViewById(R.id.r3_btn_good) as ImageButton
                        var r3_btn_comment = cTypeResultView.findViewById(R.id.r3_btn_comment) as ImageButton
                        var r3_detail_btn = cTypeResultView.findViewById(R.id.r3_detail_btn) as Button


                        var check: Boolean // 좋아요 버튼 체크

                        //퍼센트 계산
                        percent1 = (detailList.servay_choice1_selection_count / detailList.servay_participation_count) * 100
                        percent2 = (detailList.servay_choice2_selection_count / detailList.servay_participation_count) * 100

                        r3_text_main.text = "Q. " + detailList.servay_title
                        r3_text_ok.text = "찬성"
                        r3_text_no.text = "반대"
                        r3_text_result1.text = percent1.toString() + "%"
                        r3_text_result2.text = percent2.toString() + "%"

                        val scale = resources.displayMetrics.density
                        if(percent1 > percent2){
                            r3_image_option1.layoutParams.height = (100 * scale).toInt()
                            r3_image_option1.layoutParams.width = (100 * scale).toInt()
                            r3_image_option1.setImageResource(R.drawable.survey_ctype_circle3)
                            r3_text_ok.textSize = 20f
                            r3_text_ok.setTextColor(Color.parseColor("#000000"))
                            r3_text_result1.textSize = 22f
                            r3_text_result1.setTextColor(Color.parseColor("#8BBDFF"))

                        }else if(percent1 < percent2){
                            r3_image_option2.layoutParams.height = (100 * scale).toInt()
                            r3_image_option2.layoutParams.width = (100 * scale).toInt()
                            r3_image_option2.setImageResource(R.drawable.survey_ctype_circle4)

                            r3_text_no.textSize = 20f
                            r3_text_no.setTextColor(Color.parseColor("#000000"))
                            r3_text_result2.textSize = 22f
                            r3_text_result2.setTextColor(Color.parseColor("#8BBDFF"))
                        }

                        resultCtypeDialog = builder.create()
                        resultCtypeDialog!!.setView(cTypeResultView)
                        resultCtypeDialog!!.show()

                        r3_detail_btn.setOnClickListener {
                            Toast.makeText(context, "서비스 준비중입니다", Toast.LENGTH_SHORT).show()
                        }

                        //닫기
                        r3_btn_close.setOnClickListener {
                            resultCtypeDialog!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //초기 좋아요
                        if (detailList.is_like == 0) { //좋아요 한적X
                            r3_btn_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            r3_btn_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }

                        r3_btn_good.setOnClickListener {
                            if (!check!!) {
                                r3_btn_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                r3_btn_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }

                        //댓글
                        r3_btn_comment.setOnClickListener {
                            getComment(servay_id)
                        }
                    }


                }


            }

            override fun onFailure(call: Call<SurveyDetailResult>?, t: Throwable?) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                Log.v("detail", "detail_fail")
            }
        })


    }

    //설문에 답하기
    fun surveyAnswer(user_id: String, servay_id: Int, coin: Int) {

        val answerResponse = networkService!!.surveyAnswer(user_id, servay_id)

        answerResponse.enqueue(object : Callback<SurveyAnswerResult> {

            override fun onResponse(call: Call<SurveyAnswerResult>?, response: Response<SurveyAnswerResult>?) {
                if (response!!.isSuccessful) {
                    Log.v(tag, "success")
                    answerList = response!!.body().data //설문 참여 창의 모든 정보를 담음

                    Log.v("type", answerList.servay_type.toString())

                    if (answerList.servay_type == 0) //A타입
                    {
                        var select_choice1_noduple = -1 //다중선택 아닐때 보낼 값
                        var select_choice2_noduple = -1 //다중선택 아닐때 보낼 값
                        var select_choice3_noduple = -1 //다중선택 아닐때 보낼 값
                        var select_choice4_noduple = -1 //다중선택 아닐때 보낼 값


                        var serveyView1 = LayoutInflater.from(context).inflate(R.layout.survey_type_a, null) //설문 참여 type A
                        val noticeView = LayoutInflater.from(context).inflate(R.layout.survey_finish_notice, null) //설문 완료 notice 창

                        //다이얼로그 빌더
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder

                        //다이얼로그
                        var search_dialog1: AlertDialog? = null //설문 참여 다이얼로그 type A
                        var declar_dialog: AlertDialog? = null //신고 dialog
                        var notice_dialog: AlertDialog? = null //완료 알림 dialog

                        //설문창 A 뷰
                        var s1_text1 = serveyView1.findViewById(R.id.s1_text_name) as TextView
                        var s1_text2 = serveyView1.findViewById(R.id.s1_text_explain) as TextView
                        var s1_option1 = serveyView1.findViewById(R.id.s1_btn_option1) as Button
                        var s1_option2 = serveyView1.findViewById(R.id.s1_btn_option2) as Button
                        var s1_option3 = serveyView1.findViewById(R.id.s1_btn_option3) as Button
                        var s1_option4 = serveyView1.findViewById(R.id.s1_btn_option4) as Button
                        var s1_finish = serveyView1.findViewById(R.id.s1_btn_finish) as Button
                        var s1_good = serveyView1.findViewById(R.id.s1_btn_good) as ImageButton
                        var s1_declar = serveyView1.findViewById(R.id.s1_btn_declar) as ImageButton
                        var s1_close = serveyView1.findViewById(R.id.s1_btn_close) as ImageButton
                        var s1_duple = serveyView1.findViewById(R.id.s1_text_duple) as TextView


                        //완료 다이얼로그 뷰
                        var notice_coin = noticeView.findViewById(R.id.coin_num) as TextView
                        var notice_close = noticeView.findViewById(R.id.notice_close) as ImageButton //설문 완료 띄운 후 종료
                        var notice_show = noticeView.findViewById(R.id.notice_finish) as Button//설문 완료에서 결과보기 창으로 이동


                        var check: Boolean //좋아요 체크
                        var checkOption1: Boolean = false //중복 옵션 체크
                        var checkOption2: Boolean = false //중복 옵션 체크
                        var checkOption3: Boolean = false //중복 옵션 체크
                        var checkOption4: Boolean = false //중복 옵션 체크


                        //설문 이름
                        s1_text1.text = "Q. " + answerList.servay_title


                        //설문 상세정보
                        if (answerList.servay_explanation == "") //상세 정보 없으면
                        {
                            s1_text2.text = ""

                        } else {
                            s1_text2.text = answerList.servay_explanation
                        }


                        //다중선택 불가할 때
                        if (answerList.servay_duple == 0) {
                            //항목별로 옵션 다르게 띄우기

                            if (answerList.servay_q_count == 2) //다중선택 불가 & 항목 2개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.visibility = View.INVISIBLE
                                s1_option4.visibility = View.INVISIBLE

                                s1_option1.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 1
                                    select_choice2_noduple = 0

                                }
                                s1_option2.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 1
                                }
                            }

                            if (answerList.servay_q_count == 3) //다중선택 불가 & 항목 3개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.text = answerList.servay_q3
                                s1_option4.visibility = View.INVISIBLE

                                s1_option1.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 1
                                    select_choice2_noduple = 0
                                    select_choice3_noduple = 0
                                }
                                s1_option2.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 1
                                    select_choice3_noduple = 0
                                }
                                s1_option3.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box1)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 0
                                    select_choice3_noduple = 1
                                }

                            }
                            if (answerList.servay_q_count == 4) //다중선택 불가 & 항목 4개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.text = answerList.servay_q3
                                s1_option4.text = answerList.servay_q4

                                s1_option1.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option4.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 1
                                    select_choice2_noduple = 0
                                    select_choice3_noduple = 0
                                    select_choice4_noduple = 0
                                }
                                s1_option2.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option4.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 1
                                    select_choice3_noduple = 0
                                    select_choice4_noduple = 0
                                }
                                s1_option3.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box1)
                                    s1_option4.setBackgroundResource(R.drawable.survey_atype_box2)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 0
                                    select_choice3_noduple = 1
                                    select_choice4_noduple = 0
                                }
                                s1_option4.setOnClickListener {
                                    s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)
                                    s1_option4.setBackgroundResource(R.drawable.survey_atype_box1)

                                    select_choice1_noduple = 0
                                    select_choice2_noduple = 0
                                    select_choice3_noduple = 0
                                    select_choice4_noduple = 1
                                }
                            }


                        } else { //여기 이상함

                            s1_duple.visibility = View.VISIBLE //중복선택 가능 TEXT 띄우기

                            if (answerList.servay_q_count == 2) //다중선택 가능 & 항목 2개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.visibility = View.INVISIBLE
                                s1_option4.visibility = View.INVISIBLE

                                select_choice1_noduple = 0
                                select_choice2_noduple = 0

                                s1_option1.setOnClickListener {
                                    if (!checkOption1) {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice1_noduple = 1
                                        checkOption1 = true
                                    } else {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice1_noduple = 0
                                        checkOption1 = false

                                    }
                                }

                                s1_option2.setOnClickListener {
                                    if (!checkOption2) {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice2_noduple = 1
                                        checkOption2 = true
                                    } else {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice2_noduple = 0
                                        checkOption2 = false

                                    }
                                }

                            }

                            if (answerList.servay_q_count == 3) //다중선택 가능 & 항목 3개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.text = answerList.servay_q3
                                s1_option4.visibility = View.INVISIBLE

                                select_choice1_noduple = 0
                                select_choice2_noduple = 0
                                select_choice3_noduple = 0


                                s1_option1.setOnClickListener {
                                    if (!checkOption1) {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice1_noduple = 1
                                        checkOption1 = true
                                    } else {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice1_noduple = 0
                                        checkOption1 = false

                                    }
                                }

                                s1_option2.setOnClickListener {

                                    if (!checkOption2) {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice2_noduple = 1
                                        checkOption2 = true
                                    } else {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice2_noduple = 0
                                        checkOption2 = false

                                    }
                                }


                                s1_option3.setOnClickListener {
                                    if (!checkOption3) {
                                        s1_option3.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice3_noduple = 1
                                        checkOption3 = true
                                    } else {
                                        s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice3_noduple = 0
                                        checkOption3 = false

                                    }
                                }
                            }
                            if (answerList.servay_q_count == 4) //다중선택 가능 & 항목 4개
                            {
                                s1_option1.text = answerList.servay_q1
                                s1_option2.text = answerList.servay_q2
                                s1_option3.text = answerList.servay_q3
                                s1_option4.text = answerList.servay_q4


                                select_choice1_noduple = 0
                                select_choice2_noduple = 0
                                select_choice3_noduple = 0
                                select_choice4_noduple = 0


                                s1_option1.setOnClickListener {
                                    if (!checkOption1) {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice1_noduple = 1
                                        checkOption1 = true
                                    } else {
                                        s1_option1.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice1_noduple = 0
                                        checkOption1 = false

                                    }
                                }

                                s1_option2.setOnClickListener {

                                    if (!checkOption2) {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice2_noduple = 1
                                        checkOption2 = true
                                    } else {
                                        s1_option2.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice2_noduple = 0
                                        checkOption2 = false

                                    }
                                }


                                s1_option3.setOnClickListener {
                                    if (!checkOption3) {
                                        s1_option3.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice3_noduple = 1
                                        checkOption3 = true
                                    } else {
                                        s1_option3.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice3_noduple = 0
                                        checkOption3 = false

                                    }
                                }

                                s1_option4.setOnClickListener {
                                    if (!checkOption4) {
                                        s1_option4.setBackgroundResource(R.drawable.survey_atype_box1)
                                        select_choice4_noduple = 1
                                        checkOption4 = true
                                    } else {
                                        s1_option4.setBackgroundResource(R.drawable.survey_atype_box2)
                                        select_choice4_noduple = 0
                                        checkOption4 = false

                                    }
                                }


                            }
                        }


                        //초기 좋아요 0/1
                        if (answerList.is_like == 0) { //좋아요 한적X
                            s1_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            s1_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }


                        s1_good.setOnClickListener {
                            if (!check!!) {
                                s1_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                s1_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }


                        //신고 버튼 누르면
                        s1_declar.setOnClickListener {
                            goDeclar(servay_id)
                        }

                        //다이얼로그 X버튼
                        s1_close.setOnClickListener {
                            search_dialog1!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //완료 버튼
                        s1_finish.setOnClickListener {
                            var surveyFinishData = SurveyFinishData()
                            surveyFinishData.servay_id = answerList.servay_id
                            surveyFinishData.selection_start_select_time = answerList.selection_start_select_time
                            surveyFinishData.selection_choice1 = select_choice1_noduple
                            surveyFinishData.selection_choice2 = select_choice2_noduple
                            surveyFinishData.selection_choice3 = select_choice3_noduple
                            surveyFinishData.selection_choice4 = select_choice4_noduple
                            surveyFinish(user_id, surveyFinishData) //완료 버튼 누를 때 서버랑 통신
                            search_dialog1!!.dismiss()


                            notice_dialog = builder.create()
                            notice_dialog!!.setView(noticeView)
                            notice_dialog!!.show()

                            notice_coin.text = coin.toString() //코인 세팅

                            notice_show.setOnClickListener {
                                notice_dialog!!.dismiss()
                                surveyDetailResult(user_id, answerList.servay_id)
                            }

                            notice_close.setOnClickListener {
                                //X버튼
                                notice_dialog!!.dismiss() //노티스 창도 끄고
                                search_dialog1!!.dismiss() //참여 창도 끄고
                                searchSurveyNetwork("") //창끄면 설문 참여했는지 안했는지 반영해서 다시 띄워주기
                            }


                        }

                        search_dialog1 = builder.create()
                        search_dialog1!!.setView(serveyView1)
                        search_dialog1!!.show()

                    }


                    if (answerList.servay_type == 1) {

                        Toast.makeText(context, "B타입", Toast.LENGTH_SHORT).show()


                        var select_choice1 = 0 //다중선택 아닐때 보낼 값
                        var select_choice2 = 0 //다중선택 아닐때 보낼 값

                        var lpWindow = WindowManager.LayoutParams()
                        var serveyView2 = LayoutInflater.from(context).inflate(R.layout.survey_type_b, null) //설문 참여 type A
                        val declareView = LayoutInflater.from(context).inflate(R.layout.survey_declar, null) //신고
                        val noticeView = LayoutInflater.from(context).inflate(R.layout.survey_finish_notice, null) //설문 완료 notice 창

                        //다이얼로그
                        var search_dialog2: AlertDialog? = null //설문 참여 다이얼로그 type A
                        var declar_dialog: AlertDialog? = null //신고 dialog
                        var notice_dialog: AlertDialog? = null //완료 알림 dialog

                        //설문창 C뷰
                        var s2_text1 = serveyView2.findViewById(R.id.s2_text_name) as TextView
                        var s2_text2 = serveyView2.findViewById(R.id.s2_text_explain) as TextView
                        var s2_option1 = serveyView2.findViewById(R.id.s2_btn_option1) as CircleImageView
                        var s2_option1_check = serveyView2.findViewById(R.id.s2_btn_option1_check) as CircleImageView
                        var s2_option2 = serveyView2.findViewById(R.id.s2_btn_option2) as CircleImageView
                        var s2_option2_check = serveyView2.findViewById(R.id.s2_btn_option2_check) as CircleImageView
                        var s2_text_a = serveyView2.findViewById(R.id.s2_text_a) as TextView
                        var s2_text_b = serveyView2.findViewById(R.id.s2_text_b) as TextView
                        var s2_finish = serveyView2.findViewById(R.id.s2_btn_finish) as Button
                        var s2_good = serveyView2.findViewById(R.id.s2_btn_good) as ImageButton
                        var s2_declar = serveyView2.findViewById(R.id.s2_btn_declar) as ImageButton
                        var s2_close = serveyView2.findViewById(R.id.s2_btn_close) as ImageButton

                        //신고 다이얼로그 뷰
                        var declar_close = declareView.findViewById(R.id.declar_close) as ImageButton
                        var declar_text = declareView.findViewById(R.id.declar_text) as EditText
                        var declar_finish = declareView.findViewById(R.id.declar_finish) as Button

                        //완료 다이얼로그 뷰
                        var notice_coin = noticeView.findViewById(R.id.coin_num) as TextView
                        var notice_close = noticeView.findViewById(R.id.notice_close) as ImageButton //설문 완료 띄운 후 종료
                        var notice_show = noticeView.findViewById(R.id.notice_finish) as Button

                        //다이얼로그 빌더
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder

                        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT
                        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT  //커스텀 크기대로 적용되게 함

                        var check: Boolean //좋아요 체크

                        //설문 이름
                        s2_text1.text = "Q." + answerList.servay_title

                        //설문 상세정보
                        if (answerList.servay_explanation == "") //상세 정보 없으면
                        {
                            s2_text2.visibility = View.GONE //안보이게

                        } else {
                            s2_text2.text = answerList.servay_explanation
                        }

                        s2_text_a.text = answerList.servay_a_txt
                        s2_text_b.text = answerList.servay_b_txt

                        //TODO 이곳을 해야함-string으로 받아온 이미지를 넣어주기..

                        s2_option1.setOnClickListener {
                            s2_option1_check.visibility = View.VISIBLE
                            s2_option2_check.visibility = View.INVISIBLE

                            select_choice1 = 1
                            select_choice2 = 0
                        }

                        s2_option2.setOnClickListener {
                            s2_option1_check.visibility = View.INVISIBLE
                            s2_option2_check.visibility = View.VISIBLE

                            select_choice1 = 0
                            select_choice2 = 1
                        }


                        //초기 좋아요 0/1
                        if (answerList.is_like == 0) { //좋아요 한적X
                            s2_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            s2_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }

                        s2_good.setOnClickListener {
                            if (!check!!) {
                                s2_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                s2_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }
/*

                        //신고 버튼 누르면
                        s2_declar.setOnClickListener {
                            declar_dialog = builder.create()
                            declar_dialog!!.setView(declareView)
                            declar_dialog!!.show()

                        }
                        declar_finish.setOnClickListener {
                            var surveyDeclarData = SurveyDeclarData()
                            surveyDeclarData.servay_id = answerList.servay_id
                            surveyDeclarData.alert_alert_content = declar_text.text.toString()
                            surveyDeclare(user_id, surveyDeclarData)
                        }

                        declar_close.setOnClickListener {
                            declar_dialog!!.dismiss()
                        }
*/


                        //다이얼로그 X버튼
                        s2_close.setOnClickListener {
                            search_dialog2!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //완료 버튼
                        s2_finish.setOnClickListener {
                            var surveyFinishData = SurveyFinishData()
                            surveyFinishData.servay_id = answerList.servay_id
                            surveyFinishData.selection_start_select_time = answerList.selection_start_select_time
                            surveyFinishData.selection_choice1 = select_choice1
                            surveyFinishData.selection_choice2 = select_choice2
                            surveyFinish(user_id, surveyFinishData) //완료 버튼 누를 때 서버랑 통신
                            search_dialog2!!.dismiss()

                            notice_dialog = builder.create()
                            notice_dialog!!.setView(noticeView)
                            notice_dialog!!.show()

                            notice_coin.text = coin.toString() //코인 세팅

                            notice_close.setOnClickListener {
                                notice_dialog!!.dismiss()
                            }

                        }

                        search_dialog2 = builder.create()
                        search_dialog2!!.setView(serveyView2)
                        search_dialog2!!.show()


                    }


                    //C타입 찬/반
                    if (answerList.servay_type == 2)  //C타입
                    {
                        var select_choice1 = 0 //다중선택 아닐때 보낼 값
                        var select_choice2 = 0 //다중선택 아닐때 보낼 값

                        var lpWindow = WindowManager.LayoutParams()
                        var serveyView3 = LayoutInflater.from(context).inflate(R.layout.survey_type_c, null) //설문 참여 type A
                        val declareView = LayoutInflater.from(context).inflate(R.layout.survey_declar, null) //신고
                        val noticeView = LayoutInflater.from(context).inflate(R.layout.survey_finish_notice, null) //설문 완료 notice 창

                        //다이얼로그
                        var search_dialog3: AlertDialog? = null //설문 참여 다이얼로그 type A
                        var declar_dialog: AlertDialog? = null //신고 dialog
                        var notice_dialog: AlertDialog? = null //완료 알림 dialog

                        //설문창 C뷰
                        var s3_text1 = serveyView3.findViewById(R.id.s3_text_name) as TextView
                        var s3_text2 = serveyView3.findViewById(R.id.s3_text_explain) as TextView
                        var s3_option1 = serveyView3.findViewById(R.id.s3_btn_option1) as Button
                        var s3_option2 = serveyView3.findViewById(R.id.s3_btn_option2) as Button
                        var s3_finish = serveyView3.findViewById(R.id.s3_btn_finish) as Button
                        var s3_good = serveyView3.findViewById(R.id.s3_btn_good) as ImageButton
                        var s3_declar = serveyView3.findViewById(R.id.s3_btn_declar) as ImageButton
                        var s3_close = serveyView3.findViewById(R.id.s3_btn_close) as ImageButton

                        //신고 다이얼로그 뷰
                        var declar_close = declareView.findViewById(R.id.declar_close) as ImageButton
                        var declar_text = declareView.findViewById(R.id.declar_text) as EditText
                        var declar_finish = declareView.findViewById(R.id.declar_finish) as Button

                        //완료 다이얼로그 뷰
                        var notice_coin = noticeView.findViewById(R.id.coin_num) as TextView
                        var notice_close = noticeView.findViewById(R.id.notice_close) as ImageButton //설문 완료 띄운 후 종료
                        var notice_show = noticeView.findViewById(R.id.notice_finish) as Button

                        //다이얼로그 빌더
                        var builder: AlertDialog.Builder = AlertDialog.Builder(activity) //dialog 띄울 builder

                        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT
                        lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT  //커스텀 크기대로 적용되게 함

                        var check: Boolean //좋아요 체크

                        //설문 이름
                        s3_text1.text = "Q." + answerList.servay_title

                        //설문 상세정보
                        if (answerList.servay_explanation == "") //상세 정보 없으면
                        {
                            s3_text2.visibility = View.GONE //안보이게

                        } else {
                            s3_text2.text = answerList.servay_explanation
                        }

                        s3_option1.text = answerList.servay_q1
                        s3_option2.text = answerList.servay_q2

                        s3_option1.setOnClickListener {
                            s3_option1.setBackgroundResource(R.drawable.survey_ctype_circle3)
                            s3_option2.setBackgroundResource(R.drawable.survey_ctype_circle2)

                            select_choice1 = 1
                            select_choice2 = 0
                        }

                        s3_option2.setOnClickListener {
                            s3_option1.setBackgroundResource(R.drawable.survey_ctype_circle1)
                            s3_option2.setBackgroundResource(R.drawable.survey_ctype_circle4)

                            select_choice1 = 0
                            select_choice2 = 1
                        }

                        //초기 좋아요 0/1
                        if (answerList.is_like == 0) { //좋아요 한적X
                            s3_good.setBackgroundResource(R.drawable.survey_atype_heart) //기본 하트
                            check = false
                        } else {
                            s3_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                            check = true
                        }

                        s3_good.setOnClickListener {
                            if (!check!!) {
                                s3_good.setBackgroundResource(R.drawable.survey_atype_heart_fill)
                                check = true
                                surveyLike(user_id, servay_id)
                            } else {
                                s3_good.setBackgroundResource(R.drawable.survey_atype_heart)
                                check = false
                                surveyLike(user_id, servay_id)
                            }
                        }

                        /*  //신고 버튼 누르면
                          s3_declar.setOnClickListener {
                              declar_dialog = builder.create()
                              declar_dialog!!.setView(declareView)
                              declar_dialog!!.show()


                          }


                          declar_finish.setOnClickListener {
                              var surveyDeclarData = SurveyDeclarData()
                              surveyDeclarData.servay_id = answerList.servay_id
                              surveyDeclarData.alert_alert_content = declar_text.text.toString()
                              surveyDeclare(user_id, surveyDeclarData)
                          }

                          declar_close.setOnClickListener {
                              declar_dialog!!.dismiss()
                          }
  */

                        //다이얼로그 X버튼
                        s3_close.setOnClickListener {
                            search_dialog3!!.dismiss()
                            searchSurveyNetwork("")
                        }

                        //완료 버튼
                        s3_finish.setOnClickListener {
                            var surveyFinishData = SurveyFinishData()
                            surveyFinishData.servay_id = answerList.servay_id
                            surveyFinishData.selection_start_select_time = answerList.selection_start_select_time
                            surveyFinishData.selection_choice1 = select_choice1
                            surveyFinishData.selection_choice2 = select_choice2
                            surveyFinish(user_id, surveyFinishData) //완료 버튼 누를 때 서버랑 통신
                            search_dialog3!!.dismiss()
                            notice_dialog = builder.create()
                            notice_dialog!!.setView(noticeView)
                            notice_dialog!!.show()

                            notice_coin.text = coin.toString() //코인 세팅

                            notice_close.setOnClickListener {
                                notice_dialog!!.dismiss()
                            }

                        }

                        search_dialog3 = builder.create()
                        search_dialog3!!.setView(serveyView3)
                        search_dialog3!!.show()

                        Toast.makeText(context, "C타입", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.v("answersurvey", "fail")
                }
            }

            override fun onFailure(call: Call<SurveyAnswerResult>?, t: Throwable?) {
                ApplicationController.instance!!.makeToast("네트워크가 불안정합니다.")
            }
        })
    }





}