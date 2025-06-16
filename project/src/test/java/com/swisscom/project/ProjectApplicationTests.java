package com.swisscom.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import com.swisscom.project.repository.ServiceRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServiceRepository repository;

    @AfterEach
    void cleanDB() {
        repository.deleteAll();
    }

    private final String sampleServiceJson = """
        {
            "id": "service_test",
            "resources": [
                {
                    "id": "res1",
                    "owners": [
                        {
                            "id": "own1",
                            "name": "John",
                            "accountNumber": "AC001",
                            "level": 1
                        }
                    ]
                }
            ]
        }
        """;

    @Test
    void testCreateService() throws Exception {
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("service_test"));
        
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetServiceById() throws Exception {
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/services/service_test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resources[0].id").value("res1"));
    }

    @Test
    void testGetAllServices() throws Exception {
        String service2Json = sampleServiceJson.replace("service_test", "service_2");

        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson));
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(service2Json));

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testUpdateService() throws Exception {
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson));

        String updatedJson = """
            {
                "id": "service_test",
                "resources": [
                    {
                        "id": "res1",
                        "owners": [
                            {
                                "id": "own1",
                                "name": "Jane Doe",
                                "accountNumber": "AC999",
                                "level": 2
                            }
                        ]
                    }
                ]
            }
            """;

        mockMvc.perform(put("/api/services/service_test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resources[0].owners[0].name").value("Jane Doe"));
        
        mockMvc.perform(put("/api/services/service_test_2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteService() throws Exception {
        mockMvc.perform(post("/api/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleServiceJson));

        mockMvc.perform(delete("/api/services/service_test"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/services/service_test"))
                .andExpect(status().isNotFound());
    }

}
