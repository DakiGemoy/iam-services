BEGIN;

INSERT INTO role (role_name, role_desc)
VALUES
    ('User', NULL),
    ('Manager', NULL),
    ('Admin', NULL);

INSERT INTO access_catalog (access_name, base_url, created_at)
VALUES
    ('Figma', NULL, NOW()),
    ('Gitlab', NULL, NOW()),
    ('Jenkins', NULL, NOW()),
    ('Jira', NULL, NOW()),
    ('Senhasegura', NULL, NOW());

-- semua password block ini berasal dari bcrypt encoder yang karakter aslinya adalah 'P@ssw0rd'
INSERT INTO "user" (username, password, email, manager_id, last_updated, is_blocked)
VALUES
    (
        'andri.santoso',
        '$2a$10$PsxArh.naGnJSta9HTVEJObW4fywhC3Ae88fjzgB2PeV/8.DmxPgS',
        'andri.santoso@example.com',
        NULL,
        NOW(),
        FALSE
    ),
    (
        'budi.hartono',
        '$2a$10$PsxArh.naGnJSta9HTVEJObW4fywhC3Ae88fjzgB2PeV/8.DmxPgS',
        'budi.hartono@example.com',
        NULL,
        NOW(),
        FALSE
    ),
    (
        'citra.lestari',
        '$2a$10$PsxArh.naGnJSta9HTVEJObW4fywhC3Ae88fjzgB2PeV/8.DmxPgS',
        'citra.lestari@example.com',
        NULL,
        NOW(),
        FALSE
    ),
    (
        'dimas.pratama',
        '$2a$10$PsxArh.naGnJSta9HTVEJObW4fywhC3Ae88fjzgB2PeV/8.DmxPgS',
        'dimas.pratama@example.com',
        1,
        NOW(),
        FALSE
    ),
    (
        'eka.wulandari',
        '$2a$10$PsxArh.naGnJSta9HTVEJObW4fywhC3Ae88fjzgB2PeV/8.DmxPgS',
        'eka.wulandari@example.com',
        3,
        NOW(),
        FALSE
    );

INSERT INTO user_role (user_id, role_id, last_updated)
VALUES ( 1,1,NOW() ), ( 1,3,NOW() ),
     ( 2,1,NOW() ), ( 2,2,NOW() ),
     ( 3,1,NOW() ),
     ( 4,1,NOW() ),
     ( 5,1,NOW() ), ( 5,2,NOW() );

INSERT INTO user_access(user_id, access_catalog_id, created_at)
VALUES (1, 1, NOW()), (1, 3, NOW()), (1, 4, NOW()),
    (2, 2, NOW()), (2, 3, NOW()), (2, 4, NOW()),
    (3, 1, NOW()), (3, 2, NOW()), (3, 3, NOW()), (3, 4, NOW()), (3, 5, NOW()),
    (4, 1, NOW()), (4, 5, NOW()),
    (5, 5, NOW());

END;
