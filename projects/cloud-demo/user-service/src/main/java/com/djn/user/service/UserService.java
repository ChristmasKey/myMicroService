package com.djn.user.service;

import com.djn.user.domain.User;

/**
 * Name: UserService
 * Description: 用户Service
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 15:28
 */
public interface UserService {

    User queryById(Long id);
}
