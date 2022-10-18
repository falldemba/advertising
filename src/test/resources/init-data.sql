-- LM_Dealer
INSERT INTO LM_Dealer
(dealer_id, name, email, dealer_published_listings_limit)
VALUES
    (2, 'name2', 'name2@gmail.com' ,10),
    (3, 'name3', 'name3@gmail.com' ,10),
    (4, 'name4', 'name4@gmail.com' ,1),
    (5, 'name5', 'name5@gmail.com' ,10),
    (6, 'name6', 'name6@gmail.com' ,10),
    (7, 'name7', 'name7@gmail.com' ,10);

-- LM_Listing
INSERT INTO LM_Listing
(listing_id, price, state ,vehicle, dealer_id)
VALUES
    (2, 1023, 'draft', 'DODGE', 2),
    (3, 1024, 'published', 'TMAX', 3),
    (4, 1024, 'published', 'TMAX', 4),
    (5, 1034, 'draft', 'BMW', 2),
    (6, 1045, 'draft', 'GMC', 4),
    (7, 1056, 'draft', 'FORD', 3);