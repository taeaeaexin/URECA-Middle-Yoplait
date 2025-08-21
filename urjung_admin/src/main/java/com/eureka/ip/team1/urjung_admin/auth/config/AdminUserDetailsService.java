package com.eureka.ip.team1.urjung_admin.auth.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eureka.ip.team1.urjung_admin.admin.entity.Admin;
import com.eureka.ip.team1.urjung_admin.admin.repository.AdminRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(username);
        
        if( optionalAdmin.isPresent() ) {
            
            Admin admin = optionalAdmin.get();
 
            UserDetails userDetails = AdminUserDetails.builder()
            		.admin(admin)
            		.build();
            
            return userDetails;
        }
        
        throw new UsernameNotFoundException("User not found");
    }

}
