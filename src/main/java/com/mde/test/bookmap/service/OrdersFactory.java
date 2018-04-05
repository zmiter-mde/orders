package com.mde.test.bookmap.service;

import com.mde.test.bookmap.entity.Order;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderTypeException;
import com.mde.test.bookmap.enums.OrderType;
import com.mde.test.bookmap.enums.QueryType;
import com.mde.test.bookmap.exceptions.UnrecognizedQueryTypeException;

public class OrdersFactory {

    /*
    * Maybe not really object-oriented enough, but again
    * since we don't actually store anything anywhere
    * this is more like a DTO rather than a part of some
    * order-specific hierarchy. Also the task doesn't specify
    * to use fully OOP solution
    * */
    public static Order createOrder(String[] data) throws UnrecognizedOrderTypeException, UnrecognizedQueryTypeException {
        OrderType type = OrderType.fromString(data[0]);
        if (type == null) {
            throw new UnrecognizedOrderTypeException(data[0]);
        }
        switch (type) {
            case ORDER:
                return createOrder(data[1], data[2], data[3], data[4]);
            case CANCELLATION:
                return createCancellation(data[1]);
            case QUERY:
                return createQuery(data);
            default:
                return null;
        }
    }

    private static Order createOrder(String id, String side, String price, String size) {
        return new Order(Integer.parseInt(id), side.charAt(0), Integer.valueOf(price), Integer.valueOf(size));
    }

    private static Order createCancellation(String cancellationId) {
        return new Order(Integer.parseInt(cancellationId));
    }

    private static Order createQuery(String[] data) throws UnrecognizedQueryTypeException {
        QueryType queryType = QueryType.fromString(data[1]);
        if (queryType == null) {
            throw new UnrecognizedQueryTypeException(data[1]);
        }
        return QueryType.SIZE.equals(queryType) ? new Order(queryType, Integer.valueOf(data[2])) : new Order(queryType);
    }

}
