package Model;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final int customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        boolean ticketsAvailable = true;
        while (ticketsAvailable) {
            Ticket ticket = ticketPool.purchaseTicket();
            if (ticket != null) {
                System.out.println("Customer " + customerId + " purchased Ticket ID " + ticket.getTicketId() + ".");
            } else {
                ticketsAvailable = ticketPool.hasTicketsLeft();
            }

            if (ticketsAvailable) {
                try {
                    Thread.sleep(retrievalRate);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
