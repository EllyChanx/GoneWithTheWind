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

  test("#isBookBorrowed borrow non-existed book return error") {
    val library = new com.company.library.Library()
    val nonExistBook = Book("a book", "an author", "a isbn")
    an [NoSuchElementException] should be thrownBy(library.isBookBorrowed(nonExistBook))
  }

  test("#borrowBook can borrow book if book available") {
    val library = new com.company.library.Library()
    val book = Book("Da Vinci Code,The", "Brown, Dan", "pidtkl")
    library.isBookBorrowed(book) shouldBe false
    library.borrowBook(book)
    library.isBookBorrowed(book) shouldBe true
    the [InternalError] thrownBy(library.borrowBook(book)) should have message "Already borrowed this book!!"
  }

//  test("") {
//                  ???
//  }

}
