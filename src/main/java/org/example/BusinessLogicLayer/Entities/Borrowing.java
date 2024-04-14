package org.example.BusinessLogicLayer.Entities;

import java.util.Objects;

public class Borrowing {
    private int patronId;
    private int bookId;
    private String borrowingDate;
    private String returnDate;

    public Borrowing() {}

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(String borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrowing borrowing = (Borrowing) o;
        return patronId == borrowing.patronId && bookId == borrowing.bookId && Objects.equals(borrowingDate, borrowing.borrowingDate) && Objects.equals(returnDate, borrowing.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId, bookId, borrowingDate, returnDate);
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "patron_id=" + patronId +
                ", book_id=" + bookId +
                ", borrowing_date='" + borrowingDate + '\'' +
                ", return_date='" + returnDate + '\'' +
                '}';
    }
}
