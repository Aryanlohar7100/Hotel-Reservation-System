package com.stayease.dto.response;

import java.math.BigDecimal;

public class AdminStatsResponse {
    private long totalHotels;
    private long totalRooms;
    private long totalBookings;
    private long totalCustomers;
    private BigDecimal totalRevenue;
    private long activeOccupancies;
    private double occupancyRatePercentage;

    public AdminStatsResponse() {
    }

    public AdminStatsResponse(long totalHotels, long totalRooms, long totalBookings, long totalCustomers,
                              BigDecimal totalRevenue, long activeOccupancies, double occupancyRatePercentage) {
        this.totalHotels = totalHotels;
        this.totalRooms = totalRooms;
        this.totalBookings = totalBookings;
        this.totalCustomers = totalCustomers;
        this.totalRevenue = totalRevenue;
        this.activeOccupancies = activeOccupancies;
        this.occupancyRatePercentage = occupancyRatePercentage;
    }

    public long getTotalHotels() {
        return totalHotels;
    }

    public void setTotalHotels(long totalHotels) {
        this.totalHotels = totalHotels;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getActiveOccupancies() {
        return activeOccupancies;
    }

    public void setActiveOccupancies(long activeOccupancies) {
        this.activeOccupancies = activeOccupancies;
    }

    public double getOccupancyRatePercentage() {
        return occupancyRatePercentage;
    }

    public void setOccupancyRatePercentage(double occupancyRatePercentage) {
        this.occupancyRatePercentage = occupancyRatePercentage;
    }
}
