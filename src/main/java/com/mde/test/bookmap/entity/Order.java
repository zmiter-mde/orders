package com.mde.test.bookmap.entity;

import com.mde.test.bookmap.enums.OrderType;
import com.mde.test.bookmap.enums.QueryType;

public class Order {

    private OrderType type;

    private int id;
    private char side;
    private int price;
    private int size;

    private int cancellationId; // better store it separately rather that share id variable

    private QueryType queryType;
    private int queryPrice;

    // For a new order
    public Order(int id, char side, int price, int size) {
        this.type = OrderType.ORDER;
        this.id = id;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    // For a new cancellation
    public Order(int cancellationId) {
        this.type = OrderType.CANCELLATION;
        this.cancellationId = cancellationId;
    }

    // For a new query
    public Order(QueryType queryType) {
        this.type = OrderType.QUERY;
        this.queryType = queryType;
    }

    // To make a clear distinction between queries creation
    public Order(QueryType queryType, int queryPrice) {
        this.type = OrderType.QUERY;
        this.queryType = queryType;
        this.queryPrice = queryPrice;
    }

    public OrderType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public char getSide() {
        return side;
    }

    public int getPrice() {
        return price;
    }

    public int getSize() {
        return size;
    }

    public int getCancellationId() {
        return cancellationId;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public int getQueryPrice() {
        return queryPrice;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
