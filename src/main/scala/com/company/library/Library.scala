package com.company.library

class Library {

  def findBookByTitle(title: String): List[Book] = {
    Books.all.filter(_.title.contains(title))
  }

  def findBookByIsbn(isbn: String): Book = {
    Books.all.filter(_.ISBN == isbn).head
  }

}
