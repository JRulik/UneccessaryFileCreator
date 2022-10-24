module SimTestFx {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
