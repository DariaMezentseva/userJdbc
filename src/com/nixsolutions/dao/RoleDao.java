package com.nixsolutions.dao;

import com.nixsolutions.entity.Role;

public interface RoleDao {
    void create(Role role);

    void update(Role role);

    void remove(Role role);

    Role findByName(String name);
}
