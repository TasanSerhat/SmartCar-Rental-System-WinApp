-- SmartCar Database Seed Data

-- 1. Branches
INSERT INTO Branch (branchName, city, address, phone) VALUES 
('Central Station', 'Istanbul', 'Taksim Sq. No:1', '02125550001'),
('Airport Hub', 'Istanbul', 'Sabiha Gokcen Int. Airport', '02165550002'),
('Ankara Main', 'Ankara', 'Kizilay Sq. No:10', '03125550003'),
('Izmir Coast', 'Izmir', 'Alsancak Blvd. No:5', '02325550004'),
('Antalya Resort', 'Antalya', 'Lara Beach Rd.', '02425550005');

-- 2. Vehicle Models (10 models)
INSERT INTO VehicleModel (brand, modelName, year, transmissionType, pricePerDay) VALUES 
('Toyota', 'Corolla', 2023, 'Automatic', 1200.00),
('Renault', 'Clio', 2024, 'Manual', 900.00),
('Honda', 'Civic', 2022, 'Automatic', 1300.00),
('BMW', '320i', 2023, 'Automatic', 2500.00),
('Mercedes', 'C200', 2023, 'Automatic', 2700.00),
('Fiat', 'Egea', 2024, 'Manual', 800.00),
('Ford', 'Focus', 2022, 'Automatic', 1100.00),
('Hyundai', 'i20', 2023, 'Manual', 850.00),
('Peugeot', '3008', 2023, 'Automatic', 1800.00),
('Volkswagen', 'Golf', 2022, 'Automatic', 1400.00);

-- 3. Employees (15 Employees)
INSERT INTO Employee (branchID, firstName, lastName, role, salary) VALUES
(1, 'Ahmet', 'Yilmaz', 'Manager', 35000.00),
(1, 'Mehmet', 'Demir', 'Salesperson', 22000.00),
(1, 'Ayse', 'Kara', 'Technician', 24000.00),
(1, 'Fatma', 'Celik', 'Salesperson', 22000.00),
(2, 'Mustafa', 'Ozturk', 'Manager', 34000.00),
(2, 'Emre', 'Aydin', 'Salesperson', 21500.00),
(2, 'Can', 'Ural', 'Salesperson', 21500.00),
(3, 'Zeynep', 'Koc', 'Manager', 33000.00),
(3, 'Burak', 'Arslan', 'Technician', 23000.00),
(3, 'Elif', 'Polat', 'Salesperson', 21000.00),
(4, 'Kemal', 'Sunal', 'Manager', 32000.00),
(4, 'Sener', 'Sen', 'Technician', 22500.00),
(5, 'Haluk', 'Bilginer', 'Manager', 36000.00),
(5, 'Cem', 'Yilmaz', 'Salesperson', 23000.00),
(5, 'Ozan', 'Guven', 'Technician', 24000.00);

-- 4. Customers (15 Customers)
INSERT INTO Customer (firstName, lastName, licenseNo, phone, email, address) VALUES
('Ali', 'Veli', 'B123456', '05321112233', 'ali@test.com', 'Istanbul'),
('Veli', 'Deli', 'B654321', '05332223344', 'veli@test.com', 'Istanbul'),
('Hakan', 'Calhanoglu', 'B987654', '05445556677', 'hakan@test.com', 'Ankara'),
('Arda', 'Turan', 'B111222', '05556667788', 'arda@test.com', 'Istanbul'),
('Fatih', 'Terim', 'B333444', '05329998877', 'fatih@test.com', 'Izmir'),
('Muslera', 'Fernando', 'B555666', '05311231234', 'nando@test.com', 'Istanbul'),
('Alex', 'De Souza', 'B777888', '05419876543', 'alex@test.com', 'Istanbul'),
('Sergen', 'Yalcin', 'B999000', '05551112222', 'sergen@test.com', 'Istanbul'),
('Cengiz', 'Under', 'B121212', '05334445566', 'cengiz@test.com', 'Roma'),
('Merih', 'Demiral', 'B343434', '05325556611', 'merih@test.com', 'Riyad'),
('Hidayet', 'Turkoglu', 'B565656', '05423334455', 'hedo@test.com', 'Istanbul'),
('Mehmet', 'Okur', 'B787878', '05327778899', 'memo@test.com', 'Utah'),
('Kenan', 'Imirzali', 'B909090', '05552223311', 'ezel@test.com', 'Istanbul'),
('Kivanc', 'Tatlitug', 'B010101', '05338889900', 'kuzey@test.com', 'Istanbul'),
('Baris', 'Manco', 'B232323', '05417777777', 'baris@test.com', 'Istanbul');

