INSERT INTO template (id, name, subject, body) VALUES
(UUID_TO_BIN(UUID()), 'Account activation TEMPLATE', 'Account activation',
 '<h1 th:text="${nickName + '''', activate your account by clicking this url:'''' + url}"></h1>'),


(UUID_TO_BIN(UUID()), 'Admin role request TEMPLATE', 'Request for giving admin role',
 '<h1 th:text="${''''User '''' + nickName + '''' wants to become an admin. Give him this role by clicking on the link below (if you think it is a good idea.) '''' + url}"></h1>'),


(UUID_TO_BIN(UUID()), 'Give admin role TEMPLATE', 'Confirmation of assigning a new role',
 '<h1 th:text="${''''Congratulations '''' + nickName + ''''! You are now an admin! You can delete and edit vehicles added by other people.''''}"></h1>'),


(UUID_TO_BIN(UUID()), 'Remove admin role TEMPLATE', 'Confirmation of removing the role from the user',
 '<h1 th:text="${''''User '''' + nickName + '''', unfortunately you have been stripped of your admin role. It was taken away by user '''' + adminName + ''''. if you have any problem please contact us (same email address from which you got this message).''''}"></h1>');