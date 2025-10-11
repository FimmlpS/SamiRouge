package SamiRouge.helper;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ImageHelper {
    public static final TextureAtlas.AtlasRegion deviceFix;
    public static final TextureAtlas.AtlasRegion snow01;
    public static final TextureAtlas.AtlasRegion snow02;
    public static final TextureAtlas.AtlasRegion snow03;

    public static final TextureAtlas.AtlasRegion xgMiddle01;
    public static final TextureAtlas.AtlasRegion xgMiddle02;
    public static final TextureAtlas.AtlasRegion xgMiddle03;
    public static final TextureAtlas.AtlasRegion xgLeft01;
    public static final TextureAtlas.AtlasRegion xgLeft02;
    public static final TextureAtlas.AtlasRegion xgRight01;
    public static final TextureAtlas.AtlasRegion xgRight02;
    public static final TextureAtlas.AtlasRegion xgText;
    public static final TextureAtlas.AtlasRegion xgMask;
    public static final TextureAtlas.AtlasRegion xgCipherBg;
    public static final TextureAtlas.AtlasRegion xgDeclare;


    static {
        deviceFix = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/icons/DeviceFix.png"),0,0,172,134);
        snow01 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/snow01.png"),0,0,32,64);
        snow02 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/snow02.png"),0,0,32,64);
        snow03 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/snow03.png"),0,0,32,64);

        xgMiddle01 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgMiddle01.png"),0,0,1200,1000);
        xgMiddle02 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgMiddle02.png"),0,0,1200,1000);
        xgMiddle03 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgMiddle03.png"),0,0,1200,1000);
        xgLeft01 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgLeft01.png"),0,0,1200,1000);
        xgLeft02 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgLeft02.png"),0,0,1200,1000);
        xgRight01 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgRight01.png"),0,0,1200,1000);
        xgRight02 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgRight02.png"),0,0,1200,1000);
        xgText = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgText.png"),0,0,1200,1000);
        xgMask = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgMask.png"),0,0,1200,1000);
        xgCipherBg = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgCipherBg.png"),0,0,300,300);
        xgDeclare = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/effects/xgDeclare.png"),0,0,300,300);
    }
}
