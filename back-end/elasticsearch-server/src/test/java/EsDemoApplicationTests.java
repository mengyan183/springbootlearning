import com.xing.elasticsearch.ElasticSearchServerApplication;
import com.xing.elasticsearch.entity.Book;
import com.xing.elasticsearch.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ElasticSearchServerApplication.class,})
public class EsDemoApplicationTests {

    @Autowired
    private BookService bookService;

    @Test
    public void getOne() {
        System.out.println(bookService.getById(1).toString());
    }

    @Test
    public void getAll() {
        List<Book> res = bookService.getAll();
        res.forEach(System.out::println);
    }

    @Test
    public void addOneTest() {
        bookService.putOne(new Book(1, 1, "格林童话"));
        bookService.putOne(new Book(2, 1, "美人鱼"));
    }

    @Test
    public void addBatchTest() {
        List<Book> list = new ArrayList<>();
        list.add(new Book(3, 1, "第一本书"));
        list.add(new Book(4, 1, "第二本书"));
        bookService.putList(list);
    }

    @Test
    public void deleteBatch() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        bookService.deleteBatch(list);
    }

    @Test
    public void deleteByQuery() {
        bookService.deleteByUserId(1);
    }

    @Test
    public void deleteIndex() {
        bookService.deleteIndex();
    }

    @Test
    public void updateList(){
        List<Book> list = new ArrayList<>();
        list.add(new Book(3, 1, "第1本书"));
        list.add(new Book(4, 1, "第2本书"));
        list.add(new Book(4, 1, "第3本书"));
        list.add(new Book(1, 1, "第4本书"));
        bookService.updateList(list);
    }

    @Test
    public void testPage(){
        List<Book> books = bookService.searchPage();
    }
}