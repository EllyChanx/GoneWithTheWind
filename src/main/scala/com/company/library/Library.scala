package com.company.library

import scala.collection.mutable
import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import java.time.LocalDate

class Library {

  val authorMap:immutable.Map[String, List[Book]] = Books.all.groupBy(book => book.author)

  val isBookAvailable:mutable.HashMap[Book, Boolean] = mutable.HashMap(
    Books.all.map(x => x -> true): _*
  )

  val outBookStatus: mutable.Map[Book, List[outBook]] = mutable.Map[Book, List[outBook]]()
  val borrowDate: LocalDate = LocalDate.now
  val dueDate: LocalDate = borrowDate.plusDays(14)

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

  def borrowBook(book: Book, name: String): String = {
    if (book.reference) throw new InternalError("Reference book cannot be borrowed!")
    this.isBookAvailable.get(book) match {
      case None => throw new NoSuchElementException("Book doesn't exist!")
      case Some(isAvailable) =>
        if (!isAvailable) {
          throw new InternalError("Already borrowed this book!")
        } else {
          this.isBookAvailable(book) = false
          this.addOutBook(book, name)
          s"${book.title} - Borrowed Successfully"
        }

    }
  }

  def returnBook(book: Book): String = {
    this.isBookAvailable.get(book) match {
      case None => throw new NoSuchElementException("Book doesn't exist!")
      case Some(isAvailable) =>
        if (isAvailable) {
          throw new InternalError("Book already on shelf!")
        } else {
          this.isBookAvailable(book) = true
          this.removeOutBook(book)
          s"${book.title} - Return Successfully"
        }

    }
  }

  def addOutBook(book: Book, name: String): Unit = {
    val newStatus = List(outBook(name, borrowDate, dueDate))
    this.outBookStatus += book -> newStatus
  }

  def removeOutBook(book: Book): Unit = {
    this.outBookStatus -= book
  }

  def findLateOutBook():List[(Book, List[outBook])] = {
    val dueBooks = new ListBuffer[(Book, List[outBook])]()
    this.outBookStatus.foreach(x =>
       if (x._2.head.dueDate isBefore LocalDate.now) dueBooks += x
    )
    dueBooks.toList
  }

}
