package edu.log.services.booking;

import edu.log.models.booking.Booking;
import edu.log.models.booking.BookingQuote;
import edu.log.models.booking.enums.BookingStatus;
import edu.log.repositories.booking.BookingRepository;
// import edu.log.repositories.booking.BookingQuoteRepository;
import edu.log.services.google.GoogleMapsService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository b_repo;
    // private final BookingQuoteRepository bq_repo;
    private final GoogleMapsService gms;

    public BookingService(BookingRepository b_repo, GoogleMapsService gms) {
        this.b_repo = b_repo;
        // this.bq_repo = bq_repo;
        this.gms = gms;
    }

    // Create a new booking
    public Booking createBooking(Booking booking) {
        booking.setStatus(BookingStatus.PENDING);
        
        // Compute distance using google maps service
        double distance = gms.getDistance(booking.getFromAddress(), booking.getToAddress());

        // Set distance in booking
        booking.setDistance(distance);

        // Compute price and estimated delivery time
        double price = computePrice(booking);
        int estimatedDeliveryTime = computeEstimatedDeliveryTime(booking);

        // Create booking quote and set it in booking
        BookingQuote bookingQuote = new BookingQuote(booking, price, estimatedDeliveryTime);
        booking.setBookingQuote(bookingQuote);

        return b_repo.save(booking);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return b_repo.findAll();
    }

    // Get booking by id
    public Optional<Booking> getBookingById(long id) {
        return Optional.ofNullable(b_repo.findById(id));
    }

    // Compute price for booking
    private double computePrice(Booking booking) {
        return 10 * booking.getVolume() + 5 * booking.getWeight() + 2 * convertMetersToMiles(booking.getDistance());
    }

    // compute estimated delivery time
    private int computeEstimatedDeliveryTime(Booking booking) {
        return (int) Math.ceil(booking.getDistance() / 500.0) + 1;
    }

    // Distance conversion methods
    private double convertMetersToMiles(double meters) {
        return meters * 0.000621371;
    }

    // private double convertMetersToKilometers(double meters) {
    //     return meters * 0.001;
    // }

    // private double convertMilesToMeters(double miles) {
    //     return miles / 0.000621371;
    // }

    // private double convertKilometersToMeters(double kilometers) {
    //     return kilometers / 0.001;
    // }
}
