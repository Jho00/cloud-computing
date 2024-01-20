import FIODto
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto (
    val fio: FIODto,
    val recordBook:Long,
    val grades:List<Int> = mutableListOf()
)


