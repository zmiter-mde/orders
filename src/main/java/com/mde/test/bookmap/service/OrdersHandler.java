package com.mde.test.bookmap.service;

import com.mde.test.bookmap.entity.Order;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderSideException;

import static com.mde.test.bookmap.util.Constants.*;

/*
* There's a strong temptation to create a separate data structure for heaps
* and remove this code duplication. But at the moment it takes less than 250
* lines of code and all the logic is here, so it seems like a little bit of an
* overhead pain for such a small task.
*
* */
public class OrdersHandler {

    private int buyersCount;
    private int sellersCount;

    private Order[] buyers = new Order[MAX_ORDERS]; // ~ Order size * MB in memory
    private Order[] sellers = new Order[MAX_ORDERS]; // same here, should be < 25MB each

    // The task says for each query total size is <= 10^9 so int should be enough
    private int[] totalSizeForPrice = new int[MAX_PRICE]; // < 4GB, ask sysadmin to add RAM if necessary :)

    // position of an order in a heap
    // So, to find a place of an order in a heap will take O(1)
    private int[] orderPos = new int[MAX_ORDERS]; // < 4MB in memory
    // To find which heap to use
    private char[] orderSide = new char[MAX_ORDERS]; // < 4MB in memory

    // As we use heaps it should take O(log N) ~ O(log MAX_ORDERS) ~ O(log 10^6) ~ up to O(20) :)
    public void addOrder(Order order) throws UnrecognizedOrderSideException {
        orderSide[order.getId()] = order.getSide();
        switch (order.getSide()) {
            case BUYER_ORDER_SIDE:
                addBuyers(order);
                break;
            case SELLER_ORDER_SIDE:
                addSellers(order);
                break;
            default:
                throw new UnrecognizedOrderSideException(order.getSide());
        }
        finishDeals();
    }

    // O(log N) * deals
    private void finishDeals() {
        while (buyersCount > 0 && sellersCount > 0 &&
               getHighestBuyer().getPrice() >= getLowestSeller().getPrice()) {
            Order buyer = popTopBuyer();
            Order seller = popTopSeller();

            int dealSize = Math.min(buyer.getSize(), seller.getSize());

            buyer.setSize(buyer.getSize() - dealSize);
            seller.setSize(seller.getSize() - dealSize);

            if (buyer.getSize() > 0) {
                addBuyers(buyer);
            }
            if (seller.getSize() > 0) {
                addSellers(seller);
            }
        }
    }

    // O(log N)
    public void cancelOrder(int id) {
        boolean isBuyerOrder = orderSide[id] == BUYER_ORDER_SIDE;
        if (isBuyerOrder) {
            cancelBuyer(buyers[orderPos[id]]);
        } else {
            cancelSeller(sellers[orderPos[id]]);
        }
    }

    /*
    * Buyers
    * */
    private void addBuyers(Order order) {
        totalSizeForPrice[order.getPrice()] += order.getSize();
        buyers[buyersCount] = order;
        orderPos[order.getId()] = buyersCount;
        raiseBuyers(buyersCount);
        buyersCount++; // I'll better do this explicitly
    }

    // O(log N)
    private void raiseBuyers(int position) {
        while (position > 0 && buyers[position].getPrice() > buyers[(position - 1) / 2].getPrice()) {
            swapBuyers(position, (position - 1) / 2);
            position = (position - 1) / 2;
        }
    }

    // O(log N)
    private Order popTopBuyer() {
        return popBuyer(0);
    }

    // O(log N)
    private Order popBuyer(int position) {
        Order buyer = buyers[position];
        buyers[position] = buyers[buyersCount - 1];
        // Don't forget to change the position
        orderPos[buyers[position].getId()] = position;
        buyersCount--;
        downshiftBuyer(position);
        // Don't forget to decrement total size
        totalSizeForPrice[buyer.getPrice()] -= buyer.getSize();
        return buyer;
    }

    // O(log N)
    private void downshiftBuyer(int position) {
        while (position * 2 + 1 < buyersCount) {
            int left = position * 2 + 1,
                right = position * 2 + 2,
                next = left;
            if (right < buyersCount && buyers[right].getPrice() > buyers[left].getPrice()) {
                next = right;
            }
            if (buyers[position].getPrice() > buyers[next].getPrice()) {
                break;
            }
            swapBuyers(position, next);
            position = next;
        }
    }

    // O(1)
    private void swapBuyers(int xPos, int yPos) {
        Order order = buyers[xPos];
        buyers[xPos] = buyers[yPos];
        buyers[yPos] = order;

        // Don't forget to reset positions for quick cancel
        orderPos[buyers[xPos].getId()] = xPos;
        orderPos[buyers[yPos].getId()] = yPos;
    }

    // O(log N)
    private void cancelBuyer(Order order) {
        int orderId = order.getId(),
            orderPosition = orderPos[orderId];
        popBuyer(orderPosition);
    }

    /*
    * Sellers
    * */

    private void addSellers(Order order) {
        totalSizeForPrice[order.getPrice()] += order.getSize();
        sellers[sellersCount] = order;
        orderPos[order.getId()] = sellersCount;
        raiseSellers(sellersCount);
        sellersCount++; // I'll better do this explicitly
    }

    // O(log N)
    private void raiseSellers(int position) {
        while (position > 0 && sellers[position].getPrice() < sellers[(position - 1) / 2].getPrice()) {
            swapSellers(position, (position - 1) / 2);
            position = (position - 1) / 2;
        }
    }

    // O(1)
    private void swapSellers(int xPos, int yPos) {
        Order order = sellers[xPos];
        sellers[xPos] = sellers[yPos];
        sellers[yPos] = order;

        // Don't forget to reset positions for quick cancel
        orderPos[sellers[xPos].getId()] = xPos;
        orderPos[sellers[yPos].getId()] = yPos;
    }

    private void cancelSeller(Order order) {
        int orderId = order.getId(),
            orderPosition = orderPos[orderId];
        popSeller(orderPosition);
    }

    // O(log N)
    private Order popTopSeller() {
        return popSeller(0);
    }

    // O(log N)
    private Order popSeller(int position) {
        Order seller = sellers[position];
        sellers[position] = sellers[sellersCount - 1];
        // Don't forget to change the position
        orderPos[sellers[position].getId()] = position;
        sellersCount--;
        downshiftSeller(position);
        // Don't forget to decrement total size
        totalSizeForPrice[seller.getPrice()] -= seller.getSize();
        return seller;
    }

    // O(log N)
    private void downshiftSeller(int position) {
        while (position * 2 + 1 < sellersCount) {
            int left = position * 2 + 1,
                right = position * 2 + 2,
                next = left;
            if (right < sellersCount && sellers[right].getPrice() < sellers[left].getPrice()) {
                next = right;
            }
            if (sellers[position].getPrice() < sellers[next].getPrice()) {
                break;
            }
            swapSellers(position, next);
            position = next;
        }
    }

    // O(1)
    public Order getHighestBuyer() {
        return buyersCount > 0 ? buyers[0] : null;
    }

    // O(1)
    public Order getLowestSeller() {
        return sellersCount > 0 ? sellers[0] : null;
    }

    // O(1)
    public int getTotalSizeAtPrice(int price) {
        return totalSizeForPrice[price];
    }

}
