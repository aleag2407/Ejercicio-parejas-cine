package service
import com.opencsv.CSVReader
import domain.*
import exceptions.EntradaNoDisponibleException
import repository.CinemaRepository
import java.io.File
import java.io.FileReader

import java.time.LocalDate

class CinemaServiceImpl(private val repository: CinemaRepository) : CinemaService {

    override fun comprarEntrada(asientoId: String, clienteId: String, complementos: List<Complement>) {
        val asiento = repository.getSeatById(asientoId)
        if (asiento.status == SeatStatus.ACTIVE) {
            asiento.status = SeatStatus.OCCUPIED
            repository.updateSeat(asiento)

            var precioTotal = if (asiento.type == SeatType.VIP) 8.0 else 5.0
            for (complemento in complementos) {
                precioTotal += complemento.price
            }

            val ticket = Ticket(seat = asientoId, purchaseDate = LocalDate.now().toString(), complements = complementos, customerId = clienteId, totalPrice = precioTotal)
            repository.addTicket(ticket)
        } else {
            throw EntradaNoDisponibleException("Lo sentimos, el asiento no está disponible para la compra.")
        }
    }

    override fun devolverEntrada(entradaId: String) {
        val ticket = repository.getTicketById(entradaId)
        repository.removeTicket(ticket)
        val asiento = repository.getSeatById(ticket.id)
        asiento.status = SeatStatus.ACTIVE
        repository.updateSeat(asiento)
    }

    override fun mostrarEstadoCine(): List<Seat> {
        return repository.getSeats()
    }

    override fun obtenerRecaudacionPorFecha(fecha: String): Double {
        val tickets = repository.getTicketsByDate(fecha)
        return tickets.sumOf { it.totalPrice }
    }

    override fun importarComplementos(rutaArchivo: String) {
        val file = File(rutaArchivo)
        val csvReader = CSVReader(FileReader(file))
        val lines: List<Array<String>> = csvReader.readAll()

        for (line in lines) {
            val nombre = line[0]
            val categoria = ComplementType.valueOf(line[1])
            val precio = line[2].toDouble()

            val complemento = Complement(name = nombre, type = categoria, price = precio)
            repository.addComplement(complemento)
        }
        csvReader.close()
    }

    override fun exportarEstadoCine(fecha: String, rutaArchivo: String) {
    }

    override fun configurarButacas(rutaArchivo: String) {
    }

    override fun actualizarButaca(idButaca: String, estado: SeatStatus, tipo: SeatType) {
        val asiento = repository.getSeatById(idButaca)
        asiento.status = estado
        asiento.type = tipo
        repository.updateSeat(asiento)
    }

    override fun getButacaById(idButaca: String): Seat {
        return repository.getSeatById(idButaca)
    }

    override fun getComplementosByCategoria(categoria: ComplementType): List<Complement> {
        return repository.getComplementsByCategory(categoria)
    }

    override fun getVentaById(idVenta: String): Ticket {
        return repository.getTicketById(idVenta)
    }

    override fun addVenta(ticket: Ticket) {
        repository.addTicket(ticket)
    }

    override fun removeVenta(ticketId: String) {
        val ticket = repository.getTicketById(ticketId)
        repository.removeTicket(ticket)
    }

}