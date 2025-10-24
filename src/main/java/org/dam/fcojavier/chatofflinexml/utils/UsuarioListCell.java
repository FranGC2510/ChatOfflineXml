package org.dam.fcojavier.chatofflinexml.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.dam.fcojavier.chatofflinexml.model.Usuario;

/**
 * Celda personalizada para la ListView de usuarios.
 * Muestra un ícono de avatar y el nombre del usuario.
 */
public class UsuarioListCell extends ListCell<Usuario> {

    private final HBox content;
    private final ImageView avatarImageView;
    private final Label nameLabel;

    public UsuarioListCell() {
        super();
        avatarImageView = new ImageView();
        avatarImageView.setFitHeight(32);
        avatarImageView.setFitWidth(32);

        nameLabel = new Label();
        nameLabel.getStyleClass().add("user-list-cell-label");

        VBox vBox = new VBox(nameLabel);
        vBox.setAlignment(Pos.CENTER_LEFT);

        content = new HBox(avatarImageView, vBox);
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Usuario user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null && !empty) {
            nameLabel.setText(user.getNombre() + " " + user.getApellido());
            // Usamos un ícono genérico, pero esto podría extenderse para avatares personalizados
            avatarImageView.setImage(new Image(getClass().getResourceAsStream("/images/usuario.png")));
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}
