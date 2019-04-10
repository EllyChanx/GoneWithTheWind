package com.company.library

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class LibrarySpec extends FunSuite {

  test("#findBookByIsbn find book by full ISBN") {
    val library = new com.company.library.Library()
    library.findBookByIsbn("pidtkl") shouldBe (Book("Da Vinci Code,The", "Brown, Dan", "pidtkl"))
  }

}
