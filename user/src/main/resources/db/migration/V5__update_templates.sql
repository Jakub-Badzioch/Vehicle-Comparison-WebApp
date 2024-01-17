UPDATE template
SET `body` = '<h1 th:text="${nickName + \', activate your account by clicking this url:\' + url}"></h1>'
WHERE `id` = uuid_to_bin('229e2131-9dcd-11ee-86cf-305a3a066a2f');

UPDATE template
SET `body` = '<h1 th:text="${\'User \' + nickName + \' wants to become an admin. Give him this role by clicking on the link below (if you think it is a good idea.) \' + url}"></h1>'
WHERE `id` = uuid_to_bin('229e2668-9dcd-11ee-86cf-305a3a066a2f');

UPDATE template
SET `body` = '<h1 th:text="${\'Congratulations \' + nickName + \'! You are now an admin! You can delete and edit vehicles added by other people.\'}"></h1>'
WHERE `id` = uuid_to_bin('229e2746-9dcd-11ee-86cf-305a3a066a2f');

UPDATE template
SET `body` = '<h1 th:text="${\'User \' + nickName + \', unfortunately you have been stripped of your admin role. It was taken away by user \' + adminName + \'. if you have any problem please contact us (same email address from which you got this message).\'}"></h1>'
WHERE `id` = uuid_to_bin('229e27aa-9dcd-11ee-86cf-305a3a066a2f');
