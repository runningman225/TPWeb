package com.tim3.ois.service;

import com.tim3.ois.model.Item;
import com.tim3.ois.model.Request;
import com.tim3.ois.model.RequestCount;
import com.tim3.ois.model.User;
import com.tim3.ois.repository.ItemRepository;
import com.tim3.ois.repository.RequestRepository;
import com.tim3.ois.repository.UserRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RequestServiceTest.class)
public class RequestServiceTest {
    @MockBean
    RequestRepository requestRepository;

    @MockBean
    UserRepository userRepository;

    @InjectMocks
    RequestService requestService;

    @Mock
    UserService userService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = Exception.class)
    public void test_getMyRequestCount_throwsException() throws Exception {
        when(userService.findUserById(Mockito.anyInt())).thenReturn(null);
        RequestCount a = requestService.getMyRequestCount(1);

    }

    @Test
    public void test_getMyRequestCount_success() throws Exception {
        User a = new User();

        when(userService.findUserById(1)).thenReturn(a);
        int x = 2;
        when(requestRepository.getMyRequestRejectedCount(true, 1)).thenReturn(x);
        when(requestRepository.getMyRequestApprovedCount(true, 1)).thenReturn(x);
        when(requestRepository.getMyRequestPendingCount(true, 1)).thenReturn(x);

        RequestCount b = requestService.getMyRequestCount(1);

        assertEquals(b.getRejected(), 2);
        assertEquals(b.getApproved(), 2);
        assertEquals(b.getPending(), 2);
        assertEquals(b.getTotal(), 6);

    }

    @Test
    public void test_getRequestCount(){
        RequestCount reqCount= RequestCount.builder()
                .approved(0)
                .pending(0)
                .rejected(0)
                .total(0).build();
        int x = 0;
        when(requestRepository.getRequestRejectedCount(true)).thenReturn(x);
        when(requestRepository.getRequestApprovedCount(true)).thenReturn(x);
        when(requestRepository.getRequestRejectedCount(true)).thenReturn(x);

        RequestCount b = requestService.getRequestCount();

        assertEquals(b.getRejected(), 0);
        assertEquals(b.getApproved(), 0);
        assertEquals(b.getPending(), 0);
        assertEquals(b.getTotal(),0 );
    }
}
