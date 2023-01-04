package task.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import task.models.dao.RoleDAO;
import task.models.dao.UserDAO;
import task.models.entity.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDAO userDao;

    private final RoleDAO roleDao;

    public UserDetailsServiceImpl(UserDAO userDao, RoleDAO roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with such username"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                true, true, true, true,
                roleDao.roleToAuthorityList(roleDao.getRole(user.getRole_id()).get()));
//        return securityUser.fromUser(user);
    }
}
