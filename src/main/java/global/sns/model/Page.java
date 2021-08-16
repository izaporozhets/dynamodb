package global.sns.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBDocument
@Data
@NoArgsConstructor
public class Page {
    @DynamoDBAttribute
    @JsonProperty("letters")
    private Integer letters;
    @DynamoDBAttribute
    @JsonProperty("width")
    private Integer width;
    @DynamoDBAttribute
    @JsonProperty("height")
    private Integer height;
}
