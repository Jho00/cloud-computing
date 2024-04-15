
import kotlinx.serialization.Serializable

@Serializable
data class FIODto(
    var firstName: String,
    var lastName: String,
    var patronymic: String?
)