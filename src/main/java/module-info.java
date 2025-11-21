module com.burrito.king.burrito_king_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.burrito.king.burrito_king_3 to javafx.fxml;
    exports com.burrito.king.burrito_king_3;
}