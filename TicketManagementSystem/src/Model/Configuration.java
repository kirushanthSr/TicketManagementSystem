package Model;

import java.math.BigDecimal;

public class Configuration {
    public String eventName;
    public BigDecimal ticketPrice;
    public int totalTickets;
    public int ticketReleaseRate;
    public int customerRetrievalRate;
    public int maxTicketCapacity;
    public int numberOfVendors;
    public int numberOfCustomers;

    public Configuration(String eventName, BigDecimal ticketPrice, int totalTickets, int ticketReleaseRate,
                         int customerRetrievalRate, int maxTicketCapacity, int numberOfVendors, int numberOfCustomers) {
        this.eventName = eventName;
        this.ticketPrice = ticketPrice;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.numberOfVendors = numberOfVendors;
        this.numberOfCustomers = numberOfCustomers;
    }
}

