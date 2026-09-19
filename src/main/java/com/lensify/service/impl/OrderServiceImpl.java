package com.lensify.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lensify.dto.order.OrderItemRequestDto;
import com.lensify.dto.order.OrderItemResponseDto;
import com.lensify.dto.order.OrderRequestDto;
import com.lensify.dto.order.OrderResponseDto;
import com.lensify.entity.Customer;
import com.lensify.entity.Lens;
import com.lensify.entity.Order;
import com.lensify.entity.OrderItem;
import com.lensify.entity.Product;
import com.lensify.repository.CustomerRepository;
import com.lensify.repository.LensRepository;
import com.lensify.repository.OrderRepository;
import com.lensify.repository.ProductRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final LensRepository lensRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            LensRepository lensRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.lensRepository = lensRepository;
        this.productRepository = productRepository;
    }

    // ============================================================
    // CREATE ORDER
    // ============================================================

    @Override
    @Transactional
    public ApiResponse<OrderResponseDto> createOrder(OrderRequestDto request) {

        log.info(
                "Creating order for customerId={}",
                request.getCustomerId()
        );

        if (request.getCustomerId() == null) {
            throw new RuntimeException("Customer is required");
        }

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        Order order = new Order();

        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());

        order.setStatus(
                request.getStatus() == null
                        ? "PENDING"
                        : request.getStatus()
        );

        order.setItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (request.getItems() != null
                && !request.getItems().isEmpty()) {

            for (OrderItemRequestDto itemRequest : request.getItems()) {

                validateItem(itemRequest);

                OrderItem orderItem = new OrderItem();

                orderItem.setOrder(order);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(itemRequest.getPrice());

                // ------------------------------------------------
                // PRODUCT / FRAME
                // ------------------------------------------------

                if (itemRequest.getProductId() != null) {

                    Product product = productRepository
                            .findById(itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product not found"
                                    ));

                    validateActiveProduct(product);
                    validateProductStock(
                            product,
                            itemRequest.getQuantity()
                    );

                    // Deduct stock only when the order is being created.
                    // Product selection in the frontend does not change stock.
                    product.setStockQuantity(
                            product.getStockQuantity()
                                    - itemRequest.getQuantity()
                    );

                    productRepository.save(product);

                    orderItem.setProduct(product);
                    orderItem.setLens(null);
                }

                // ------------------------------------------------
                // LENS
                // ------------------------------------------------

                if (itemRequest.getLensId() != null) {

                    Lens lens = lensRepository
                            .findById(itemRequest.getLensId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Lens not found"
                                    ));

                    validateActiveLens(lens);
                    validateLensStock(
                            lens,
                            itemRequest.getQuantity()
                    );

                    // Deduct lens stock only when the order is created.
                    // Selecting a lens in the frontend does not change stock.
                    lens.setStock(
                            lens.getStock() - itemRequest.getQuantity()
                    );

                    lensRepository.save(lens);

                    orderItem.setLens(lens);
                    orderItem.setProduct(null);
                }

                BigDecimal itemTotal =
                        itemRequest.getPrice()
                                .multiply(
                                        BigDecimal.valueOf(
                                                itemRequest.getQuantity()
                                        )
                                );

                totalAmount =
                        totalAmount.add(itemTotal);

                order.getItems().add(orderItem);
            }
        }

        if (order.getItems().isEmpty()) {
            throw new RuntimeException(
                    "Order must contain at least one item"
            );
        }

        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        log.info(
                "Order created successfully. orderId={}, total={}",
                order.getOrderId(),
                totalAmount
        );

        return new ApiResponse<>(
                true,
                "Order created successfully.",
                toDto(order)
        );
    }

    // ============================================================
    // GET ALL ORDERS
    // ============================================================

    @Override
    public ApiResponse<List<OrderResponseDto>> getAllOrders(
            Pageable pageable,
            String search) {

        log.info(
                "Fetching orders. page={}, size={}, search={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                search
        );

        Page<Order> page;

        if (search != null && !search.isBlank()) {

            page = orderRepository
                    .findByCustomerCustomerNameContainingIgnoreCase(
                            search,
                            pageable
                    );

        } else {

            page = orderRepository.findAll(pageable);
        }

        List<OrderResponseDto> orders =
                page.getContent()
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Orders fetched successfully.",
                orders
        );
    }

    // ============================================================
    // GET ORDER BY ID
    // ============================================================

    @Override
    public ApiResponse<OrderResponseDto> getOrderById(Long id) {

        Order order = orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        return new ApiResponse<>(
                true,
                "Order fetched successfully.",
                toDto(order)
        );
    }

    // ============================================================
    // UPDATE ORDER
    // ============================================================

    @Override
    @Transactional
    public ApiResponse<OrderResponseDto> updateOrder(
            Long id,
            OrderRequestDto request) {

        log.info(
                "Updating order. orderId={}",
                id
        );

        Order order = orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        // --------------------------------------------------------
        // UPDATE CUSTOMER
        // --------------------------------------------------------

        if (request.getCustomerId() != null) {

            Customer customer = customerRepository
                    .findById(request.getCustomerId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Customer not found"
                            ));

            order.setCustomer(customer);
        }

        // --------------------------------------------------------
        // UPDATE STATUS
        // --------------------------------------------------------

        if (request.getStatus() != null) {

            order.setStatus(
                    request.getStatus()
            );
        }

        // --------------------------------------------------------
        // VALIDATE ITEMS
        // --------------------------------------------------------

        if (request.getItems() == null
                || request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Order must contain at least one item"
            );
        }

        // --------------------------------------------------------
        // RESTORE STOCK FROM THE EXISTING ORDER
        // --------------------------------------------------------
        //
        // Example:
        // Original receipt quantity = 2
        // Stock after original sale = 10
        //
        // Before applying the edited receipt, return the original
        // quantity to stock:
        // 10 + 2 = 12
        //
        // Then the new quantity will be deducted below.
        //
        // Because this method is @Transactional, if anything fails
        // later, the complete update (including stock changes) rolls
        // back.

        for (OrderItem oldItem : order.getItems()) {

            if (oldItem.getProduct() != null) {

                Product oldProduct = oldItem.getProduct();

                Integer currentStock =
                        oldProduct.getStockQuantity() == null
                                ? 0
                                : oldProduct.getStockQuantity();

                oldProduct.setStockQuantity(
                        currentStock + oldItem.getQuantity()
                );

                productRepository.save(oldProduct);
            }

            if (oldItem.getLens() != null) {

                Lens oldLens = oldItem.getLens();

                Integer currentStock =
                        oldLens.getStock() == null
                                ? 0
                                : oldLens.getStock();

                oldLens.setStock(
                        currentStock + oldItem.getQuantity()
                );

                lensRepository.save(oldLens);
            }
        }

        // Remove old order items only after their product stock has
        // been restored.
        order.getItems().clear();

        BigDecimal totalAmount = BigDecimal.ZERO;

        // --------------------------------------------------------
        // ADD UPDATED ITEMS + DEDUCT NEW PRODUCT STOCK
        // --------------------------------------------------------

        for (OrderItemRequestDto itemRequest
                : request.getItems()) {

            validateItem(itemRequest);

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setQuantity(
                    itemRequest.getQuantity()
            );
            orderItem.setPrice(
                    itemRequest.getPrice()
            );

            // ----------------------------------------------------
            // PRODUCT / FRAME
            // ----------------------------------------------------

            if (itemRequest.getProductId() != null) {

                Product product = productRepository
                        .findById(
                                itemRequest.getProductId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

                validateActiveProduct(product);

                // Check stock after restoring the old order stock.
                validateProductStock(
                        product,
                        itemRequest.getQuantity()
                );

                // Deduct the NEW quantity.
                product.setStockQuantity(
                        product.getStockQuantity()
                                - itemRequest.getQuantity()
                );

                productRepository.save(product);

                orderItem.setProduct(product);
                orderItem.setLens(null);
            }

            // ----------------------------------------------------
            // LENS
            // ----------------------------------------------------

            if (itemRequest.getLensId() != null) {

                Lens lens = lensRepository
                        .findById(
                                itemRequest.getLensId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lens not found"
                                ));

                validateActiveLens(lens);
                validateLensStock(
                        lens,
                        itemRequest.getQuantity()
                );

                // Deduct the NEW lens quantity after the old order
                // quantities have already been restored above.
                lens.setStock(
                        lens.getStock() - itemRequest.getQuantity()
                );

                lensRepository.save(lens);

                orderItem.setLens(lens);
                orderItem.setProduct(null);
            }

            BigDecimal itemTotal =
                    itemRequest.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            totalAmount =
                    totalAmount.add(itemTotal);

            order.getItems().add(orderItem);
        }

        // --------------------------------------------------------
        // UPDATE TOTAL
        // --------------------------------------------------------

        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        log.info(
                "Order updated successfully. orderId={}, total={}",
                id,
                totalAmount
        );

        return new ApiResponse<>(
                true,
                "Order updated successfully.",
                toDto(order)
        );
    }

    // ============================================================
    // DELETE ORDER
    // ============================================================

    @Override
    @Transactional
    public ApiResponse<String> deleteOrder(Long id) {

        Order order = orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        orderRepository.delete(order);

        return new ApiResponse<>(
                true,
                "Order deleted successfully.",
                null
        );
    }

    // ============================================================
    // VALIDATE ORDER ITEM
    // ============================================================

    private void validateItem(
            OrderItemRequestDto itemRequest) {

        // Neither product nor lens
        if (itemRequest.getProductId() == null
                && itemRequest.getLensId() == null) {

            throw new RuntimeException(
                    "Either productId or lensId is required"
            );
        }

        // Both product and lens
        if (itemRequest.getProductId() != null
                && itemRequest.getLensId() != null) {

            throw new RuntimeException(
                    "An order item cannot contain both productId and lensId"
            );
        }

        // Quantity
        if (itemRequest.getQuantity() == null
                || itemRequest.getQuantity() < 1) {

            throw new RuntimeException(
                    "Quantity must be at least 1"
            );
        }

        // Price
        if (itemRequest.getPrice() == null
                || itemRequest.getPrice()
                        .compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Price cannot be negative"
            );
        }
    }

    // ============================================================
    // VALIDATE ACTIVE PRODUCT
    // ============================================================

    private void validateActiveProduct(Product product) {

        if (product.getStatus() == null
                || !"Active".equalsIgnoreCase(product.getStatus())) {

            throw new RuntimeException(
                    "Selected product is inactive and cannot be added to receipt."
            );
        }
    }

    // ============================================================
    // VALIDATE PRODUCT STOCK
    // ============================================================

    private void validateProductStock(
            Product product,
            Integer requestedQuantity) {

        Integer availableStock = product.getStockQuantity();

        if (availableStock == null || availableStock <= 0) {

            throw new RuntimeException(
                    "Product "
                            + product.getProductName()
                            + " is out of stock."
            );
        }

        if (requestedQuantity > availableStock) {

            throw new RuntimeException(
                    "Insufficient stock for "
                            + product.getProductName()
                            + ". Available stock: "
                            + availableStock
            );
        }
    }

    // ============================================================
    // VALIDATE ACTIVE LENS
    // ============================================================

    private void validateActiveLens(Lens lens) {

        if (!Boolean.TRUE.equals(
                lens.getStatus())) {

            throw new RuntimeException(
                    "Selected lens is inactive and cannot be added to receipt."
            );
        }
    }

    // ============================================================
    // VALIDATE LENS STOCK
    // ============================================================

    private void validateLensStock(
            Lens lens,
            Integer requestedQuantity) {

        Integer availableStock = lens.getStock();

        if (availableStock == null || availableStock <= 0) {

            throw new RuntimeException(
                    "Lens "
                            + lens.getBrand()
                            + " is out of stock."
            );
        }

        if (requestedQuantity > availableStock) {

            throw new RuntimeException(
                    "Insufficient stock for lens "
                            + lens.getBrand()
                            + ". Available stock: "
                            + availableStock
            );
        }
    }

    // ============================================================
    // CONVERT ENTITY TO DTO
    // ============================================================

    private OrderResponseDto toDto(Order order) {

        OrderResponseDto dto =
                new OrderResponseDto();

        dto.setOrderId(
                order.getOrderId()
        );

        // --------------------------------------------------------
        // CUSTOMER
        // --------------------------------------------------------

        if (order.getCustomer() != null) {

            dto.setCustomerId(
                    order.getCustomer()
                            .getCustomerId()
            );

            dto.setCustomerName(
                    order.getCustomer()
                            .getCustomerName()
            );
        }

        // --------------------------------------------------------
        // ORDER DETAILS
        // --------------------------------------------------------

        dto.setOrderDate(
                order.getOrderDate()
        );

        dto.setStatus(
                order.getStatus()
        );

        dto.setTotalAmount(
                order.getTotalAmount()
        );

        // --------------------------------------------------------
        // ORDER ITEMS
        // --------------------------------------------------------

        List<OrderItemResponseDto> itemDtos =
                order.getItems()
                        .stream()
                        .map(item -> {

                            OrderItemResponseDto itemDto =
                                    new OrderItemResponseDto();

                            itemDto.setOrderItemId(
                                    item.getOrderItemId()
                            );

                            // ------------------------------------
                            // PRODUCT / FRAME
                            // ------------------------------------

                            if (item.getProduct() != null) {

                                itemDto.setProductId(
                                        item.getProduct()
                                                .getProductId()
                                );

                                itemDto.setProductName(
                                        item.getProduct()
                                                .getProductName()
                                );
                            }

                            // ------------------------------------
                            // LENS
                            // ------------------------------------

                            if (item.getLens() != null) {

                                itemDto.setLensId(
                                        item.getLens()
                                                .getLensId()
                                );

                                itemDto.setLensBrand(
                                        item.getLens()
                                                .getBrand()
                                );
                            }

                            itemDto.setQuantity(
                                    item.getQuantity()
                            );

                            itemDto.setPrice(
                                    item.getPrice()
                            );

                            return itemDto;
                        })
                        .collect(Collectors.toList());

        dto.setItems(itemDtos);

        return dto;
    }
}