package com.github.dapeng.web.system;

import com.github.dapeng.dto.UserDetail;
import com.github.dapeng.entity.system.TRole;
import com.github.dapeng.entity.system.TRoleUser;
import com.github.dapeng.entity.system.TUsers;
import com.github.dapeng.query.SystemQuery;
import com.github.dapeng.repository.system.RoleRepository;
import com.github.dapeng.repository.system.RoleUserRepository;
import com.github.dapeng.repository.system.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.dapeng.common.Commons.ROLE_PREFIX;

/**
 * @author with struy.
 * Create by 2018/10/13 10:24
 * email :yq1724555319@gmail.com
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        TUsers users = userRepository.findByUsername(name);
        if (null != users) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            List<TRoleUser> roleUserList = roleUserRepository.findAllByUserId(users.getId());
            roleUserList.forEach(x -> {
                TRole role = roleRepository.findOne(x.getRoleId());
                if (null != role) {
                    authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role.getRole()));
                }
            });
            List<Long> list = roleUserList.stream().map(x -> x.getRoleId()).collect(Collectors.toList());
            List<String> urls = SystemQuery.getMenuRolesByRoleId(list);
            UserDetail userDetail = new UserDetail(users.getUsername(), users.getPassword(), authorities);
            userDetail.setEnabled(users.getEnabled() == 0L);
            userDetail.setEmail(users.getEmail());
            userDetail.setNickname(users.getNickname());
            userDetail.setTel(users.getTel());
            userDetail.setRemark(users.getRemark());
            userDetail.setMenus(urls);
            userDetail.setId(users.getId());
            return userDetail;
        }
        throw new UsernameNotFoundException("用户" + name + "未找到");
    }
}
