package com.mde.test.bookmap;

import com.mde.test.bookmap.entity.Order;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderSideException;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderTypeException;
import com.mde.test.bookmap.exceptions.UnrecognizedQueryTypeException;
import com.mde.test.bookmap.service.OrdersHandler;
import com.mde.test.bookmap.service.OrdersLoader;
import com.mde.test.bookmap.util.OrdersProcessorFacade;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        /*
            DI is probably overhead here, so I'll just create a loader

            We can read all the orders from the file beforehand, but if we
            really want to act like a real-time responding system we should
            probably read one at a time.
            This also saves us little time not iterating again
         */
        OrdersLoader ordersLoader = new OrdersLoader(args[0]);

        int line = 1;
        while (ordersLoader.hasNextOrder()) {
            try {
                Order order = ordersLoader.nextOrder();
                OrdersProcessorFacade.processOrder(order);
            } catch (UnrecognizedOrderTypeException e) {
                System.out.println("Unrecognized order type " + e.getType() + " on the line " + line);
                break;
            } catch (UnrecognizedOrderSideException e) {
                System.out.println("Unrecognized order side " + e.getSide() + " on the line " + line);
                break;
            } catch (UnrecognizedQueryTypeException e) {
                System.out.println("Unrecognized query type " + e.getType() + " on the line " + line);
                break;
            }
            ++line;
        }

        ordersLoader.closeLoader();
    }


}
