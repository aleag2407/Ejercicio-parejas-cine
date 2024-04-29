package repository

import domain.*
import util.DatabaseManager

abstract class CinemaRepository(private val databaseManager: DatabaseManager) {
    abstract fun getSeats(): List<Seat>
    abstract fun updateSeat(seat: Seat)
    abstract fun addSeat(seat: Seat)
    abstract fun removeSeat(seatId: String)
    abstract fun getAllCustomers(): List<Customer>
    abstract fun getCustomerById(customerId: String): Customer?
    abstract fun addCustomer(customer: Customer)
    abstract fun removeCustomer(customerId: String)
    abstract fun getTicketsByDate(date: String): List<Ticket>
    abstract fun addTicket(ticket: Ticket)
    abstract fun removeTicket(ticket: Ticket)
    abstract fun getSeatById(asientoId: String): Seat
    abstract fun getTicketById(entradaId: String): Ticket
    abstract fun getComplementsByCategory(categoria: Any): List<Complement>
    abstract fun getPrice(entradaId: String): Double
    abstract fun getAllSeats(): List<Seat>
    abstract fun addComplement(complemento: Complement)
}