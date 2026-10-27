package com.lld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.lld.enums.OrderStatus;
import com.lld.model.Address;
import com.lld.model.Customer;
import com.lld.model.DeliveryAgent;
import com.lld.model.Menu;
import com.lld.model.MenuItem;
import com.lld.model.Order;
import com.lld.model.OrderItem;
import com.lld.model.Restaurant;
import com.lld.strategy.NearestAvailableAgentStrategy;
import com.lld.strategy.SearchByCityStrategy;
import com.lld.strategy.SearchByMenuStrategy;
import com.lld.strategy.SearchByProximity;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     FOOD DELIVERY SYSTEM - QA TEST SUITE         ");
        System.out.println("==================================================\n");

        FoodDeliveryService service = FoodDeliveryService.getInstance();

        // -------------------------------------------------------------
        // Setup Test Data
        // -------------------------------------------------------------
        System.out.println(">>> 1. REGISTRATION");
        Address aliceAddr = new Address("1st Main", "New York", "10001", 10.0, 10.0);
        Customer alice = service.registerUser("Alice", "1234567890", aliceAddr);
        System.out.println("Registered Customer: " + alice.getName() + " (ID: " + alice.getId() + ")");

        // Menu 1: Italian
        Map<String, MenuItem> pizzaMenu = new HashMap<>();
        MenuItem pizza = new MenuItem("m1", "Pizza", 15.0);
        MenuItem pasta = new MenuItem("m2", "Pasta", 12.0);
        pizzaMenu.put(pizza.getId(), pizza);
        pizzaMenu.put(pasta.getId(), pasta);

        // Menu 2: Fast Food
        Map<String, MenuItem> burgerMenu = new HashMap<>();
        MenuItem burger = new MenuItem("m3", "Burger", 10.0);
        MenuItem fries = new MenuItem("m4", "Fries", 5.0);
        burgerMenu.put(burger.getId(), burger);
        burgerMenu.put(fries.getId(), fries);

        // Menu 3: Mexican
        Map<String, MenuItem> tacoMenu = new HashMap<>();
        MenuItem taco = new MenuItem("m5", "Taco", 8.0);
        tacoMenu.put(taco.getId(), taco);

        Restaurant pizzaPalace = service.registerRestaurant("Pizza Palace", new Menu("menu1", pizzaMenu),
                new Address("2nd Ave", "New York", "10002", 10.5, 10.5));
        Restaurant burgerBarn = service.registerRestaurant("Burger Barn", new Menu("menu2", burgerMenu),
                new Address("5th Ave", "New York", "10003", 25.0, 25.0));
        Restaurant tacoTown = service.registerRestaurant("Taco Town", new Menu("menu3", tacoMenu),
                new Address("Main St", "Boston", "02101", 100.0, 100.0));

        System.out.println("Registered Restaurants: " + pizzaPalace.getName() + ", " + burgerBarn.getName() + ", " + tacoTown.getName());

        DeliveryAgent agentBob = service.registerDeliveryPerson("Bob", "9876543210",
                new Address("Close St", "New York", "10002", 10.6, 10.6));
        DeliveryAgent agentCharlie = service.registerDeliveryPerson("Charlie", "5555555555",
                new Address("Far St", "New York", "10005", 50.0, 50.0));

        System.out.println("Registered Agents: " + agentBob.getName() + ", " + agentCharlie.getName());

        System.out.println("\n--------------------------------------------------");
        System.out.println(">>> 2. TESTING RESTAURANT SEARCH STRATEGIES");
        System.out.println("--------------------------------------------------");

        // Strategy A: Search by City
        System.out.println("\n[A] Search by City ('New York'):");
        List<Restaurant> nyRestaurants = service.searcRestaurants(new SearchByCityStrategy("New York"));
        for (Restaurant r : nyRestaurants) {
            System.out.println("  -> Found: " + r.getName());
        }
        assertEqual("SearchByCity count", nyRestaurants.size(), 2);

        // Strategy B: Search by Menu Item
        System.out.println("\n[B] Search by Menu Item ('Burger'):");
        List<Restaurant> burgerRestaurants = service.searcRestaurants(new SearchByMenuStrategy("Burger"));
        for (Restaurant r : burgerRestaurants) {
            System.out.println("  -> Found: " + r.getName());
        }
        assertEqual("SearchByMenu count", burgerRestaurants.size(), 1);
        assertEqual("SearchByMenu restaurant name", burgerRestaurants.get(0).getName(), "Burger Barn");

        // Strategy C: Search by Proximity
        System.out.println("\n[C] Search by Proximity (closest to Alice at 10.0, 10.0):");
        List<Restaurant> proximate = service.searcRestaurants(new SearchByProximity(aliceAddr));
        for (Restaurant r : proximate) {
            double dist = r.getAddress().getDistanceTo(aliceAddr);
            System.out.printf("  -> %s (Distance: %.2f)%n", r.getName(), dist);
        }
        assertEqual("Closest restaurant is Pizza Palace", proximate.get(0).getName(), "Pizza Palace");

        System.out.println("\n--------------------------------------------------");
        System.out.println(">>> 3. ORDER PLACEMENT & OBSERVER PATTERN");
        System.out.println("--------------------------------------------------");

        List<OrderItem> orderItems = List.of(new OrderItem("oi1", pizza, 2));
        System.out.println("Alice placing order at Pizza Palace for 2 Pizzas:");
        Order order = service.placeOrder(alice.getId(), pizzaPalace.getId(), orderItems);
        assertEqual("Order status initial", order.getStatus(), OrderStatus.PENDING);

        System.out.println("\nAdvancing status: PENDING -> CONFIRMED -> PREPARING -> READY_FOR_PICKUP:");
        order.setStatus(OrderStatus.CONFIRMED);
        order.setStatus(OrderStatus.PREPARING);
        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        assertEqual("Order status ready for pickup", order.getStatus(), OrderStatus.READY_FOR_PICKUP);

        System.out.println("\n--------------------------------------------------");
        System.out.println(">>> 4. DELIVERY AGENT ASSIGNMENT STRATEGY");
        System.out.println("--------------------------------------------------");

        NearestAvailableAgentStrategy agentStrategy = new NearestAvailableAgentStrategy();
        Optional<DeliveryAgent> nearestAgent = agentStrategy.findAgent(List.of(agentBob, agentCharlie), order);

        System.out.println("Finding nearest agent for order...");
        if (nearestAgent.isPresent()) {
            System.out.println("Nearest agent selected: " + nearestAgent.get().getName());
            assertEqual("Nearest agent should be Bob", nearestAgent.get().getName(), "Bob");

            service.assignDeliveryAgent(order.getId(), nearestAgent.get().getId());
            assertEqual("Order status OUT_FOR_DELIVERY", order.getStatus(), OrderStatus.OUT_FOR_DELIVERY);
            assertEqual("Bob is now busy", agentBob.isAvailable(), false);
        } else {
            System.err.println("FAIL: No agent found!");
        }

        System.out.println("\nOrder delivered:");
        order.setStatus(OrderStatus.DELIVERED);
        assertEqual("Order status DELIVERED", order.getStatus(), OrderStatus.DELIVERED);
        assertEqual("Bob is available again after delivery", agentBob.isAvailable(), true);

        System.out.println("\n--------------------------------------------------");
        System.out.println(">>> 5. CANCELLATION & EDGE CASES");
        System.out.println("--------------------------------------------------");

        // Test cancel on new order
        Order orderToCancel = service.placeOrder(alice.getId(), burgerBarn.getId(), List.of(new OrderItem("oi2", burger, 1)));
        System.out.println("Cancelling order (in PENDING status):");
        service.cancelOrder(orderToCancel.getId());
        assertEqual("Order cancelled", orderToCancel.getStatus(), OrderStatus.CANCELLED);

        // Test cannot cancel DELIVERED order
        System.out.println("Attempting to cancel already DELIVERED order:");
        boolean cancelResult = order.cancel();
        assertEqual("Cannot cancel DELIVERED order", cancelResult, false);

        // Edge case: item not available
        System.out.println("Testing unavailable item order exception:");
        taco.setAvailable(false);
        try {
            service.placeOrder(alice.getId(), tacoTown.getId(), List.of(new OrderItem("oi3", taco, 1)));
            System.err.println("FAIL: Expected IllegalArgumentException for unavailable item");
        } catch (IllegalArgumentException e) {
            System.out.println("  [PASS] Caught expected exception: " + e.getMessage());
        }

        // Edge case: non-existent customer
        System.out.println("Testing non-existent customer exception:");
        try {
            service.placeOrder("fake-id", pizzaPalace.getId(), orderItems);
            System.err.println("FAIL: Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("  [PASS] Caught expected exception: " + e.getMessage());
        }

        System.out.println("\n==================================================");
        System.out.println("          ALL QA TESTS PASSED SUCCESSFULLY!       ");
        System.out.println("==================================================");
    }

    private static void assertEqual(String testName, Object actual, Object expected) {
        if (actual.equals(expected)) {
            System.out.println("  [PASS] " + testName + " -> " + actual);
        } else {
            System.err.println("  [FAIL] " + testName + " -> expected: " + expected + ", got: " + actual);
            throw new AssertionError("Test failed: " + testName);
        }
    }
}