
INSERT INTO additional_equipment (id, air_conditioning, control, headlights)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e6"), "FourOrMultiZoneAutomatic", "AdaptiveCruise", "FullLed");


INSERT INTO body (id, body_type, number_of_doors, seats, additional_equipment_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e7"), "Minivan", 5, 7, UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e6"));


INSERT INTO euroncap (id, adult_occupant_percentage, child_occupant_percentage, safety_assist_percentage, vulnerable_road_users_percentage)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e8"), 92, 87, 60, 90);


INSERT INTO engine (id, energy_source, engine_torque_in_nm, hp)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e9"), "Electricity", 310, 201);


INSERT INTO vehicle (id, acceleration, brand, created_by, drive, euro_car_segment, generation, gross_weight, kerb_weight,
                     model, transmission, year_of_production, body_id, euroncap_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5a9"), 6.4,
        "Volkswagen", "bodziov3@gmail.com", "AWDPermanent", "M", 1, 3000, 2384, "ID. Buzz", "automatic", 2022,
        UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e7"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e8"));


INSERT INTO vehicle_engine (vehicle_id, engines_id)
VALUES (UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5a9"), UNHEX("bc9d8e235f4a4b3c88d72a1b3c4d5e9"));