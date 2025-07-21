package Model;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager {
    private final TicketPool ticketPool;
    private final Configuration configuration;
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();

    public ThreadManager(TicketPool ticketPool, Configuration configuration) {
        this.ticketPool = ticketPool;
        this.configuration = configuration;
    }

    public void startVendorThreads() {
        int baseTicketsPerVendor = configuration.totalTickets / configuration.numberOfVendors;
        int extraTickets = configuration.totalTickets % configuration.numberOfVendors;
        int i = 0;

        while (i < configuration.numberOfVendors) {
            int ticketsToProduce = baseTicketsPerVendor + (i < extraTickets ? 1 : 0);
            Vendor vendor = new Vendor(ticketPool, ticketsToProduce, configuration.ticketReleaseRate, i + 1);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start();
            i++;
        }
    }

    public void startCustomerThreads() {
        int i = 0;

        while (i < configuration.numberOfCustomers) {
            Customer customer = new Customer(ticketPool, configuration.customerRetrievalRate, i + 1);
            Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
            customerThreads.add(customerThread);
            customerThread.start();
            i++;
        }
    }

    public void waitForCompletion() {
        int i = 0;
        while (i < vendorThreads.size()) {
            try {
                vendorThreads.get(i).join();
            } catch (InterruptedException ignored) {
            }
            i++;
        }
        System.out.println("All tickets are released.");

        i = 0;
        while (i < customerThreads.size()) {
            try {
                customerThreads.get(i).join();
            } catch (InterruptedException ignored) {
            }
            i++;
        }
    }
}
