package com.stayease.config;

import com.stayease.entity.*;
import com.stayease.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Seeds the database with demo hotels, rooms, and a default admin account
 * on first startup. Safe to run repeatedly (skips if data already exists).
 */
@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDatabase(
            HotelRepository hotelRepo,
            RoomRepository roomRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // ── Admin account ────────────────────────────────────────────────
            if (!userRepo.existsByEmail("admin@stayease.com")) {
                User admin = new User();
                admin.setEmail("admin@stayease.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setFullName("StayEase Administrator");
                admin.setPhoneNumber("+91-9800000001");
                admin.setRole(Role.ROLE_ADMIN);
                admin.setActive(true);
                userRepo.save(admin);
                System.out.println("✅ Admin created → admin@stayease.com / Admin@123");
            }

            // ── Demo customer ─────────────────────────────────────────────────
            if (!userRepo.existsByEmail("customer@stayease.com")) {
                User customer = new User();
                customer.setEmail("customer@stayease.com");
                customer.setPassword(passwordEncoder.encode("Customer@123"));
                customer.setFullName("Aryan Kumar");
                customer.setPhoneNumber("+91-9800000002");
                customer.setRole(Role.ROLE_CUSTOMER);
                customer.setActive(true);
                userRepo.save(customer);
                System.out.println("✅ Demo customer created → customer@stayease.com / Customer@123");
            }

            // ── Skip if hotels already seeded ────────────────────────────────
            if (hotelRepo.count() > 0) {
                System.out.println("ℹ️  Hotels already seeded — skipping.");
                return;
            }

            // ══════════════════════════════════════════════════════════
            //  Hotel 1 — The Leela Palace, Delhi
            // ══════════════════════════════════════════════════════════
            Hotel h1 = new Hotel(
                "The Leela Palace",
                "A landmark of luxury in the heart of New Delhi. Inspired by Lutyens' Delhi architecture, " +
                "The Leela Palace offers 254 rooms and suites with impeccable butler service, award-winning " +
                "dining and a world-class spa.",
                "Diplomatic Enclave, Chanakyapuri, New Delhi",
                "New Delhi", "Delhi", "India",
                "reservations@theleela.com", "+91-11-39331234",
                5.0,
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80"
            );
            hotelRepo.save(h1);
            createRooms(roomRepo, h1,
                new Object[]{"101", RoomType.STANDARD,   new BigDecimal("8500"),  2, 1},
                new Object[]{"102", RoomType.STANDARD,   new BigDecimal("8500"),  2, 1},
                new Object[]{"201", RoomType.DELUXE,     new BigDecimal("14000"), 2, 2},
                new Object[]{"202", RoomType.DELUXE,     new BigDecimal("14000"), 2, 2},
                new Object[]{"301", RoomType.SUITE,      new BigDecimal("28000"), 3, 2},
                new Object[]{"401", RoomType.PRESIDENTIAL, new BigDecimal("85000"), 4, 2}
            );

            // ══════════════════════════════════════════════════════════
            //  Hotel 2 — Taj Lake Palace, Udaipur
            // ══════════════════════════════════════════════════════════
            Hotel h2 = new Hotel(
                "Taj Lake Palace",
                "Rising from the shimmering waters of Lake Pichola, the Taj Lake Palace is a floating " +
                "fantasy built in 1746. Every room opens to views of the Aravalli hills or the " +
                "city palace across the water.",
                "Lake Pichola, Udaipur",
                "Udaipur", "Rajasthan", "India",
                "tajlake@tajhotels.com", "+91-294-2428800",
                5.0,
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80"
            );
            hotelRepo.save(h2);
            createRooms(roomRepo, h2,
                new Object[]{"101", RoomType.STANDARD,   new BigDecimal("12000"), 2, 1},
                new Object[]{"102", RoomType.STANDARD,   new BigDecimal("12000"), 2, 1},
                new Object[]{"201", RoomType.DELUXE,     new BigDecimal("22000"), 2, 2},
                new Object[]{"301", RoomType.SUITE,      new BigDecimal("45000"), 3, 2},
                new Object[]{"302", RoomType.SUITE,      new BigDecimal("45000"), 3, 2},
                new Object[]{"401", RoomType.PRESIDENTIAL, new BigDecimal("120000"), 4, 2}
            );

            // ══════════════════════════════════════════════════════════
            //  Hotel 3 — ITC Grand Goa
            // ══════════════════════════════════════════════════════════
            Hotel h3 = new Hotel(
                "ITC Grand Goa Resort & Spa",
                "A Portuguese-inspired coastal retreat spread across 45 acres of coconut groves and " +
                "picturesque lagoons in South Goa. Award-winning cuisine, world-class spa and direct " +
                "beach access make this an unmatched escape.",
                "Arossim Beach, Cansaulim, South Goa",
                "Goa", "Goa", "India",
                "goa@itchotels.in", "+91-832-2721234",
                5.0,
                "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80"
            );
            hotelRepo.save(h3);
            createRooms(roomRepo, h3,
                new Object[]{"101", RoomType.STANDARD,   new BigDecimal("9500"),  2, 1},
                new Object[]{"102", RoomType.STANDARD,   new BigDecimal("9500"),  2, 1},
                new Object[]{"103", RoomType.STANDARD,   new BigDecimal("9500"),  2, 1},
                new Object[]{"201", RoomType.DELUXE,     new BigDecimal("18000"), 2, 2},
                new Object[]{"202", RoomType.DELUXE,     new BigDecimal("18000"), 2, 2},
                new Object[]{"301", RoomType.SUITE,      new BigDecimal("35000"), 4, 2},
                new Object[]{"401", RoomType.FAMILY_SUITE, new BigDecimal("42000"), 4, 3}
            );

            // ══════════════════════════════════════════════════════════
            //  Hotel 4 — Oberoi Trident, Mumbai
            // ══════════════════════════════════════════════════════════
            Hotel h4 = new Hotel(
                "Oberoi Trident Mumbai",
                "Overlooking the iconic Marine Drive and the Arabian Sea, the Oberoi Trident offers an " +
                "unmatched location in Nariman Point. The hotel combines contemporary luxury with warm " +
                "Indian hospitality.",
                "Nariman Point, Mumbai",
                "Mumbai", "Maharashtra", "India",
                "reservations.tridentmumbai@oberoihotels.com", "+91-22-66321234",
                5.0,
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=1200&q=80"
            );
            hotelRepo.save(h4);
            createRooms(roomRepo, h4,
                new Object[]{"101", RoomType.STANDARD,   new BigDecimal("11000"), 2, 1},
                new Object[]{"102", RoomType.STANDARD,   new BigDecimal("11000"), 2, 1},
                new Object[]{"201", RoomType.DELUXE,     new BigDecimal("19000"), 2, 2},
                new Object[]{"202", RoomType.DELUXE,     new BigDecimal("19000"), 2, 2},
                new Object[]{"301", RoomType.SUITE,      new BigDecimal("38000"), 3, 2},
                new Object[]{"401", RoomType.PRESIDENTIAL, new BigDecimal("95000"), 4, 2}
            );

            // ══════════════════════════════════════════════════════════
            //  Hotel 5 — Taj Rambagh Palace, Jaipur
            // ══════════════════════════════════════════════════════════
            Hotel h5 = new Hotel(
                "Taj Rambagh Palace",
                "Once the jewel of Jaipur and the residence of the Maharaja of Jaipur, Rambagh Palace " +
                "is a symbol of India's royal heritage. Set in 47 acres of Mughal gardens, it combines " +
                "regal grandeur with modern luxury.",
                "Bhawani Singh Road, Jaipur",
                "Jaipur", "Rajasthan", "India",
                "rambagh.jaipur@tajhotels.com", "+91-141-2211919",
                5.0,
                "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?auto=format&fit=crop&w=1200&q=80"
            );
            hotelRepo.save(h5);
            createRooms(roomRepo, h5,
                new Object[]{"101", RoomType.STANDARD,   new BigDecimal("10000"), 2, 1},
                new Object[]{"102", RoomType.STANDARD,   new BigDecimal("10000"), 2, 1},
                new Object[]{"201", RoomType.DELUXE,     new BigDecimal("17500"), 2, 2},
                new Object[]{"202", RoomType.DELUXE,     new BigDecimal("17500"), 2, 2},
                new Object[]{"301", RoomType.SUITE,      new BigDecimal("40000"), 3, 2},
                new Object[]{"302", RoomType.SUITE,      new BigDecimal("40000"), 3, 2},
                new Object[]{"401", RoomType.PRESIDENTIAL, new BigDecimal("110000"), 4, 2}
            );

            System.out.println("✅ Seeded 5 hotels with rooms successfully.");
        };
    }

    private void createRooms(RoomRepository roomRepo, Hotel hotel, Object[]... roomDefs) {
        for (Object[] def : roomDefs) {
            Room room = new Room();
            room.setHotel(hotel);
            room.setRoomNumber((String) def[0]);
            room.setRoomType((RoomType) def[1]);
            room.setBasePricePerNight((BigDecimal) def[2]);
            room.setCapacityAdults((Integer) def[3]);
            room.setCapacityChildren((Integer) def[4]);
            room.setActive(true);

            // Assign different amenities by room type
            switch ((RoomType) def[1]) {
                case STANDARD -> room.setAmenities(
                    "Free High-Speed WiFi, Air Conditioning, 32\" LED TV, En-suite Bathroom, " +
                    "Daily Housekeeping, Room Service (24hr), Writing Desk, Tea/Coffee Maker");
                case DELUXE -> room.setAmenities(
                    "Free High-Speed WiFi, Air Conditioning, 48\" Smart TV, King/Twin Beds, " +
                    "En-suite Marble Bathroom, Complimentary Minibar, Daily Housekeeping, " +
                    "Room Service (24hr), City/Pool View, Bathrobe & Slippers");
                case SUITE -> room.setAmenities(
                    "Dedicated Butler Service, Free High-Speed WiFi, Air Conditioning, " +
                    "65\" 4K Smart TV, King Bed, Separate Living Room, Jacuzzi Bathtub, " +
                    "Complimentary Minibar & Fruit Basket, Panoramic Views, Pillow Menu");
                case FAMILY_SUITE -> room.setAmenities(
                    "2 Bedrooms, Free High-Speed WiFi, Air Conditioning, 55\" Smart TVs in all rooms, " +
                    "2 Full Bathrooms, Living Room, Dedicated Butler, Complimentary Breakfast, " +
                    "Kids Welcome Amenities, Interconnecting Rooms");
                case PRESIDENTIAL -> room.setAmenities(
                    "Exclusive Presidential Butler & Concierge, Free High-Speed WiFi, " +
                    "Air Conditioning, Home Theatre, Grand Piano, 2 King Bedrooms, " +
                    "2 Marble Bathrooms, Private Dining Room, Rooftop Terrace/Private Pool, " +
                    "Complimentary Airport Transfer & Minibar");
            }
            roomRepo.save(room);
        }
    }
}
