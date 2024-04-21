module rodrigolopezCOMP1011Sec03 {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.sql;
	requires java.sql.rowset;
	requires java.desktop;
	requires javafx.swing;
	
	opens rodrigolopez_ex01 to javafx.graphics, javafx.fxml;
}
