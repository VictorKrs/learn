package liga.tasks.ru.learn.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.exceptions.AuthorAlreadyExistException;
import liga.tasks.ru.learn.models.author.AuthorCreate;
import liga.tasks.ru.learn.models.author.AuthorModel;
import liga.tasks.ru.learn.services.AuthorsService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthorsControllerTest {
    private final Long AUTHOR_ID = 1L;
    private final String AUTHOR_SECOND_NAME = "Ivanov";
    private final String AUTHOR_FIRST_NAME = "Ivan";
    private final String AUTHOR_MIDDLE_NAME = "Ivanovich";
    private final AuthorCreate AUTHOR_CREATE = AuthorCreate.builder().secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build();
    private final AuthorModel AUTHOR_MODEL = AuthorModel.builder().id(AUTHOR_ID).secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build();


    @MockBean
    private AuthorsService authorsService;

    @LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/authors";
    }

    @Test
    public void addAuthorTest_good() {
        when(authorsService.save(AUTHOR_CREATE)).thenReturn(AUTHOR_MODEL);

        assertEquals(AUTHOR_MODEL, restTemplate.postForObject(getBaseUrl(), AUTHOR_CREATE, AuthorModel.class));
    }

    @Test
    public void addAuthorTest_alreadyExist_badRequest() {
        when(authorsService.save(AUTHOR_CREATE)).thenThrow(new AuthorAlreadyExistException(Author.builder().secondName(AUTHOR_SECOND_NAME).firstName(AUTHOR_FIRST_NAME).middleName(AUTHOR_MIDDLE_NAME).build()));

        ResponseEntity<String> result = restTemplate.exchange(getBaseUrl(), HttpMethod.POST, new HttpEntity<>(AUTHOR_CREATE), String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void getAuthorByIdTest_good() {
        when(authorsService.findById(AUTHOR_ID)).thenReturn(AUTHOR_MODEL);

        assertEquals(AUTHOR_MODEL, restTemplate.getForObject(getBaseUrl() + "/" + AUTHOR_ID, AuthorModel.class));
    }

    @Test
    public void updateAuthorTest_good() {
        when(authorsService.update(AUTHOR_MODEL)).thenReturn(AUTHOR_MODEL);

        assertEquals(AUTHOR_MODEL, restTemplate.exchange(getBaseUrl(), HttpMethod.PUT, new HttpEntity<>(AUTHOR_MODEL), AuthorModel.class).getBody());
    }

    @Test
    public void deleteByIdTest_good() {
        ResponseEntity<String> result = restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(getBaseUrl()).queryParam("id", AUTHOR_ID).toUriString(), HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(authorsService).deleteById(AUTHOR_ID);
    }
}
