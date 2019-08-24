package hackathon.co.kr.util

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import hackathon.co.kr.neopen.temp.PenActivity
import java.io.*

fun PenActivity.getImageFile(bitmap: Bitmap) :File? {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 113)
        return null;
    } else {
        // 1. 갤러리 해당 경로에 파일 생성
        val tempFilePath = getExternalFilesDir(null).path + "/" + "${System.currentTimeMillis()}_neopen.jpg"
        val tempFile = File(tempFilePath)

        // 2. 테마색이 반영된 비트맵 생성
//        val bitmapPhoto = BitmapFactory.decodeResource(resources, homeVM.getBiggestEmotionImage(month - 1))
//        val copyBitmap: Bitmap = saveGallery(this, bitmap);bitmapPhoto.copy(Bitmap.Config.ARGB_8888, true)

//        var canvas = Canvas(copyBitmap)
//        canvas.drawColor(homeVM.getSecondEmotionFilter(month - 1))
//        canvas.drawBitmap(copyBitmap, 0F, 0F, null)

        // 3. 만들어진 비트맵을 파일과 매칭
        var newFile = getFile(tempFile, bitmap)

        // 4. 갤러리 경로 존재 확인
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/neopen"
        val file = File(path)

        // Log.d("Directory Pictures 존재? ", File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath).exists().toString())
        if (!file.exists()) {
            file.mkdirs()
            Log.d("갤러리 저장 ", "디렉토리 생성완료")
        }

        val galleryFilePath = "$path/${System.currentTimeMillis()}_neopen.jpg"
        val galleryFile = File(galleryFilePath)
        Log.d("galleryAddPic.newFile", galleryFilePath)

        copyFile(tempFile, galleryFile)

        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$galleryFilePath")))
        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)))

        // 5. 갤러리로 copy 한 후 내장되어있는 파일은 삭제한다.
        tempFile.delete()

        Toast.makeText(this, "성공적으로 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        return galleryFile;
    }
}



fun getFile(file: File, bitmap: Bitmap): File {
    Log.d("FilePath : ", file.absolutePath)
    var fos: FileOutputStream? = null
    try {
        val bos = ByteArrayOutputStream()
        // bitmap = imgRotate(bitmap!!, orientation.toFloat())
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapData = bos.toByteArray()

        fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        fos!!.close()
    }

    return file
}

fun copyFile(pivotFile: File, newFile: File): File {
    var result: Boolean
    if (pivotFile.exists()) {
        try {
            var fis = FileInputStream(pivotFile)
            var newfos = FileOutputStream(newFile)
            var buffer = ByteArray(1024)
            var readcount = fis.read(buffer, 0, 1024)
            while (readcount != -1) {
                newfos.write(buffer, 0, readcount)
                readcount = fis.read(buffer, 0, 1024)
            }
            newfos.close()
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        result = true
    } else {
        result = false
    }
    return newFile
}