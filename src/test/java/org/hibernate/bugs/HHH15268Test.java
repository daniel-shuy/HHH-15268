package org.hibernate.bugs;

import jakarta.persistence.*;

import org.hibernate.annotations.Immutable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class HHH15268Test {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void hhh15268Test() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		var book = new Book();
		book.setBorrowed(true);
		entityManager.persist(book);

		var borrowedBooks = entityManager.createQuery("from BorrowedBook ").getResultList();

		entityManager.getTransaction().commit();
		entityManager.close();

		assertThat(borrowedBooks)
				.allSatisfy(borrowedBook -> assertThat(borrowedBook).isOfAnyClassIn(BorrowedBook.class));
	}

	@Entity(name = "Publication")
	@Table(name = "publication")
	@Inheritance(strategy = InheritanceType.JOINED)
	public abstract static class Publication {
		@Id
		@GeneratedValue
		private Long id;

		private String title;

		private String author;

		public Long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}
	}

	@Entity(name = "BlogPost")
	@Table(name = "blog_post")
	public static class BlogPost extends Publication {
		private String url;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	@MappedSuperclass
	public abstract static class AbstractBook extends Publication {
		private int pageCount;

		private boolean borrowed;

		public int getPageCount() {
			return pageCount;
		}

		public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}

		public boolean isBorrowed() {
			return borrowed;
		}

		public void setBorrowed(boolean borrowed) {
			this.borrowed = borrowed;
		}
	}

	@Entity(name = "Book")
	@Table(name = "book")
	public static class Book extends AbstractBook {
	}

	@Entity(name = "BorrowedBook")
	@Table(name = "borrowed_book")
	@Immutable
	public static class BorrowedBook extends AbstractBook {
	}
}
