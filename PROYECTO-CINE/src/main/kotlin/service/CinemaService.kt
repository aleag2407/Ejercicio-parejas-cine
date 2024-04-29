package service

import domain.*

interface CinemaService {
    fun comprarEntrada(asientoId: String, clienteId: String, complementos: List<Complement>)
    fun devolverEntrada(entradaId: String)
    fun mostrarEstadoCine(): List<Seat>
    fun obtenerRecaudacionPorFecha(fecha: String): Double
    fun importarComplementos(rutaArchivo: String)
    fun exportarEstadoCine(fecha: String, rutaArchivo: String)
    fun configurarButacas(rutaArchivo: String)
    fun actualizarButaca(idButaca: String, estado: SeatStatus, tipo: SeatType)
    fun getButacaById(idButaca: String): Seat
    fun getComplementosByCategoria(categoria: ComplementType): List<Complement>
    fun getVentaById(idVenta: String): Ticket
    fun addVenta(ticket: Ticket)
    fun removeVenta(ticketId: String)
}

