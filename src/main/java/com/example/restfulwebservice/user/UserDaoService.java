package com.example.restfulwebservice.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserDaoService {

	private static List<User> users = new ArrayList<>();
	
	private static int usersCount = 3;
	static {
		users.add(new User(1, "Kenneth", new Date(),"pass1", "701010-1111111")); 
		users.add(new User(2, "lgy", new Date(), "pass2", "701010-2222222")); 
		users.add(new User(3, "elena", new Date(), "pass3", "701010-2222222")); 
//		users.add(new User(1, "test1", "20201129", "pass1", "701010-2222222")); 
//		users.add(new User(2, "lgy", "20201129", "pass2", "701010-2222222")); 
//		users.add(new User(3, "elena", "20201129", "pass3", "701010-2222222")); 
	}
	
	public List<User> findAll() {
		return users;
	}
	
	public User findOne(int id) {
		for(User user : users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	public User save(User user) {
		if(user.getId() == null) {
			user.setId(++usersCount);
		}
		
		users.add(user);
		
		return user;
	}
	
	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();
		
		while(iterator.hasNext()) {
			User user = iterator.next();
			
			if(user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
	
    public User updateById(int id, User user) {
    	
    	List<User> userList= findAll();
    	
        for (User updateUser : userList) {

            if (updateUser.getId() == id) {

                userList.get(id-1).setName(user.getName());

                userList.get(id-1).setJoinDate(user.getJoinDate());

                return user;

            }

        }

        return null;

    }
}
