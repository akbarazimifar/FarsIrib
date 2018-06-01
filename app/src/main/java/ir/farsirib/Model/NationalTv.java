package ir.farsirib.Model;

/**
 * Created by alireza on 2017/09/23.
 */

public class NationalTv {

    String name;
    String Address;

    public NationalTv(String name, String address) {
        this.name = name;
        Address = address;
    }

    public NationalTv() {
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

    public static final NationalTv[] NATIONAL_TVS = new NationalTv[] {
            new NationalTv("شبکه یک","http://cdn1.live.irib.ir:1935/channel-live/smil:tv1/playlist.m3u8"),
            new NationalTv("شبکه دو","http://cdn1.live.irib.ir:1935/channel-live/smil:tv2/playlist.m3u8"),
            new NationalTv("شبکه سه","http://cdn1.live.irib.ir:1935/channel-live/smil:tv3/playlist.m3u8"),
            new NationalTv("شبکه چهار","http://cdn1.live.irib.ir:1935/channel-live/smil:tv4/playlist.m3u8"),
            new NationalTv("شبکه پنج","http://cdn1.live.irib.ir:1935/channel-live/smil:tv5/playlist.m3u8"),
            new NationalTv(" شبکه خبر","http://cdn1.live.irib.ir:1935/channel-live/smil:irinn/playlist.m3u8"),
            new NationalTv("شبکه آموزش","http://cdn1.live.irib.ir:1935/channel-live/smil:amouzesh/playlist.m3u8"),
            new NationalTv("شبکه قرآن و معارف","http://cdn1.live.irib.ir:1935/channel-live/smil:quran/playlist.m3u8"),
            new NationalTv("شبکه مستند","http://cdn1.live.irib.ir:1935/channel-live/smil:mostanad/playlist.m3u8"),
            new NationalTv("شبکه آی فیلم","http://cdn1.live.irib.ir:1935/channel-live/smil:ifilmfa/playlist.m3u8"),
            new NationalTv("شبکه نمایش","http://cdn1.live.irib.ir:1935/channel-live/smil:namayesh/playlist.m3u8"),
            new NationalTv("شبکه تماشا","http://cdn1.live.irib.ir:1935/channel-live/smil:tamasha/playlist.m3u8"),
            new NationalTv("شبکه ورزش","http://cdn1.live.irib.ir:1935/channel-live/smil:varzesh/playlist.m3u8"),
            new NationalTv("شبکه نسیم","http://cdn1.live.irib.ir:1935/channel-live/smil:nasim/playlist.m3u8"),
            new NationalTv("شبکه نهال","http://cdn1.live.irib.ir:1935/channel-live/smil:pouya/playlist.m3u8"),
            new NationalTv("شبکه امید","http://cdn1.live.irib.ir:1935/channel-live/smil:omid/playlist.m3u8"),
            new NationalTv("شبکه سلامت","http://cdn1.live.irib.ir:1935/channel-live/smil:salamat/playlist.m3u8"),
            new NationalTv("شبکه شما","http://cdn1.live.irib.ir:1935/channel-live/smil:shoma/playlist.m3u8"),
            new NationalTv("شبکه افق","http://cdn1.live.irib.ir:1935/channel-live/smil:ofogh/playlist.m3u8"),
            new NationalTv("شبکه ایران کالا","http://cdn1.live.irib.ir:1935/channel-live/smil:irankala/playlist.m3u8"),
    };

    public static NationalTv getItem(int id) {
        for (NationalTv item : NATIONAL_TVS) {
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
    public String get(NationalTv.Field f) {
        switch (f) {
            case ADDRESS: return Address;
            case NAME: default: return name;
        }
    }
}
