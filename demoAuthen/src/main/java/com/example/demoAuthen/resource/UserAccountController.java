package com.example.demoAuthen.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoAuthen.model.ConfirmationToken;
import com.example.demoAuthen.model.Role;
import com.example.demoAuthen.model.User;
import com.example.demoAuthen.repository.ConfirmationTokenRepository;
import com.example.demoAuthen.repository.RoleRepository;
import com.example.demoAuthen.repository.UserRepository;
import com.example.demoAuthen.service.EmailSenderService;

@RestController
public class UserAccountController {
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;


    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;
    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String registerUser(@RequestBody User user)
    {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
        {
            return "This email has already exist!";
        }
        else
        {
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("realestate.uit.edu@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
            emailSenderService.sendEmail(mailMessage);
            String hashPwd = BCrypt.hashpw(user.getPassword(),BCrypt.gensalt());
            user.setPassword(hashPwd);
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.findByName("USER").get();
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);
            confirmationTokenRepository.save(confirmationToken);
            return "Successful Registration!";
        }
    }
    @RequestMapping(value="/confirm-account", method= RequestMethod.GET)
    public String confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByEmail(token.getUser().getEmail()).get();
            user.setActive(1);
            userRepository.save(user);
            return "Account verified";
        }
        else
        {
            return "The link is invalid or broken!";
        }
    }
}
