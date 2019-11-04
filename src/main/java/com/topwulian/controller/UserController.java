package com.topwulian.controller;

import com.topwulian.annotation.LogAnnotation;
import com.topwulian.dao.FarmDao;
import com.topwulian.dao.UserDao;
import com.topwulian.dto.UserDto;
import com.topwulian.model.Farm;
import com.topwulian.model.User;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.service.UserService;
import com.topwulian.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户相关接口
 * 
 * @author 小威老师 xiaoweijiagou@163.com
 *
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/users")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;

	@Autowired
    private FarmDao farmDao;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存用户")
	@RequiresPermissions("sys:user:add")
	public User saveUser(@RequestBody UserDto userDto) {
		User u = userService.getUser(userDto.getUsername());
		if (u != null) {
			throw new IllegalArgumentException(userDto.getUsername() + "已存在");
		}
        User addUser = userService.saveUser(userDto);
        User user = UserUtil.getCurrentUser();
		if (!user.getUsername().equals("admin")){
            //给用户添加对应的基地
            List<Farm> farmListByUserId = farmDao.getFarmListByUserId(user.getId());
            if (farmListByUserId != null && farmListByUserId.size() > 0) {

                List<Long> farmIds=new ArrayList<>();
                for (Farm farm : farmListByUserId) {
                    farmIds.add(farm.getId());
                }
                userDao.saveUserFarms(addUser.getId(),farmIds);
            }
        }

        return addUser;
	}

	@LogAnnotation
	@PutMapping
	@ApiOperation(value = "修改用户")
	@RequiresPermissions("sys:user:add")
	public User updateUser(@RequestBody UserDto userDto) {
		return userService.updateUser(userDto);
	}


	@LogAnnotation
	@PutMapping("/associateFarm")
	@ApiOperation(value = "给用关联的基地")
	public User associateFarmForUser(@RequestBody UserDto userDto) {
		return userService.associateFarmForUser(userDto);
	}

	@LogAnnotation
	@PutMapping(params = "headImgUrl")
	@ApiOperation(value = "修改头像")
	public void updateHeadImgUrl(String headImgUrl) {
		User user = UserUtil.getCurrentUser();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setHeadImgUrl(headImgUrl);

		userService.updateUser(userDto);
		log.debug("{}修改了头像", user.getUsername());
	}

	@LogAnnotation
	@PutMapping("/{username}")
	@ApiOperation(value = "修改密码")
	@RequiresPermissions("sys:user:password")
	public void changePassword(@PathVariable String username, String oldPassword, String newPassword) {
		userService.changePassword(username, oldPassword, newPassword);
	}

	@GetMapping
	@ApiOperation(value = "用户列表")
	//@RequiresPermissions("sys:user:query")
	public PageTableResponse listUsers(PageTableRequest request) {
	    //根据用户名确定返回的用户列表,如果是admin,返回全部,如果是基地管理员,返回本基地的用户,如果是用户,看不到这个菜单
        User user = UserUtil.getCurrentUser();
        if (!"admin".equals(user.getUsername())) {
            //根据用户id去farm表中将farmId查询出来,再根据farmId将关联的userId都查出来返回
            List<User> users=new ArrayList<>();
            List<Farm> farmListByUserId = farmDao.getFarmListByUserId(user.getId());
            for (Farm farm : farmListByUserId) {
                List<Long> userIds = userDao.getUserIdByFarmIdFromUserFarm(farm.getId());
                for (Long userId : userIds) {
                    User userById = userDao.getById(userId);
                    if (!"admin".equals(userById.getUsername())){
						users.add(userById);
					}
                }
            }
            return new PageTableResponse(100,0,users);
        }

		return new PageTableHandler(new PageTableHandler.CountHandler() {
			@Override
			public int count(PageTableRequest request) {
				return userDao.count(request.getParams());
			}
		}, new PageTableHandler.ListHandler() {

			@Override
			public List<User> list(PageTableRequest request) {
				List<User> list = userDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).handle(request);
	}

	@ApiOperation(value = "当前登录用户")
	@GetMapping("/current")
	public User currentUser() {
		return UserUtil.getCurrentUser();
	}

	@ApiOperation(value = "根据用户id获取用户")
	@GetMapping("/{id}")
	@RequiresPermissions("sys:user:query")
	public User user(@PathVariable Long id) {
		return userDao.getById(id);
	}

    /**
     * 删除操作应该慎重使用,admin用户不能被删除
     * 其他用户删除需要将关联的中间表:角色,权限,农场,设备一并删除
     * @param userId
     */
    @ApiOperation(value = "根据用户id删除用户")
    @DeleteMapping("/{userId}")
    @RequiresPermissions("sys:user:delete")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
