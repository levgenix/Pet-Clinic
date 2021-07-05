package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.vet24.models.user.Client;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithUserDetails(value = "user3@gmail.com")
public class ClientCommentControllerTest extends ControllerAbstractIntegrationTest {

    private final String URI = "/api/client/doctor";

    @Autowired
    private ClientService clientService;

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeNotFoundComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}",10000L, false))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeDislikedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}",1L,false))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
                (result) -> {
                    Client client = clientService.getCurrentClientWithReactions();
                    Assert.assertEquals(client.getCommentReactions().size(), 1);
                    Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), false);
                }
        );
    }

    @Test
    @DataSet(cleanBefore = true, value =  {"/datasets/clients.yml","/datasets/doctors.yml", "/datasets/comments.yml"})
    public void shouldBeLikedComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI + "/{commentId}/{positive}",2L,true))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
                (result) -> {
                    Client client = clientService.getCurrentClientWithReactions();
                    Assert.assertEquals(client.getCommentReactions().size(), 1);
                    Assert.assertEquals(client.getCommentReactions().get(0).getPositive(), true);
                }
        );
    }
}
