package it.gabrieletondi.telldontaskkata.useCase.approve_order;

public class OrderApprovalRequest {
    private int orderId;
    private boolean approved;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }
}
