package util

import domain.Customer
import domain.Seat
import domain.Ticket
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class DatabaseManager(s: String) {
    private lateinit var connection: Connection

    init {
        // Inicializa la conexión en el constructor
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "username", "password")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private val seats: MutableMap<String, Seat> = mutableMapOf()
    private val customers: MutableMap<String, Customer> = mutableMapOf()
    private val tickets: MutableMap<String, Ticket> = mutableMapOf()

    // Método para obtener todas las butacas de la base de datos
    fun getAllSeatsFromDatabase(): List<Seat> {
        return seats.values.toList()
    }

    // Método para actualizar una butaca en la base de datos
    fun updateSeatInDatabase(seat: Seat) {
        seats[seat.id] = seat
    }

    // Método para agregar una nueva butaca a la base de datos
    fun addSeatToDatabase(seat: Seat) {
        seats[seat.id] = seat
    }

    // Método para eliminar una butaca de la base de datos
    fun removeSeatFromDatabase(seatId: String) {
        seats.remove(seatId)
    }

    // Método para obtener todos los clientes de la base de datos
    fun getAllCustomersFromDatabase(): List<Customer> {
        return customers.values.toList()
    }

    // Método para obtener un cliente por su ID de la base de datos
    fun getCustomerByIdFromDatabase(customerId: String): Customer? {
        return customers[customerId]
    }

    // Método para agregar un nuevo cliente a la base de datos
    fun addCustomerToDatabase(customer: Customer) {
        customers[customer.id] = customer
    }

    // Método para eliminar un cliente de la base de datos
    fun removeCustomerFromDatabase(customerId: String) {
        customers.remove(customerId)
    }

    // Método para obtener los tickets por fecha de la base de datos
    fun getTicketsByDateFromDatabase(date: String): List<Ticket> {
        return tickets.values.filter { it.purchaseDate == date }
    }

    // Método para agregar un nuevo ticket a la base de datos
    fun addTicketToDatabase(ticket: Ticket) {
        tickets[ticket.id] = ticket
    }

    // Método para eliminar un ticket de la base de datos
    fun removeTicketFromDatabase(ticketId: String) {
        tickets.remove(ticketId)
    }

    // Método para obtener la conexión a la base de datos
    fun getConnection(): Connection? {
        return connection
    }

    // Método para cerrar la conexión a la base de datos
    fun close() {
        connection.close()
    }
}
