package com.andrea.services;

import com.andrea.vo.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserRetrievalService implements IUserRetrievalService {

	// MOCKED DATA (SERVICE OUT OF SCOPE OF THE PROJECT)
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if (userName != null && userName.equals("andrea")) {
			return new UserDetailsImpl("andrea", "$2y$10$Ma4Rj29GBcB42T7l/Drq4usbLy49/7YQ1JM57B/rihGrlpV0foNeu"); // encoded of "password"
		} else if (userName != null && userName.equals("leo")) {
			return new UserDetailsImpl("leo", "$2y$10$Ma4Rj29GBcB42T7l/Drq4usbLy49/7YQ1JM57B/rihGrlpV0foNeu");
		}
		throw new UsernameNotFoundException(userName);
	}
}
