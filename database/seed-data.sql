-- =============================================================================
-- StayEase Hotel Reservation System — Sample Seed Data
-- =============================================================================

USE stayease_db;

-- 1. Insert Initial Users
-- BCrypt password for admin123: $2a$10$w099zOvgJ6ZJ2qI8bC9KVuJkK0cHzv5c0oA5e7m1B5p9aYjE6jJj.
-- BCrypt password for customer123: $2a$10$Q7yMvBfZsM69.b51q47.OOTb7s7Bw6G2YkQeGjD7hYx1S3B0xYm8e
INSERT INTO users (id, email, password, full_name, phone_number, role, active) VALUES
(1, 'admin@stayease.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Alexander Wright (Admin)', '+91 9876543210', 'ROLE_ADMIN', 1),
(2, 'john@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Johnathan Miller', '+91 9811122233', 'ROLE_CUSTOMER', 1),
(3, 'priya.sharma@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Priya Sharma', '+91 9822233344', 'ROLE_CUSTOMER', 1);

-- 2. Insert Amenities
INSERT INTO amenities (id, name, icon, description, category) VALUES
(1, 'High-Speed Wi-Fi', 'bi-wifi', 'Ultra-fast fiber optic internet access', 'Connectivity'),
(2, 'Swimming Pool', 'bi-water', 'Temperature-controlled infinity swimming pool', 'Recreation'),
(3, 'Spa & Wellness', 'bi-heart-pulse', 'Ayurvedic & international therapy center', 'Wellness'),
(4, 'Fine Dining Restaurant', 'bi-cup-hot', 'Multi-cuisine restaurant with master chefs', 'Dining'),
(5, 'Fitness Center', 'bi-activity', '24/7 state-of-the-art gym with personal trainers', 'Fitness'),
(6, 'Airport Shuttle', 'bi-car-front', 'Chauffeured luxury airport pick-up and drop', 'Transport'),
(7, '24/7 Concierge', 'bi-person-badge', 'Dedicated butler and personalized concierge assistance', 'Service');

-- 3. Insert Luxury Hotels
INSERT INTO hotels (id, name, description, address, city, state, country, contact_email, contact_phone, star_rating, image_url, active) VALUES
(1, 'The Leela Palace New Delhi', 'A modern palace hotel combining majestic architecture with lavish modern comfort in the heart of Chanakyapuri Diplomatic Enclave.', 'Diplomatic Enclave, Chanakyapuri', 'New Delhi', 'Delhi', 'India', 'delhi@theleela.com', '+91 11 3933 1234', 5.0, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80', 1),
(2, 'Taj Lake Palace Udaipur', 'An iconic marble marvel floating on the serene waters of Lake Pichola, offering regal Rajput hospitality and panoramic sunset vistas.', 'P.O. Box 5, Lake Pichola', 'Udaipur', 'Rajasthan', 'India', 'lakepalace.udaipur@tajhotels.com', '+91 294 242 8800', 5.0, 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=80', 1),
(3, 'ITC Grand Chola Chennai', 'A tribute to the majestic Chola dynasty architecture, renowned for world-class sustainable luxury and Michelin-starred dining experiences.', '63 Mount Road, Guindy', 'Chennai', 'Tamil Nadu', 'India', 'grandchola@itchotels.in', '+91 44 2220 0000', 4.9, 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80', 1),
(4, 'JW Marriott Mumbai Juhu', 'A premier beachside luxury resort overlooking the Arabian Sea, featuring lush tropical grounds and premier award-winning culinary venues.', 'Juhu Tara Road, Juhu Beach', 'Mumbai', 'Maharashtra', 'India', 'jw.mumbai@marriott.com', '+91 22 6693 3000', 4.8, 'https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80', 1),
(5, 'Wildflower Hall, An Oberoi Resort Shimla', 'Perched at 8,250 feet amidst fragrant cedar forests, featuring magnificent views of snow-capped Himalayan peaks and world-class tranquil spas.', 'Chharabra, Shimla', 'Shimla', 'Himachal Pradesh', 'India', 'wildflowerhall@oberoihotels.com', '+91 177 264 8585', 5.0, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=1200&q=80', 1);

-- 4. Associate Amenities with Hotels
INSERT INTO hotel_amenities (hotel_id, amenity_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
(2, 1), (2, 2), (2, 3), (2, 4), (2, 6), (2, 7),
(3, 1), (3, 2), (3, 4), (3, 5), (3, 7),
(4, 1), (4, 2), (4, 4), (4, 5), (4, 6),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 7);

-- 5. Insert Sample Rooms
INSERT INTO rooms (id, hotel_id, room_number, room_type, base_price_per_night, capacity_adults, capacity_children, amenities, image_url, active) VALUES
(1, 1, '101', 'DELUXE', 12500.00, 2, 1, 'King Bed, City View, Marble Bath, High-speed Wi-Fi', 'https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=800&q=80', 1),
(2, 1, '201', 'SUPER_DELUXE', 18500.00, 2, 2, 'Heritage Garden View, Private Balcony, Jacuzzi Tub', 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=800&q=80', 1),
(3, 1, '301', 'EXECUTIVE_SUITE', 28000.00, 3, 2, 'Panoramic Skyline View, Executive Lounge Access, Butler Service', 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80', 1),
(4, 1, 'PH-01', 'PRESIDENTIAL_SUITE', 65000.00, 4, 2, 'Full Palace Floor, Private Plunge Pool, Bulletproof Glass, 24/7 Chef', 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?auto=format&fit=crop&w=800&q=80', 1),
(5, 2, 'LAKE-10', 'DELUXE', 19000.00, 2, 1, 'Lake Pichola Water View, Heritage Mewar Carvings, Antique Furnishing', 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=800&q=80', 1),
(6, 2, 'ROYAL-01', 'EXECUTIVE_SUITE', 36000.00, 2, 2, 'Private Jharokha Balcony, Champagne Breakfast, Lake Sunsets', 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?auto=format&fit=crop&w=800&q=80', 1),
(7, 3, 'CHOLA-102', 'DELUXE', 11000.00, 2, 1, 'Granite Pillars Ambience, Smart iPad Room Controls, Pillow Menu', 'https://images.unsplash.com/photo-1591088398332-8a7791972843?auto=format&fit=crop&w=800&q=80', 1),
(8, 4, 'BEACH-205', 'SUPER_DELUXE', 16500.00, 3, 1, 'Arabian Sea Sunset View, Rainfall Shower, Executive Club Entry', 'https://images.unsplash.com/photo-1595576508898-0ad5c879a061?auto=format&fit=crop&w=800&q=80', 1),
(9, 5, 'HIMALAYA-01', 'SUPER_DELUXE', 24000.00, 2, 1, 'Heated Timber Flooring, Cedar Forest View, Open Fireplace', 'https://images.unsplash.com/photo-1507652313519-d4e9174996dd?auto=format&fit=crop&w=800&q=80', 1);

-- 6. Insert Reviews
INSERT INTO reviews (hotel_id, user_id, rating, comment) VALUES
(1, 2, 5, 'Exceptional hospitality and palatial decor. The rooftop infinity pool overlooking Delhi skyline was breathtaking!'),
(2, 3, 5, 'A truly magical romantic getaway on Lake Pichola. Arriving by private boat sets an unforgettable tone.'),
(3, 2, 5, 'Unmatched culinary standards. The Peshawri dinner was divine, and staff anticipated every need.');
