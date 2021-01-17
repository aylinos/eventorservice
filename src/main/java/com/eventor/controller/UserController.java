package com.eventor.controller;

import com.eventor.exception.ResourceNotFound;
import com.eventor.model.Role;
import com.eventor.model.User;
import com.eventor.payload.response.MessageResponse;
import com.eventor.payload.response.ResponseWithError;
import com.eventor.repository.RoleRepository;
import com.eventor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/eventor/users")
public class UserController {

    @Autowired
    UserRepository repository;

    @Autowired
    RoleRepository roleRepository;

    public static final String USER_NOT_FOUND_EXCEPTION_CONTENT = "User not found:";

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();

            repository.findAll().forEach(users::add);

            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping(path="/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable int id, @RequestBody User user) {
        String message = "";
        try {
            Optional<User> userData = repository.findById(id);

            if (userData.isPresent()) {
                User updatedUser = userData.get();

                if(!user.getEmail().equals(updatedUser.getEmail()))
                {
                    if(user.getEmail().isEmpty())
                    {
                        message = "Email cannot be empty!";
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
                    }
                    else if(repository.existsByEmail(user.getEmail()))
                    {
                        message = "This email is already registered by other user!";
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
                    }
                }

                updatedUser.setFirstName(user.getFirstName());
                updatedUser.setLastName(user.getLastName());
                updatedUser.setUsername(user.getUsername());
                updatedUser.setEmail(user.getEmail());
                updatedUser.setBirthday(user.getBirthday());
                updatedUser.setGender(user.getGender());
                updatedUser.setPhone(user.getPhone());
                updatedUser.setNationality(user.getNationality());

                repository.save(updatedUser);

                message = "Your information has been updated successfully!";
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } else {
                message = "User not found!";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(message));
            }
        } catch (Exception e) {
            message = "Something went wrong!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(message));
        }
    }

    //Get one user
    @GetMapping(path="/{id}")
    public User find(@PathVariable int id) {
        return  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND_EXCEPTION_CONTENT + id));
    }

    //Delete user
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") int id) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(USER_NOT_FOUND_EXCEPTION_CONTENT + id));
        this.repository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping(path="/role/{id}")
    public ResponseEntity<ResponseWithError<Boolean>> updateUserRole(@PathVariable int id, @RequestBody User user) {
        Optional<User> userData;

        try {
            userData = repository.findById(id);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (!userData.isPresent())
            return new ResponseEntity<>(new ResponseWithError<>(null, "User not found"), HttpStatus.NOT_FOUND);

        User currentUser = userData.get();

        Optional<Role> adminRoleData;
        try {
            adminRoleData = roleRepository.findByName("ROLE_ADMIN");
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "Role repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if(!adminRoleData.isPresent())
            return new ResponseEntity<>(new ResponseWithError<>(null, "Role admin not found"), HttpStatus.NOT_FOUND);

        Role adminRole = adminRoleData.get();
        Set<Role> currentUserRoles = currentUser.getRoles();

        if(!currentUserRoles.contains(adminRole))
            return new ResponseEntity<>(new ResponseWithError<>(null, "You are not authorized to change roles!"), HttpStatus.UNAUTHORIZED);

        Optional<User> updatedUserData;

        try {
            updatedUserData = repository.findById(user.getId());
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if(!updatedUserData.isPresent())
            return new ResponseEntity<>(new ResponseWithError<>(null, "User not found"), HttpStatus.NOT_FOUND);

        User updatedUser = updatedUserData.get();
        Set<Role> updatedUserRoles = updatedUser.getRoles();

        if(updatedUserRoles.contains(adminRole))
        {
            updatedUserRoles.remove(adminRole);
            updatedUser.setRoles(updatedUserRoles);
        }
        else
        {
            updatedUserRoles.add(adminRole);
            updatedUser.setRoles(updatedUserRoles);
        }

        try {
            repository.save(updatedUser);

            return new ResponseEntity<>(new ResponseWithError<>(true, null), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

    }
}
