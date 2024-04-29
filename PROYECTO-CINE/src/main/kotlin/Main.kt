import domain.Complement
import domain.Seat
import repository.CinemaRepository
import repository.CinemaRepositoryImpl
import service.CinemaService
import service.CinemaServiceImpl
import util.DatabaseManager

fun main() {
    val databaseManager = DatabaseManager("src/main/sqldelight/cinemadb/CinemaDatabase.sq")
    val repository = CinemaRepositoryImpl(databaseManager)
    val service = CinemaServiceImpl(repository)

    while (true) {
        println("Sistema de Gestión de Cine")
        println("1. Comprar Entrada")
        println("2. Devolver Entrada")
        println("3. Estado del Cine")
        println("4. Recaudación por Fecha")
        println("5. Salir")
        println("Elige una opción:")

        when (readLine()?.toIntOrNull() ?: 0) {
            1 -> comprarEntrada(service)
            2 -> devolverEntrada(service)
            3 -> mostrarEstadoCine(service)
            4 -> mostrarRecaudacionPorFecha(service)
            5 -> return
            else -> println("Opción inválida. Por favor, elige una opción válida.")
        }
    }
}

fun comprarEntrada(service: CinemaService) {
    println("Ingresa el ID del asiento:")
    val seatId = readLine() ?: return

    println("Ingresa el ID del cliente:")
    val customerId = readLine() ?: return

    // Puedes implementar más lógica para agregar complementos aquí
    val complementos = emptyList<Complement>()

    try {
        val entrada = service.comprarEntrada(seatId, customerId, complementos)
        println("Entrada comprada con éxito: $entrada")
    } catch (e: Exception) {
        println("Error al comprar la entrada: ${e.message}")
    }
}

fun devolverEntrada(service: CinemaService) {
    println("Ingresa el ID de la entrada:")
    val entradaId = readLine() ?: return

    // Implementa más lógica para devolver la entrada aquí

    println("Entrada devuelta con éxito.")
}

fun mostrarEstadoCine(service: CinemaService) {
    val estadoCine = service.mostrarEstadoCine()
    println("Estado del Cine: $estadoCine")
}

fun mostrarRecaudacionPorFecha(service: CinemaService) {
    println("Ingresa la fecha (AAAA-MM-DD):")
    val fecha = readLine() ?: return

    val recaudacion = service.obtenerRecaudacionPorFecha(fecha)
    println("Recaudación para $fecha: $recaudacion")
}
