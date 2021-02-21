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
            new ProvincialTv("شبکه سهند", "http://cdnlive.irib.ir/live-channels/smil:sahand/playlist.m3u8?s=kODX77eGsmK9091rAkMRKA"),
            new ProvincialTv("شبکه آذربایجان غربی", "http://cdnlive.irib.ir/live-channels/smil:azarbayjan/playlist.m3u8?s=iOhkx3nBe-0pQ54IsBMLTw"),
            new ProvincialTv("شبکه سبلان", "http://cdnlive.irib.ir/live-channels/smil:sabalan/playlist.m3u8?s=H9h_41AgCww2VMgnZxQ5xw"),
            new ProvincialTv("شبکه اصفهان", "http://cdnlive.irib.ir/live-channels/smil:esfahan/playlist.m3u8?s=cBgqqDH6KFT3F8cWPrhJ5g"),
            new ProvincialTv("شبکه البرز", "http://cdnlive.irib.ir/live-channels/smil:alborz/playlist.m3u8?s=IZB2S-xv2kPYK18hvawhNQ"),
            new ProvincialTv("شبکه ایلام", "http://cdnlive.irib.ir/live-channels/smil:ilam/playlist.m3u8?s=xU2Zd1gzWvlXT8lfIj6y1w"),
            new ProvincialTv("شبکه بوشهر", "http://cdnlive.irib.ir/live-channels/smil:bushehr/playlist.m3u8?s=CmZL7npRILW_2kI_i-mIaA"),
            new ProvincialTv("شبکه جهان بین", "http://cdnlive.irib.ir/live-channels/smil:jahanbin/playlist.m3u8?s=uNF61LZs4apH4FuytZN1mQ"),
            new ProvincialTv("شبکه خراسان جنوبی", "http://cdnlive.irib.ir/live-channels/smil:khorasanjonoobi/playlist.m3u8?s=WCLUWMLdJjFuCpIMBNBlmw"),
            new ProvincialTv("شبکه خراسان رضوی", "http://cdnlive.irib.ir/live-channels/smil:khorasanrazavi/playlist.m3u8?s=P67WP5w4zjdDxpKCW3bxNQ"),
            new ProvincialTv("شبکه خراسان شمالی", "http://cdnlive.irib.ir/live-channels/smil:khorasanshomali/playlist.m3u8?s=xcFws0Tsk5mt817a6sYLyg"),
            new ProvincialTv("شبکه خوزستان", "http://cdnlive.irib.ir/live-channels/smil:khoozestan/playlist.m3u8?s=ODgJFJPiiIpHPXjDhjb2NA"),
            new ProvincialTv("شبکه اشراق(زنجان)", "http://cdnlive.irib.ir/live-channels/smil:eshragh/playlist.m3u8?s=xi7PsDdbKNwMEAgRP3DMCg"),
            new ProvincialTv("شبکه سمنان", "http://cdnlive.irib.ir/live-channels/smil:semnan/playlist.m3u8?s=eD80kqLjvkCiQAG6FEIUAg"),
            new ProvincialTv("شبکه هامون", "http://cdnlive.irib.ir/live-channels/smil:hamoon/playlist.m3u8?s=GnyFVKJgmfeRAp7GpcbE3g"),
            new ProvincialTv("شبکه فارس", "http://cdnlive.irib.ir/live-channels/smil:fars/playlist.m3u8?s=8w0tSVBU7jfDXqy66pL6Dw"),
            new ProvincialTv("شبکه قزوین", "http://cdnlive.irib.ir/live-channels/smil:qazvin/playlist.m3u8?s=gBQ5TMEdMKkzRMhKYS0ZBg"),
            new ProvincialTv("شبکه نور(قم)", "http://cdnlive.irib.ir/live-channels/smil:noor/playlist.m3u8?s=5kyk7SOAn-2XS4b3_AUCwQ"),
            new ProvincialTv("شبکه کردستان", "http://cdnlive.irib.ir/live-channels/smil:kordestan/playlist.m3u8?s=N7QmIPmhbPBrTsZU8oue0w"),
            new ProvincialTv("شبکه کرمان", "http://cdnlive.irib.ir/live-channels/smil:kerman/playlist.m3u8?s=yRUy_nG7vsBkdoMSMCh_iA"),
            new ProvincialTv("شبکه کرمانشاه", "http://cdnlive.irib.ir/live-channels/smil:kermanshah/playlist.m3u8?s=2-blQz66Y7W1qujVClHWpg"),
            new ProvincialTv("شبکه دنا", "http://cdnlive.irib.ir/live-channels/smil:dena/playlist.m3u8?s=O2-P6pGio2NcuuN25RyruA"),
            new ProvincialTv("شبکه گلستان", "http://cdnlive.irib.ir/live-channels/smil:golestan/playlist.m3u8?s=4lgiOla_Tqudt7SMMmeN7w"),
            new ProvincialTv("شبکه باران", "http://cdnlive.irib.ir/live-channels/smil:baran/playlist.m3u8?s=ljuV3j9XlDcRi2CzD0XMLg"),
            new ProvincialTv("شبکه افلاک(لرستان)", "http://cdnlive.irib.ir/live-channels/smil:aflak/playlist.m3u8?s=xWkv2hOjg6B3QBgRqDFd8w"),
            new ProvincialTv("شبکه مازندران", "http://cdnlive.irib.ir/live-channels/smil:mazandaran/playlist.m3u8?s=agfoas5Mx2KxM3owzQSakg"),
            new ProvincialTv("شبکه آفتاب(اراک)", "http://cdnlive.irib.ir/live-channels/smil:aftab/playlist.m3u8?s=xHZAMOD4sz3PecQjY8wlyQ"),
            new ProvincialTv("شبکه خلیج فارس", "http://cdnlive.irib.ir/live-channels/smil:khalijefars/playlist.m3u8?s=DymlTqEyRPXpWyqi3BDV0Q"),
            new ProvincialTv("شبکه کیش", "http://cdnlive.irib.ir/live-channels/smil:kish/playlist.m3u8?s=sxe3HcPNQAwqsw2Ky-QgdQ"),
            new ProvincialTv("شبکه همدان", "http://cdnlive.irib.ir/live-channels/smil:hamedan/playlist.m3u8?s=hWlN0ti_FoZWhlBaP_1Z5g"),
            new ProvincialTv("شبکه تابان(یزد)", "http://cdnlive.irib.ir/live-channels/smil:taban/playlist.m3u8?s=DPawr9OL6Qaydhq3sgkkpA"),
            new ProvincialTv("شبکه مهاباد", "http://cdnlive.irib.ir/live-channels/smil:mahabad/playlist.m3u8?s=vt7RZ82F7_6ba7YClvJySw"),
            new ProvincialTv("شبکه آبادان", "http://cdnlive.irib.ir/live-channels/smil:abadan/playlist.m3u8?s=2Ipu9HpDc3rap549nfd_sA"),
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
