package com.djn.feign.domain;

import lombok.Data;

/**
 * Name: User
 * Description: 用户
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 17:28
 */
@Data
public class User {
    private Long id;
    private String username;
    private String address;
}
