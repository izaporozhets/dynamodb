package global.sns.service;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import global.sns.model.Book;
import global.sns.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public List<Book> getAll(){
        return dynamoDBMapper.scan(Book.class, new DynamoDBScanExpression());
    }

    public Book saveBook(Book book){
        dynamoDBMapper.save(book);
        return book;
    }

    public Book getBook(String bookId){
        return dynamoDBMapper.load(Book.class, bookId);
    }

    public Book deleteBook(Book book){
        dynamoDBMapper.delete(book);
        return book;
    }

    public Book editBook(Book book){
        dynamoDBMapper.save(book, buildExpression(book));
        return book;
    }

    public List<Book> findByName(String name){
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":val1", new AttributeValue().withS(name));
        Map<String, String> attributeNames = new HashMap<>();
        attributeNames.put("#name","name");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with (#name,:val1)").withExpressionAttributeNames(attributeNames)
                .withExpressionAttributeValues(attributeValues);

        return dynamoDBMapper.scan(Book.class, scanExpression);
    }

    public List<Book> findByGenre(String genre){
        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":val1", new AttributeValue().withS(genre));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with (genre,:val1)")
                .withExpressionAttributeValues(attributeValues);
        return dynamoDBMapper.scan(Book.class, scanExpression);
    }

    public List<Book> findByNestedCollectionScan(List<Page> pages){
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":pages", new AttributeValue().withM(fillPageMap(pages)));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains (pages,:pages)")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.scan(Book.class, scanExpression);

    }

    public List<Book> findByNestedCollectionQuery(String bookId,List<Page> pages){

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":pages", new AttributeValue().withM(fillPageMap(pages)));
        eav.put(":bookId",new AttributeValue().withS(bookId));

        DynamoDBQueryExpression<Book> queryExpression = new DynamoDBQueryExpression<Book>()
                .withFilterExpression("contains (pages, :pages)")
                .withKeyConditionExpression("bookId = :bookId")
                .withExpressionAttributeValues(eav);
        return dynamoDBMapper.query(Book.class, queryExpression);
    }

    public void getBookByPage(Integer limit){
        DynamoDBScanExpression paginatedScanListExpression = new DynamoDBScanExpression()
                .withLimit(limit);
        PaginatedScanList<Book> paginatedList = dynamoDBMapper.scan(Book.class, paginatedScanListExpression);
        paginatedList.forEach(System.out::println);
    }

    private Map<String, AttributeValue> fillPageMap(List<Page> pages) {
        Map<String, AttributeValue> withM = new HashMap<>();
        for (Page page: pages) {
            withM.put("width", new AttributeValue().withN(String.valueOf(page.getWidth())));
            withM.put("height", new AttributeValue().withN(String.valueOf(page.getHeight())));
            withM.put("letters", new AttributeValue().withN(String.valueOf(page.getLetters())));
        }
        return withM;
    }

    private DynamoDBSaveExpression buildExpression(Book book){
        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
        expectedAttributeValueMap.put("bookId",new ExpectedAttributeValue(
                new AttributeValue().withS(book.getBookId())));
        dynamoDBSaveExpression.setExpected(expectedAttributeValueMap);
        return dynamoDBSaveExpression;
    }
}
;