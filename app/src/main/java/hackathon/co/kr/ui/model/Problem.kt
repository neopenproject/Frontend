package hackathon.co.kr.ui.model

data class Problem(
        var id: Int,                // id
        var title: String,          // IELTS Writing Sample #189
        var description: String,    // Task2 - boys and gi…
        var number: Int,            // 점수
        var tags: ArrayList<String> // discussion, education
)

fun getDefaultProblem(): Problem {
    return Problem(
            id = 0,
            title = "IELTS Writing Sample #189",
            description = "Task2 - boys and gi…",
            number = 239,
            tags = arrayListOf("discussion", "education")
    )
}

fun getDefaultProblems(): ArrayList<Problem> {
    return arrayListOf(
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem(),
            getDefaultProblem()
    )
}