package com.example.lolapp_sam_api;
import java.util.List;

public class Champion {
    private String releasedate;
    private String name;
    private String id;
    private String faction;
    private ImageDTO image;
    private String url;

    public Champion(String type, String releasedate, String name, String title, String sectiontitle, String slug, String associatedfaction, String associatedfactionslug, ImageDTO image, BackgroundDTO background, String url) {
        this.releasedate = releasedate.substring(0,10);
        this.name = name;
        this.id = slug;
        this.faction = associatedfactionslug;
        this.image = image;
        this.url = url;
    }

    public String getImageUrl(){
        return this.image.getUri();
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public class ImageDTO {
        private String title;
        private String subtitle;
        private String uri;
        private String encoding;
        private Integer width;
        private Integer height;
        private Integer x;
        private Integer y;
        private List<?> featuredchampions;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public List<?> getFeaturedchampions() {
            return featuredchampions;
        }

        public void setFeaturedchampions(List<?> featuredchampions) {
            this.featuredchampions = featuredchampions;
        }
    }

    public static class BackgroundDTO {
        private String title;
        private String subtitle;
        private String description;
        private String uri;
        private String encoding;
        private Integer width;
        private Integer height;
        private Integer x;
        private Integer y;
        private List<?> featuredchampions;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }

        public Integer getY() {
            return y;
        }

        public void setY(Integer y) {
            this.y = y;
        }

        public List<?> getFeaturedchampions() {
            return featuredchampions;
        }

        public void setFeaturedchampions(List<?> featuredchampions) {
            this.featuredchampions = featuredchampions;
        }
    }
}