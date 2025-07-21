package Model;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsToProduce;
    private final int releaseRate;
    private final int vendorId;

    public Vendor(TicketPool ticketPool, int ticketsToProduce, int releaseRate, int vendorId) {
        this.ticketPool = ticketPool;
        this.ticketsToProduce = ticketsToProduce;
        this.releaseRate = releaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        int producedTickets = 0;
        while (producedTickets < ticketsToProduce) {
            ticketPool.addTicket(vendorId);
            producedTickets++;
            try {
                Thread.sleep(releaseRate);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
