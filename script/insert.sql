INSERT
INTO
    oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri,
    authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES
    ('cliente-app', 'books', '{noop}123456', 'read,write', 'password,authorization_code,refresh_token', 'http://localhost:9000/callback','read,write', 3000, -1, NULL, 'false');
GO

INSERT
INTO
    oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri,
    authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES
    ('resource-server', 'books', '{noop}abc', 'read,write', 'password,authorization_code,refresh_token', 'http://localhost:9000/callback','introspection', 3000, -1, NULL, 'false');
GO

INSERT INTO user_bookserver(email, name, password) VALUES ('bookserver@bookserver.com', 'bookserver', 'bookserver');
GO
