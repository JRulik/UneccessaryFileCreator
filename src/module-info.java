module UneccessaryFileCreator {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.controls,javafx.graphics, javafx.fxml;
}
