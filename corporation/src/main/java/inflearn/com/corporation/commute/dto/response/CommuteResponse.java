package inflearn.com.corporation.commute.dto.response;

import java.util.ArrayList;
import java.util.List;

public class CommuteResponse {

    private List<DetailResponse> detail = new ArrayList<>();
    private Long sum;

    protected CommuteResponse() {}

    public CommuteResponse(List<DetailResponse> detail, Long sum) {
        this.detail = detail;
        this.sum = sum;
    }

    public List<DetailResponse> getDetail() {
        return detail;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(long totalSum) {
    }
}
