-- SmartCar Database Programmability

-- View to show detailed rental information (Customer + Vehicle + Rental info)
CREATE OR REPLACE VIEW vw_RentalDetails AS
SELECT 
    r.rentalID,
    c.firstName || ' ' || c.lastName AS CustomerName,
    m.brand || ' ' || m.modelName AS VehicleModel,
    v.plateNo,
    res.startDate,
    res.endDate,
    r.totalCost,
    r.totalDays,
    p.amount AS PaidAmount,
    p.paymentMethod
FROM Rental r
JOIN Reservation res ON r.reservationID = res.reservationID
JOIN Customer c ON res.customerID = c.customerID
JOIN Vehicle v ON res.vehicleID = v.vehicleID
JOIN VehicleModel m ON v.modelID = m.modelID
LEFT JOIN Payment p ON r.rentalID = p.rentalID;

-- View used for lookup in UI (Lookup Table)
CREATE OR REPLACE VIEW vw_AvailableVehicles AS
SELECT 
    v.vehicleID,
    m.brand, 
    m.modelName, 
    v.plateNo, 
    m.pricePerDay
FROM Vehicle v
JOIN VehicleModel m ON v.modelID = m.modelID
WHERE v.status = 'Available';


-- ==========================================
-- 2. STORED PROCEDURES
-- ==========================================
-- Procedure to add a new reservation ensuring vehicle is available
CREATE OR REPLACE PROCEDURE sp_CreateReservation(
    p_customerID INT,
    p_vehicleID INT,
    p_startDate DATE,
    p_endDate DATE
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_pricePerDay DECIMAL(10,2);
    v_totalPrice DECIMAL(10,2);
    v_days INT;
    v_vehicleStatus VARCHAR(20);
BEGIN
    -- Check if vehicle exists and get price
    SELECT m.pricePerDay, v.status INTO v_pricePerDay, v_vehicleStatus
    FROM Vehicle v
    JOIN VehicleModel m ON v.modelID = m.modelID
    WHERE v.vehicleID = p_vehicleID;

    IF v_pricePerDay IS NULL THEN
        RAISE EXCEPTION 'Vehicle not found.';
    END IF;

    -- Check if vehicle is available (Simple check, ideally should check date ranges)
    IF v_vehicleStatus <> 'Available' THEN
        RAISE EXCEPTION 'Vehicle is currently not available (Status: %)', v_vehicleStatus;
    END IF;

    -- Calculate Total Price
    v_days := p_endDate - p_startDate;
    IF v_days <= 0 THEN
        RAISE EXCEPTION 'End date must be after start date.';
    END IF;

    v_totalPrice := v_days * v_pricePerDay;

    -- Insert Reservation
    INSERT INTO Reservation (customerID, vehicleID, startDate, endDate, totalPrice, status)
    VALUES (p_customerID, p_vehicleID, p_startDate, p_endDate, v_totalPrice, 'Confirmed');

    -- Auto-update vehicle status to Rented (or Reserved) 
    UPDATE Vehicle SET status = 'Rented' WHERE vehicleID = p_vehicleID;

    RAISE NOTICE 'Reservation created successfully. Total Price: %', v_totalPrice;
END;
$$;


-- ==========================================
-- 3. TRIGGERS 
-- ==========================================
-- Trigger to prevent renting a vehicle if it is in Maintenance

CREATE OR REPLACE FUNCTION fn_CheckVehicleMaintenance()
RETURNS TRIGGER AS $$
DECLARE
    v_status VARCHAR(20);
BEGIN
    SELECT status INTO v_status FROM Vehicle WHERE vehicleID = NEW.vehicleID;
    
    IF v_status = 'Maintenance' THEN
        RAISE EXCEPTION 'Cannot reserve/rent a vehicle that is in Maintenance.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_PreventMaintenanceRental
BEFORE INSERT ON Reservation
FOR EACH ROW
EXECUTE FUNCTION fn_CheckVehicleMaintenance();


-- ==========================================
-- 4. CURSORS 
-- ==========================================
-- Function that uses a cursor to calculate total revenue for a specific branch
CREATE OR REPLACE FUNCTION fn_CalculateBranchRevenue(p_branchID INT)
RETURNS DECIMAL(10,2) AS $$
DECLARE
    v_totalRevenue DECIMAL(10,2) := 0;
    v_amount DECIMAL(10,2);
    
    -- Cursor declaration
    cur_payments CURSOR FOR 
        SELECT p.amount 
        FROM Payment p
        JOIN Rental r ON p.rentalID = r.rentalID
        JOIN Reservation res ON r.reservationID = res.reservationID
        JOIN Vehicle v ON res.vehicleID = v.vehicleID
        WHERE v.branchID = p_branchID;
BEGIN
    OPEN cur_payments;
    
    LOOP
        FETCH cur_payments INTO v_amount;
        EXIT WHEN NOT FOUND;
        v_totalRevenue := v_totalRevenue + v_amount;
    END LOOP;
    
    CLOSE cur_payments;
    
    RETURN v_totalRevenue;
END;
$$ LANGUAGE plpgsql;
