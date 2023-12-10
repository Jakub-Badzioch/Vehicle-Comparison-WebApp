
INSERT INTO additional_equipment (id, air_conditioning, control, headlights)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f0"), "FourOrMultiZoneAutomatic", "AdaptiveCruise", "Xenon");


INSERT INTO body (id, body_type, number_of_doors, seats, additional_equipment_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f1"), "Convertible", 5, 7, UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f0"));


INSERT INTO euroncap (id, adult_occupant_percentage, child_occupant_percentage, safety_assist_percentage, vulnerable_road_users_percentage)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f2"), 92, 87, 60, 90);


INSERT INTO engine (id, energy_source, engine_torque_in_nm, hp)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f3"), "Hybrid", 310, 201);


INSERT INTO vehicle (id, acceleration, brand, created_by, drive, euro_car_segment, generation, gross_weight, kerb_weight,
                     model, transmission, year_of_production, body_id, euroncap_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f4"), 6.4,
        "Volkswagen", "bodziov3@gmail.com", "AWDManuallyAttached", "C", 1, 3000, 2384, "Qe", "automatic", 2022,
        UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f1"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f2"));


INSERT INTO vehicle_engine (vehicle_id, engines_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f4"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f3"));






INSERT INTO additional_equipment (id, air_conditioning, control, headlights)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f5"), "ThreeZoneAutomatic", "PredictiveCruise", "Halogen");


INSERT INTO body (id, body_type, number_of_doors, seats, additional_equipment_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f6"), "PanelTruck", 4, 5, UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f5"));


INSERT INTO euroncap (id, adult_occupant_percentage, child_occupant_percentage, safety_assist_percentage, vulnerable_road_users_percentage)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f7"), 88, 82, 70, 85);


INSERT INTO engine (id, energy_source, engine_torque_in_nm, hp)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f8"), "NaturalGas", 250, 180);


INSERT INTO vehicle (id, acceleration, brand, created_by, drive, euro_car_segment, generation, gross_weight, kerb_weight,
                     model, transmission, year_of_production, body_id, euroncap_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f9"), 7.2,
        "Toyota", "user@example.com", "FWD", "D", 2, 1800, 1500, "Camry", "automatic", 2023,
        UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f6"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f7"));


INSERT INTO vehicle_engine (vehicle_id, engines_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f9"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5f8"));







INSERT INTO additional_equipment (id, air_conditioning, control, headlights)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fa"), "Automatic", "Cruise", "FullLed");


INSERT INTO body (id, body_type, number_of_doors, seats, additional_equipment_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fb"), "SUV", 5, 5, UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fa"));


INSERT INTO euroncap (id, adult_occupant_percentage, child_occupant_percentage, safety_assist_percentage, vulnerable_road_users_percentage)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fc"), 90, 85, 75, 88);


INSERT INTO engine (id, energy_source, engine_torque_in_nm, hp)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fd"), "Diesel", 320, 220);


INSERT INTO vehicle (id, acceleration, brand, created_by, drive, euro_car_segment, generation, gross_weight, kerb_weight,
                     model, transmission, year_of_production, body_id, euroncap_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fe"), 8.0,
        "Ford", "user@example.com", "AWDPermanent", "J", 3, 2200, 1800, "Explorer", "automatic", 2013,
        UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fb"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fc"));


INSERT INTO vehicle_engine (vehicle_id, engines_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fe"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5fd"));






-- Insert into additional_equipment table
INSERT INTO additional_equipment (id, air_conditioning, control, headlights)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5ff"), "DualZoneAutomatic", "AdaptiveCruise", "FrontLED");

-- Insert into body table
INSERT INTO body (id, body_type, number_of_doors, seats, additional_equipment_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d500"), "Hatchback", 5, 5, UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5ff"));

-- Insert into euroncap table
INSERT INTO euroncap (id, adult_occupant_percentage, child_occupant_percentage, safety_assist_percentage, vulnerable_road_users_percentage)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d501"), 94, 89, 80, 92);

-- Insert into engine table
INSERT INTO engine (id, energy_source, engine_torque_in_nm, hp)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d502"), "Hybrid", 280, 190);

-- Insert into vehicle table
INSERT INTO vehicle (id, acceleration, brand, created_by, drive, euro_car_segment, generation, gross_weight, kerb_weight,
                     model, transmission, year_of_production, body_id, euroncap_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d503"), 7.8,
        "Mercedes", "user@example.com", "RWD", "D", 2, 1900, 1600, "NX Hybrid", "automatic", 2003,
        UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d500"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d501"));

-- Insert into vehicle_engine table
INSERT INTO vehicle_engine (vehicle_id, engines_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d503"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d502"));
