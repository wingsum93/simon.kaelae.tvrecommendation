package simon.kaelae.tvrecommendation

import com.google.firebase.database.IgnoreExtraProperties

// [START comment_class]
@IgnoreExtraProperties
data class Comment(
    var name: String? = "",
    var msg: String? = "",
    var time: Long? = 0
)
// [END comment_class]