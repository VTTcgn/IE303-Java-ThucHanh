import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

// Ứng dụng hiển thị danh sách sản phẩm và chi tiết sản phẩm
public class ProductViewer extends Application {

    private ImageView mainImageView; // Hiển thị hình ảnh sản phẩm chính
    private Label productNameLabel; // Tên sản phẩm chính
    private Label priceLabel; // Giá sản phẩm chính
    private Label categoryLabel; // Danh mục sản phẩm chính
    private Label descriptionLabel; // Mô tả sản phẩm chính

    private List<Product> products; // Danh sách các sản phẩm

    @Override
    public void start(Stage primaryStage) {
        // Khởi tạo danh sách sản phẩm mẫu
        products = new ArrayList<>();
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "This product is excluded from all promotional discount and offers", "Adidas", "image/img1.png"));
        products.add(new Product("FORUM MID SHOES", "$100.00", "This product is excluded from all promotional discount and offers", "Adidas", "image/img2.png"));
        products.add(new Product("SUPERNOVA SHOES", "$150.00", "NMD City Stock 2", "Adidas", "image/img3.png"));
        products.add(new Product("Adidas", "$160.00", "NMD City Stock 2", "Adidas", "image/img4.png"));
        products.add(new Product("Adidas", "$120.00", "NMD City Stock 2", "Adidas", "image/img5.png"));
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "This product is excluded from all promotional discount and offers", "Adidas", "image/img6.png"));
        products.add(new Product("4DFWD PULSE SHOES", "$160.00", "This product is excluded from all promotional discount and offers", "Adidas", "image/img1.png"));
        products.add(new Product("FORUM MID SHOES", "$100.00", "This product is excluded from all promotional discount and offers", "Adidas", "image/img2.png"));

        // Khu vực hiển thị sản phẩm chính
        VBox mainProductBox = new VBox(3);
        mainImageView = new ImageView(new Image(products.get(0).imageUrl));
        mainImageView.setFitWidth(250);
        mainImageView.setPreserveRatio(true);

        productNameLabel = new Label(products.get(0).name);
        productNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        priceLabel = new Label(products.get(0).price);
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        categoryLabel = new Label(products.get(0).category);
        categoryLabel.setStyle("-fx-font-size: 12px;");

        descriptionLabel = new Label(products.get(0).description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(280);
        descriptionLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");

        // Tạo đường ngang (Separator)
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #000000;"); // Màu của đường ngang
        separator.setPrefHeight(1); // Độ dày của đường ngang
        separator.setMaxWidth(280); // Đặt chiều rộng bằng với descriptionLabel để căn chỉnh

        mainProductBox.getChildren().addAll(mainImageView, separator, productNameLabel, priceLabel, categoryLabel, descriptionLabel);
        mainProductBox.setPadding(new Insets(10));

        // Khu vực danh sách các sản phẩm
        TilePane productList = new TilePane();
        productList.setHgap(10);
        productList.setVgap(10);
        for (Product product : products) {
            VBox productBox = new VBox(5);

            Label name = new Label(product.name);
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label description = new Label(product.description);
            description.setMaxWidth(140);
            description.setEllipsisString("...");
            description.setTextOverrun(OverrunStyle.ELLIPSIS);
            description.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");

            ImageView img = new ImageView(new Image(product.imageUrl));
            img.setFitWidth(140);
            img.setPreserveRatio(true);

            Label category = new Label(product.category);
            category.setStyle("-fx-font-size: 12px;");

            Label price = new Label(product.price);
            price.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            HBox priceBox = new HBox();
            priceBox.getChildren().addAll(category, new Label("   "), price);
            priceBox.setSpacing(5); // Giảm spacing từ 10 xuống 5
            priceBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(price, Priority.ALWAYS); // Đẩy price sang bên phải
            price.setMaxWidth(Double.MAX_VALUE); // Đảm bảo price chiếm không gian tối đa
            price.setAlignment(Pos.CENTER_RIGHT); // Căn lề phải cho price

            productBox.getChildren().addAll(name, description, img, priceBox);
            productBox.setPadding(new Insets(10));
            productBox.setSpacing(8);
            productBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-background-color: #F5F5F5;");

            // Xử lý sự kiện khi nhấp vào sản phẩm
            productBox.setOnMouseClicked(e -> updateMainProduct(product));
            productList.getChildren().add(productBox);
        }

        // Bố cục chính của giao diện
        HBox root = new HBox(20);
        root.setStyle("-fx-background-color: #FFFFFF;");
        root.getChildren().addAll(mainProductBox, productList);

        Scene scene = new Scene(root, 1100, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Product Viewer");
        primaryStage.show();
    }

    // Cập nhật thông tin sản phẩm chính với hiệu ứng chuyển đổi
    private void updateMainProduct(Product product) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), mainImageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            mainImageView.setImage(new Image(product.imageUrl));
            productNameLabel.setText(product.name);
            priceLabel.setText(product.price);
            descriptionLabel.setText(product.description);
            categoryLabel.setText(product.category);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), mainImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Lớp lưu trữ thông tin sản phẩm
    static class Product {
        String name, price, imageUrl, description, category;
        Product(String name, String price, String description, String category, String imageUrl) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imageUrl = imageUrl;
            this.category = category;
        }
    }
}