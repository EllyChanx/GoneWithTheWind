package com.company.library

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class LibrarySpec extends FunSuite {

  test("#findBookByTitle find book by partial title" ) {
    val library = new com.company.library.Library()
    library.findBookByTitle("Life") shouldBe List(Book("Life of Pi", "Martel, Yann", "nggzbsum"), Book("You are What You Eat:The Plan That Will Change Your Life", "McKeith, Gillian","xskevg"))
  }

  test("#findBookByAuthor") {
    val library = new com.company.library.Library()
    library.findBookByAuthor("Khal") shouldBe List(Book("Kite Runner,The", "Hosseini, Khaled", "brldgqczq"), Book("Thousand Splendid Suns,A", "Hosseini, Khaled", "itlizwzbz"))
  }

  test("#findBookByIsbn find book by full ISBN") {
    val library = new com.company.library.Library()
    library.findBookByIsbn("pidtkl") shouldBe (Book("Da Vinci Code,The", "Brown, Dan", "pidtkl"))
  }

  test("#findBookByTitle, #findBookByAuthor, #findBookByIsbn for reference books") {
    val library = new com.company.library.Library()
    library.findBookByTitle("Ref") shouldBe List(Book("Reference Book 1", "Coffee", "qwerty", true), Book("Reference Book 2", "Coffee", "asdfgh", true), Book("Reference Book 3", "Mocha", "zxcvbn", true))
    library.findBookByAuthor("Coffe") shouldBe List(Book("Reference Book 1", "Coffee", "qwerty", true), Book("Reference Book 2", "Coffee", "asdfgh", true))
    library.findBookByIsbn("zxcvbn") shouldBe Book("Reference Book 3", "Mocha", "zxcvbn", true)
  }

  test("#isBookBorrowed borrow non-existed book return error") {
    val library = new com.company.library.Library()
    val nonExistBook = Book("a book", "an author", "a isbn")
    an [NoSuchElementException] should be thrownBy(library.isBookAvailable(nonExistBook))
  }

  test("#borrowBook can borrow book if book available") {
    val library = new com.company.library.Library()
    val book = Book("Da Vinci Code,The", "Brown, Dan", "pidtkl")
    library.isBookAvailable(book) shouldBe true
    library.borrowBook(book) shouldBe "Da Vinci Code,The - Borrowed Successfully"
    library.isBookAvailable(book) shouldBe false
    the [InternalError] thrownBy(library.borrowBook(book)) should have message "Already borrowed this book!"
  }

  test("#borrowBook cannot borrow reference book") {
    val library = new com.company.library.Library()
    val refBook = Book("Reference Book 3", "Mocha", "zxcvbn", true)
    the [InternalError] thrownBy(library.borrowBook(refBook)) should have message "Reference book cannot be borrowed!"
  }

}
