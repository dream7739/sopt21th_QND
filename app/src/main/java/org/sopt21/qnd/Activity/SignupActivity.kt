package org.sopt21.qnd.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sopt21.qnd.Application.ApplicationController
import org.sopt21.qnd.CommonData
import org.sopt21.qnd.Data.*
import org.sopt21.qnd.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File

class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private val PICK_FROM_CAMERA = 0
    private val PICK_FROM_ALBUM = 1
    private val CROP_FROM_IMAGE = 2
    private val TRY_DUPLICATION_OK = 1001
    private val TRY_DUPLICATION_NO = 1002
    private val EMAIL_CAN_USE = 2001
    private val EMAIL_CANT_USE = 2002
    private val AUTH_SUCCESS = 3001
    private val AUTH_FAIL = 3002

    private var photoUri: Uri? = null
    private var absolutePath: String? = null
    private var fileName: String? = null

    internal lateinit var edit_email: EditText
    internal lateinit var edit_passwd: EditText
    internal lateinit var edit_cpasswd: EditText
    internal lateinit var edit_number: EditText
    internal lateinit var btn_duplicate_check: RelativeLayout
    internal lateinit var btn_submit: RelativeLayout
    internal lateinit var btn_signup_next: RelativeLayout
    internal lateinit var btn_profile_img: RelativeLayout
    internal lateinit var btn_confirm: RelativeLayout
    internal lateinit var iv_profile: CircleImageView
    internal lateinit var tv_passwd_hint: TextView

    private var tryDuplication = TRY_DUPLICATION_NO
    private var duplicationCheckResult = EMAIL_CANT_USE
    private var authResult = AUTH_FAIL

    private var authNumber: String? = null

    private var myImage : MultipartBody.Part? = null
    private var photoFile : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edit_email = findViewById(R.id.edit_email) as EditText
        edit_passwd = findViewById(R.id.edit_passwd) as EditText
        edit_cpasswd = findViewById(R.id.edit_cpasswd) as EditText
        edit_number = findViewById(R.id.edit_number) as EditText

        tv_passwd_hint = findViewById(R.id.tv_passwd_hint) as TextView

        btn_duplicate_check = findViewById(R.id.btn_duplicate_check) as RelativeLayout
        btn_submit = findViewById(R.id.btn_submit) as RelativeLayout
        btn_signup_next = findViewById(R.id.btn_signup_next) as RelativeLayout
        btn_profile_img = findViewById(R.id.btn_profile_img) as RelativeLayout
        btn_confirm = findViewById(R.id.btn_confirm) as RelativeLayout

        iv_profile = findViewById(R.id.iv_profile) as CircleImageView

        btn_duplicate_check.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        btn_confirm.setOnClickListener(this)
        btn_signup_next.setOnClickListener(this)
        btn_profile_img.setOnClickListener(this)

        edit_passwd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    tv_passwd_hint.visibility = View.VISIBLE
                } else if (s.length > 0) {
                    tv_passwd_hint.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_duplicate_check -> if (edit_email.length() == 0) {
                Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
            } else {
                checkEmailDuplication(edit_email.text.toString())
            }

            R.id.btn_submit -> if (edit_email.length() == 0) {
                Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
                Toast.makeText(applicationContext, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
            } else {
                checkEmailAuth(edit_email.text.toString())
            }
            R.id.btn_confirm -> if (edit_number.length() == 0) {
                Toast.makeText(applicationContext, "인증번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_number.requestFocus()
            } else {
                checkAuth(authNumber, edit_number.text.toString())
                //인증번호 확인 네트워킹
            }

            R.id.btn_signup_next -> if (edit_email.length() == 0) {
                Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
                Toast.makeText(applicationContext, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                edit_email.requestFocus()
            } else if (tryDuplication != TRY_DUPLICATION_OK) {
                Toast.makeText(applicationContext, "이메일 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show()
                btn_duplicate_check.requestFocus()
            } else if (duplicationCheckResult != EMAIL_CAN_USE) {
                edit_email.requestFocus()
            } else if (edit_passwd.length() == 0) {
                Toast.makeText(applicationContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_passwd.requestFocus()
            } else if (edit_passwd.length() < 8) {
                Toast.makeText(applicationContext, "비밀번호를 8자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_passwd.requestFocus()
            } else if (edit_cpasswd.length() == 0) {
                Toast.makeText(applicationContext, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
                edit_cpasswd.requestFocus()
            } else if (edit_passwd.text.toString() != edit_cpasswd.text.toString()) {
                Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                edit_passwd.requestFocus()
            } else if (authResult != AUTH_SUCCESS) {
                Toast.makeText(applicationContext, "이메일 인증을 진행해주세요.", Toast.LENGTH_SHORT).show()
            } else { // 모든 항목 정상 입력시
                val intent = Intent(applicationContext, SignupEtcActivity::class.java)
                intent.putExtra("email", edit_email.text.toString())
                intent.putExtra("passwd", edit_passwd.text.toString())
                if (fileName != null) {
                    intent.putExtra("profile", fileName)
                }

                startActivity(intent)
                finish()
            }

            R.id.btn_profile_img -> doTakeAlbumAction()
        }
        //                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener(){
        //
        //                    @Override
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        doTakePhotoAction();
        //                    }
        //                };
        //                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
        //                    @Override
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        doTakeAlbumAction();
        //                    }
        //                };
        //                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener(){
        //                    @Override
        //                    public void onClick(DialogInterface dialog, int which) {
        //                        dialog.dismiss();
        //                    }
        //                };
        //
        //                new AlertDialog.Builder(SignupActivity.this)
        //                        .setTitle("업로드할 이미지 선택")
        ////                        .setPositiveButton("사진 촬영", cameraListener)
        //                        .setNeutralButton("앨범 선택", albumListener)
        //                        .setNegativeButton("취소", cancelListener)
        //                        .show();
    }

    //카메라에서 사진 촬영
    fun doTakePhotoAction() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileName = "tmp_" + System.currentTimeMillis().toString() + ".jpg"
        //임시로 사용할 파일의 경로 생성
        val url = "tmp_$fileName.jpg"
        photoUri = Uri.fromFile(File(Environment.getExternalStorageDirectory(), url))

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, PICK_FROM_CAMERA)
    }

    //앨범에서 이미지 가져오기
    fun doTakeAlbumAction() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            PICK_FROM_ALBUM -> {
                run {
                    //이후의 처리가 카메라와 같으므로 break문 달지 않음
                    photoUri = data!!.data

                    Log.d("SignupActivityLog", photoUri!!.path.toString())
                }
                run {
                    val intent = Intent("com.android.camera.action.CROP")
                    intent.setDataAndType(photoUri, "image/*")

                    //CROP할 이미지를 200*200 크기로 저장
                    intent.putExtra("outputX", 200)
                    intent.putExtra("outputY", 200)
                    intent.putExtra("aspectX", 1) //crop박스의 x축 비율
                    intent.putExtra("aspectY", 1) //crop박스의 y축 비율
                    intent.putExtra("scale", true)
                    intent.putExtra("return-data", true)
                    startActivityForResult(intent, CROP_FROM_IMAGE)
                }
            }
            PICK_FROM_CAMERA -> {
                val intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(photoUri, "image/*")
                intent.putExtra("outputX", 200)
                intent.putExtra("outputY", 200)
                intent.putExtra("aspectX", 1)
                intent.putExtra("aspectY", 1)
                intent.putExtra("scale", true)
                intent.putExtra("return-data", true)
                startActivityForResult(intent, CROP_FROM_IMAGE)
            }
            CROP_FROM_IMAGE -> {
                run { if (resultCode != Activity.RESULT_OK) return }
                val extras = data!!.extras

                fileName = System.currentTimeMillis().toString()
                val filePath = Environment.getExternalStorageDirectory().absolutePath + "/QnDFile/" + System.currentTimeMillis() + ".jpg"

                if (extras != null) {
                    val photo = extras.getParcelable<Bitmap>("data")

                    photoFile = photo
                    iv_profile.setImageBitmap(photo)

                    storeCropImage(photo, filePath) //crop된 이미지 저장

                    absolutePath = filePath
                }
                //임시파일 삭제
                val f = File(photoUri!!.path)
                if (f.exists()) {
                    f.delete()
                }
            }
        }
    }

    private fun storeCropImage(bitmap: Bitmap?, filePath: String) {
        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/QnDFile"
        val directory_QnDFile = File(dirPath)

        if (!directory_QnDFile.exists()) directory_QnDFile.mkdir()

//        val copyFile = File(filePath)
        var out: BufferedOutputStream? = null
        var baos : ByteArrayOutputStream? = null

        try {
//            copyFile.createNewFile()
//            out = BufferedOutputStream(FileOutputStream(copyFile))
            baos = ByteArrayOutputStream()
//            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            photoFile!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)


            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
            myImage = MultipartBody.Part.createFormData("img", fileName, photoBody)
            Log.v("099999", myImage.toString())
            CommonData.img = myImage

//            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(photoFile)))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) try {
                out.close()
            } catch (e: Exception) {
            }

        }
    }

    //이메일 중복 체크 통신
    private fun checkEmailDuplication(userId: String) {
        val networkService = ApplicationController.instance!!.networkService

        val emailDuplicationData = EmailDuplicationData()
        emailDuplicationData.data.input_email = userId

        val emailDuplication = networkService!!.checkEmailDuplication(emailDuplicationData)
        emailDuplication.enqueue(object : Callback<EmailDuplicationResult> {
            override fun onResponse(call: Call<EmailDuplicationResult>, response: Response<EmailDuplicationResult>) =
                    if (response.isSuccessful) {
                        Log.v("checkDuple", response.body().data!!.response_code.toString())
                        if (response.body().data!!.response_code == 1) { //중복되지 않았을 때
                            duplicationCheckResult = EMAIL_CAN_USE
                            tryDuplication = TRY_DUPLICATION_OK
                            Toast.makeText(this@SignupActivity, "이메일이 사용이 가능합니다.", Toast.LENGTH_SHORT).show()
                        } else{
                            //email이 중복일 때
                            duplicationCheckResult = EMAIL_CANT_USE
                            tryDuplication = TRY_DUPLICATION_OK
                            edit_email.requestFocus()
                            Toast.makeText(this@SignupActivity, "이메일이 중복되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
    //                    //emali이 중복일 때
    //                    duplicationCheckResult = EMAIL_CANT_USE
    //                    tryDuplication = TRY_DUPLICATION_OK
    //                    edit_email.requestFocus()
    //                    Toast.makeText(this@SignupActivity, "이메일이 중복되었습니다.", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@SignupActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }

            override fun onFailure(call: Call<EmailDuplicationResult>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    //인증번호 받기 통신
    private fun checkEmailAuth(userId: String) {
        val networkService = ApplicationController.instance!!.networkService

        val emailAuthData = EmailAuthData()
        emailAuthData.input_user_email = userId
        Log.v("emailAuthData:", emailAuthData.input_user_email)

        val emailAuth = networkService!!.checkEmailAuth(emailAuthData)
        emailAuth.enqueue(object : Callback<EmailAuthResult> {
            override fun onResponse(call: Call<EmailAuthResult>, response: Response<EmailAuthResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") {
                        authNumber = response.body().data!!.authorization_code
                        Toast.makeText(this@SignupActivity, "해당 이메일의 인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        btn_submit.visibility = View.GONE
                        btn_confirm.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(applicationContext, "인증 메일 발송 중 오류가 발생하였습니다.\n다시 회원가입을 진행해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EmailAuthResult>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    private fun checkAuth(successAuthNumber: String?, inputNumber: String) {
        val networkService = ApplicationController.instance!!.networkService

        val checkAuthData = CheckAuthData()
        checkAuthData.data.authorization_code = successAuthNumber
        checkAuthData.data.authorization_code_accept = inputNumber

        val authCheck = networkService!!.checkAuth(checkAuthData)
        authCheck.enqueue(object : Callback<CheckAuthResult> {
            override fun onResponse(call: Call<CheckAuthResult>, response: Response<CheckAuthResult>) {
                if (response.isSuccessful) {
                    if (response.body().status == "Success") { //인증 성공
                        authResult = AUTH_SUCCESS
                        Toast.makeText(this@SignupActivity, "인증 성공", Toast.LENGTH_SHORT).show()

                    } else { //인증 실패
                        authResult = AUTH_FAIL
                        Toast.makeText(applicationContext, "인증번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "인증 메일 발송 중 오류가 발생하였습니다.\n다시 회원가입을 진행해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CheckAuthResult>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onFailure: ")
            }
        })
    }

    companion object {
        private val TAG = "SignupActivityLog"
    }
}
