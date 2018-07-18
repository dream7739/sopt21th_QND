package org.sopt21.qnd.Fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.sopt21.qnd.PicsData
import org.sopt21.qnd.R
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File

class FragmentMakeTypeA : Fragment() {
    internal lateinit var create_btn: Button
    internal lateinit var img_answer1: CircleImageView
    internal lateinit var img_answer2: CircleImageView

    private val PICK_FROM_CAMERA = 0
    private val PICK_FROM_ALBUM = 1
    private val CROP_FROM_IMAGE = 2

    private var photoUri: Uri? = null
    private var absolutePath: String? = null
    private var fileName: String? = null

    internal lateinit var edit_answer1: EditText
    internal lateinit var edit_answer2: EditText
    internal lateinit var answer1: String
    internal lateinit var answer2: String

    internal var click: Boolean? = null
    internal lateinit var fragment_make_add_info: FragmentMakeAddInfo

    private var myImage: MultipartBody.Part? = null
    private var photoFile: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_make_type_a, container, false)

        fragment_make_add_info = FragmentMakeAddInfo()

        img_answer1 = view.findViewById(R.id.frag1_img_answer1) as CircleImageView
        img_answer2 = view.findViewById(R.id.frag1_img_answer2) as CircleImageView
        create_btn = view.findViewById(R.id.frag1_btn_create) as Button

        edit_answer1 = view.findViewById(R.id.frag1_edit_answer1) as EditText
        edit_answer2 = view.findViewById(R.id.frag1_edit_answer2) as EditText

        img_answer1.setOnClickListener {
            doTakeAlbumAction()
            click = true
        }

        img_answer2.setOnClickListener {
            doTakeAlbumAction()
            click = false
        }

        edit_answer1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                answer1 = edit_answer1.text.toString()
                if (answer1.length == 6) {
                    Toast.makeText(activity, "6자 이상 입력하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edit_answer2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                answer2 = edit_answer2.text.toString()
                if (answer2.length == 6) {
                    Toast.makeText(activity, "6자 이상 입력하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        create_btn.setOnClickListener {
            val parentView = view.parent.parent as View
            val edit_title = parentView.findViewById(R.id.make_edit_title) as EditText
            val edit_subtitle = parentView.findViewById(R.id.make_edit_sub) as EditText
            val edit_tag1 = parentView.findViewById(R.id.make_edit_tag1) as EditText
            val edit_tag2 = parentView.findViewById(R.id.make_edit_tag2) as EditText
            val edit_tag3 = parentView.findViewById(R.id.make_edit_tag3) as EditText

            val title = edit_title.text.toString()
            val subtitle = edit_subtitle.text.toString()
            val tag1 = edit_tag1.text.toString()
            val tag2 = edit_tag2.text.toString()
            val tag3 = edit_tag3.text.toString()

            answer1 = edit_answer1.text.toString()
            answer2 = edit_answer2.text.toString()

            if (title.length == 0) {
                edit_title.requestFocus()
                Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (answer1.length == 0 || answer2.length == 0) {
                    Toast.makeText(context, "항목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    if (answer1.length == 0) {
                        edit_answer1.requestFocus()
                    } else if (answer2.length == 0) {
                        edit_answer2.requestFocus()
                    }
                } else {
                    val args = Bundle()
                    args.putString("type", "A")
                    args.putString("title", title)
                    if (subtitle.length == 0) {
                        args.putString("subtitle", "")
                    } else {
                        args.putString("subtitle", subtitle)
                    }
                    if (tag1.length == 0) {
                        args.putString("tag1", "")
                    } else {
                        args.putString("tag1", tag1)
                    }
                    if (tag2.length == 0) {
                        args.putString("tag2", "")
                    } else {
                        args.putString("tag2", tag2)
                    }
                    if (tag3.length == 0) {
                        args.putString("tag3", "")
                    } else {
                        args.putString("tag3", tag3)
                    }
                    args.putString("answer1", answer1)
                    args.putString("answer2", answer2)

                    fragment_make_add_info.arguments = args

                    if(PicsData.servay_a_img == null || PicsData.servay_b_img == null){
                        if(PicsData.servay_a_img == null || PicsData.servay_b_img == null) {
                            Toast.makeText(context, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }else {

                        val fm = activity.supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.add(R.id.make_container, fragment_make_add_info)
                        transaction.commit()
                    }
                }
            }
        }

        return view
    }

    //앨범에서 이미지 가져오기
    fun doTakeAlbumAction() {
        Log.d("albumTest", "doTakeAlbumAction: in method")
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        when (requestCode) {
            PICK_FROM_ALBUM -> {
                run {
                    //이후의 처리가 카메라와 같으므로 break문 달지 않음
                    photoUri = data!!.data
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
                run { if (resultCode != RESULT_OK) return }
                val extras = data!!.extras

                fileName = System.currentTimeMillis().toString()
                val filePath = Environment.getExternalStorageDirectory().absolutePath + "/QnDFile/" + System.currentTimeMillis() + ".jpg"

                if (extras != null) {
                    val photo = extras.getParcelable<Bitmap>("data")

                    photoFile = photo

                    if (click!!) {
                        img_answer1.setImageBitmap(photo)
                        storeCropImage(photo, filePath) //crop된 이미지 저장
                        absolutePath = filePath
                    } else {
                        img_answer2.setImageBitmap(photo)
                        storeCropImage(photo, filePath) //crop된 이미지 저장
                        absolutePath = filePath
                    }
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
        var baos: ByteArrayOutputStream? = null

        try {
            if (click!!) {
                baos = ByteArrayOutputStream()
                photoFile!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                myImage = MultipartBody.Part.createFormData("servay_a_img", fileName, photoBody)

                PicsData.servay_a_img = myImage!!
            } else {
                baos = ByteArrayOutputStream()
                photoFile!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                myImage = MultipartBody.Part.createFormData("servay_b_img", fileName, photoBody)
                PicsData.servay_b_img = myImage!!
            }
//            copyFile.createNewFile()
//            out = BufferedOutputStream(FileOutputStream(copyFile))
            /*baos = ByteArrayOutputStream()
//            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            photoFile!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
            myImage = MultipartBody.Part.createFormData("img", fileName, photoBody)
            Log.v("taehyung","myImage: " + myImage)

            CommonData.img = myImage*/

//            activity.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) try {
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
