package com.company.library

import scala.collection.mutable
import scala.collection.immutable
import java.time.LocalDate

class Library {

  val authorMap:immutable.Map[String, List[Book]] = Books.all.groupBy(book => book.author)
  val titleMap:immutable.Map[String, List[Book]] = Books.all.groupBy(book => book.title)

  var isBookAvailable:mutable.HashMap[Book, Boolean] = mutable.HashMap(
    Books.all.map(x => x -> true): _*
  )

  var outBook: mutable.Map[Book, List[outBookStatus]] = mutable.Map[Book, List[outBookStatus]]()
  val borrowDate: LocalDate = LocalDate.now
  val dueDate: LocalDate = borrowDate.plusDays(14)

  def findBookByTitle(title: String):List[Book] = {
    this.titleMap.filter(
      p => p._1.containsSlice(title)
    ).values.flatten.toList
  }

  def findBookByAuthor(author: String): List[Book] ={
    this.authorMap.filter(
      p => p._1.containsSlice(author)
    ).values.flatten.toList
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
          throw new Exception("Already borrowed this book!")
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
          throw new Exception("Book already on shelf!")
        } else {
          this.isBookAvailable(book) = true
          this.removeOutBook(book)
          s"${book.title} - Return Successfully"
        }
    }
  }

  def addOutBook(book: Book, name: String): Unit = {
    val newStatus = List(outBookStatus(name, borrowDate, dueDate))
    this.outBook += book -> newStatus
  }

  def removeOutBook(book: Book): Unit = this.outBook -= book

  def findLateOutBook(): Map[Book, List[outBookStatus]] = {
    this.outBook.collect {
      case (k, v) if v.head.dueDate isBefore LocalDate.now => k -> v
    }.toMap
  }

}
