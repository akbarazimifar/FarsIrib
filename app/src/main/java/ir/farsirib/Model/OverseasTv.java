package ir.farsirib.Model;

/**
 * Created by alireza on 2017/08/13.
 */

public class OverseasTv {

    String name;
    String Address;

    public OverseasTv(String name, String address) {
        this.name = name;
        Address = address;
    }

    public OverseasTv() {

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

    public static final OverseasTv[] OVERSEAS_TVS = new OverseasTv[] {
            new OverseasTv("شبکه پرس تی وی", "http://cdn1.live.irib.ir:1935/channel-live/smil:presstv/playlist.m3u8"),
            new OverseasTv("شبکه جام جم", "http://cdn1.live.irib.ir:1935/channel-live/smil:jjtvn1/playlist.m3u8"),
            new OverseasTv("شبکه العالم", "http://cdn1.live.irib.ir:1935/channel-live/smil:alalam/playlist.m3u8"),
            new OverseasTv("شبکه سحر 1", "http://cdn1.live.irib.ir:1935/channel-live/smil:sahar1/playlist.m3u8"),
            new OverseasTv("شبکه سحر 2", "http://cdn1.live.irib.ir:1935/channel-live/smil:sahar2/playlist.m3u8"),
            new OverseasTv("شبکه سحر 3", "http://cdn1.live.irib.ir:1935/channel-live/smil:sahar3/playlist.m3u8"),
           // new OverseasTv("شبکه هیسپان تی وی", "http://cdn1.live.irib.ir:1935/channel-live"),
            new OverseasTv("شبکه الکوثر", "http://cdn1.live.irib.ir:1935/channel-live/smil:alkosar/playlist.m3u8"),
            new OverseasTv("شبکه آی فیلم عربی", "http://cdn1.live.irib.ir:1935/channel-live/smil:ifilmar/playlist.m3u8"),
           // new OverseasTv("شبکه آی فیلم انگلیسی", ""),
    };

    public static OverseasTv getItem(int id) {
        for (OverseasTv item : OVERSEAS_TVS) {
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
    public String get(OverseasTv.Field f) {
        switch (f) {
            case ADDRESS: return Address;
            case NAME: default: return name;
        }
    }
}
