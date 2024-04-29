package domain

data class Complement(
    val name: String,
    val type: ComplementType,
    val price: Double
)
enum class ComplementType { BEVERAGE, FOOD, OTHER }