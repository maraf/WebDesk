SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Vypisuji data pro tabulku `core_accesstype`
--

INSERT INTO `core_accesstype` (`id`, `system_value`, `category`, `name`, `perm_level`) VALUES
(1, '', 'fsys', 'Read', 1),
(2, '', 'fsys', 'ReadWrite', 2),
(3, '', 'fsys', 'Manage', 3);
--
-- Vypisuji data pro tabulku `core_identitybase`
--

INSERT INTO `core_identitybase` (`id`, `system_value`) VALUES
(1, ''),
(2, ''),
(3, '');

--
-- Vypisuji data pro tabulku `core_permission`
--

INSERT INTO `core_permission` (`permtype`, `id`, `system_value`, `identity_id`, `type_id`, `target_id`) VALUES
('fsysperm', 1, '', 1, 3, 1),
('fsysperm', 2, '', 1, 3, 2);

--
-- Vypisuji data pro tabulku `core_user`
--

INSERT INTO `core_user` (`created`, `enabled`, `name`, `password`, `surname`, `username`, `id`) VALUES
('2011-01-18 15:31:05', '', 'admin', 'dd94709528bb1c83d08f3088d4043f4742891f4f', 'admin', 'admin', 3);

--
-- Vypisuji data pro tabulku `core_userinrole`
--

INSERT INTO `core_userinrole` (`user_id`, `userrole_id`) VALUES
(3, 1);

--
-- Vypisuji data pro tabulku `core_userrole`
--

INSERT INTO `core_userrole` (`name`, `id`, `parent_id`) VALUES
('admins', 1, null),
('everyone', 2, null);

--
-- Vypisuji data pro tabulku `fs_directory`
--

INSERT INTO `fsys_directory` (`id`) VALUES
(1),
(2);

--
-- Vypisuji data pro tabulku `fs_drive`
--

INSERT INTO `fsys_drive` (`id`, `system_value`, `label`, `name`, `directory_id`, `owner_id`, `url`, `physicalPath`) VALUES
(1, '', NULL, 'System', 1, NULL, NULL, 'D:/Temp/FileBrowser/System'),
(2, '', NULL, 'Users', 2, NULL, NULL, 'D:/Temp/FileBrowser/Users');

--
-- Vypisuji data pro tabulku `fs_filesystemitem`
--

INSERT INTO `fsys_filesystemitem` (`id`, `system_value`, `created`, `modified`, `name`, `parent_id`, `is_public`, `public_id`) VALUES
(1, '', '2011-02-23 15:38:10', '2011-02-23 15:38:17', 'System', NULL, 1, "44b98276-b17f-4639-81c6-08754317025f"),
(2, '', '2011-02-23 15:38:10', '2011-02-23 15:38:17', 'Users', NULL, 1, "fcf812a1-dd13-40c0-b769-fd4aeddaa98c");