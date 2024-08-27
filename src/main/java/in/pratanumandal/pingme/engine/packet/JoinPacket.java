package in.pratanumandal.pingme.engine.packet;

import in.pratanumandal.pingme.engine.entity.ChatImage;
import javafx.scene.image.Image;

import java.security.PublicKey;

public class JoinPacket implements Packet {

    private final String name;
    private final ChatImage avatar;
    private final PublicKey publicKey;

    public JoinPacket(String name, Image avatar, PublicKey publicKey) {
        this.name = name;
        this.avatar = new ChatImage(avatar);
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public Image getAvatar() {
        return avatar.getImage();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public PacketType getType() {
        return PacketType.JOIN;
    }

}
