INSERT INTO users (user_id, username, password, name, role, created_at, sold_tickets_no, validated_tickets_no) VALUES(1001, 'admin','$2a$10$.YxXBBkTg1h3D1fj.lsw9uJkzjvoxK5QZ1ssok92APUjN328TIVCS', 'Administrator', 'ROLE_ADMIN', NOW(), 0, 0);
INSERT INTO users (user_id, username, password, name, role, created_at, sold_tickets_no, validated_tickets_no) VALUES(1002, 'test','$2a$10$lfvw4OZd9WNO0f94S3x8heykKFFBPYWL8HRxYM3bTDEwa6YKB8ooe', 'Utilizator', 'ROLE_USER', NOW(), 0, 0);
INSERT INTO tickets (ticket_id, sold_to, sold_to_birthdate, sold_by_user_id, sold_at, validated_by_user_id, validated_at) VALUES('1111', 'Daniel', NULL, 1001, NOW(), NULL, NULL);
INSERT INTO tickets (ticket_id, sold_to, sold_to_birthdate, sold_by_user_id, sold_at, validated_by_user_id, validated_at) VALUES('1112', 'Iasmina', NULL, 1001, NOW(), NULL, NULL);
INSERT INTO tickets (ticket_id, sold_to, sold_to_birthdate, sold_by_user_id, sold_at, validated_by_user_id, validated_at) VALUES('1113', 'Darius', NULL, 1001, NOW(), NULL, NULL);
INSERT INTO tickets (ticket_id, sold_to, sold_to_birthdate, sold_by_user_id, sold_at, validated_by_user_id, validated_at) VALUES('1114', 'Toni', NULL, 1001, NOW(), NULL, NULL);
INSERT INTO tickets (ticket_id, sold_to, sold_to_birthdate, sold_by_user_id, sold_at, validated_by_user_id, validated_at) VALUES('1115', 'Carla', NULL, 1001, NOW(), NULL, NULL);
