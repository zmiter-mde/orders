package com.mde.test.bookmap.service;

import com.mde.test.bookmap.entity.Order;
import com.mde.test.bookmap.exceptions.UnrecognizedOrderTypeException;
import com.mde.test.bookmap.exceptions.UnrecognizedQueryTypeException;

import java.io.*;
import java.util.Scanner;

import static com.mde.test.bookmap.util.Constants.SEPARATOR;

public class OrdersLoader {

    private final String filename;
    private final Scanner scanner;

    public OrdersLoader(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.scanner = new Scanner(new File(filename));
    }

    public Order nextOrder() throws UnrecognizedOrderTypeException, UnrecognizedQueryTypeException {
        String[] orderData = scanner.nextLine().split(SEPARATOR);
        return OrdersFactory.createOrder(orderData);
    }

    public boolean hasNextOrder() {
        return scanner.hasNextLine();
    }

    public void closeLoader() {
        scanner.close();
    }

    public String getFilename() {
        return filename;
    }
}
