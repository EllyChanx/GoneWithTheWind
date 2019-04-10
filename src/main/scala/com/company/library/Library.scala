package com.company.library

import scala.collection.immutable
import scala.collection.mutable.ListBuffer


class Library {

  val authorMap:immutable.Map[String, List[Book]] = Books.all.groupBy(book => book.author)

  def findBookByTitle(title: String): List[Book] = {
    Books.all.filter(_.title.contains(title))
  }

  def findBookByAuthor(author: String) = {
    val matchBooks = new ListBuffer[Book]()
    val matchAuthor = this.authorMap.keys.filter(_.contains(author))
    matchAuthor.foreach(author => this.authorMap.get(author) match {
      case Some(value) => matchBooks ++= value
    })
    matchBooks.toList
  }

  def findBookByIsbn(isbn: String): Book = {
    Books.all.filter(_.ISBN == isbn).head
  }

}
