-- SmartCar Database Schema

-- 1. Branch Table
CREATE TABLE Branch (
    branchID SERIAL PRIMARY KEY,
    branchName VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    address TEXT,
    phone VARCHAR(20)
);

-- 2. Employee Table (ISA Relationship: Manager, Technician, Salesperson)
-- Single Table Inheritance with Discriminator 'role'
-- Requirement: Disjoint Constraint (Employee can only be one role at a time)
CREATE TABLE Employee (
    employeeID SERIAL PRIMARY KEY,
    branchID INT NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    salary DECIMAL(10, 2),
    FOREIGN KEY (branchID) REFERENCES Branch(branchID) ON DELETE SET NULL,
    CONSTRAINT chk_role CHECK (role IN ('Manager', 'Technician', 'Salesperson'))
);

-- 3. Customer Table
CREATE TABLE Customer (
    customerID SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    licenseNo VARCHAR(20) UNIQUE NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

-- 4. VehicleModel Table
CREATE TABLE VehicleModel (
    modelID SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    modelName VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    transmissionType VARCHAR(20) CHECK (transmissionType IN ('Manual', 'Automatic')),
    pricePerDay DECIMAL(10, 2) NOT NULL
);

-- 5. Vehicle Table
-- Requirement: Exclusion Constraint (Vehicle cannot be Rented and Maintenance at same time)
-- Controlled by 'status' attribute.
CREATE TABLE Vehicle (
    vehicleID SERIAL PRIMARY KEY,
    plateNo VARCHAR(20) UNIQUE NOT NULL,
    modelID INT NOT NULL,
    branchID INT NOT NULL,
    color VARCHAR(30),
    kilometer INT DEFAULT 0,
    fuelType VARCHAR(20),
    status VARCHAR(20) DEFAULT 'Available' CHECK (status IN ('Available', 'Rented', 'Maintenance')),
    FOREIGN KEY (modelID) REFERENCES VehicleModel(modelID),
    FOREIGN KEY (branchID) REFERENCES Branch(branchID)
);

-- 6. Maintenance Table (Weak Entity)
-- Dependent on Vehicle. PK is composite (vehicleID, maintenanceID).
CREATE TABLE Maintenance (
    vehicleID INT NOT NULL,
    maintenanceID INT NOT NULL, -- Partial Key
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    description TEXT,
    cost DECIMAL(10, 2),
    PRIMARY KEY (vehicleID, maintenanceID),
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID) ON DELETE CASCADE
);

-- 7. Additional Services Table
CREATE TABLE AdditionalServices (
    serviceID SERIAL PRIMARY KEY,
    serviceName VARCHAR(100) NOT NULL,
    description TEXT,
    cost DECIMAL(10, 2) NOT NULL
);

-- 8. Reservation Table
CREATE TABLE Reservation (
    reservationID SERIAL PRIMARY KEY,
    customerID INT NOT NULL,
    vehicleID INT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    totalPrice DECIMAL(10, 2),
    reservationDate DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'Pending' CHECK (status IN ('Pending', 'Confirmed', 'Cancelled', 'Completed')),
    FOREIGN KEY (customerID) REFERENCES Customer(customerID) ON DELETE CASCADE,
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID),
    CONSTRAINT chk_dates CHECK (endDate >= startDate)
);

-- 9. Rental Table
-- Circular Dependency with Payment: PaymentID is nullable here to allow insertion of Rental first.
CREATE TABLE Rental (
    rentalID SERIAL PRIMARY KEY,
    reservationID INT UNIQUE NOT NULL, -- One Reservation -> One Rental
    paymentID INT, -- FK to Payment (Added later via UPDATE)
    totalDays INT,
    totalCost DECIMAL(10, 2),
    FOREIGN KEY (reservationID) REFERENCES Reservation(reservationID)
);

-- 10. Payment Table
CREATE TABLE Payment (
    paymentID SERIAL PRIMARY KEY,
    rentalID INT UNIQUE NOT NULL, -- One Rental -> One Payment
    amount DECIMAL(10, 2) NOT NULL,
    paymentDate DATE DEFAULT CURRENT_DATE,
    paymentMethod VARCHAR(50),
    FOREIGN KEY (rentalID) REFERENCES Rental(rentalID)
);

-- Add the Circular FK for Rental -> Payment
ALTER TABLE Rental
ADD CONSTRAINT fk_rental_payment
FOREIGN KEY (paymentID) REFERENCES Payment(paymentID);

-- 11. Insurance Table
-- 1-to-1 Relationship with Vehicle (Assuming one active policy per vehicle)
CREATE TABLE Insurance (
    insuranceID SERIAL PRIMARY KEY,
    vehicleID INT UNIQUE NOT NULL,
    policyType VARCHAR(50),
    provider VARCHAR(100),
    startDate DATE,
    expiryDate DATE,
    cost DECIMAL(10, 2),
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID) ON DELETE CASCADE
);

-- Junction Table for Many-to-Many: Reservation <-> AdditionalServies
CREATE TABLE Reservation_Services (
    reservationID INT NOT NULL,
    serviceID INT NOT NULL,
    PRIMARY KEY (reservationID, serviceID),
    FOREIGN KEY (reservationID) REFERENCES Reservation(reservationID) ON DELETE CASCADE,
    FOREIGN KEY (serviceID) REFERENCES AdditionalServices(serviceID) ON DELETE CASCADE
);
