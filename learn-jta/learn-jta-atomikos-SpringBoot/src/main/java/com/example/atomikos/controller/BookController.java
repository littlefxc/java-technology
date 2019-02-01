package com.example.atomikos.controller;

import com.example.atomikos.entity.BookDO;
import com.example.atomikos.entity.BookVo;
import com.example.atomikos.service.BookService;
import com.example.atomikos.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fengxuechao
 */
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<BookDO> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return bookService.list(page, size);
    }

    @GetMapping("/{id}")
    public BookDO get(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PostMapping
    public BookDO save(@RequestBody BookVo book) {
        return bookService.save(book, book.getUser());
    }

    @PutMapping
    public BookDO update(@RequestBody BookVo book) {
        return ((BookServiceImpl) bookService).update(book, book.getUser());
    }

    @DeleteMapping("/{id}")
    public int delete(@PathVariable Long id) {
        return ((BookServiceImpl) bookService).delete(id);
    }

}
