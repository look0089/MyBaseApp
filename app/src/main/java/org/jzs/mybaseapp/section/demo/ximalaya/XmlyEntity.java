package org.jzs.mybaseapp.section.demo.ximalaya;

import java.io.Serializable;

public class XmlyEntity implements Serializable {


    //分类列表
    /**
     * id : 3
     * kind : category
     * category_name : 有声书
     * cover_url_small : http://fdfs.xmcdn.com/group31/M05/84/25/wKgJSVl2sVTTQ1EOAAAMRgI-yqM098.jpg
     * cover_url_middle : http://fdfs.xmcdn.com/group31/M05/84/25/wKgJSVl2sVPxiPpMAAAMRgI-yqM007.jpg
     * cover_url_large : http://fdfs.xmcdn.com/group31/M05/84/25/wKgJSVl2sVPxiPpMAAAMRgI-yqM007.jpg
     * order_num : 2
     */
    public String id;
    public String kind;
    public String category_name;
    public String cover_url_small;
    public String cover_url_middle;
    public String cover_url_large;
    public String order_num;
}
