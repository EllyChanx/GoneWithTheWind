package com.company.library

import scala.collection.mutable
import scala.collection.immutable
import scala.collection.mutable.ListBuffer


class Library {

  val authorMap:immutable.Map[String, List[Book]] = Books.all.groupBy(book => book.author)

  val isBookAvailable:mutable.HashMap[Book, Boolean] = mutable.HashMap(
    Books.all.map(x => (x -> true)): _*
  )

  def findBookByTitle(title: String): List[Book] = {
    Books.all.filter(_.title.contains(title))
  }

  def findBookByAuthor(author: String): List[Book] = {
    val matchBooks = new ListBuffer[Book]()
    val matchAuthors = this.authorMap.keys.filter(_.contains(author))
    matchAuthors.foreach(author => this.authorMap.get(author) match {
      case Some(value) => matchBooks ++= value
    })
    matchBooks.toList
  }

  def findBookByIsbn(isbn: String): Book = {
    Books.all.filter(_.ISBN == isbn).head
  }

  def borrowBook(book: Book): String = {
    if (book.reference) throw new InternalError("Reference book cannot be borrowed!")
    this.isBookAvailable.get(book) match {
      case Some(isAvailable) => {
        if (!isAvailable) {
          throw new InternalError("Already borrowed this book!")
        } else {
          this.isBookAvailable(book) = false
          s"${book.title} - Borrowed Successfully"
        }
      }
      case None => throw new NoSuchElementException("Book doesn't exist!")
    }
  }

  def returnBook(book: Book): String = {
    this.isBookAvailable.get(book) match {
      case Some(isAvailable) => {
        if (isAvailable) {
          throw new InternalError("Book already on shelf!")
        } else {
          this.isBookAvailable(book) = true
          s"${book.title} - Return Successfully"
        }
      }
      case None => throw new NoSuchElementException("Book doesn't exist!")
    }
  }

}
