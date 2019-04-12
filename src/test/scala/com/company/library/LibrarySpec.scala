package com.company.library

import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import java.time.LocalDate

class LibrarySpec extends FunSuite with BeforeAndAfterEach {

  var library: Library = _
  var book1: Book = Book("Da Vinci Code,The", "Brown, Dan", "pidtkl")
  var book2: Book = Book("Life of Pi", "Martel, Yann", "nggzbsum")
  val refBook: Book = Book("Reference Book 3", "Mocha", "zxcvbn", reference = true)
  val nonExistBook = Book("a book", "an author", "an isbn")
  val borrower: String = "TesterEl"
  val today: LocalDate = LocalDate.now

  override def beforeEach(): Unit = {
    library = new com.company.library.Library() // reset library to clear the book status in hashes
  }

  test("#findBookByTitle by partial title") {
    library.findBookByTitle("Life") shouldBe List(Book("You are What You Eat:The Plan That Will Change Your Life", "McKeith, Gillian", "xskevg"), book2)
  }

  test("#findBookByAuthor by partial author") {
    library.findBookByAuthor("Khal") shouldBe List(Book("Kite Runner,The", "Hosseini, Khaled", "brldgqczq"), Book("Thousand Splendid Suns,A", "Hosseini, Khaled", "itlizwzbz"))
  }

  test("#findBookByIsbn by full ISBN") {
    library.findBookByIsbn("pidtkl") shouldBe Book("Da Vinci Code,The", "Brown, Dan", "pidtkl")
  }

  test("#findBookByTitle, #findBookByAuthor, #findBookByIsbn for reference books") {
    library.findBookByTitle("Ref") shouldBe List(Book("Reference Book 2", "Coffee", "asdfgh", reference = true), Book("Reference Book 3", "Mocha", "zxcvbn", reference = true), Book("Reference Book 1", "Coffee", "qwerty", reference = true))
    library.findBookByAuthor("Coffe") shouldBe List(Book("Reference Book 1", "Coffee", "qwerty", reference = true), Book("Reference Book 2", "Coffee", "asdfgh", reference = true))
    library.findBookByIsbn("zxcvbn") shouldBe Book("Reference Book 3", "Mocha", "zxcvbn", reference = true)
  }

  test("#borrowBook - Error Case: non-existed book") {
    the[NoSuchElementException] thrownBy library.borrowBook(nonExistBook, borrower) should have message "Book doesn't exist!"
  }

  test("#borrowBook - book availability change from true to false") {
    library.isBookAvailable(book1) shouldBe true
    library.borrowBook(book1, borrower) shouldBe "Da Vinci Code,The - Borrowed Successfully"
    library.isBookAvailable(book1) shouldBe false
  }

  test("#borrowBook - Error Case: reference book") {
    the[InternalError] thrownBy library.borrowBook(refBook, borrower) should have message "Reference book cannot be borrowed!"
  }

  test("#borrowBook - Error Case: borrow twice") {
    library.borrowBook(book1, borrower)
    the[Exception] thrownBy library.borrowBook(book1, borrower) should have message "Already borrowed this book!"
  }

  test("#returnBook book availability become true after return") {
    library.isBookAvailable(book1) shouldBe true
    library.borrowBook(book1, borrower)
    library.isBookAvailable(book1) shouldBe false
    library.returnBook(book1) shouldBe "Da Vinci Code,The - Return Successfully - Is Book Overdue: false"
    library.isBookAvailable(book1) shouldBe true
  }

  test("#returnBook - Error Case: reference book / book never borrowed") {
    the[Exception] thrownBy library.returnBook(refBook) should have message "Book already on shelf!"
    the[Exception] thrownBy library.returnBook(book1) should have message "Book already on shelf!"
  }

  test("#returnBook - Error Case: non-existed book") {
    the[NoSuchElementException] thrownBy library.returnBook(nonExistBook) should have message "Book doesn't exist!"
  }

  test("#addOutBook add borrowed book to outBookStatus") {
    library.addOutBook(book1, borrower)
    library.outBook.head shouldBe (book1, List(outBookStatus(borrower,LocalDate.now, LocalDate.now.plusDays(14))))
  }

  test("#removeOutBook remove borrowed book from outBookStatus") {
    library.addOutBook(book1, borrower)
    library.removeOutBook(book1)
    library.outBook shouldBe Map()
  }

  test("#borrowBook & #returnBook update #outBookStatus") {
    library.borrowBook(book1, borrower)
    library.borrowBook(book2, borrower)
    library.outBook.head shouldBe (book2, List(outBookStatus(borrower,today, today.plusDays(14))))
    library.outBook.last shouldBe (book1, List(outBookStatus(borrower,today, today.plusDays(14))))
    library.returnBook(book1)
    library.outBook.head shouldBe (book2, List(outBookStatus(borrower,today, today.plusDays(14))))
  }

  test("#findLateOutBook search due books from #outBookStatus") {
    library.borrowBook(book1, borrower)
    library.borrowBook(book2, borrower)
    library.outBook(book1) = List(outBookStatus(borrower,today, today.minusDays(1)))
    library.findLateOutBook() shouldBe Map(book1 -> List(outBookStatus(borrower,today, today.minusDays(1))))
  }
}