-- 5. Vehicles (20 Vehicles)
-- Status mixed: Available, Rented, Maintenance
INSERT INTO Vehicle (plateNo, modelID, branchID, color, kilometer, fuelType, status) VALUES
('34ABC01', 1, 1, 'White', 15000, 'Hybrid', 'Available'),
('34ABC02', 1, 1, 'Silver', 12000, 'Hybrid', 'Rented'),
('34ABC03', 2, 1, 'Red', 45000, 'Diesel', 'Available'),
('34ABC04', 2, 2, 'Blue', 30000, 'Diesel', 'Available'),
('34ABC05', 3, 2, 'Black', 20000, 'Gasoline', 'Maintenance'),
('06XYZ01', 3, 3, 'White', 22000, 'Gasoline', 'Available'),
('06XYZ02', 6, 3, 'Grey', 5000, 'Diesel', 'Rented'),
('06XYZ03', 4, 3, 'Black', 10000, 'Gasoline', 'Available'),
('35KLM01', 5, 4, 'Black', 8000, 'Hybrid', 'Available'),
('35KLM02', 5, 4, 'White', 15000, 'Hybrid', 'Maintenance'),
('35KLM03', 7, 4, 'Blue', 35000, 'Diesel', 'Available'),
('07ANT01', 8, 5, 'Red', 25000, 'Gasoline', 'Available'),
('07ANT02', 9, 5, 'Silver', 18000, 'Diesel', 'Rented'),
('07ANT03', 10, 5, 'White', 12000, 'Gasoline', 'Available'),
('34DEF01', 1, 1, 'Black', 5000, 'Hybrid', 'Available'),
('34DEF02', 2, 2, 'White', 40000, 'Diesel', 'Rented'),
('34DEF03', 6, 3, 'Red', 1000, 'Diesel', 'Available'),
('35GHI01', 8, 4, 'Grey', 50000, 'Gasoline', 'Available'),
('07JKL01', 9, 5, 'Blue', 20000, 'Diesel', 'Available'),
('34MNO01', 10, 1, 'Black', 5000, 'Gasoline', 'Available');

-- 6. Additional Services
INSERT INTO AdditionalServices (serviceName, description, cost) VALUES
('GPS Navigation', 'Satellite Navigation System', 50.00),
('Child Seat', 'Safety seat for children', 75.00),
('Additional Driver', 'Permission for second driver', 100.00),
('Full Insurance', 'Comprehensive damage coverage', 250.00),
('Snow Chains', 'Winter tire chains', 40.00);

-- 7. Reservations (20 records)
INSERT INTO Reservation (customerID, vehicleID, startDate, endDate, totalPrice, status) VALUES
(1, 1, '2024-01-01', '2024-01-05', 6000.00, 'Completed'),
(2, 2, '2024-01-02', '2024-01-07', 7200.00, 'Completed'),
(3, 3, '2024-01-10', '2024-01-12', 2000.00, 'Completed'),
(4, 7, '2024-02-01', '2024-02-05', 4000.00, 'Confirmed'),
(5, 5, '2024-02-15', '2024-02-20', 6500.00, 'Cancelled'),
(6, 13, '2024-03-01', '2024-03-10', 18000.00, 'Confirmed'),
(7, 16, '2024-04-01', '2024-04-03', 2700.00, 'Confirmed'),
(8, 2, '2024-05-01', '2024-05-05', 6000.00, 'Pending'),
(9, 6, '2024-05-10', '2024-05-15', 4000.00, 'Pending'),
(10, 4, '2024-06-01', '2024-06-05', 12500.00, 'Completed'),
(1, 8, '2024-06-10', '2024-06-12', 1700.00, 'Completed'),
(2, 9, '2024-07-01', '2024-07-05', 9000.00, 'Completed'),
(3, 10, '2024-07-10', '2024-07-15', 7000.00, 'Completed'),
(4, 11, '2024-08-01', '2024-08-03', 3300.00, 'Completed'),
(5, 12, '2024-08-10', '2024-08-20', 8500.00, 'Completed'),
(6, 14, '2024-09-01', '2024-09-05', 7000.00, 'Completed'),
(7, 15, '2024-09-10', '2024-09-12', 2400.00, 'Completed'),
(8, 17, '2024-10-01', '2024-10-02', 1800.00, 'Completed'),
(9, 18, '2024-10-05', '2024-10-10', 4250.00, 'Completed'),
(10, 19, '2024-11-01', '2024-11-05', 9000.00, 'Completed');

-- 8. Rentals (Linked to some reservations)
INSERT INTO Rental (reservationID, totalDays, totalCost) VALUES
(1, 4, 6000.00), -- paymentID added later
(2, 5, 7200.00),
(3, 2, 2000.00),
(4, 4, 4000.00),
(6, 9, 18000.00),
(7, 2, 2700.00),
(10, 4, 12500.00),
(11, 2, 1700.00),
(12, 4, 9000.00),
(13, 5, 7000.00);

