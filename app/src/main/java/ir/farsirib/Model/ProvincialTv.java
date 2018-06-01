package ir.farsirib.Model;

/**
 * Created by alireza on 2017/08/13.
 */

public class ProvincialTv {

    String name;
    String Address;

    public ProvincialTv(String name, String address) {
        this.name = name;
        Address = address;
    }

    public ProvincialTv() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public static final ProvincialTv[] PROVINCIAL_TVS = new ProvincialTv[] {
            new ProvincialTv("شبکه سهند", "http://cdn1.live.irib.ir:1935/channel-live/smil:sahand/playlist.m3u8"),
            new ProvincialTv("شبکه آذربایجان غربی", "http://cdn1.live.irib.ir:1935/channel-live/smil:azarbayjan/playlist.m3u8"),
            new ProvincialTv("شبکه سبلان", "http://cdn1.live.irib.ir:1935/channel-live/smil:sabalan/playlist.m3u8"),
            new ProvincialTv("شبکه اصفهان", "http://cdn1.live.irib.ir:1935/channel-live/smil:esfahan/playlist.m3u8"),
            new ProvincialTv("شبکه البرز", "http://cdn1.live.irib.ir:1935/channel-live/smil:alborz/playlist.m3u8"),
            new ProvincialTv("شبکه ایلام", "http://cdn1.live.irib.ir:1935/channel-live/smil:ilam/playlist.m3u8"),
            new ProvincialTv("شبکه بوشهر", "http://cdn1.live.irib.ir:1935/channel-live/smil:bushehr/playlist.m3u8"),
            new ProvincialTv("شبکه جهان بین", "http://cdn1.live.irib.ir:1935/channel-live/smil:jahanbin/playlist.m3u8"),
            new ProvincialTv("شبکه خراسان جنوبی", "http://cdn1.live.irib.ir:1935/channel-live/smil:khorasanjonoobi/playlist.m3u8"),
            new ProvincialTv("شبکه خراسان رضوی", "http://cdn1.live.irib.ir:1935/channel-live/smil:khorasanrazavi/playlist.m3u8"),
            new ProvincialTv("شبکه خراسان شمالی", "http://cdn1.live.irib.ir:1935/channel-live/smil:khorasanshomali/playlist.m3u8"),
            new ProvincialTv("شبکه خوزستان", "http://cdn1.live.irib.ir:1935/channel-live/smil:khoozestan/playlist.m3u8"),
            new ProvincialTv("شبکه اشراق(زنجان)", "http://cdn1.live.irib.ir:1935/channel-live/smil:eshragh/playlist.m3u8"),
            new ProvincialTv("شبکه سمنان", "http://cdn1.live.irib.ir:1935/channel-live/smil:semnan/playlist.m3u8"),
            new ProvincialTv("شبکه هامون", "http://cdn1.live.irib.ir:1935/channel-live/smil:hamoon/playlist.m3u8"),
            new ProvincialTv("شبکه فارس", "http://cdn1.live.irib.ir:1935/channel-live/smil:fars/playlist.m3u8"),
            new ProvincialTv("شبکه قزوین", "http://cdn1.live.irib.ir:1935/channel-live/smil:qazvin/playlist.m3u8"),
            new ProvincialTv("شبکه نور(قم)", "http://cdn1.live.irib.ir:1935/channel-live/smil:noor/playlist.m3u8"),
            new ProvincialTv("شبکه کردستان", "http://cdn1.live.irib.ir:1935/channel-live/smil:kordestan/playlist.m3u8"),
            new ProvincialTv("شبکه کرمان", "http://cdn1.live.irib.ir:1935/channel-live/smil:kerman/playlist.m3u8"),
            new ProvincialTv("شبکه کرمانشاه", "http://cdn1.live.irib.ir:1935/channel-live/smil:kermanshah/playlist.m3u8"),
            new ProvincialTv("شبکه دنا", "http://cdn1.live.irib.ir:1935/channel-live/smil:dena/playlist.m3u8"),
            new ProvincialTv("شبکه گلستان", "http://cdn1.live.irib.ir:1935/channel-live/smil:golestan/playlist.m3u8"),
            new ProvincialTv("شبکه باران", "http://cdn1.live.irib.ir:1935/channel-live/smil:baran/playlist.m3u8"),
            new ProvincialTv("شبکه افلاک(لرستان)", "http://cdn1.live.irib.ir:1935/channel-live/smil:aflak/playlist.m3u8"),
            new ProvincialTv("شبکه مازندران", "http://cdn1.live.irib.ir:1935/channel-live/smil:mazandaran/playlist.m3u8"),
            new ProvincialTv("شبکه آفتاب(اراک)", "http://cdn1.live.irib.ir:1935/channel-live/smil:aftab/playlist.m3u8"),
            new ProvincialTv("شبکه خلیج فارس", "http://cdn1.live.irib.ir:1935/channel-live/smil:khalijefars/playlist.m3u8"),
            new ProvincialTv("شبکه کیش", "http://cdn1.live.irib.ir:1935/channel-live/smil:kish/playlist.m3u8"),
            new ProvincialTv("شبکه همدان", "http://cdn1.live.irib.ir:1935/channel-live/smil:hamedan/playlist.m3u8"),
            new ProvincialTv("شبکه تابان(یزد)", "http://cdn1.live.irib.ir:1935/channel-live/smil:taban/playlist.m3u8"),
            new ProvincialTv("شبکه مهاباد", "http://cdn1.live.irib.ir:1935/channel-live/smil:mahabad/playlist.m3u8"),
            new ProvincialTv("شبکه آبادان", "http://cdn1.live.irib.ir:1935/channel-live/smil:abadan/playlist.m3u8"),
    };

    public static ProvincialTv getItem(int id) {
        for (ProvincialTv item : PROVINCIAL_TVS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int getId() {
        return name.hashCode() + Address.hashCode();
    }

    public enum Field {
        NAME, ADDRESS
    }
    public String get(Field f) {
        switch (f) {
            case ADDRESS: return Address;
            case NAME: default: return name;
        }
    }
}
