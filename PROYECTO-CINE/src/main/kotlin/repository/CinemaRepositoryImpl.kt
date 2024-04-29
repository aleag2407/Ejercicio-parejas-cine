package repository

import domain.*
import exceptions.TicketNotFoundException
import util.DatabaseManager
import java.sql.SQLException

class CinemaRepositoryImpl(private val databaseManager: DatabaseManager) : CinemaRepository(databaseManager) {
    override fun getSeats(): List<Seat> {
        val seats = mutableListOf<Seat>()
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("SELECT * FROM seats").use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    val seat = Seat(
                        id = resultSet.getString("id"),
                        row = resultSet.getString("row")[0],
                        column = resultSet.getInt("column"),
                        status = SeatStatus.valueOf(resultSet.getString("status")),
                        type = SeatType.valueOf(resultSet.getString("type")),
                        customer = null,
                        complements = mutableListOf()
                    )
                    seats.add(seat)
                }
            }
        }
        return seats
    }

    override fun updateSeat(seat: Seat) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("UPDATE seats SET status = ?, type = ? WHERE id = ?").use { statement ->
                statement.setString(1, seat.status.name)
                statement.setString(2, seat.type.name)
                statement.setString(3, seat.id)
                statement.executeUpdate()
            }
        }
    }

    override fun addSeat(seat: Seat) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("INSERT INTO seats (id, row, column, status, type) VALUES (?, ?, ?, ?, ?)")
                .use { statement ->
                    statement.setString(1, seat.id)
                    statement.setString(2, seat.row.toString())
                    statement.setInt(3, seat.column)
                    statement.setString(4, seat.status.name)
                    statement.setString(5, seat.type.name)
                    statement.executeUpdate()
                }
        }
    }

    override fun removeSeat(seatId: String) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("DELETE FROM seats WHERE id = ?").use { statement ->
                statement.setString(1, seatId)
                statement.executeUpdate()
            }
        }
    }

    override fun getAllCustomers(): List<Customer> {
        val customers = mutableListOf<Customer>()
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("SELECT * FROM customers").use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    val customer = Customer(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        phoneNumber = resultSet.getString("phoneNumber")
                    )
                    customers.add(customer)
                }
            }
        }
        return customers
    }

    override fun getCustomerById(customerId: String): Customer? {
        var customer: Customer? = null
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("SELECT * FROM customers WHERE id = ?").use { statement ->
                statement.setString(1, customerId)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    customer = Customer(
                        id = resultSet.getString("id"),
                        name = resultSet.getString("name"),
                        phoneNumber = resultSet.getString("phoneNumber")
                    )
                }
            }
        }
        return customer
    }

    override fun addCustomer(customer: Customer) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("INSERT INTO customers (id, name, phoneNumber) VALUES (?, ?, ?)")
                .use { statement ->
                    statement.setString(1, customer.id)
                    statement.setString(2, customer.name)
                    statement.setString(3, customer.phoneNumber)
                    statement.executeUpdate()
                }
        }
    }

    override fun removeCustomer(customerId: String) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("DELETE FROM customers WHERE id = ?").use { statement ->
                statement.setString(1, customerId)
                statement.executeUpdate()
            }
        }
    }

    override fun getTicketsByDate(date: String): List<Ticket> {
        val tickets = mutableListOf<Ticket>()
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("SELECT * FROM tickets WHERE purchaseDate = ?").use { statement ->
                statement.setString(1, date)
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    val ticket = Ticket(
                        seat = getSeatById(resultSet.getString("seatId")).toString(),
                        customerId = getCustomerById(resultSet.getString("customerId"))!!.toString(),
                        purchaseDate = resultSet.getString("purchaseDate"),
                        complements = emptyList(),
                        totalPrice = getPrice("totalPrice")
                    )
                    tickets.add(ticket)
                }
            }
        }
        return tickets
    }

    override fun addTicket(ticket: Ticket) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("INSERT INTO tickets (id, seatId, customerId, purchaseDate) VALUES (?, ?, ?, ?)")
                .use { statement ->
                    statement.setString(1, ticket.id)
                    statement.setString(2, ticket.seat)
                    statement.setString(3, ticket.customerId)
                    statement.setString(4, ticket.purchaseDate)
                    statement.executeUpdate()
                }
        }
    }

    override fun removeTicket(ticket: Ticket) {
        databaseManager.getConnection()?.use { connection ->
            connection.prepareStatement("DELETE FROM tickets WHERE id = ?").use { statement ->
                statement.setString(1, ticket.id)
                statement.executeUpdate()
            }
        }
    }

    override fun getSeatById(seatId: String): Seat {
        val connection = databaseManager.getConnection() ?: throw SQLException("Database connection error")
        connection.prepareStatement("SELECT * FROM seats WHERE id = ?").use { statement ->
            statement.setString(1, seatId)
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return Seat(
                    id = resultSet.getString("id"),
                    row = resultSet.getString("row")[0],
                    column = resultSet.getInt("column"),
                    status = SeatStatus.valueOf(resultSet.getString("status")),
                    type = SeatType.valueOf(resultSet.getString("type")),
                    customer = null,
                    complements = mutableListOf()
                )
            } else {
                throw SQLException("Seat not found")
            }
        }
    }

    override fun getTicketById(entradaId: String): Ticket {
        val tickets: MutableMap<String, Ticket> = mutableMapOf()
        return tickets[entradaId] ?: throw TicketNotFoundException("Ticket with ID $entradaId not found")
    }


    override fun getComplementsByCategory(categoria: Any): List<Complement> {
        val complements: MutableList<Complement> = mutableListOf()
        return complements.filter { it.type == categoria }
    }

    override fun getPrice(ticketId: String): Double {
        val ticket = getTicketById(ticketId)
        return ticket.totalPrice ?: 0.0
    }

    private val seats: MutableMap<String, Seat> = mutableMapOf()
    override fun getAllSeats(): List<Seat> {
        return seats.values.toList()
    }

    override fun addComplement(complemento: Complement) {
        val complements: MutableList<Complement> = mutableListOf()
        complements.add(complemento)
    }
}
