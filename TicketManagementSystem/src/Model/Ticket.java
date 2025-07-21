package Model;

import java.math.BigDecimal;

public class Ticket {
    private final int ticketId;
    private final String eventName;
    private final BigDecimal ticketPrice;
    private final int vendorId;

    public Ticket(int ticketId, String eventName, BigDecimal ticketPrice, int vendorId) {
        this.ticketId = ticketId;
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
        this.vendorId = vendorId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getVendorId() {
        return vendorId;
    }
}

