Sample test case to reproduce Hibernate Bug [HHH-15268](https://hibernate.atlassian.net/browse/HHH-15268)

## Running the test

**Prerequisites: Maven**

```sh
mvn test
```

The test should fail with:
```
java.lang.AssertionError: 
Expecting all elements of:
  <[org.hibernate.bugs.HHH15268Test$Book@...]>
to satisfy given requirements, but these elements did not:

  <org.hibernate.bugs.HHH15268Test$Book@...> error: 
Expecting:
 <org.hibernate.bugs.HHH15268Test$Book@...>
to be of one these types:
 <[org.hibernate.bugs.HHH15268Test.BorrowedBook]>
but was:
 <org.hibernate.bugs.HHH15268Test.Book>
```

## Domain Model
![image](https://user-images.githubusercontent.com/17351764/169220954-17bc2796-64d1-4355-abc3-e5b64412cdd4.png)

- `publication` is a polymorphic table, with `blog_post` and `book` as implementations
- `borrowed_book` is a view of `book`, where `borrowed = true`

This is represented in JPA as:
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Publication {
  @Id
  @GeneratedValue
  private Long id;

  private String title;

  private String author;

  // getters and setters
}

@Entity
public class BlogPost extends Publication {
  private String url;

  // getters and setters
}

@MappedSuperclass
public abstract class AbstractBook extends Publication {
  private int pageCount;

  private boolean borrowed;

  // getters and setters  
}

@Entity
public class Book extends AbstractBook {
}

@Entity
public class BorrowedBook extends AbstractBook {
}
```
