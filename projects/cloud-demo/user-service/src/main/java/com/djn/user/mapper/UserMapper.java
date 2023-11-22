package com.djn.user.mapper;

import com.djn.user.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Name: UserMapper
 * Description: 用户Mapper
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 15:36
 */
public interface UserMapper {

    User findById(@Param("id") Long id);
}
