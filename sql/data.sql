-- 初始角色数据
INSERT INTO `ai_code`.`role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_delete`) VALUES (0, '超级管理员', 'SUPER_ADMIN', '系统超级管理员，拥有所有权限', '2026-03-27 07:07:58', '2026-03-27 07:21:25', 0);
INSERT INTO `ai_code`.`role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_delete`) VALUES (1, '管理员', 'ADMIN', '系统管理员，拥有管理功能权限', '2026-03-27 07:07:58', '2026-03-27 07:21:26', 0);
INSERT INTO `ai_code`.`role` (`id`, `role_name`, `role_code`, `description`, `create_time`, `update_time`, `is_delete`) VALUES (2, '普通用户', 'USER', '系统普通用户，拥有基本功能权限', '2026-03-27 07:07:58', '2026-03-27 07:21:29', 0);

-- 初始用户数据
INSERT INTO `ai_code`.`user` (`id`, `user_account`, `user_password`, `user_name`, `user_avatar`, `user_profile`, `user_role`, `edit_time`, `create_time`, `update_time`, `is_delete`) VALUES (1, 'superadmin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', NULL, NULL, 0, '2026-03-27 07:19:03', '2026-03-27 07:19:03', '2026-03-27 07:21:57', 0);
INSERT INTO `ai_code`.`user` (`id`, `user_account`, `user_password`, `user_name`, `user_avatar`, `user_profile`, `user_role`, `edit_time`, `create_time`, `update_time`, `is_delete`) VALUES (2, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', NULL, NULL, 1, '2026-03-27 07:19:03', '2026-03-27 07:19:03', '2026-03-27 07:19:51', 0);
INSERT INTO `ai_code`.`user` (`id`, `user_account`, `user_password`, `user_name`, `user_avatar`, `user_profile`, `user_role`, `edit_time`, `create_time`, `update_time`, `is_delete`) VALUES (3, 'test', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', NULL, NULL, 2, '2026-03-27 07:19:03', '2026-03-27 07:19:03', '2026-03-27 07:22:00', 0);
