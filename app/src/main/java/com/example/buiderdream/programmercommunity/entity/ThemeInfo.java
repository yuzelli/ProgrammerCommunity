package com.example.buiderdream.programmercommunity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 * 话题各个item的实体类
 * @author 李秉龙
 */

public class ThemeInfo implements Serializable {

    /**
     * id : 327759
     * title : 又到了春节买票的好时光，如果对 12306 组织一波 D^D^O^S 会是怎么样的
     * url : http://www.v2ex.com/t/327759
     * content : 我说的是恶意的那种，不是人肉那种
     是不是每年都会有国外黑客干这种事， 12306 能应对吗
     * content_rendered : <p>我说的是恶意的那种，不是人肉那种
     是不是每年都会有国外黑客干这种事， 12306 能应对吗</p>

     * replies : 21
     * member : {"id":204041,"username":"okprodigy","tagline":"None","avatar_mini":"//cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=24&d=retro","avatar_normal":"//cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=48&d=retro","avatar_large":"//cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=73&d=retro"}
     * node : {"id":12,"name":"qna","title":"问与答","title_alternative":"Questions and Answers","url":"http://www.v2ex.com/go/qna","topics":80302,"avatar_mini":"//cdn.v2ex.co/navatar/c20a/d4d7/12_mini.png?m=1481312194","avatar_normal":"//cdn.v2ex.co/navatar/c20a/d4d7/12_normal.png?m=1481312194","avatar_large":"//cdn.v2ex.co/navatar/c20a/d4d7/12_large.png?m=1481312194"}
     * created : 1481768549
     * last_modified : 1481768549
     * last_touched : 1481773545
     */

    private int id;
    private String title;
    private String url;
    private String content;
    private String content_rendered;
    private int replies;
    /**
     * id : 204041
     * username : okprodigy
     * tagline : None
     * avatar_mini : //cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=24&d=retro
     * avatar_normal : //cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=48&d=retro
     * avatar_large : //cdn.v2ex.co/gravatar/2254ba7d1b3a0cd43a064f8ddede452f?s=73&d=retro
     */

    private MemberBean member;
    /**
     * id : 12
     * name : qna
     * title : 问与答
     * title_alternative : Questions and Answers
     * url : http://www.v2ex.com/go/qna
     * topics : 80302
     * avatar_mini : //cdn.v2ex.co/navatar/c20a/d4d7/12_mini.png?m=1481312194
     * avatar_normal : //cdn.v2ex.co/navatar/c20a/d4d7/12_normal.png?m=1481312194
     * avatar_large : //cdn.v2ex.co/navatar/c20a/d4d7/12_large.png?m=1481312194
     */

    private NodeBean node;
    private long created;
    private long last_modified;
    private long last_touched;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_rendered() {
        return content_rendered;
    }

    public void setContent_rendered(String content_rendered) {
        this.content_rendered = content_rendered;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public NodeBean getNode() {
        return node;
    }

    public void setNode(NodeBean node) {
        this.node = node;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(long last_modified) {
        this.last_modified = last_modified;
    }

    public long getLast_touched() {
        return last_touched;
    }

    public void setLast_touched(long last_touched) {
        this.last_touched = last_touched;
    }

    public static class MemberBean implements Serializable{
        private int id;
        private String username;
        private String tagline;
        private String avatar_mini;
        private String avatar_normal;
        private String avatar_large;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public void setAvatar_normal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }
    }

    public static class NodeBean implements Serializable{
        private int id;
        private String name;
        private String title;
        private String title_alternative;
        private String url;
        private int topics;
        private String avatar_mini;
        private String avatar_normal;
        private String avatar_large;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle_alternative() {
            return title_alternative;
        }

        public void setTitle_alternative(String title_alternative) {
            this.title_alternative = title_alternative;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getTopics() {
            return topics;
        }

        public void setTopics(int topics) {
            this.topics = topics;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public void setAvatar_normal(String avatar_normal) {
            this.avatar_normal = avatar_normal;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }
    }
}
