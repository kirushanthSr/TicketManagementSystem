package Model;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final int maxCapacity;
    private final Queue<Ticket> ticketQueue;
    private int nextTicketId = 1;
    private int remainingTickets;
    private final String eventName;
    private final BigDecimal ticketPrice;

    public TicketPool(int maxCapacity, int totalTickets, String eventName, BigDecimal ticketPrice) {
        this.maxCapacity = maxCapacity;
        this.ticketQueue = new LinkedList<>();
        this.remainingTickets = totalTickets;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
    }

    // Method to add tickets to the pool
    public synchronized void addTicket(int vendorId) {
        while (ticketQueue.size() >= maxCapacity || remainingTickets <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        Ticket ticket = new Ticket(nextTicketId++, eventName, ticketPrice, vendorId);
        remainingTickets--;
        ticketQueue.add(ticket);
        System.out.println("Vendor " + vendorId + " released Ticket ID " + ticket.getTicketId());
        notifyAll();
    }

    // Method to buy a ticket from the pool
    public synchronized Ticket purchaseTicket() {
        while (ticketQueue.isEmpty() && remainingTickets > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (ticketQueue.isEmpty() && remainingTickets <= 0) {
            return null; // No tickets available
        }
        Ticket ticket = ticketQueue.poll();
        notifyAll();
        return ticket;
    }

    // Check if tickets are available in the pool
    public synchronized boolean hasTicketsLeft() {
        return remainingTickets > 0 || !ticketQueue.isEmpty();
    }

    // Get the remaining tickets in the pool
    public synchronized int getRemainingTickets() {
        return remainingTickets;
    }

    // Get the current number of tickets in the pool
    public synchronized int getCurrentPoolSize() {
        return ticketQueue.size();
    }
}