-- 9. Payments (Linked to Rentals)
INSERT INTO Payment (rentalID, amount, paymentMethod) VALUES
(1, 6000.00, 'Credit Card'),
(2, 7200.00, 'Credit Card'),
(3, 2000.00, 'Cash'),
(4, 4000.00, 'Credit Card'),
(5, 18000.00, 'Bank Transfer'),
(6, 2700.00, 'Credit Card'),
(7, 12500.00, 'Credit Card'),
(8, 1700.00, 'Cash'),
(9, 9000.00, 'Credit Card'),
(10, 7000.00, 'Credit Card');

-- Update Rentals with Payment IDs (Manually matching for seed data)
UPDATE Rental SET paymentID = 1 WHERE rentalID = 1;
UPDATE Rental SET paymentID = 2 WHERE rentalID = 2;
UPDATE Rental SET paymentID = 3 WHERE rentalID = 3;
UPDATE Rental SET paymentID = 4 WHERE rentalID = 4;
UPDATE Rental SET paymentID = 5 WHERE rentalID = 5;
UPDATE Rental SET paymentID = 6 WHERE rentalID = 6;
UPDATE Rental SET paymentID = 7 WHERE rentalID = 7;
UPDATE Rental SET paymentID = 8 WHERE rentalID = 8;
UPDATE Rental SET paymentID = 9 WHERE rentalID = 9;
UPDATE Rental SET paymentID = 10 WHERE rentalID = 10;

-- 10. Insurance
INSERT INTO Insurance (vehicleID, policyType, provider, startDate, expiryDate, cost) VALUES
(1, 'Full Coverage', 'Axa', '2024-01-01', '2025-01-01', 5000.00),
(2, 'Full Coverage', 'Allianz', '2024-01-01', '2025-01-01', 5500.00),
(3, 'Basic', 'Mapfre', '2024-01-01', '2025-01-01', 4000.00),
(4, 'Basic', 'Anadolu', '2024-01-01', '2025-01-01', 4000.00),
(5, 'Full Coverage', 'Axa', '2024-01-01', '2025-01-01', 6000.00),
(6, 'Full Coverage', 'Allianz', '2024-01-01', '2025-01-01', 5000.00),
(7, 'Basic', 'Mapfre', '2024-01-01', '2025-01-01', 3500.00),
(8, 'Basic', 'Anadolu', '2024-01-01', '2025-01-01', 3500.00),
(9, 'Full Coverage', 'Axa', '2024-01-01', '2025-01-01', 5200.00),
(10, 'Full Coverage', 'Allianz', '2024-01-01', '2025-01-01', 5300.00),
(11, 'Basic', 'Mapfre', '2024-01-01', '2025-01-01', 3000.00),
(12, 'Basic', 'Anadolu', '2024-01-01', '2025-01-01', 3000.00),
(13, 'Full Coverage', 'Axa', '2024-01-01', '2025-01-01', 7000.00),
(14, 'Full Coverage', 'Allianz', '2024-01-01', '2025-01-01', 6500.00),
(15, 'Basic', 'Mapfre', '2024-01-01', '2025-01-01', 4500.00),
(16, 'Basic', 'Anadolu', '2024-01-01', '2025-01-01', 4200.00),
(17, 'Full Coverage', 'Axa', '2024-01-01', '2025-01-01', 3500.00),
(18, 'Basic', 'Mapfre', '2024-01-01', '2025-01-01', 2500.00),
(19, 'Full Coverage', 'Allianz', '2024-01-01', '2025-01-01', 5800.00),
(20, 'Basic', 'Anadolu', '2024-01-01', '2025-01-01', 3800.00);

-- 11. Maintenance (Weak entity logic: vehicleID + maintenanceID)
INSERT INTO Maintenance (vehicleID, maintenanceID, date, description, cost) VALUES
(5, 1, '2024-01-15', 'Oil Change', 1500.00),
(5, 2, '2024-06-15', 'Brake Pad Replacement', 2500.00),
(10, 1, '2024-02-20', 'Tire Change', 4000.00),
(1, 1, '2023-12-01', 'Annual Service', 3000.00),
(2, 1, '2023-11-15', 'Battery Replacement', 2000.00),
(3, 1, '2024-03-30', 'Oil Change', 1500.00),
(6, 1, '2024-05-10', 'Filter Change', 500.00),
(7, 1, '2024-06-20', 'Routine Checkup', 1000.00),
(8, 1, '2024-07-01', 'Engine Diagnostic', 1200.00),
(9, 1, '2024-08-15', 'AC Repair', 2500.00);

-- 12. Reservation Services
INSERT INTO Reservation_Services (reservationID, serviceID) VALUES
(1, 1), (1, 5),
(2, 2),
(3, 3),
(4, 4),
(5, 1),
(6, 4), (6, 3),
(7, 2),
(8, 1),
(9, 5),
(10, 4);
