package com.tim3.ois.service;

import com.tim3.ois.exception.ResourceNotFoundException;
import com.tim3.ois.model.User;
import com.tim3.ois.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service("userService")
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public List<User> findBy(Integer sId,String sortBy, String orderBy) {
        if(sId != -1 && orderBy.toLowerCase().equals("asc")) return userRepository.findAllBySuperiorId(true,sId,Sort.by(sortBy,"id").ascending());
        else if(sId != -1 && orderBy.toLowerCase().equals("desc")) return userRepository.findAllBySuperiorId(true,sId,Sort.by(sortBy,"id").descending());
        else if(orderBy.toLowerCase().equals("asc")) return userRepository.findAllBy(true, Sort.by(sortBy,"id").ascending());

        else if(orderBy.toLowerCase().equals("desc")) return userRepository.findAllBy(true, Sort.by(sortBy,"id").descending());

        else return userRepository.findAll();
    }
    public User findUserById(int id) throws ResourceNotFoundException {
        User user = userRepository.findById(id);
        if(user==null){throw new ResourceNotFoundException("User","id",id);}
        return user;
    }
    public List<User> findUsersByRole(int role) {return userRepository.findAllByRoleAndEnabledOrderByEmailAsc(role,true);}

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByEmailAndPassword(String email,String password) throws ResourceNotFoundException{
        User user = userRepository.findByEmailAndPasswordAndEnabled(email,password,true);
        if(user==null){
            throw new ResourceNotFoundException("User","email/password",email+ "/"+password);
        }
        System.out.println(user);
        return user;
    }

    public User updateUser(int id,User u) throws ResourceNotFoundException{
        User user = userRepository.findById(id);
        if(user==null){
            throw new ResourceNotFoundException("User","id",u.getId());
        }
        user.setNik(u.getNik());
        user.setSuperior(
                u.getSuperior());
        user.setPosition(u.getPosition());
        user.setDivision(u.getDivision());
        user.setEmail(u.getEmail());
        user.setCnumber(u.getCnumber());
        user.setAddress(u.getAddress());
        user.setName(u.getName());
        user.setRole(u.getRole());
        user.setPassword(u.getPassword());
        User updatedUser = userRepository.save(user);
        return updatedUser;
    }

    public Boolean saveUser(User u){
        User userExists = userRepository.findByEmail(u.getEmail());
        if (userExists != null) {
            //bindingResult.rejectValue("email", "There is already a user registered with the email provided");
            return false;
        }
        else {
            u.setEnabled(true);
            userRepository.save(u);
            return true;
        }
    }

    public Boolean deleteUser(int id){
        User user = userRepository.findById(id);
        if(user==null){
            return false;
        }
        user.setEnabled(false);
        userRepository.save(user);
        return true;
    }
}
