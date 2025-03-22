package edu.log.controllers;

import edu.log.models.booking.Booking;
import edu.log.services.booking.BookingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService s;

    public BookingController(BookingService s) {
        this.s = s;
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = s.getAllBookings();        
        if (bookings.isEmpty()) throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No bookings found");
        return ResponseEntity.ok(bookings);
    }

    // Get booking by id
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") long id) {
        Optional<Booking> booking = s.getBookingById(id);
        if (booking.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        return ResponseEntity.ok(booking.get());
    }

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking createdBooking = s.createBooking(booking);
        if (createdBooking == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking could not be created");
        return ResponseEntity.status(201).body(createdBooking);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAllBookings() {
        try {
            s.deleteAllBookings();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace(); // or log.error("Error deleting bookings", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting bookings", e);
        }
    }
    

}
