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
            new OverseasTv("شبکه پرس تی وی", "http://cdnlive.irib.ir/live-channels/smil:presstv/playlist.m3u8?s=n6k_ElRPm75S4lz65FfclA"),
            new OverseasTv("شبکه جام جم", "http://cdnlive.irib.ir/live-channels/smil:jjtvn1/playlist.m3u8?s=EFrmrJwVf8i7ZH3cwnrRnw"),
            new OverseasTv("شبکه العالم", "http://cdnlive.irib.ir/live-channels/smil:alalam/playlist.m3u8?s=Y6P5rdt3KD0N2Rl_kM8_iQ"),
            new OverseasTv("شبکه سحر بالکان", "http://cdnlive.irib.ir/live-channels/smil:sahar4/playlist.m3u8?s=NMaf7cfwaKij6uzeM5_JDg"),
            new OverseasTv("شبکه سحر آذری", "http://cdnlive.irib.ir/live-channels/smil:sahar1/playlist.m3u8?s=an0jjoA8fx8zBn_UTUm8UQ"),
            new OverseasTv("شبکه سحر اردو", "http://cdnlive.irib.ir/live-channels/smil:sahar2/playlist.m3u8?s=aExEhD49YpBA6Ll1ATJxTQ"),
            new OverseasTv("شبکه سحر کردی", "http://cdnlive.irib.ir/live-channels/smil:sahar3/playlist.m3u8?s=nSJpQOk748zpTojQyzbqcw"),
            new OverseasTv("شبکه هیسپان تی وی", "http://cdnlive.irib.ir/live-channels/smil:hispan/playlist.m3u8?s=9Pup9cwT-m2MC9zlw8u5BA"),
            new OverseasTv("شبکه الکوثر", "http://cdnlive.irib.ir/live-channels/smil:alkosar/playlist.m3u8?s=QeYUx9d_kh42YYwsEgxSsA"),
           // new OverseasTv("شبکه آی فیلم عربی", "http://cdn1.live.irib.ir:1935/channel-live/smil:ifilmar/playlist.m3u8"),
            new OverseasTv("شبکه آی فیلم انگلیسی", "http://cdnlive.irib.ir/live-channels/smil:ifilmen/playlist.m3u8?s=MzEc8YCFlWHtRAZvWe2r6Q"),
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
