package com.navi.cloudconfig.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.navi.cloudconfig.server.model.ConfigModel;
import com.navi.cloudconfig.server.repo.ConfigStoreRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudConfigServerApplication.class)
@AutoConfigureMockMvc
public class CloudConfigApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ConfigStoreRepository repo;

    private String payload;

    @Before
    public void init() throws Exception {
        Map<String, Object> configValues = new HashMap<>();
        configValues.put("name", "foo");
        configValues.put("email", "somebar.com");
        ConfigModel configModel = new ConfigModel("test", "dev", 1L, configValues);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        payload = ow.writeValueAsString(configModel);
    }

    @After
    public void clearData(){
        repo.deleteAll();
    }

    @Test
    public void should_save_config() throws Exception {
        this.mockMvc.perform(post("/save")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void should_give_error_for_invalid_save_input() throws Exception {

        ConfigModel configModel = new ConfigModel("test", "dev", 1L, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String invalid = ow.writeValueAsString(configModel);

        try {
            this.mockMvc.perform(post("/save")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(invalid));
            fail("Should have thrown RuntimeException but did not!");
        }catch (Exception e){
            String message = e.getCause().getMessage();
            Assert.assertEquals(message, "Input param is null/empty");
        }
    }

    @Test
    public void should_get_config() throws Exception {
        this.mockMvc.perform(post("/save")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload));

        this.mockMvc.perform(get("/fetch")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("nameSpace", "test")
                .param("profile", "dev")
                .param("version", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(payload));
    }

    @Test
    public void should_give_error_for_invalid_get_input() throws Exception {

        this.mockMvc.perform(post("/save")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(payload));

        try {
            this.mockMvc.perform(get("/fetch")
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .param("nameSpace", "invalid")
                    .param("profile", "dev")
                    .param("version", "1"));
            fail("Should have thrown RuntimeException but did not!");
        }catch (Exception e){
            String message = e.getCause().getMessage();
            Assert.assertEquals(message, "No config found for namespace :invalid, profile:dev and version:1");
        }

    }

}
