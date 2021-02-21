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
            new NationalTv("شبکه یک","http://cdnlive.irib.ir/live-channels/smil:tv1/playlist.m3u8?s=1OpaG8Ni4Fqm1U-tJruO1w"),
            new NationalTv("شبکه دو","http://cdnlive.irib.ir/live-channels/smil:tv2/playlist.m3u8?s=-o7pCet47k7PjBdgH-cOGw"),
            new NationalTv("شبکه سه","http://cdnlive.irib.ir/live-channels/smil:tv3/playlist.m3u8?s=jvc62QhtapWJXC9AFebSIA"),
            new NationalTv("شبکه چهار","http://cdnlive.irib.ir/live-channels/smil:tv4/playlist.m3u8?s=1MMApCBxRrbv0af-uJgLcQ"),
            new NationalTv("شبکه پنج","http://cdnlive.irib.ir/live-channels/smil:tv5/playlist.m3u8?s=WjitrxJHpfKenRKu44-xrw"),
            new NationalTv(" شبکه خبر","http://cdnlive.irib.ir/live-channels/smil:irinn/playlist.m3u8?s=e2xByHKj4z8Oj59b3_LYdA"),
            new NationalTv("شبکه آموزش","http://cdnlive.irib.ir/live-channels/smil:amouzesh/playlist.m3u8?s=9AI2q9tyLTdNRPAw5HpM4w"),
            new NationalTv("شبکه قرآن و معارف","http://cdnlive.irib.ir/live-channels/smil:quran/playlist.m3u8?s=wm_U-3Zd2b7KPyORHaViKw"),
            new NationalTv("شبکه مستند","http://cdnlive.irib.ir/live-channels/smil:mostanad/playlist.m3u8?s=mh7Bd87SwKLwMAZO3ijPTQ"),
            new NationalTv("شبکه آی فیلم","http://cdnlive.irib.ir/live-channels/smil:ifilmfa/playlist.m3u8?s=xt7MIAYzBhaFTTCzdh62Tw"),
            new NationalTv("شبکه نمایش","http://cdnlive.irib.ir/live-channels/smil:namayesh/playlist.m3u8?s=p4YEIqvJhvkXtPUQFAjCRw"),
            new NationalTv("شبکه تماشا","http://cdnlive.irib.ir/live-channels/smil:tamasha/playlist.m3u8?s=TMMwDT3249EIlmoys-Z_Ew"),
            new NationalTv("شبکه ورزش","http://cdnlive.irib.ir/live-channels/smil:varzesh/playlist.m3u8?s=Xfrwvyd8Iea2aaXVvDBXWg"),
            new NationalTv("شبکه نسیم","http://cdnlive.irib.ir/live-channels/smil:nasim/playlist.m3u8?s=pJCCr-1fsaO4KvLbZvSUqQ"),
            new NationalTv("شبکه نهال","http://cdnlive.irib.ir/live-channels/smil:pouya/playlist.m3u8?s=9N3WV0ti0ConBvuVkpjuhw"),
            new NationalTv("شبکه امید","http://cdnlive.irib.ir/live-channels/smil:omid/playlist.m3u8?s=W7pebgx1r1H_ylvh937_ug"),
            new NationalTv("شبکه سلامت","http://cdnlive.irib.ir/live-channels/smil:salamat/playlist.m3u8?s=i_Tj_XlDPfv0NhIDw5KSOA"),
            new NationalTv("شبکه شما","http://cdnlive.irib.ir/live-channels/smil:shoma/playlist.m3u8?s=AJAMlV5gwBL9UzLJreF-Og"),
            new NationalTv("شبکه افق","http://cdnlive.irib.ir/live-channels/smil:ofogh/playlist.m3u8?s=h9UyWnNSsr2TWJj96HarhA"),
            new NationalTv("شبکه ایران کالا","http://cdnlive.irib.ir/live-channels/smil:irankala/playlist.m3u8?s=ceQayJRJwbNfCuYkCDhNxw"),
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
