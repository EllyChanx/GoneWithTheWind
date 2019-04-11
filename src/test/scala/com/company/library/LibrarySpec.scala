package com.company.library

import org.scalatest.Matchers._
import org.scalatest.{BeforeAndAfterEach, FunSuite}


class LibrarySpec extends FunSuite with BeforeAndAfterEach {

  var library: Library = _
  var book: Book = _
  var nonExistBook: Book = _
  var refBook: Book = _

  override def beforeEach(): Unit = {
    library = new com.company.library.Library()
    book = Book("Da Vinci Code,The", "Brown, Dan", "pidtkl")
    nonExistBook = Book("a book", "an author", "an isbn")
    refBook = Book("Reference Book 3", "Mocha", "zxcvbn", true)
  }

  test("#findBookByTitle find book by partial title") {
    library.findBookByTitle("Life") shouldBe List(Book("Life of Pi", "Martel, Yann", "nggzbsum"), Book("You are What You Eat:The Plan That Will Change Your Life", "McKeith, Gillian", "xskevg"))
  }

  test("#findBookByAuthor") {
    library.findBookByAuthor("Khal") shouldBe List(Book("Kite Runner,The", "Hosseini, Khaled", "brldgqczq"), Book("Thousand Splendid Suns,A", "Hosseini, Khaled", "itlizwzbz"))
  }

  test("#findBookByIsbn find book by full ISBN") {
    library.findBookByIsbn("pidtkl") shouldBe (Book("Da Vinci Code,The", "Brown, Dan", "pidtkl"))
  }

  test("#findBookByTitle, #findBookByAuthor, #findBookByIsbn for reference books") {
    library.findBookByTitle("Ref") shouldBe List(Book("Reference Book 1", "Coffee", "qwerty", true), Book("Reference Book 2", "Coffee", "asdfgh", true), Book("Reference Book 3", "Mocha", "zxcvbn", true))
    library.findBookByAuthor("Coffe") shouldBe List(Book("Reference Book 1", "Coffee", "qwerty", true), Book("Reference Book 2", "Coffee", "asdfgh", true))
    library.findBookByIsbn("zxcvbn") shouldBe Book("Reference Book 3", "Mocha", "zxcvbn", true)
  }

  test("#isBookAvailable enter non-existed book return error") {
    an[NoSuchElementException] should be thrownBy (library.isBookAvailable(nonExistBook))
  }

  test("#borrowBook - book availability default: true; after borrow: false") {
    library.isBookAvailable(book) shouldBe true
    library.borrowBook(book) shouldBe "Da Vinci Code,The - Borrowed Successfully"
    library.isBookAvailable(book) shouldBe false
    the[InternalError] thrownBy (library.borrowBook(book)) should have message "Already borrowed this book!"
  }

  test("#borrowBook cannot borrow reference book") {
    the[InternalError] thrownBy (library.borrowBook(refBook)) should have message "Reference book cannot be borrowed!"
  }

  test("#returnBook book availability become true after return") {
    library.isBookAvailable(book) shouldBe true
    library.borrowBook(book)
    library.isBookAvailable(book) shouldBe false
    library.returnBook(book) shouldBe "Da Vinci Code,The - Return Successfully"
    library.isBookAvailable(book) shouldBe true
  }

  test ("#returnBook - Error Case: reference book / book never borrowed") {
    the[InternalError] thrownBy (library.returnBook(refBook)) should have message "Book already on shelf!"
    the[InternalError] thrownBy (library.returnBook(book)) should have message "Book already on shelf!"
  }

  test ("#returnBook - Error Case: non-existed book") {
    the[NoSuchElementException] thrownBy(library.returnBook(nonExistBook)) should have message "Book doesn't exist!"
  }
}