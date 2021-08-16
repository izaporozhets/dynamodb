package global.sns.controller;


import global.sns.model.Book;
import global.sns.model.Page;
import global.sns.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    public BookService service;

    @GetMapping("/books")
    public List<Book> getAll(){
        return service.getAll();
    }

    @GetMapping("/book/{id}")
    public Book getById(@PathVariable("id")String id){
        return service.getBook(id);
    }

    @PostMapping("/book")
    public Book saveBook(@RequestBody Book book){
        return service.saveBook(book);
    }

    @GetMapping("/book/name/{name}")
    public List<Book> getByName(@PathVariable("name")String name){
        return service.findByName(name);
    }

    @GetMapping("/book/genre/{genre}")
    public List<Book> getByGenre(@PathVariable("genre")String genre){
        return service.findByGenre(genre);
    }

    @GetMapping(value = "/book/{id}/pages")
    public List<Book> getByCollectionAndId(@PathVariable("id")String bookId, @RequestBody List<Page> pages){
        return service.findByNestedCollectionQuery(bookId, pages);
    }

    @GetMapping(value = "/book/bypages")
    public List<Book> getByCollection(@RequestBody List<Page> pages){
        return service.findByNestedCollectionScan(pages);
    }

    @GetMapping(value = "/book/pagination/{limit}")
    public void getPage(@PathVariable("limit") Integer limit){
        service.getBookByPage(limit);
    }
}
