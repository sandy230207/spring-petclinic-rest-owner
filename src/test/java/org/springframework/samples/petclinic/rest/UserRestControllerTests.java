package org.springframework.samples.petclinic.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class UserRestControllerTests {

    @Mock
    private UserService userService;

    @Autowired
    private UserRestController userRestController;

    private MockMvc mockMvc;

    @Before
    public void initUsers() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
            .setControllerAdvice(new ExceptionControllerAdvice()).build();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testSignUpSuccess() throws Exception {
        User user = new User();
        user.setUsername("username3");
        user.setPassword("password3");
        user.setEnabled(true);
        user.setUid(3);
        user.addRole( "OWNER_ADMIN" );   
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/users/signup")
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testSignUpError() throws Exception {
        User user = new User();
        user.setUsername("username4");
        user.setPassword("password4");
        user.setEnabled(true);
        user.setUid(4);
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/users/signup")
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testSignInSuccess() throws Exception {
        User user = new User();
        user.setUsername("username3");
        user.setPassword("password3");
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/users/signin")
            .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
        	.andExpect(jsonPath("$.username").value("username3"))
        	.andExpect(jsonPath("$.password").value(""))
            .andExpect(jsonPath("$.enabled").value(true))
        	.andExpect(jsonPath("$.uid").value(3));
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void testSignInError() throws Exception {
        User user = new User();
        // user.setUsername("username5");
        // user.setPassword("password");
        // ObjectMapper mapper = new ObjectMapper();
        // String newOwnerAsJSON = mapper.writeValueAsString(user);
        // this.mockMvc.perform(post("/api/users/signin")
        //     .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
        //     .contentType(MediaType.APPLICATION_JSON_VALUE))
        //     .andExpect(status().isNotFound());

        // user.setUsername("username3");
        // user.setPassword("password5");
        // ObjectMapper mapper2 = new ObjectMapper();
        // newOwnerAsJSON = mapper2.writeValueAsString(user);
        // this.mockMvc.perform(post("/api/users/signin")
        //     .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
        //     .contentType(MediaType.APPLICATION_JSON_VALUE))
        //     // .andExpect(status().isForbidden())
        // 	.andExpect(jsonPath("$.username").value("username3"))
        // 	.andExpect(jsonPath("$.password").value("password3"));

        // user.setUsername("username5");
        // user.setPassword("password5");
        // mapper = new ObjectMapper();
        // newOwnerAsJSON = mapper.writeValueAsString(user);
        // this.mockMvc.perform(post("/api/users/signin")
        //     .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE)
        //     .contentType(MediaType.APPLICATION_JSON_VALUE))
        //     .andExpect(status().isNotFound());
    }
}
