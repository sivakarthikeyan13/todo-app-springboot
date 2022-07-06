package com.sivakarthikeyan.todoapp.service;

import com.sivakarthikeyan.todoapp.domain.User;
import com.sivakarthikeyan.todoapp.exception.ApiRequestException;
import com.sivakarthikeyan.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository);
    }

    @Test
    void canFetchUserLogin() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "123"
        );
        given(userRepository.findByEmailAndPassword(anyString(), anyString())).willReturn(user);
        //when
        underTest.fetchUserLogin(user);
        //then
        assertThat(user).isEqualTo(user);
    }

    @Test
    void willThrowWhenFetchUserLogin() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "123"
        );
        given(userRepository.findByEmailAndPassword(anyString(), anyString())).willReturn(null);
        //when
        //then
        assertThatThrownBy(() ->underTest.fetchUserLogin(user))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Invalid Email or Password!!!");
    }

    @Test
    void canCreateNewUser() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "1234",
                "1234"
        );
        //when
        underTest.createNewUser(user);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }


    @Test
    void willThrowWhenEmailExists() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "1234",
                "1234"
        );

        given(userRepository.existsByEmail(anyString())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() ->underTest.createNewUser(user))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Email " + user.getEmail() + " is already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenCreatePasswordNoMatch() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "1234",
                "123"
        );

        given(userRepository.existsByEmail(anyString())).willReturn(false);

        //when
        //then
        assertThatThrownBy(() ->underTest.createNewUser(user))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Password and Confirm-password does not match!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canUpdateUser() {
        //given
        String oldPassword = "123";
        User user = new User(
                10L,
                "testUser",
                "testUser@gmail.com",
                oldPassword
        );

        User updatedUser = new User(
                "testUser",
                "testUser@gmail.com",
                "1234",
                "1234",
                oldPassword
        );
        Long id = 10L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.existsByIdAndPassword(id,oldPassword)).willReturn(true);
        //when
        underTest.updateUser(id,updatedUser);
        //then
        verify(userRepository).save(updatedUser);
    }

    @Test
    void willThrowWhenUpdateIdNotExists() {
        //given
        User updatedUser = new User(
                "testUser",
                "testUser@gmail.com",
                "123",
                "123"
        );
        Long id = 10L;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() ->underTest.updateUser(id, updatedUser))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("user with id " + id + " does not exists");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenOldPasswordIsIncorrect() {
        //given
        String oldPassword = "12345";
        User user = new User(
                10L,
                "testUser",
                "testUser@gmail.com",
                "123"
        );

        User updatedUser = new User(
                "testUser",
                "testUser@gmail.com",
                "1234",
                "1234",
                oldPassword
        );
        Long id = 10L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.existsByIdAndPassword(id,oldPassword)).willReturn(false);
        //when
        //then
        assertThatThrownBy(() ->underTest.updateUser(id, updatedUser))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Old Password is Incorrect!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void willThrowWhenUpdatePasswordNoMatch() {
        //given
        String oldPassword = "123";
        User user = new User(
                10L,
                "testUser",
                "testUser@gmail.com",
                oldPassword
        );

        User updatedUser = new User(
                "testUser",
                "testUser@gmail.com",
                "12345",
                "1234",
                oldPassword
        );
        Long id = 10L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        //when
        //then
        assertThatThrownBy(() ->underTest.updateUser(id, updatedUser))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Password and Confirm-password does not match!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void canDeleteUser() {
        //given
        Long id = 1L;
        given(userRepository.existsById(anyLong())).willReturn(true);

        //when
        underTest.deleteUser(id);

        //then
        verify(userRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteUser() {
        //given
        Long id = 1L;
        given(userRepository.existsById(anyLong())).willReturn(false);
        //when
        //then
        assertThatThrownBy(() ->underTest.deleteUser(id))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("user with id " + id + " does not exists");
        verify(userRepository, never()).deleteById(id);
    }
}