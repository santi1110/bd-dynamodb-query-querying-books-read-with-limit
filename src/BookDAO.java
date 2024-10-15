import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDAO {

    private DynamoDBMapper mapper;

    /**
     * Allows access to and manipulation of Book objects from the data store.
     * @param mapper Access to DynamoDB
     */
    public BookDAO(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Uses the queryPage() method to query the BooksRead table for the given employeeId. If exclusiveStartAsin is null,
     * this method will return the list of books starting from the beginning. If exclusiveStartAsin is not null, it will
     * return a list of books starting from after the exclusiveStartAsin.
     *
     * @param employeeId the given employee Id to query on
     * @param exclusiveStartAsin the given exclusive start asin for the query call.
     * @param limit the upper limit of items retrieved from the query
     * @return list of Books for employee id
     */
    public List<Book> getBooksReadByEmployee(String employeeId, String exclusiveStartAsin, int limit) {
        //TODO: implement
        Book bookKey = new Book();
        bookKey.setId(employeeId);

        // Create the query expression
        DynamoDBQueryExpression<Book> queryExpression = new DynamoDBQueryExpression<Book>()
                .withHashKeyValues(bookKey)
                .withLimit(limit);

        // Set the exclusive start key if provided
        if (exclusiveStartAsin != null) {
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("id", new AttributeValue().withS(employeeId));
            exclusiveStartKey.put("asin", new AttributeValue().withS(exclusiveStartAsin));
            queryExpression.setExclusiveStartKey(exclusiveStartKey);
        }

        // Execute the queryPage() method
        QueryResultPage<Book> resultPage = mapper.queryPage(Book.class, queryExpression);

        // Return the list of books
        return resultPage.getResults();
    }

}
