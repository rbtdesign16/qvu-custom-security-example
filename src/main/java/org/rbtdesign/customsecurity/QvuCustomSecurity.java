package org.rbtdesign.customsecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.rbtdesign.qvu.client.utils.AuthenticationException;


import org.rbtdesign.qvu.client.utils.DeleteException;
import org.rbtdesign.qvu.client.utils.OperationResult;
import org.rbtdesign.qvu.client.utils.Role;
import org.rbtdesign.qvu.client.utils.SaveException;
import org.rbtdesign.qvu.client.utils.SecurityService;
import org.rbtdesign.qvu.client.utils.User;

/**
 *
 * @author rbtuc
 */
public class QvuCustomSecurity implements SecurityService {
    // dummy user
    private static final String ADMIN = "admin";
    private static final String USER = "admin";
    
    public static final String ADMINISTRATOR_ROLE = "administrator";
    public static final String QUERY_DESIGNER_ROLE = "query designer";

    private static final String[] DEFAULT_ROLE_NAMES = {ADMINISTRATOR_ROLE, QUERY_DESIGNER_ROLE};

    public QvuCustomSecurity() {
        System.out.println("in QvuCustomSecurity()");
    }
    
    @Override
    public boolean authenticate(String user, String pass) throws AuthenticationException  {
        boolean retval = false;

        if ((ADMIN.equalsIgnoreCase(user) && ADMIN.equals(pass))
                || (USER.equalsIgnoreCase(user) && USER.equals(pass))) {
            retval = true;
        }
        
        System.out.println("in authenticate: user=" + user + ", retval=" + retval);
        return retval;
    }

    @Override
    public User getUser(String userId) {
        return findUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> retval = new ArrayList<>();
        User ui = new User();
        ui.setUserId("admin");
        ui.setFirstName("John");
        ui.setLastName("Doe");
        ui.setPassword("F6FDFFE48C908DEB0F4C3BD36C032E72");
        
        // add all roles to admin
        ui.getRoles().addAll(Arrays.asList(DEFAULT_ROLE_NAMES));
        
        retval.add(ui);
                
        ui = new User();
        ui.setUserId("user");
        ui.setFirstName("Joe");
        ui.setLastName("Blow");
        ui.setPassword("5CC32E366C87C4CB49E4309B75F57D64");
        ui.getRoles().add(QUERY_DESIGNER_ROLE);
        
        retval.add(ui);
        
        return retval;
    }

    @Override
    public List<Role> getAllRoles() {
        List <Role> retval = new ArrayList<>();
        
        for (String r : DEFAULT_ROLE_NAMES) {
            Role role = new Role();
            role.setName(r);
            retval.add(role);
        }
        
        return retval;
    }

    private User findUser(String userId) {
        User retval = null;
        List<User> users = getAllUsers();
        for (User u : users) {
            if (userId.equalsIgnoreCase(u.getUserId())) {
                retval = u;
                break;
            }
        }
        return retval;
    }

    @Override
    public OperationResult saveUser(User user)  throws SaveException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public OperationResult saveRole(Role role)  throws SaveException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    @Override
    public OperationResult deleteUser(String userId) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public OperationResult deleteRole(String roleName) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
