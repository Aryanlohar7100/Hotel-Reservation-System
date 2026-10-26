package com.stayease.config;

import com.stayease.entity.*;
import com.stayease.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final AmenityRepository amenityRepository;
    private final HotelAmenityRepository hotelAmenityRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      HotelRepository hotelRepository,
                      RoomRepository roomRepository,
                      BookingRepository bookingRepository,
                      ReviewRepository reviewRepository,
                      AmenityRepository amenityRepository,
                      HotelAmenityRepository hotelAmenityRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.amenityRepository = amenityRepository;
        this.hotelAmenityRepository = hotelAmenityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already seeded
        }

        System.out.println(">>> [StayEase] Initializing demo data...");

        // 1. Seed Users
        User admin = new User(
                "admin@stayease.com",
                passwordEncoder.encode("admin123"),
                "Alexander Wright (Admin)",
                "+91 9876543210",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        User customer = new User(
                "john@example.com",
                passwordEncoder.encode("customer123"),
                "John Doe",
                "+91 9123456780",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(customer);

        User customer2 = new User(
                "priya.sharma@example.com",
                passwordEncoder.encode("customer123"),
                "Priya Sharma",
                "+91 9811223344",
                Role.ROLE_CUSTOMER
        );
        userRepository.save(customer2);

        // 2. Seed Hotels
        Hotel h1 = new Hotel(
                "The Grand Horizon Palace",
                "Perched over Marine Drive with panoramic Arabian Sea views, The Grand Horizon Palace offers world-class luxury, an infinity rooftop pool, fine-dining restaurants, and five-star hospitality.",
                "88 Marine Drive, Nariman Point",
                "Mumbai",
                "Maharashtra",
                "India",
                "mumbai@stayease.com",
                "+91 22 6677 8899",
                4.8,
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80"
        );

        Hotel h2 = new Hotel(
                "Royal Heritage Haveli & Spa",
                "An authentic 18th-century Rajput palace exquisitely restored into an oasis of regal calm, featuring sprawling courtyard fountains, Ayurvedic spa therapies, and cultural evening recitals.",
                "14 Amber Fort Road, Civil Lines",
                "Jaipur",
                "Rajasthan",
                "India",
                "jaipur@stayease.com",
                "+91 141 2345 6789",
                4.9,
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=80"
        );

        Hotel h3 = new Hotel(
                "Azure Cove Beachfront Sanctuary",
                "Nestled among tropical swaying coconut groves on Candolim Beach, Azure Cove features direct sandy beach access, private cabanas, sunset cocktail lounges, and holistic wellness programs.",
                "Calangute-Candolim Beach Walkway",
                "Goa",
                "Goa",
                "India",
                "goa@stayease.com",
                "+91 832 2987 6543",
                4.7,
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=1200&q=80"
        );

        Hotel h4 = new Hotel(
                "The Silicon Valley Grand Suites",
                "Bangalore's premier business and lifestyle address adjacent to UB City, featuring cutting-edge conference facilities, high-speed fiber internet, artisanal cafe, and heated indoor plunge pool.",
                "24 Vittal Mallya Road, Lavelle Road",
                "Bengaluru",
                "Karnataka",
                "India",
                "bengaluru@stayease.com",
                "+91 80 4112 3344",
                4.6,
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80"
        );

        Hotel h5 = new Hotel(
                "The Imperial Diplomat",
                "Steeped in colonial heritage in the diplomatic heart of Lutyens' Delhi, surrounded by landscaped gardens, offering bespoke butler service and Michelin-starred dining.",
                "1 Janpath Road, Connaught Place",
                "New Delhi",
                "Delhi",
                "India",
                "delhi@stayease.com",
                "+91 11 2334 1234",
                4.7,
                "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80"
        );

        Hotel h6 = new Hotel(
                "Lake Pichola Serenade Resort",
                "A mesmerizing waterfront sanctuary offering breathtaking views of the City Palace and Jag Mandir across the sparkling waters of Lake Pichola with private wooden boat excursions.",
                "Lake Palace Road, Pichola",
                "Udaipur",
                "Rajasthan",
                "India",
                "udaipur@stayease.com",
                "+91 294 242 1100",
                4.9,
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80"
        );

        hotelRepository.saveAll(Arrays.asList(h1, h2, h3, h4, h5, h6));

        // 2.1 Seed Amenities
        Amenity aWifi = new Amenity("High-Speed WiFi", "bi-wifi", "Complimentary 500 Mbps gigabit optical fiber connection");
        Amenity aPool = new Amenity("Infinity Swimming Pool", "bi-water", "Heated horizon pool with sunloungers and poolside bar");
        Amenity aSpa = new Amenity("Ayurvedic Luxury Spa", "bi-flower1", "Holistic rejuvenation massages, sauna, and steam wellness rooms");
        Amenity aDining = new Amenity("Fine Dining Restaurant", "bi-cup-hot-fill", "Multi-cuisine gastronomy and award-winning wine cellar");
        Amenity aValet = new Amenity("Valet Parking", "bi-car-front-fill", "24-hour secured valet parking with EV charging station");
        Amenity aGym = new Amenity("24/7 Fitness Center", "bi-heart-pulse-fill", "Cardio machines, free weights, and private yoga sessions");

        amenityRepository.saveAll(Arrays.asList(aWifi, aPool, aSpa, aDining, aValet, aGym));

        // Link Amenities to Hotels
        hotelAmenityRepository.save(new HotelAmenity(h1, aWifi, true, "Available property-wide"));
        hotelAmenityRepository.save(new HotelAmenity(h1, aPool, true, "Infinity ocean-view pool"));
        hotelAmenityRepository.save(new HotelAmenity(h1, aDining, true, "Marine Drive fine dining"));

        hotelAmenityRepository.save(new HotelAmenity(h2, aWifi, true, "High-speed courtyard coverage"));
        hotelAmenityRepository.save(new HotelAmenity(h2, aSpa, false, "Signature royal Rajput treatments"));
        hotelAmenityRepository.save(new HotelAmenity(h2, aDining, true, "Rooftop thali experience"));

        hotelAmenityRepository.save(new HotelAmenity(h3, aPool, true, "Private beachfront access"));
        hotelAmenityRepository.save(new HotelAmenity(h3, aWifi, true, "Beach cabana coverage"));

        // 3. Seed Rooms
        // Mumbai Rooms
        Room r101 = new Room(h1, "101", RoomType.DELUXE, new BigDecimal("8500.00"), 2, 1,
                "Sea View Balcony, King Bed, Rain Shower, Free High-Speed WiFi, Mini Bar, 55-inch OLED TV",
                "https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80");
        Room r102 = new Room(h1, "201", RoomType.SUITE, new BigDecimal("14500.00"), 3, 2,
                "Panoramic Arabian Sea View, Separate Living Lounge, Jacuzzi Tub, Butler Service, Nespresso Machine",
                "https://images.unsplash.com/photo-1591088398332-8a7791972843?auto=format&fit=crop&w=800&q=80");
        Room r103 = new Room(h1, "301", RoomType.PRESIDENTIAL, new BigDecimal("29000.00"), 4, 2,
                "Full Ocean Terrace, Private Bar, Dining Area, Deep Soaking Tub, 24/7 Dedicated Concierge",
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80");

        // Jaipur Rooms
        Room r201 = new Room(h2, "101", RoomType.STANDARD, new BigDecimal("6200.00"), 2, 0,
                "Hand-carved Teak Bed, Courtyard Garden View, Artisanal Tea Bar, High-Speed WiFi",
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=800&q=80");
        Room r202 = new Room(h2, "202", RoomType.DELUXE, new BigDecimal("9800.00"), 2, 1,
                "Private Jharokha Balcony, Royal Rajasthani Decor, Copper Tub, Complimentary Breakfast",
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=800&q=80");
        Room r203 = new Room(h2, "305", RoomType.FAMILY_SUITE, new BigDecimal("16500.00"), 4, 3,
                "Two Interconnected Bedrooms, Sprawling Terrace, Hand-painted Frescoes, Living Lounge",
                "https://images.unsplash.com/photo-1578683010236-d716f9a3f461?auto=format&fit=crop&w=800&q=80");

        // Goa Rooms
        Room r301 = new Room(h3, "G-01", RoomType.DELUXE, new BigDecimal("7500.00"), 2, 1,
                "Direct Beach Access, Outdoor Hammock, King Bed, Tropical Rain Shower, Sun Loungers",
                "https://images.unsplash.com/photo-1507652313519-d4e9174996dd?auto=format&fit=crop&w=800&q=80");
        Room r302 = new Room(h3, "G-05", RoomType.SUITE, new BigDecimal("13000.00"), 3, 2,
                "Private Plunge Pool, Sunset Sea View, Sundeck, Complimentary Sunset Drinks, King Bed",
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?auto=format&fit=crop&w=800&q=80");

        // Bengaluru Rooms
        Room r401 = new Room(h4, "501", RoomType.STANDARD, new BigDecimal("5500.00"), 2, 0,
                "Ergonomic Workstation, High-Speed WiFi, City Skyline View, Smart TV, Rain Shower",
                "https://images.unsplash.com/photo-1611892440504-42a792e24d32?auto=format&fit=crop&w=800&q=80");
        Room r402 = new Room(h4, "702", RoomType.DELUXE, new BigDecimal("8200.00"), 2, 1,
                "Executive Club Lounge Access, King Bed, Espresso Machine, City Center View",
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=800&q=80");

        // Delhi Rooms
        Room r501 = new Room(h5, "104", RoomType.DELUXE, new BigDecimal("9200.00"), 2, 1,
                "Heritage Colonial Architecture, Italian Marble Bathroom, Garden View, 24/7 Room Service",
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=800&q=80");
        Room r502 = new Room(h5, "208", RoomType.SUITE, new BigDecimal("17000.00"), 3, 2,
                "Diplomatic Suite, High Ceilings, Dining Room, Fireplace, Chauffeur Airport Transfer",
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80");

        // Udaipur Rooms
        Room r601 = new Room(h6, "L-11", RoomType.DELUXE, new BigDecimal("11500.00"), 2, 1,
                "Lake Pichola Water View, Traditional Mewari Architecture, Sunken Marble Bath",
                "https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80");
        Room r602 = new Room(h6, "L-22", RoomType.SUITE, new BigDecimal("21000.00"), 3, 2,
                "Waterfront Private Balcony, Sunrise Palace View, Champagne Welcome, Boat Transfer",
                "https://images.unsplash.com/photo-1591088398332-8a7791972843?auto=format&fit=crop&w=800&q=80");

        roomRepository.saveAll(Arrays.asList(r101, r102, r103, r201, r202, r203, r301, r302, r401, r402, r501, r502, r601, r602));

        // 4. Seed Initial Sample Bookings for customer
        Booking b1 = new Booking(
                "STY-2026-9812A",
                customer,
                h1,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(6),
                2,
                new BigDecimal("25500.00"),
                BookingStatus.CONFIRMED
        );
        BookingItem bi1 = new BookingItem(b1, r101, new BigDecimal("8500.00"), 3, new BigDecimal("25500.00"));
        b1.setItems(Collections.singletonList(bi1));

        Payment p1 = new Payment(b1, "TXN-1718902341-9812A", new BigDecimal("25500.00"), PaymentMethod.CREDIT_CARD, PaymentStatus.SUCCESS);
        b1.setPayment(p1);
        bookingRepository.save(b1);

        Booking b2 = new Booking(
                "STY-2026-7734C",
                customer2,
                h2,
                LocalDate.now().minusDays(10),
                LocalDate.now().minusDays(7),
                2,
                new BigDecimal("29400.00"),
                BookingStatus.COMPLETED
        );
        BookingItem bi2 = new BookingItem(b2, r202, new BigDecimal("9800.00"), 3, new BigDecimal("29400.00"));
        b2.setItems(Collections.singletonList(bi2));

        Payment p2 = new Payment(b2, "TXN-1718812341-7734C", new BigDecimal("29400.00"), PaymentMethod.UPI, PaymentStatus.SUCCESS);
        b2.setPayment(p2);
        bookingRepository.save(b2);

        // 5. Seed Guest Reviews
        Review rev1 = new Review(h1, customer, 5,
                "Absolute perfection! Waking up to the sunrise over Marine Drive from our Deluxe Room was unforgettable. The staff catered to every single request with grace.");
        Review rev2 = new Review(h2, customer2, 5,
                "A truly royal experience! The architecture, cultural music at night, and royal dining made our vacation heavenly. We can't wait to return.");
        Review rev3 = new Review(h3, customer, 4,
                "Spectacular beach access and relaxing vibes. The cabanas by the pool are serene and the seafood at the restaurant is top tier.");

        reviewRepository.saveAll(Arrays.asList(rev1, rev2, rev3));

        System.out.println(">>> [StayEase] Demo data initialized successfully!");
        System.out.println(">>> Admin: admin@stayease.com / admin123");
        System.out.println(">>> Customer: john@example.com / customer123");
    }
}
