package hackathon.co.kr.util.network.model2;

public class ImageResponse {
    private String img_url;

    public String getImageUrl() {
        return "http://ec2-15-164-171-69.ap-northeast-2.compute.amazonaws.com/api/v1/" + img_url;
    }
}
