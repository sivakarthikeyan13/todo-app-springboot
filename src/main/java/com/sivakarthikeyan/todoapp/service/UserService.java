package com.sivakarthikeyan.todoapp.service;

import com.sivakarthikeyan.todoapp.domain.User;
import com.sivakarthikeyan.todoapp.exception.ApiRequestException;
import com.sivakarthikeyan.todoapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Logger logger = LoggerFactory.getLogger(UserService.class);

    String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    String nameRegex = "^[A-Za-z]{1,30}$";
    String passwordRegex = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&-+=()])" + "(?=\\S+$).{8,20}$";

    @Autowired
    private UserRepository userRepository;

    public User fetchUserLogin(User user) {

        String email = user.getEmail();
        String password = user.getPassword();

        //Check for empty fields
        if (email.isEmpty() || password.isEmpty()){
            throw new ApiRequestException("enter all fields!") ;
        }

        //Email validation
        if (!inputValidation(email, emailRegex)) {
            throw new ApiRequestException(user.getEmail() + " is not a Email id");
        }

        //get user data from repo
        User existingUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (existingUser == null) {
            throw new ApiRequestException("Invalid Email or Password!!!");
        }

        logger.info("User Login successful...");
        return existingUser;
    }

    public User createNewUser(User user) {

        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String passwordConfirm = user.getPasswordConfirm();

        //Check for empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            throw new ApiRequestException("enter all fields!") ;
        }

        //Name validation
        if (!inputValidation(name, nameRegex)) {
            throw new ApiRequestException("Name should be less than 30 character and include only english words");
        }

        //Email validation
        if (!inputValidation(email, emailRegex)) {
            throw new ApiRequestException(user.getEmail() + " is not a Email id");
        }

        //Password validation
//        if (!inputValidation(password, passwordRegex)) {
//            throw new ApiRequestException("Password should contain 8-20 char, 1 digit, 1 upper case, 1 lower case, 1 special character and no blank spaces");
//        }

        //Check password matches confirm-password
        if (!password.equals(passwordConfirm)) {
            throw new ApiRequestException("Password and Confirm-password does not match!");
        }

        //check if email already exists
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            throw new ApiRequestException("Email " + user.getEmail() + " is already registered");
        }

        //save user to repo
        userRepository.save(user);
        logger.info("User Registered successfully...");

        return user;
    }

    public User updateUser(Long id, User user) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("user with id " + id + " does not exists"));

        logger.info("Existing User with id- " + id + " found");
        user.setId(id);

        String newPassword = user.getPassword();
        String passwordConfirm = user.getPasswordConfirm();
        String oldPassword = user.getOldPassword();

        if (newPassword.equals(passwordConfirm)) {
            boolean passwordVerification = userRepository.existsByIdAndPassword(id, oldPassword);
            if (!passwordVerification){
                throw new ApiRequestException("Old Password is Incorrect!");
            }
        } else {
            throw new ApiRequestException("Password and Confirm-password does not match!");
        }

        logger.info("User with id- " + id + " updated successfully...");
        return userRepository.save(user);

    }

    public void deleteUser(Long id) {
//        boolean userExists = userRepository.existsById(id);

        //check if user exists in repo
        if (!userRepository.existsById(id)) {
            throw new ApiRequestException("user with id " + id + " does not exists");
        }

        //delete user
        userRepository.deleteById(id);
        logger.info("User with id- " + id + " deleted successfully");
    }

    //validation with regular expression
    public Boolean inputValidation(String email, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
