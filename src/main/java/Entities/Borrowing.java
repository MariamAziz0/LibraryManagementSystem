package Entities;

import java.util.Objects;

public class Borrowing {
    private int patron_id;
    private int book_id;
    private String borrowing_date;
    private String return_date;

    public Borrowing() {}

    public Borrowing(int patron_id, int book_id, String borrowing_date, String return_date) {
        this.patron_id = patron_id;
        this.book_id = book_id;
        this.borrowing_date = borrowing_date;
        this.return_date = return_date;
    }

    public int getPatron_id() {
        return patron_id;
    }

    public void setPatron_id(int patron_id) {
        this.patron_id = patron_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBorrowing_date() {
        return borrowing_date;
    }

    public void setBorrowing_date(String borrowing_date) {
        this.borrowing_date = borrowing_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrowing borrowing = (Borrowing) o;
        return patron_id == borrowing.patron_id && book_id == borrowing.book_id && Objects.equals(borrowing_date, borrowing.borrowing_date) && Objects.equals(return_date, borrowing.return_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patron_id, book_id, borrowing_date, return_date);
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "patron_id=" + patron_id +
                ", book_id=" + book_id +
                ", borrowing_date='" + borrowing_date + '\'' +
                ", return_date='" + return_date + '\'' +
                '}';
    }
}
