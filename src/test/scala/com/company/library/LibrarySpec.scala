package com.company.library

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class LibrarySpec extends FunSuite {

  test("#findBookByTitle find book by partial title" ) {
    val library = new com.company.library.Library()
    library.findBookByTitle("Life") shouldBe List(Book("Life of Pi", "Martel, Yann", "nggzbsum"), Book("You are What You Eat:The Plan That Will Change Your Life", "McKeith, Gillian","xskevg"))
  }

  test("#findBookByIsbn find book by full ISBN") {
    val library = new com.company.library.Library()
    library.findBookByIsbn("pidtkl") shouldBe (Book("Da Vinci Code,The", "Brown, Dan", "pidtkl"))
  }

}
