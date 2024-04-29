package domain

data class Seat(
    val id: String,
    val row: Char,
    val column: Int,
    var status: SeatStatus,
    var type: SeatType,
    var customer: Customer?,
    val complements: MutableList<Complement>
)
enum class SeatStatus { ACTIVE, MAINTENANCE, OUT_OF_SERVICE, OCCUPIED }
enum class SeatType { NORMAL, VIP }