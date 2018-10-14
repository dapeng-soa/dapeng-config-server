package com.github.dapeng.web.system;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ChangePwdDto;
import com.github.dapeng.dto.RoleUserDto;
import com.github.dapeng.entity.system.TRole;
import com.github.dapeng.entity.system.TRoleUser;
import com.github.dapeng.entity.system.TUsers;
import com.github.dapeng.repository.system.RoleRepository;
import com.github.dapeng.repository.system.RoleUserRepository;
import com.github.dapeng.repository.system.UserRepository;
import com.github.dapeng.util.Check;
import com.github.dapeng.util.PasswordUtil;
import com.github.dapeng.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/10/11 20:37
 * email :yq1724555319@gmail.com
 */

@Controller
@RequestMapping("/api")
public class UserController {

    Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 用户列表
     *
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @param search
     * @return
     */
    @GetMapping("/system-users")
    public ResponseEntity getUsers(@RequestParam(required = false, defaultValue = "0") int offset,
                                   @RequestParam(required = false, defaultValue = "100000") int limit,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false, defaultValue = "desc") String order,
                                   @RequestParam(required = false, defaultValue = "") String search) {
        try {
            SecurityUtil.checkAdmin();
            PageRequest pageRequest = new PageRequest
                    (offset / limit, limit,
                            new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                    null == sort ? "createdAt" : sort));
            Page<TUsers> page = userRepository.findAll((root, query, cb) -> {
                Path<String> username = root.get("username");
                Path<String> nickname = root.get("nickname");
                Path<String> remark = root.get("remark");
                List<Predicate> ps = new ArrayList<>();
                ps.add(cb.or(cb.like(username, "%" + search + "%"), cb.like(nickname, "%" + search + "%"), cb.like(remark, "%" + search + "%")));

                query.where(ps.toArray(new Predicate[ps.size()]));
                return null;
            }, pageRequest);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));
        } catch (Exception e) {
            LOGGER.error("获取用户失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 添加用户
     * 只有管理员才可以添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("/system-user/add")
    public ResponseEntity addSystemUser(@RequestBody TUsers user) {
        try {
            if (isEmpty(user.getUsername())) {
                throw new Exception("用户名不能为空");
            }
            //0:当前操作用户是不是管理员权限
            SecurityUtil.checkAdmin();
            //1:用户是否存在
            TUsers users = userRepository.findByUsername(user.getUsername());
            if (null != users) {
                throw new Exception("用户名" + user.getUsername() + "已被占用");
            }
            //2:默认密码用户名
            user.setPassword(PasswordUtil.createPassword(user.getUsername(), null));

            userRepository.save(user);

            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("添加用户失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 修改用户
     * 管理员不能通过此接口修改自己的资料，且不能禁用自己的账户
     *
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/system-user/edit/{id}")
    public ResponseEntity editSystemUser(@PathVariable Long id, @RequestBody TUsers user) {
        try {
            if (isEmpty(user.getUsername())) {
                throw new Exception("用户名不能为空");
            }
            //0:当前操作用户是不是管理员权限
            SecurityUtil.checkAdmin();
            //1:用户是否存在,如果能查到同名的用户且不是当前要修改的用户，则存在同名用户
            TUsers realUser = userRepository.findOne(id);
            TUsers users2 = userRepository.findByUsername(user.getUsername());
            if (null != realUser) {
                if (SecurityUtil.me().equals(realUser.getUsername())) {
                    throw new Exception("您不能在此处修改自己的资料");
                }
                if (users2 != null && realUser.getId() != users2.getId()) {
                    throw new Exception("用户名" + user.getUsername() + "已被占用");
                }
                //2:一旦修改用户名，则是另一个用户了，所以密码会随之改变为当前修改的用户名为默认密码
                if (!realUser.getUsername().equals(user.getUsername())) {
                    realUser.setPassword(PasswordUtil.createPassword(user.getUsername(), null));
                }
                realUser.setUsername(user.getUsername());
                realUser.setEmail(user.getEmail());
                realUser.setNickname(user.getNickname());
                realUser.setTel(user.getTel());
                realUser.setRemark(user.getRemark());
                userRepository.save(realUser);
            } else {
                throw new Exception("找不到此账号");
            }
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("修改用户信息失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 重置密码
     *
     * @param id
     * @return
     */
    @PostMapping("/system-user/reset-password/{id}")
    public ResponseEntity editUserPwd2Default(@PathVariable Long id) {
        try {
            SecurityUtil.checkAdmin();
            TUsers realUser = userRepository.findOne(id);
            if (null != realUser) {
                if (SecurityUtil.me().equals(realUser.getUsername())) {
                    throw new Exception("您不能在此重置自己的密码");
                }
                realUser.setPassword(PasswordUtil.createPassword(realUser.getUsername(), null));
            }
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("重置密码失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    @PostMapping("/system-user/switch-status/{id}")
    public ResponseEntity editUserStatus(@PathVariable Long id) {
        try {
            SecurityUtil.checkAdmin();
            TUsers realUser = userRepository.findOne(id);
            if (null != realUser) {
                if (SecurityUtil.me().equals(realUser.getUsername())) {
                    throw new Exception("您不能禁用自己的账号");
                }
                if (realUser.getEnabled() == 0) {
                    realUser.setEnabled(-1);
                } else if (realUser.getEnabled() == -1) {
                    realUser.setEnabled(0);
                }
            }
            userRepository.save(realUser);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("禁用用户失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 获取单个用户
     *
     * @param id
     * @return
     */
    @GetMapping("/system-user/{id}")
    public ResponseEntity getSystemUserById(@PathVariable Long id) {
        try {
            SecurityUtil.checkAdmin();
            TUsers user = userRepository.findOne(id);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, user));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    @GetMapping("/system-roles")
    public ResponseEntity getRoles() {
        try {
            SecurityUtil.checkAdmin();
            List<TRole> roles = roleRepository.findAllByRoleIsNot(ADMIN);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, roles));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 根据用户id查找一关联的角色
     *
     * @param userId
     * @return
     */
    @GetMapping("/system-role/{userId}")
    public ResponseEntity getRoles(@PathVariable Long userId) {
        List<TRoleUser> roleUsers = roleUserRepository.findAllByUserId(userId);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, roleUsers));
    }


    /**
     * 关联角色
     *
     * @return
     */
    @PostMapping("/system-user/link-role")
    public ResponseEntity linkRole(@RequestBody RoleUserDto dto) {
        try {
            SecurityUtil.checkAdmin();
            TUsers realUser = userRepository.findOne(dto.getUserId());
            if (null != realUser) {
                if (SecurityUtil.me().equals(realUser.getUsername())) {
                    throw new Exception("您不修改自己账号的权限");
                }
            }
            List<TRoleUser> roleUsers = new ArrayList<>();
            dto.getRoleIds().forEach(rid -> {
                List<TRoleUser> roleUserList = roleUserRepository.findByUserIdAndRoleId(dto.getUserId(), rid);
                if (isEmpty(roleUserList)) {
                    TRoleUser tRoleUser = new TRoleUser();
                    tRoleUser.setRoleId(rid);
                    tRoleUser.setUserId(dto.getUserId());
                    roleUsers.add(tRoleUser);
                }
            });
            roleUserRepository.save(roleUsers);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "角色关联成功"));
        } catch (Exception e) {
            LOGGER.error("角色关联失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 取消关联角色
     *
     * @return
     */
    @PostMapping("/system-user/unlink-role")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity execUnlinkRole(@RequestBody RoleUserDto dto) {
        try {
            SecurityUtil.checkAdmin();
            TUsers realUser = userRepository.findOne(dto.getUserId());
            if (null != realUser) {
                if (SecurityUtil.me().equals(realUser.getUsername())) {
                    throw new Exception("您不修改自己账号的权限");
                }
            }
            dto.getRoleIds().forEach(rid -> {
                roleUserRepository.deleteByUserIdAndRoleId(dto.getUserId(), rid);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "解除关联成功"));
        } catch (Exception e) {
            LOGGER.error("角色取消关联失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @PostMapping("/change-pwd")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity changePwd(@RequestBody ChangePwdDto dto) {
        try {
            //0
            if (isEmpty(dto.getConfirmPwd()) || isEmpty(dto.getOldPwd()) || isEmpty(dto.getNewPwd()) || isEmpty(dto.getUserName())) {
                throw new Exception("请验证输入");
            }
            //1.是否是修改自己的密码
            if (!dto.getUserName().equals(SecurityUtil.me())) {
                throw new Exception("权限不足！");
            }
            //2.两次确认的密码是不是相同
            if (!dto.getConfirmPwd().equals(dto.getNewPwd())) {
                throw new Exception("两次输入的密码不同！");
            }

            Check.hasChinese(dto.getNewPwd(), "密码");
            TUsers user = userRepository.findByUsername(dto.getUserName());
            if (null == user) {
                throw new Exception("找不到改用户！");
            }
            Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
            //3.对比旧密码
            boolean b = md5PasswordEncoder.isPasswordValid(user.getPassword(), dto.getOldPwd(), null);
            if (!b) {
                throw new Exception("输入的旧密码不正确！");
            }

            // 修改密码
            user.setPassword(PasswordUtil.createPassword(dto.getNewPwd(), null));
            userRepository.save(user);
            // 让当前登陆失效
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "修改成功,请使用新密码登陆!"));
        } catch (Exception e) {
            LOGGER.error("修改密码失败", e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, "修改失败:" + e.getMessage()));
        }
    }

}
