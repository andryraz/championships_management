INSERT INTO goal (id, match_id, scorer_id, club_id, minute_of_goal, own_goal)
VALUES
  (gen_random_uuid(), 'uuid_match', 'uuid_gardien1', 'uuid_c1', 23, FALSE),
  (gen_random_uuid(), 'uuid_match', 'uuid_defense2', 'uuid_c2', 57, FALSE);