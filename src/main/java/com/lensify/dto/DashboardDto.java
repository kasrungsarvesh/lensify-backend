package com.lensify.dto;

import java.math.BigDecimal;

public class DashboardDto {

    private Long totalCustomers;

    private Long totalUsers;

    private Long totalOrders;

    private Long totalBills;

    private BigDecimal totalRevenue;

    private Long pendingOrders;

    private Long lowStockLenses;

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(Long totalBills) {
        this.totalBills = totalBills;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Long getLowStockLenses() {
        return lowStockLenses;
    }

    public void setLowStockLenses(Long lowStockLenses) {
        this.lowStockLenses = lowStockLenses;
    }

}
