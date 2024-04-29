package domain

data class Ticket(
    val id: String = "",
    val seat: String,
    val customerId: String,
    val purchaseDate: String,
    val complements: List<Complement>,
    val totalPrice: Double,
)
