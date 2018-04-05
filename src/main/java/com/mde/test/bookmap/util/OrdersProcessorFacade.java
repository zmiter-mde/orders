package com.mde.test.bookmap.util;

import com.mde.test.bookmap.entity.Order;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderSideException;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderTypeException;
import com.mde.test.bookmap.exceptions.UnrecognizedQueryTypeException;
import com.mde.test.bookmap.service.OrdersHandler;

import static com.mde.test.bookmap.util.Constants.EMPTY_STRING;
import static com.mde.test.bookmap.util.Constants.PRICE_SIZE_FORMAT;

public class OrdersProcessorFacade {

    private static OrdersHandler ordersHandler = new OrdersHandler();

    public static void processOrder(Order order)
            throws UnrecognizedOrderTypeException, UnrecognizedOrderSideException, UnrecognizedQueryTypeException {
        switch (order.getType()) {
            case ORDER:
                ordersHandler.addOrder(order);
                break;
            case CANCELLATION:
                ordersHandler.cancelOrder(order.getCancellationId());
                break;
            case QUERY:
                processQuery(order, ordersHandler);
                break;
            default:
                throw new UnrecognizedOrderTypeException(order.getType().getOrderTypeString());
        }
    }

    private static void processQuery(Order order, OrdersHandler ordersHandler) throws UnrecognizedQueryTypeException {
        switch (order.getQueryType()) {
            case BUYERS:
                queryBuyers(ordersHandler);
                break;
            case SELLERS:
                querySellers(ordersHandler);
                break;
            case SIZE:
                querySize(order, ordersHandler);
                break;
            default:
                throw new UnrecognizedQueryTypeException(order.getQueryType().getQueryTypeString());
        }
    }

    private static void querySize(Order order, OrdersHandler ordersHandler) {
        int totalSizeAtPrice = ordersHandler.getTotalSizeAtPrice(order.getQueryPrice());
        System.out.println(totalSizeAtPrice);
    }

    private static void querySellers(OrdersHandler ordersHandler) {
        Order lowestSeller = ordersHandler.getLowestSeller();
        if (lowestSeller == null) {
            System.out.println(EMPTY_STRING);
        } else {
            int price = lowestSeller.getPrice(),
                    totalPrice = ordersHandler.getTotalSizeAtPrice(price);
            System.out.println(String.format(PRICE_SIZE_FORMAT, price, totalPrice));
        }
    }

    private static void queryBuyers(OrdersHandler ordersHandler) {
        Order highestBuyer = ordersHandler.getHighestBuyer();
        if (highestBuyer == null) {
            System.out.println(EMPTY_STRING);
        } else {
            int price = highestBuyer.getPrice(),
                    totalPrice = ordersHandler.getTotalSizeAtPrice(price);
            System.out.println(String.format(PRICE_SIZE_FORMAT, price, totalPrice));
        }
    }

}
