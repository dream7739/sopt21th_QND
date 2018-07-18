package org.sopt21.qnd.Network

import com.soyeon.qnd_search2.data.SearchSurveyData
import com.soyeon.qnd_search2.data.SearchSurveyResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sopt21.qnd.Data.*
import retrofit2.Call
import retrofit2.http.*



interface NetworkService {
    //email 중복확인 -> 성공
    @POST("user/email/duplication")
    fun checkEmailDuplication(@Body emailDuplicationData: EmailDuplicationData): Call<EmailDuplicationResult>

    //email 인증 번호 받기 -> 성공
    @POST("user/email/auth")
    fun checkEmailAuth(@Body emailAuthData: EmailAuthData): Call<EmailAuthResult>

    //email 인증 번호 확인 -> 성공
    @POST("user/email/auth-acceptance")
    fun checkAuth(@Body checkAuthData: CheckAuthData): Call<CheckAuthResult>

    //회원가입 완료 -> 성공 // img가 null일때 처리 어떻게 할 것인지?
    @Multipart
    @POST("user/join")
    fun joinUser(
            @Part img: MultipartBody.Part?,
            @Part("user_type") user_type: RequestBody,
            @Part("user_email") user_email: RequestBody,
            @Part("user_age") user_age: RequestBody,
            @Part("user_gender") user_gender: RequestBody,
            @Part("user_marriage") user_marriage: RequestBody,
            @Part("user_job") user_job: RequestBody,
            @Part("user_city") user_city: RequestBody,
            @Part("user_pwd") user_pwd: RequestBody): Call<RegisterUserAddResult>
    //            @Part("data") data : RequestBody):

    //로그인 -> 성공
    @POST("user/login")
    fun login(@Body loginData: LoginData): Call<LoginResult>

    //참여한 설문 -> 성공
    @GET("user/mypage/servays/participation")
    fun getMySurveyList(@Header("token") token: String?): Call<GetMySurveyListResult>

    //내가 만든 설문 -> 성공
    @GET("user/mypage/servays/creation")
    fun getMakedSurvey(@Header("token") token: String?): Call<GetMySurveyListResult>

    //내가 구매한 설문 -> 성공
    @GET("user/mypage/servays/purchase")
    fun getPurchaseSurvey(@Header("token") token: String?): Call<GetMySurveyListResult>

    //신고 내역 보기 -> 성공
    @GET("user/mypage/servays/report")
    fun getReportSurvey(@Header("token") token: String?): Call<GetMySurveyListResult>

    //개인정보 변경 -> 성공
    @POST("user/mypage/profile")
    fun changeUserInfo(@Header("token") token: String?, @Body changeUserInfoData: ChangeUserInfoData): Call<ChangeUserInfoResult>

    //비밀번호 변경 -> 성공
    @POST("user/mypage/password")
    fun changeUserPwd(@Header("token") token: String?, @Body changeUserPwdData: ChangeUserPwdData): Call<ChangeUserPwdResult>

    //프로필 정보 불러오기 -> 성공 01.11
    @GET("user/mypage/profile")
    fun getMyProfile(@Header("token") token: String?): Call<MyProfileInfo>

    //설문 등록 -> 성공 01.11
    @Multipart
    @POST("servay/registration")
    fun makeSurvey(
            @Header("token") token: String?,
            @Part image1: ArrayList<MultipartBody.Part>?,
            @Part("servay_servay_type") servay_servay_type: RequestBody,
            @Part("servay_title") servay_title: RequestBody,
            @Part("servay_valid_period") servay_valid_period: RequestBody,
            @Part("servay_goal") servay_goal: RequestBody,
            @Part("servay_anonymous") servay_anonymous: RequestBody,
            @Part("servay_start_age") servay_start_age: RequestBody,
            @Part("servay_end_age") servay_end_age: RequestBody,
            @Part("servay_tag1") servay_tag1: RequestBody,
            @Part("servay_tag2") servay_tag2: RequestBody,
            @Part("servay_tag3") servay_tag3: RequestBody,
            @Part("servay_explanation") servay_explanation: RequestBody,
            @Part("servay_gender") servay_gender: RequestBody,
            @Part("servay_marriage") servay_marriage: RequestBody,
            @Part("servay_option_count") servay_option_count: RequestBody,
            @Part("servay_tag_count") servay_tag_count: RequestBody,
            @Part("servay_q1") servay_q1: RequestBody?,
            @Part("servay_q2") servay_q2: RequestBody?,
            @Part("servay_q3") servay_q3: RequestBody?,
            @Part("servay_q4") servay_q4: RequestBody?,
            @Part("servay_q_count") servay_q_count: RequestBody?,
            @Part("servay_duple") servay_duple: RequestBody?,
            @Part("servay_a_txt") servay_a_txt: RequestBody?,
            @Part("servay_b_txt") servay_b_txt: RequestBody?
    ): Call<MakeSurveyResult>

    /**Survey**/

    //첫 설문 보드-->성공
    @GET("servay/list")
    fun getSurveyList(@Header("token")token:String):Call<SurveyResult>


    //설문 답하기-->성공
    @GET("servay/list/{servay_id}")
    fun surveyAnswer(@Header("token")token:String,
                     @Path("servay_id") servay_id:Int):Call<SurveyAnswerResult>


    //좋아요 버튼-->성공
    @GET("servay/like/{servay_id}")
    fun surveyLike(@Header("token")token:String,
                   @Path("servay_id") servay_id: Int):Call<SurveyLikeResult>


    //신고 버튼-->성공
    @POST("servay/report")
    fun surveyDeclare(@Header("token")token:String,
                      @Body surveyDeclarData: SurveyDeclarData):Call<SurveyDeclarResult>

    //설문 참여 완료 버튼-->성공
    @POST("servay/list")
    fun surveyFinish(@Header("token")token:String,
                     @Body  surveyFinishData: SurveyFinishData):Call<SurveyFinishResult>

    //결과 보기 버튼-->성공
    @GET("servay/result/{servay_id}")
    fun surveyDetailRdesult(@Header("token")token:String,
                            @Path("servay_id")servay_id: Int):Call<SurveyDetailResult>


    //댓글 보기 버튼
    @GET("servay/comment/{servay_id}")
    fun getComment(@Path("servay_id")servay_id:Int):Call<CommentGetResult>

    //댓글 달기
    @POST("servay/comment")
    fun writeComment(@Header("token")token:String,
                     @Body commentWriteData: CommentWriteData):Call<CommentWriteResult>


    //검색
    @POST("servay/search")
    fun searchSurvey(@Header("token") token: String?, @Body searchSurveyData: SearchSurveyData): Call<SearchSurveyResult>


}