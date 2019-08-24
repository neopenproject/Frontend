package hackathon.co.kr.util.network.model

import hackathon.co.kr.ui.model.Problem
import java.io.Serializable

data class Posts(
        var id: Int,                // "" : 1,
        var max_time: Int,          // "": 2400,
        var title: String,          // "": ""isuure algorithm #180"",
        var sub_title: String,      // "": ""Task sub_title"",
        var subject: String,        // "": ""education"",
        var category: String,       // "": ""cause/solution"",
        var problem_img: String,    // "": ""images/problems/2019-08-25/156666646894836701.png"",
        var view: Int,              // "": 0,
        var author: String,         // "": ""TEA1566658976154392"",
        var affiliation: Int,       // "": 1,
        var created_at: Long,       // "": 15666664689518080,
        var updated_at: Long        // "": 15666664689518080
)

fun Posts.refineToProblem(): Problem {
    return Problem(
            id = this.id,
            title = this.title,
            description = this.sub_title,
            number = this.affiliation,
            tags = arrayListOf(this.subject, this.category)
    )
}