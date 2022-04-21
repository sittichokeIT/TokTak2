package th.ac.kmutnb.toktak2;

public class ModelVideo {
    String id,title,description,timestamp,videoUrl;

    //constructor

    public ModelVideo() {
        //firebase requires empty constructor
    }

    public ModelVideo(String id, String title, String description, String timestamp, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.videoUrl = videoUrl;
    }
    // Getters Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
