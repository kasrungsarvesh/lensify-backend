package com.lensify.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lensify.dto.DashboardDto;
import com.lensify.repository.BillRepository;
import com.lensify.repository.CustomerRepository;
import com.lensify.repository.LensRepository;
import com.lensify.repository.OrderRepository;
import com.lensify.repository.UserRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final BillRepository billRepository;
    private final LensRepository lensRepository;

    public DashboardServiceImpl(CustomerRepository customerRepository,
                                UserRepository userRepository,
                                OrderRepository orderRepository,
                                BillRepository billRepository,
                                LensRepository lensRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.billRepository = billRepository;
        this.lensRepository = lensRepository;
    }

    @Override
    public ApiResponse<DashboardDto> getDashboard() {
        log.info("Fetching dashboard metrics");

        DashboardDto dashboard = new DashboardDto();

        // Total customers
        dashboard.setTotalCustomers(customerRepository.count());

        // Total users
        dashboard.setTotalUsers(userRepository.count());

        // Total orders
        dashboard.setTotalOrders(orderRepository.count());

        // Total bills
        dashboard.setTotalBills(billRepository.count());

        // Total revenue
        BigDecimal revenue = billRepository.getTotalRevenue();
        dashboard.setTotalRevenue(revenue == null ? BigDecimal.ZERO : revenue);

        // Pending orders
        dashboard.setPendingOrders(orderRepository.countByStatus("PENDING"));

        // Low stock lenses (stock < 10)
        dashboard.setLowStockLenses(lensRepository.countByStockLessThan(10));

        return new ApiResponse<>(true, "Dashboard data fetched successfully.", dashboard);
    }

}
