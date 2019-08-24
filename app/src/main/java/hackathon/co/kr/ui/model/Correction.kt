package hackathon.co.kr.ui.model

data class Correction(
        var id: Int,                // id
        var imageURL: String,       // 이미지 URL
        var grade: Float,           // 점수
        var gradeTitle: String,     // Competent
        var title: String,          // IELTS Writing Sample #189
        var description: String,    // Task2 - boys and gi…
        var tags: ArrayList<String> // discussion, education
)

fun getDefaultCorrection(): Correction {
    return Correction(
            id = 0,
            imageURL = "http://postfiles11.naver.net/MjAxODA0MDZfMzkg/MDAxNTIzMDA0ODg3NTgz.NorIQYUOdvPxiLAE5bHvhcKmJGKBaCfGVSEdR_y11Igg.-U-knrwYaHLEUQkNpslBPW19Lulld2gEX_KBIK7M6oYg.JPEG.prule1064/%EC%97%B0%EC%84%B815%EB%AA%A8%EC%9D%98%EC%9D%B8%EB%AC%B8_1%EB%B2%88_%EC%B2%A8%EC%82%AD%EC%A7%80.jpeg.jpeg?type=w966",
            grade = 6.5f,
            gradeTitle = "Competent",
            title = "IELTS Writing Sample #189",
            description = "Task2 - boys and gi…",
            tags = arrayListOf("discussion", "education")
    )
}

fun getDefaultCorrections() : ArrayList<Correction> {
    return arrayListOf(
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection(),
            getDefaultCorrection()
    )
}