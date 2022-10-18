-- LM_Dealer
INSERT INTO LM_Dealer
(dealer_id, name, email, dealer_published_listings_limit)
VALUES
    (2, 'name2', 'name2@gmail.com' ,2),
    (3, 'name3', 'name3@gmail.com' ,10),
    (4, 'name4', 'name4@gmail.com' ,1),
    (5, 'name5', 'name5@gmail.com' ,10),
    (6, 'name6', 'name6@gmail.com' ,10),
    (7, 'name7', 'name7@gmail.com' ,10);

-- LM_Listing
INSERT INTO LM_Listing
(listing_id, price, state ,vehicle, dealer_id, published_at)
VALUES
    (2, 1023, 'published', 'DODGE', 2, '2022-10-19 00:45:30'),
    (3, 1024, 'published', 'TMAX', 3, '2022-10-19 00:43:30'),
    (4, 1024, 'published', 'TMAX', 4, '2022-10-19 00:43:30'),
    (5, 1023, 'published', 'BMW', 2, '2022-10-19 00:43:30'),
    (6, 1045, 'draft', 'GMC', 2, null),
    (7, 1045, 'draft', 'GMC', 4, null),
    (8, 1056, 'draft', 'FORD', 3, null),
    (9, 1056, 'draft', 'FORD', 6, null);