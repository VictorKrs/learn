package liga.tasks.ru.learn.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import liga.tasks.ru.learn.models.book.BookCreate;
import liga.tasks.ru.learn.models.book.BookModel;
import liga.tasks.ru.learn.services.BooksService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BooksControllerTest {
    private final String TITLE = "title";
    private final Long ID = 1L;
    private final BookModel BOOK_MODEL = BookModel.builder().id(ID).title(TITLE).build();

    @MockBean
    private BooksService booksService;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/book";
    }


    @Test
    public void addBookTest() {
        BookCreate bookCreate = BookCreate.builder().title(TITLE).build();
        when(booksService.save(bookCreate)).thenReturn(BOOK_MODEL);

        BookModel result = restTemplate.postForEntity(getBaseUrl(), bookCreate, BookModel.class).getBody();

        checkResult(result);
    }

    @Test
    public void getBookByIdTest() {
        when(booksService.findById(ID)).thenReturn(BOOK_MODEL);

        BookModel result = restTemplate.getForEntity(getBaseUrl() + "/" + ID, BookModel.class).getBody();

        checkResult(result);
    }

    @Test
    public void updateBookTest() {
        when(booksService.update(BOOK_MODEL)).thenReturn(BOOK_MODEL);

        BookModel result = restTemplate.exchange(getBaseUrl(), HttpMethod.PUT, new HttpEntity<>(BOOK_MODEL), BookModel.class).getBody();

        checkResult(result);
    }

    @Test
    public void deleteByIdTest() {
        ResponseEntity<String> result = restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(getBaseUrl()).queryParam("id",ID).toUriString(), HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
    
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    private void checkResult(BookModel result) {
        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(TITLE, result.getTitle());
    }
}
