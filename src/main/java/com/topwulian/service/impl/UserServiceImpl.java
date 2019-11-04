package com.topwulian.service.impl;

import com.topwulian.constants.UserConstants;
import com.topwulian.dao.UserDao;
import com.topwulian.dto.UserDto;
import com.topwulian.model.User;
import com.topwulian.service.UserService;
import com.topwulian.utils.UserUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public User saveUser(UserDto userDto) {
		User user = userDto;
		user.setSalt(DigestUtils
				.md5Hex(UUID.randomUUID().toString() + System.currentTimeMillis() + UUID.randomUUID().toString()));
		user.setPassword(passwordEncoder(user.getPassword(), user.getSalt()));
		user.setStatus(User.Status.VALID);
		userDao.save(user);
		saveUserRoles(user.getId(), userDto.getRoleIds());

		log.debug("新增用户{}", user.getUsername());
		return user;
	}

	private void saveUserRoles(Long userId, List<Long> roleIds) {
		if (roleIds != null) {
			userDao.deleteUserRole(userId);
			if (!CollectionUtils.isEmpty(roleIds)) {
				userDao.saveUserRoles(userId, roleIds);
			}
		}
	}

	@Override
	public String passwordEncoder(String credentials, String salt) {
		Object object = new SimpleHash("MD5", credentials, salt, UserConstants.HASH_ITERATIONS);
		return object.toString();
	}

	@Override
	public User getUser(String username) {
		return userDao.getUser(username);
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) {
		User u = userDao.getUser(username);
		if (u == null) {
			throw new IllegalArgumentException("用户不存在");
		}

		if (!u.getPassword().equals(passwordEncoder(oldPassword, u.getSalt()))) {
			throw new IllegalArgumentException("密码错误");
		}

		userDao.changePassword(u.getId(), passwordEncoder(newPassword, u.getSalt()));

		log.debug("修改{}的密码", username);
	}

    @Override
	@Transactional
	public User updateUser(UserDto userDto) {
		userDao.update(userDto);
		saveUserRoles(userDto.getId(), userDto.getRoleIds());
		updateUserSession(userDto.getId());

		return userDto;
	}

	private void updateUserSession(Long id) {
		User current = UserUtil.getCurrentUser();
		if (current.getId().equals(id)) {
			User user = userDao.getById(id);
			UserUtil.setUserSession(user);
		}
	}

    /**
     * 删除操作应该慎重使用,admin用户不能被删除
     * 其他用户删除需要将关联的中间表:角色,农场关联关系一并删除
     * @param userId
     */
    @Override
    public void delete(Long userId) {
        User user = userDao.getById(userId);
        if ("admin".equals(user.getUsername())) {
            throw new RuntimeException("系统固有用户,不允许删除!!!");
        }
        userDao.deleteUserRole(userId);
        userDao.deleteUserFarm(userId);
        userDao.delete(userId);
    }

    /**
     * 给用户分配基地
     * @param userDto
     * @return
     */
    @Override
    public User associateFarmForUser(UserDto userDto) {
        //根据传过来的用户id和farmIds进行分配
        List<Long> farmIds = userDto.getFarmIds();
        Long userId = userDto.getId();
        if (farmIds != null) {
            userDao.deleteUserFarm(userId);
            if (!CollectionUtils.isEmpty(farmIds)) {
                userDao.saveUserFarms(userDto.getId(), farmIds);
            }
        }

        log.debug("给用户{}分配基地", userId+":"+userDto.getUsername());
        return userDto;
    }
}
