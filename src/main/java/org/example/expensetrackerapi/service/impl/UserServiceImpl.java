package org.example.projektidemo.service.impl;

import lombok.AllArgsConstructor;
import org.example.projektidemo.config.PasswordHasher;
import org.example.projektidemo.dto.UserDto;
import org.example.projektidemo.entity.User;
import org.example.projektidemo.exception.ResourceNotFoundException;
import org.example.projektidemo.mapper.UserMapper;
import org.example.projektidemo.repository.UserRepository;
import org.example.projektidemo.service.UserService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            String hashedPassword = PasswordHasher.hashPassword(userDto.getPassword()); // manually hashed password

            User user = UserMapper.mapToUser(userDto);
            user.setPassword_hash(hashedPassword);
            User savedUser = userRepository.save(user);

            return UserMapper.mapToUserDto(savedUser);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist!" + id)
        );

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();

        return userList.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUserDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist!" + id)
        );
        user.setFirstName(updatedUserDto.getFirstName());
        user.setLastName(updatedUserDto.getLastName());
        user.setEmail(updatedUserDto.getEmail());
        user.setEmail(updatedUserDto.getEmail());

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(updatedUser);
    }
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist!" + id)
        );

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserWithHighestTotalExpenses() {
        Object dbUser = userRepository.userWithLargestExpenses();
        try{
            if(dbUser == null){
                throw new ResourceNotFoundException("User doesn't exist!");
            }
        }catch (ResourceNotFoundException e){
            System.out.println(e.getMessage());
            return null;
        }
        Long userId = (Long) ((Object []) dbUser)[0];

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist!" + userId)
        );

        return UserMapper.mapToUserDto(user);
    }

}




















